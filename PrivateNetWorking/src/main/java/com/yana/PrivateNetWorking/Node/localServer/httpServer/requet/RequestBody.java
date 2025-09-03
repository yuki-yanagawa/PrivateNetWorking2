package com.yana.PrivateNetWorking.Node.localServer.httpServer.requet;

public class RequestBody {
	private String bodyDataStr;
	private RequestBody(String bodyDataStr) {
		this.bodyDataStr = bodyDataStr;
	}
	public static RequestBody createBody(String textBase) {
		int index = textBase.indexOf("\r\n\r\n");
		if(index <= 0) {
			return null;
		}
		String bodyDataStr = textBase.substring(index);
		return new RequestBody(bodyDataStr);
	}

	public String getBodyStr() {
		return this.bodyDataStr;
	}
}
