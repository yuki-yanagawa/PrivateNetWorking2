package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.yana.PrivateNetWorking.Node.keyStore.NodeKeyStore;
import com.yana.PrivateNetWorking.Node.privateNetWorker.CommunicationStore;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.key.PrivateNetWorkCommonKey;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

class RequestFileListCommand implements ICommand {
	private CommandPrivateNetHelper commandPrivateNetHelper;
	RequestFileListCommand(CommandPrivateNetHelper commandPrivateNetHelper) {
		this.commandPrivateNetHelper = commandPrivateNetHelper;
	}
	@Override
	public FutureCommandResult<?> execute(ArgumentWrapper<?> argumentsWrapper) {
		if(argumentsWrapper == null) {
			FutureCommandResult<byte[]> ret = new FutureCommandResult<>();
			ret.setResult(new byte[0]);
			return ret;
		}
		FutureCommandResult<byte[]> furture = new FutureCommandResult<>();
		CommunicationStore.setFutureCommandResult(CommunicationCommand.REQUSET_DIRLIST, furture);
		// IP:Port
		String[] ipAndPort = ((String)argumentsWrapper.getArgs()).split(":");
		String ipAddr = ipAndPort[0].trim();
		if(ipAddr.startsWith("/")) {
			ipAddr = ipAddr.substring(1);
		}
		int port = Integer.parseInt(ipAndPort[1]);
		InetSocketAddress inetSocketAddress = new InetSocketAddress(ipAddr, port);

		StringBuilder message = new StringBuilder();
		PrivateNetWorkCommonKey  privateNetWorkCommonKey = NodeKeyStore.getPrivateNetworkCommonKey();
		message.append(CommunicationDefnition.SUBJ_REQUSET_DIRLIST).append(CommunicationDefnition.LINE_SPARATOR);
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
		if(sendData.length == 0) {
			furture.setResult(sendData);
			return furture;
		}
		commandPrivateNetHelper.sendMessage(sendData, inetSocketAddress);
		return furture;
	}
}