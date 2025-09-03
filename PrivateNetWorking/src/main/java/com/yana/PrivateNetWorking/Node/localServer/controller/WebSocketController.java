package com.yana.PrivateNetWorking.Node.localServer.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseData;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;

public class WebSocketController extends AbstractController {
	private static final String KEY_CONNECTION = "CONNECTION";
	private static final String CONNECTION_UPGRADE = "UPGRADE";
	private static final String KEY_UPGRADE = "UPGRADE";
	private static final String UPGRADE_WEBSOCKET = "WEBSOCKET";
	private static final String KEY_SEC_WEBSOCKET_KEY = "SEC-WEBSOCKET-KEY";
	private static final String MAGIC_NO = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
	public ResponseData connect() {
		String conval = getRequestHeaderValue(KEY_CONNECTION);
		if(conval == null || !CONNECTION_UPGRADE.equals(conval.toUpperCase()))  {
			return badRequest();
		}
		String upVal = getRequestHeaderValue(KEY_UPGRADE);
		if(upVal == null || !UPGRADE_WEBSOCKET.equals(upVal.toUpperCase()))  {
			return badRequest();
		}
		String secKey = getRequestHeaderValue(KEY_SEC_WEBSOCKET_KEY);
		if(secKey == null) {
			return badRequest();
		}
		byte[] webSockKeyBytes = createWebSocketKey(secKey);
		if(webSockKeyBytes.length == 0) {
			return internalServerError();
		}
		String webSockKey = Base64.getEncoder().encodeToString(webSockKeyBytes);
		return upgradeWebsocket(webSockKey);
	}

	private byte[] createWebSocketKey(String key) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			return messageDigest.digest((key + MAGIC_NO).getBytes(StandardCharsets.UTF_8));
		} catch(NoSuchAlgorithmException e) {
			LoggerUtil.warn(e);
			return new byte[0];
		}
	}
}
