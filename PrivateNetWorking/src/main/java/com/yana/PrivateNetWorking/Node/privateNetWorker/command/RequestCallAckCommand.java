package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.json.JSONObject;

import com.yana.PrivateNetWorking.Node.keyStore.NodeKeyStore;
import com.yana.PrivateNetWorking.Node.localServer.websocket.WebSocketManager;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.key.PrivateNetWorkCommonKey;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

class RequestCallAckCommand implements ICommand {
	private CommandPrivateNetHelper commandPrivateNetHelper;
	RequestCallAckCommand(CommandPrivateNetHelper commandPrivateNetHelper) {
		this.commandPrivateNetHelper = commandPrivateNetHelper;
	}
	@Override
	public FutureCommandResult<?> execute(ArgumentWrapper<?> argumentsWrapper) {
		JSONObject jsonObject = (JSONObject)argumentsWrapper.getArgs();
		String targetUserAddr = jsonObject.getString("requestUserAddr");
		// IP:Port
		String[] ipAndPort = targetUserAddr.split(":");
		String ipAddr = ipAndPort[0].trim();
		if(ipAddr.startsWith("/")) {
			ipAddr = ipAddr.substring(1);
		}
		int port = Integer.parseInt(ipAndPort[1]);
		InetSocketAddress inetSocketAddress = new InetSocketAddress(ipAddr, port);

		// WebSocket Handler setting
		WebSocketManager.setPrivateCallTargetAddr(commandPrivateNetHelper.getPrivateNetSocket(), inetSocketAddress);

		StringBuilder message = new StringBuilder();
		PrivateNetWorkCommonKey  privateNetWorkCommonKey = NodeKeyStore.getPrivateNetworkCommonKey();
		message.append(CommunicationDefnition.SUBJ_REQUSET_CALL_ACK).append(CommunicationDefnition.LINE_SPARATOR);
		byte[] encryptData = privateNetWorkCommonKey.encrypt(message.toString().getBytes(CharsetUtil.charSet()));

		byte[] sendData = new byte[0];
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			bos.write((CommunicationDefnition.SUBJ_REQUSER_COMMON_COM + CommunicationDefnition.LINE_SPARATOR).getBytes(CharsetUtil.charSet()));
			bos.write(encryptData);
			bos.flush();
			sendData = bos.toByteArray();
		} catch(IOException e) {
			sendData = new byte[0];
			LoggerUtil.warn(e);
		}
		commandPrivateNetHelper.sendMessage(sendData, inetSocketAddress);
		return null;
	}
}
