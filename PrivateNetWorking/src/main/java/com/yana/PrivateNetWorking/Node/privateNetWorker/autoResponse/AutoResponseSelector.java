package com.yana.PrivateNetWorking.Node.privateNetWorker.autoResponse;

import java.net.InetSocketAddress;

import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;
import com.yana.privateNetSocket2.PrivateNetSocket;

public class AutoResponseSelector {
	public static IAutoResponse select(PrivateNetSocket privateNetSocket, CommunicationCommand command, byte[] packetData, InetSocketAddress socketAddr) {
		switch(command) {
		case REQUSET_DIRLIST:
			return new DirListAutoResponse(privateNetSocket, socketAddr);
		case REQUEST_FILE:
			return new FileDataAutoResponse(privateNetSocket, socketAddr, packetData);
		case ACTIVATE:
			return new ActivateResponse(privateNetSocket, socketAddr);
		default:
			return null;
		}
	}
}
