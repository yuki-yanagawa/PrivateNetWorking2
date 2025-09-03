package com.yana.PrivateNetWorking.Node.localServer.model.autoResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import org.json.JSONObject;

import com.yana.PrivateNetWorking.Node.keyStore.NodeKeyStore;
import com.yana.PrivateNetWorking.Node.watchdir.WatchDirManager;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.key.PrivateNetWorkCommonKey;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;
import com.yana.privateNetSocket2.PrivateNetSocket;

class DirListAutoResponse implements IAutoResponse {
	private PrivateNetSocket privateNetSocket;
	private InetSocketAddress retAddr;
	DirListAutoResponse(PrivateNetSocket privateNetSocket, InetSocketAddress retAddr) {
		this.retAddr = retAddr;
	}
	
	@Override
	public void execute() {
		List<String> list = WatchDirManager.getOutFileList();
		StringBuilder sb = new StringBuilder();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("fileList", list);
		sb.append(CommunicationDefnition.SUBJ_REQUSET_DIRLIST_ACK).append(CommunicationDefnition.LINE_SPARATOR)
			.append(jsonObj.toString());
		PrivateNetWorkCommonKey privateNetWorkCommonKey = NodeKeyStore.getPrivateNetworkCommonKey();
		byte[] encryptData = privateNetWorkCommonKey.encrypt(sb.toString().getBytes(CharsetUtil.charSet()));

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
