package com.yana.PrivateNetWorking.Node.localServer.httpServer.requet;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.requet.RequestDataDefnition.METHOD;

public class RequestData {
	private RequestHeader requestHeader;
	private RequestBody requestBody;

	public RequestData(RequestHeader
			requestHeader, RequestBody requestBody) {
		this.requestHeader = requestHeader;
		this.requestBody = requestBody;
	}

	public static RequestData createRequestdata(byte[] requestRawData) {
		//HTTP PROTOCOL IS TEXT BASE
		String textBase = new String(requestRawData, RequestDataDefnition.CHARSET);
		String[] textBaseSepa = textBase.split(RequestDataDefnition.HTTP_PLOTO_SEPARATOR);

		RequestHeader requestHeader = RequestHeader.createRequestHeader(textBaseSepa);
		if(requestHeader == null) {
			return null;
		}
		String bodySizeLenStr = requestHeader.getReqeustHeaderValue("CONTENT-LENGTH", false);
		int bodyDataSize = -1;
		if(bodySizeLenStr != null) {
			bodyDataSize = Integer.parseInt(bodySizeLenStr);
		}
		if(bodyDataSize != -1 && requestRawData.length < bodyDataSize) {
			// read Error
			return null;
		}
		RequestBody requestBody = RequestBody.createBody(textBase);
		return new RequestData(requestHeader, requestBody);
	}

	public METHOD getRequestMethod() {
		return requestHeader.getRequestMethod();
	}

	public String getRequestPath() {
		return requestHeader.getRequestPath();
	}

	public String getProtocol() {
		return requestHeader.getProtocol();
	}

	public String getRequestBody() {
		return requestBody.getBodyStr();
	}

	public String getRequestHeaderValue(String headerKey) {
		return requestHeader.getReqeustHeaderValue(headerKey, false);
	}
}
