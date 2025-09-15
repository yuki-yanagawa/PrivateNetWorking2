package com.yana.PrivateNetWorking.Node.localServer.controller;

import org.json.JSONObject;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseData;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json.DefaultJsonObject;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.CommandOperator;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.FutureCommandResult;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.ICommand;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

public class CallFuncController extends AbstractController {
	public ResponseData callWindow() {
		return render("html/callWindow.html");
	}

	public ResponseData requestCall() {
		String bodyData = getRequestBody();
		JSONObject jsonObject = new JSONObject(bodyData);
		ICommand command = CommandOperator.command(CommunicationCommand.REQUEST_CALL);
		FutureCommandResult<?> commandResult = command.execute(() -> jsonObject);
		byte[] responseData = (byte[])commandResult.getResult();
		if(responseData.length == 0) {
			return notFound();
		}
		if((new String(responseData, CharsetUtil.charSet())).startsWith(CommunicationDefnition.SUBJ_REQUSET_CALL_ACK)) {
			return json(new DefaultJsonObject("{\"RESULT\":\"SUCCESS\"}"));
		} else {
			return notFound();
		}
	}

	public ResponseData acceptCall() {
		String bodyData = getRequestBody();
		JSONObject jsonObject = new JSONObject(bodyData);
		ICommand command = CommandOperator.command(CommunicationCommand.REQUEST_CALL_ACK);
		command.execute(() -> jsonObject);
		return json(new DefaultJsonObject("{\"RESULT\":\"SUCCESS\"}"));
	}
}
