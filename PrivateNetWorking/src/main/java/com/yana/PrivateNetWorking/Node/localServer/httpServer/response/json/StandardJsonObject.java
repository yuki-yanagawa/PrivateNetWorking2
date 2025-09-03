package com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json;

import java.util.Map;

import org.json.JSONObject;

public class StandardJsonObject extends JsonObject{
	private Map<String, Object> jsonMap;
	public StandardJsonObject(Map<String, Object> jsonMap) {
		this.jsonMap = jsonMap;
	}

	@Override
	public String creatJsonData() {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject(jsonMap);
		return jsonObject.toString();
	}

}
