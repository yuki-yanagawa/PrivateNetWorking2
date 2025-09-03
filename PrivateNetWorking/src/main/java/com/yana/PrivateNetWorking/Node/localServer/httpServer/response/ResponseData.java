package com.yana.PrivateNetWorking.Node.localServer.httpServer.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

public class ResponseData {
	private static final String SEPARATOR = "\r\n";
	private ResponseHeader responseHeader;
	private ResponseBody responseBody;
	private boolean isConnectKeep;
	public ResponseData(ResponseHeader responseHeader, ResponseBody responseBody) {
		this.responseHeader = responseHeader;
		this.responseBody = responseBody;
		this.isConnectKeep = false;
	}

	public ResponseData(ResponseHeader responseHeader, ResponseBody responseBody, boolean isConnectKeep) {
		this.responseHeader = responseHeader;
		this.responseBody = responseBody;
		this.isConnectKeep = isConnectKeep;
	}

	public boolean getIsConnectKeep() {
		return isConnectKeep;
	}

	public byte[] changeByteArraysData() {
		StringBuffer sb = new StringBuffer();
		sb.append(responseHeader.getResponseLine());
		sb.append(SEPARATOR);
		for(String s : responseHeader.getResponseBodys()) {
			sb.append(s);
			sb.append(SEPARATOR);
		}
		sb.append(SEPARATOR);
		byte[] headerBytes = sb.toString().getBytes(CharsetUtil.charSet());
		if(responseBody == null) {
			return headerBytes;
		}
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			bos.write(headerBytes);
			bos.write(responseBody.getBody());
			return bos.toByteArray();
		} catch (IOException e) {
			LoggerUtil.warn(e);
			return new byte[0];
		}
	}
}
