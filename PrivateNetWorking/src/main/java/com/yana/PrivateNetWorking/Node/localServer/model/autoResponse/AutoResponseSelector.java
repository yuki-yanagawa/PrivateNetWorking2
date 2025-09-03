package com.yana.PrivateNetWorking.Node.localServer.model.autoResponse;

import java.net.InetSocketAddress;

import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;
import com.yana.privateNetSocket2.PrivateNetSocket;

public class AutoResponseSelector {
	public static IAutoResponse select(PrivateNetSocket privateNetSocket, CommunicationCommand command, byte[] packetData, InetSocketAddress socketAddr) {
		switch(command) {
		case REQUSET_DIRLIST_ACK:
			return new DirListAutoResponse(privateNetSocket, socketAddr);
		default:
			return null;
		}
	}
}
