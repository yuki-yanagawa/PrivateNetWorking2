package com.yana.PrivateNetWorking.Node.localServer.httpServer.response;

class ResponseBody {
	private final byte[] body;
	private final long lastUpdateTime;
	ResponseBody(byte[] body, long lastUpdateTime) {
		this.body = body;
		this.lastUpdateTime = lastUpdateTime;
	}

	byte[] getBody() {
		return body;
	}

	long getLastUpdateTime() {
		return lastUpdateTime;
	}

	int getBodySize() {
		return body.length;
	}
}
