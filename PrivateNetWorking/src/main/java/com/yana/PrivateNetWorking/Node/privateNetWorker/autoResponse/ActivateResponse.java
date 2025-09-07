package com.yana.PrivateNetWorking.Node.privateNetWorker.autoResponse;

import java.net.InetSocketAddress;

import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.privateNetSocket2.PrivateNetSocket;

class ActivateResponse implements IAutoResponse {
	private PrivateNetSocket privateNetSocket;
	private InetSocketAddress retAddr;
	ActivateResponse(PrivateNetSocket privateNetSocket, InetSocketAddress retAddr) {
		this.privateNetSocket = privateNetSocket;
		this.retAddr = retAddr;
	}

	@Override
	public void execute() {
		StringBuilder sb = new StringBuilder();
		sb.append(CommunicationDefnition.SUBJ_ACTIVATE_ACK)
			.append(CommunicationDefnition.LINE_SPARATOR);
		
	}
}
