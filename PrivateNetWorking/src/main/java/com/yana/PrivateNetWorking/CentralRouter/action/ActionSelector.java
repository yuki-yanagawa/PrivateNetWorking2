package com.yana.PrivateNetWorking.CentralRouter.action;

import java.net.InetSocketAddress;

import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;
import com.yana.privateNetSocket2.PrivateNetSocket;

public class ActionSelector {
	public static IAction select(CommunicationCommand command, PrivateNetSocket socket, InetSocketAddress socketAddress) {
		switch(command){
		case REQUEST_COMMONKEY:
			return new RequestCommonKeyAction(socket, socketAddress);
		case DISCONNECT:
			return new DisconnectAction(socketAddress);
		default:
			return null;
		}
	}
}
