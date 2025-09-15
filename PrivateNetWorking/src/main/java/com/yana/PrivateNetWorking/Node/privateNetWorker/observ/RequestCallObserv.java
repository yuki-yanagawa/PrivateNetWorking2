package com.yana.PrivateNetWorking.Node.privateNetWorker.observ;

import java.net.InetSocketAddress;

import com.yana.PrivateNetWorking.Node.localServer.websocket.WebSocketManager;
import com.yana.PrivateNetWorking.Node.privateNetWorker.WrapAcceptPrivateNetPacketData;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

public class RequestCallObserv  implements IObserver {

	@Override
	public void update(WrapAcceptPrivateNetPacketData data) {
		InetSocketAddress addr = data.getAcceptSocketAddress();
		String host = addr.getAddress().getHostAddress();
		int port = addr.getPort();
		String requestUser = host + ":" + String.valueOf(port);

		String userName = "";
		for(String tmp : new String(data.getAcceptPacketData(), CharsetUtil.charSet()).split(CommunicationDefnition.LINE_SPARATOR)) {
			if(tmp.startsWith(CommunicationDefnition.KEY_CALLREQUEST_NAME)) {
				userName = tmp.split(CommunicationDefnition.KEY_VAL_SEPALATOR)[1];
			}
		}
		byte[] notifyData = (CommunicationDefnition.SUBJ_REQUSET_CALL + CommunicationDefnition.LINE_SPARATOR +
				CommunicationDefnition.KEY_CALLREQUEST_NAME + CommunicationDefnition.KEY_VAL_SEPALATOR + userName + CommunicationDefnition.LINE_SPARATOR +
				CommunicationDefnition.KEY_CALLREQUEST_ADDR + CommunicationDefnition.KEY_VAL_SEPALATOR + requestUser + CommunicationDefnition.LINE_SPARATOR)
				.getBytes(CharsetUtil.charSet());
		WebSocketManager.notifyUpdateInfo(notifyData);
	}
}
