package com.yana.PrivateNetWorking.Node.localServer.httpServer.response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseDataDefiniton.CONTENT_TYPE;

class ResponseHeader {
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String responseLine;
	private List<String> responseBodys;
	ResponseHeader(String responseLine, List<String> responseBodys) {
		this.responseLine = responseLine;
		this.responseBodys = responseBodys;
	}
	static ResponseHeader createNotPageFound(String protocol) {
		String responseLine = protocol + " 404 Not Found";
		List<String> responseBodys = new ArrayList<>();
		responseBodys.add("Server: localServer");
		responseBodys.add("Date: " + SDF.format(new Date()).toString());
		return new ResponseHeader(responseLine, responseBodys);
	}

	static ResponseHeader createBadRequest(String protocol) {
		String responseLine = protocol + " 400 Bad Request";
		List<String> responseBodys = new ArrayList<>();
		responseBodys.add("Server: localServer");
		responseBodys.add("Date: " + SDF.format(new Date()).toString());
		return new ResponseHeader(responseLine, responseBodys);
	}

	static ResponseHeader createInternalServerError(String protocol) {
		String responseLine = protocol + " 500 Internal Server Error";
		List<String> responseBodys = new ArrayList<>();
		responseBodys.add("Server: localServer");
		responseBodys.add("Date: " + SDF.format(new Date()).toString());
		return new ResponseHeader(responseLine, responseBodys);
	}

	static ResponseHeader createUpgradeWebsocket(String protocol, String webSockKey) {
		String responseLine = protocol + "  101 Switching Protocols";
		List<String> responseBodys = new ArrayList<>();
		responseBodys.add("Server: localServer");
		responseBodys.add("Date: " + SDF.format(new Date()).toString());
		responseBodys.add("Connection: Upgrade");
		responseBodys.add("Upgrade: websocket");
		responseBodys.add("Sec-WebSocket-Accept: " + webSockKey);
		return new ResponseHeader(responseLine, responseBodys);
	}
	
	static ResponseHeader createResponseHeader(String protocol, CONTENT_TYPE type, int boySize) {
		String responseLine = protocol + " 200 OK";
		List<String> responseBodys = new ArrayList<>();
		responseBodys.add("Server: localServer");
		responseBodys.add("Date: " + SDF.format(new Date()).toString());
		responseBodys.add("Content-Type: " + type.getContentType());
		responseBodys.add("Content-Length: " + String.valueOf(boySize));
		return new ResponseHeader(responseLine, responseBodys);
	}

	String getResponseLine()  {
		return responseLine;
	}

	List<String> getResponseBodys() {
		return responseBodys;
	}
}
