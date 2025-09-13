package com.yana.PrivateNetWorking.Node.privateNetWorker.observ;

import com.yana.PrivateNetWorking.Node.localServer.websocket.WebSocketManager;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

public class AckFileObserver implements IObserver {

	@Override
	public void update(byte[] data) {
		WebSocketManager.notifyUpdateInfo(
				(CommunicationDefnition.SUBJ_REQUSET_FILE_ACK + CommunicationDefnition.LINE_SPARATOR).getBytes(CharsetUtil.charSet()));
	}
}
