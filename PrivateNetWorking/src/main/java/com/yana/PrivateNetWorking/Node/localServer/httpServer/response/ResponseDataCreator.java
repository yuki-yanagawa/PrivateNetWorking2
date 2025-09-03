package com.yana.PrivateNetWorking.Node.localServer.httpServer.response;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseDataDefiniton.CONTENT_TYPE;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json.JsonObject;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

public class ResponseDataCreator {
	private static final String DEFAULT_PROTOCOL = "HTTP/1.1";
	public static ResponseData createNotPageFound() {
		return createNotPageFound(DEFAULT_PROTOCOL);
	}
	public static ResponseData createNotPageFound(String protocol) {
		ResponseHeader responseHeader = ResponseHeader.createNotPageFound(protocol);
		ResponseBody responseBody = null;
		return new ResponseData(responseHeader, responseBody);
	}

	public static ResponseData createBadRequest(String protocol) {
		ResponseHeader responseHeader = ResponseHeader.createBadRequest(protocol);
		ResponseBody responseBody = null;
		return new ResponseData(responseHeader, responseBody);
	}

	public static ResponseData createInternalServerError(String protocol) {
		ResponseHeader responseHeader = ResponseHeader.createInternalServerError(protocol);
		ResponseBody responseBody = null;
		return new ResponseData(responseHeader, responseBody);
	}

	public static ResponseData createResponseData(String protocol, String fileName, CONTENT_TYPE type) {
		ResponseBody responseBody = ResponseBodyCache.getResponseBody(fileName);
		if(responseBody == null) {
			Path p = Paths.get(fileName);
			try(FileInputStream fis = new FileInputStream(p.toFile())) {
				long fSize = Files.size(p);
				if(fSize > Integer.MAX_VALUE) {
					//Not supported!!!!
					return createNotPageFound(protocol);
				}
				byte[] readBuf = new byte[(int)fSize];
				fis.read(readBuf);
				responseBody = new ResponseBody(readBuf, p.toFile().lastModified());
				ResponseBodyCache.addResponseBody(fileName, responseBody);
			} catch(IOException e) {
				LoggerUtil.warn(e);
				return createNotPageFound(protocol);
			}
		}
		ResponseHeader responseHeader = ResponseHeader.createResponseHeader(protocol, type, responseBody.getBodySize());
		return new ResponseData(responseHeader, responseBody);
	}

	public static ResponseData createResponseJsonData(String protocol, JsonObject jsonObject) {
		byte[] jsonData = jsonObject.creatJsonData().getBytes(CharsetUtil.charSet());
		ResponseHeader responseHeader = ResponseHeader.createResponseHeader(protocol, CONTENT_TYPE.JSON, jsonData.length);
		ResponseBody responseBody = new ResponseBody(jsonData, -1);
		return new ResponseData(responseHeader, responseBody);
	}

	public static ResponseData createResponseUpgradeWebSocket(String protocol, String webSockKey) {
		ResponseHeader responseHeader = ResponseHeader.createUpgradeWebsocket(protocol, webSockKey);
		ResponseBody responseBody = null;
		return new ResponseData(responseHeader, responseBody, true);
	}
}
