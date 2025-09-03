package com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json;

public class DefaultJsonObject extends JsonObject {
	private String jsonData;
	public DefaultJsonObject(String jsonData) {
		this.jsonData = jsonData;
	}
	@Override
	public String creatJsonData() {
		return this.jsonData;
	}
	
}
