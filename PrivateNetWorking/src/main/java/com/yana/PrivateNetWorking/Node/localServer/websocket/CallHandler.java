package com.yana.PrivateNetWorking.Node.localServer.websocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

import com.yana.PrivateNetWorking.CentralRouter.keygen.KeyGenerateHelper;
import com.yana.PrivateNetWorking.Node.localServer.socket.WrapperSocket;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.key.PrivateNetWorkCommonKey;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

public class CallHandler extends WebSocketHandler {
	private byte[] commonKeyHeaderBytes;
	private byte[] sendDataHeaderBytes;
	private PrivateNetWorkCommonKey privateNetWorkCommonKey;
	CallHandler(WrapperSocket socket) {
		super(socket);
		commonKeyHeaderBytes = (CommunicationDefnition.SUBJ_REQUSER_COMMON_COM + CommunicationDefnition.LINE_SPARATOR).getBytes(CharsetUtil.charSet());
		sendDataHeaderBytes = (CommunicationDefnition.SUBJ_CALLING + CommunicationDefnition.LINE_SPARATOR).getBytes(CharsetUtil.charSet());
		privateNetWorkCommonKey = KeyGenerateHelper.getPrivateNetWorkCommonKey();
	}

	@Override
	public void run() {
		try {
			socket.setSoTimeout(3000);
			while(executeFlg) {
				try {
					byte[] readData = WebSocketMessageUtil.messageDecoder(inputStream);
					//sendData(readData);
					LoggerUtil.info("Call Handler Get Data");
					sendCallData(readData);
				} catch(SocketTimeoutException e) {
				}
			}
		} catch(IOException e) {
			LoggerUtil.warn(e);
		} finally {
			try {
				socket.close();
			} catch(IOException e) {
				
			}
			//InvokeShutDownEvent.getInstance().invoke();
			//tight coupling????
			WebSocketManager.requestDelete();
		}
	}

	@Override
	public void sendData(byte[] data) {
		byte[] sendData = WebSocketMessageUtil.createSendMessage(data);
		try {
			outputStream.write(sendData);
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
	}

	private void sendCallData(byte[] data) {
		byte[] rawData = new byte[0];
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			bos.write(sendDataHeaderBytes);
			bos.write(data);
			bos.flush();
			rawData = bos.toByteArray();
		} catch(IOException e){
		}
		byte[] encryptData = privateNetWorkCommonKey.encrypt(rawData);
		byte[] sendDataToRemote = new byte[0];
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			bos.write(commonKeyHeaderBytes);
			bos.write(encryptData);
			bos.flush();
			sendDataToRemote = bos.toByteArray();
		} catch(IOException e){
		}
		if(sendDataToRemote.length == 0) {
			return;
		}
		try {
			privateNetSocket.sendData(sendDataToRemote, remoteAddr);
		} catch(IOException e) {
		}
	}
}
