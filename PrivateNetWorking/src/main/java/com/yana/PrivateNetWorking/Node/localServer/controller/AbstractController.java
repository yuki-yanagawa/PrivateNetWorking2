package com.yana.PrivateNetWorking.Node.localServer.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.requet.RequestData;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseData;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseDataCreator;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseDataDefiniton.CONTENT_TYPE;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json.JsonObject;

public abstract class AbstractController {
	private static final Pattern HTML_FILE_PATTERN = Pattern.compile("\\.html$");
	private static final Pattern CSS_FILE_PATTERN = Pattern.compile("\\.css$");
	private static final Pattern JS_FILE_PATTERN = Pattern.compile("\\.js$");
	
	protected RequestData requestData;
	public void setRequestData(RequestData requestData) {
		this.requestData = requestData;
	}

	protected ResponseData render(String fileName) {
		Path p = Paths.get(fileName);
		if(!p.toFile().exists()) {
			return ResponseDataCreator.createNotPageFound(requestData.getRequestPath());
		}
		// Cache setting !!!
		return ResponseDataCreator.createResponseData(requestData.getProtocol(), fileName, getContentType(fileName));
	}

	protected ResponseData json(JsonObject jsonObject) {
		return ResponseDataCreator.createResponseJsonData(requestData.getProtocol(), jsonObject);
	}

	protected ResponseData upgradeWebsocket(String webSocketKey) {
		return ResponseDataCreator.createResponseUpgradeWebSocket(requestData.getProtocol(), webSocketKey);
	}

	private CONTENT_TYPE getContentType(String fileName) {
		if(HTML_FILE_PATTERN.matcher(fileName).find()) {
			return CONTENT_TYPE.HTML;
		}
		if(CSS_FILE_PATTERN.matcher(fileName).find()) {
			return CONTENT_TYPE.HTML;
		}
		if(JS_FILE_PATTERN.matcher(fileName).find()) {
			return CONTENT_TYPE.JAVASCRIPT;
		}
		return CONTENT_TYPE.TEXT;
	}

	protected ResponseData notFound() {
		return ResponseDataCreator.createNotPageFound(requestData.getRequestPath());
	}

	protected ResponseData badRequest() {
		return ResponseDataCreator.createBadRequest(requestData.getRequestPath());
	}

	protected ResponseData internalServerError() {
		return ResponseDataCreator.createInternalServerError(requestData.getRequestPath());
	}

	protected String getRequestBody() {
		return requestData.getRequestBody();
	}

	protected String getRequestHeaderValue(String headerKey) {
		return requestData.getRequestHeaderValue(headerKey);
	}
}
