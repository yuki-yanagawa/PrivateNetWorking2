package com.yana.PrivateNetWorking.Node.privateNetWorker.autoResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.yana.PrivateNetWorking.Node.keyStore.NodeKeyStore;
import com.yana.PrivateNetWorking.Node.watchdir.WatchDirManager;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.key.PrivateNetWorkCommonKey;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;
import com.yana.privateNetSocket2.PrivateNetSocket;

class FileDataAutoResponse implements IAutoResponse {
	private PrivateNetSocket privateNetSocket;
	private InetSocketAddress retAddr;
	private byte[] packetData;
	FileDataAutoResponse(PrivateNetSocket privateNetSocket, InetSocketAddress retAddr, byte[] packetData) {
		this.privateNetSocket = privateNetSocket;
		this.retAddr = retAddr;
		this.packetData = packetData;
	}
	@Override
	public void execute() {
		String requestDataStr = new String(packetData, CharsetUtil.charSet());
		int index = requestDataStr.indexOf(CommunicationDefnition.KEY_FILENAME);
		String reqData = requestDataStr.substring(index, requestDataStr.length());
		String fileName = reqData.split(CommunicationDefnition.KEY_VAL_SEPALATOR)[1]
				.replaceAll(CommunicationDefnition.LINE_SPARATOR, "");
		LoggerUtil.info("requstFileData = " + fileName + " addr = " + this.retAddr.getHostName() + ":" + this.retAddr.getPort());
		byte[] fileData = WatchDirManager.getTargetOutFileData(fileName);
		if(fileData.length == 0) {
			LoggerUtil.info("REQUEST FILE = " + fileName);
			return;
		}
		StringBuilder message = new StringBuilder();
		message.append(CommunicationDefnition.SUBJ_REQUSET_FILE_ACK).append(CommunicationDefnition.LINE_SPARATOR)
			.append(CommunicationDefnition.KEY_FILENAME).append(CommunicationDefnition.KEY_VAL_SEPALATOR).append(fileName).append(CommunicationDefnition.LINE_SPARATOR)
			.append(CommunicationDefnition.KEY_FILEDATA).append(CommunicationDefnition.KEY_VAL_SEPALATOR);
		byte[] fileDataResponseBytes = new byte[0];
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			bos.write(message.toString().getBytes(CharsetUtil.charSet()));
			bos.write(fileData);
			bos.write(CommunicationDefnition.LINE_SPARATOR.getBytes(CharsetUtil.charSet()));
			bos.flush();
			fileDataResponseBytes = bos.toByteArray();
		} catch(IOException e) {
			LoggerUtil.warn(e);
			fileDataResponseBytes = new byte[0];
		}
		PrivateNetWorkCommonKey privateNetWorkCommonKey = NodeKeyStore.getPrivateNetworkCommonKey();
		byte[] encryptData = privateNetWorkCommonKey.encrypt(fileDataResponseBytes);
		
		byte[] sendData = new byte[0];
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			bos.write((CommunicationDefnition.SUBJ_REQUSER_COMMON_COM + CommunicationDefnition.LINE_SPARATOR).getBytes(CharsetUtil.charSet()));
			bos.write(encryptData);
			bos.flush();
			sendData = bos.toByteArray();
		} catch(IOException e) {
			LoggerUtil.warn(e);
			sendData = new byte[0];
		}
		if(sendData.length == 0) {
			return;
		}
		try {
			privateNetSocket.sendData(sendData, retAddr);
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
	}
}
