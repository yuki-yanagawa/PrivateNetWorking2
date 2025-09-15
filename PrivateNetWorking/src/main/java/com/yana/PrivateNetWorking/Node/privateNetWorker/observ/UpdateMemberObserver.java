package com.yana.PrivateNetWorking.Node.privateNetWorker.observ;

import com.yana.PrivateNetWorking.Node.localServer.websocket.WebSocketManager;
import com.yana.PrivateNetWorking.Node.privateNetWorker.WrapAcceptPrivateNetPacketData;

public class UpdateMemberObserver implements IObserver {

	@Override
	public void update(WrapAcceptPrivateNetPacketData data) {
		WebSocketManager.notifyUpdateInfo(data.getAcceptPacketData());
	}

}
