package com.yana.PrivateNetWorking.Node.localServer.controller;

import org.json.JSONObject;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseData;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json.DefaultJsonObject;
import com.yana.PrivateNetWorking.Node.privateNetWorker.accept.Acceptor;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommandDecider;

public class RegistObserverController extends AbstractController {
	public ResponseData regist() {
		String bodyData = getRequestBody();
		JSONObject jsonObject = new JSONObject(bodyData);
		String[] requestData = jsonObject.get("target").toString().split(",");
		Acceptor acceptor = Acceptor.getInstance();
		for(String r : requestData) {
			CommunicationCommand cmd = CommunicationCommandDecider.dicede(r.trim());
			acceptor.addObserver(cmd);
		}
		DefaultJsonObject responseJsonData = new DefaultJsonObject("{\"Regist\" : \"OK\"}");
		return json(responseJsonData);
	}
}
