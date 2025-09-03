package com.yana.PrivateNetWorking.Node.localServer.controller;

import java.util.List;

import org.json.JSONObject;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseData;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json.DefaultJsonObject;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json.JsonObject;
import com.yana.PrivateNetWorking.Node.localServer.model.responseDataAnalyze.DirListResponseDataAnalyzer;
import com.yana.PrivateNetWorking.Node.localServer.model.responseDataAnalyze.JoinResponseDataAnalyzer;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.CommandOperator;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.FutureCommandResult;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.ICommand;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;

public class CommandController extends AbstractController {
	public ResponseData join() {
		ICommand command = CommandOperator.command(CommunicationCommand.JOIN);
		String bodyData = getRequestBody();
		JSONObject jsonObject = new JSONObject(bodyData);
		FutureCommandResult<?> commandResult = command.execute(() -> jsonObject.get("name"));
		byte[] responseData = (byte[])commandResult.getResult();
		if(responseData.length == 0) {
			return notFound();
		}
		//using responsedata
		JoinResponseDataAnalyzer joinResponseDataAnalyzer = new JoinResponseDataAnalyzer(responseData);
		if(!joinResponseDataAnalyzer.analyze()) {
			return notFound();
		}
		JsonObject jsonObj = joinResponseDataAnalyzer.getResponseJsonData();
		return json(jsonObj);
	}

//	public ResponseData commonSetting() {
//		ICommand command = CommandOperator.command(CommunicationOperator.REQUEST_COMMONKEY);
//		FutureCommandResult<?> commandResult = command.execute(null);
//		
//	}

	@SuppressWarnings("unchecked")
	public ResponseData updateMembership() {
		ICommand command = CommandOperator.command(CommunicationCommand.UPDATE_MEMBER);
		FutureCommandResult<?> commandResult = command.execute(null);
		List<String> result = (List<String>)commandResult.getResult();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("memberList", result);
		return json(new DefaultJsonObject(jsonObject.toString()));
	}

	public ResponseData requestDirList() {
		ICommand command = CommandOperator.command(CommunicationCommand.REQUSET_DIRLIST);
		String bodyData = getRequestBody();
		JSONObject jsonObject = new JSONObject(bodyData);
		FutureCommandResult<?> commandResult = command.execute(() -> jsonObject.get("useraddr"));
		byte[] responseData = (byte[])commandResult.getResult();
		if(responseData.length == 0) {
			return notFound();
		}
		DirListResponseDataAnalyzer dirListResponseDataAnalyzer = new DirListResponseDataAnalyzer(responseData);
		dirListResponseDataAnalyzer.analyze();
		JsonObject jsonObj = dirListResponseDataAnalyzer.getResponseJsonData();
		return json(jsonObj);
	}

	public ResponseData disconnect() {
		ICommand command = CommandOperator.command(CommunicationCommand.DISCONNECT);
		command.execute(null);
		return json(new JsonObject() {
			@Override
			public String creatJsonData() {
				return "{\"disconnect\" : \"OK\"}";
			}
		});
	}
}
