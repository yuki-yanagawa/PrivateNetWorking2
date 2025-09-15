package com.yana.PrivateNetWorking.Node.localServer.websocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;

import com.yana.PrivateNetWorking.Node.localServer.socket.WrapperSocket;
import com.yana.privateNetSocket2.PrivateNetSocket;

abstract class WebSocketHandler implements Runnable {
	protected WrapperSocket socket;
	protected boolean executeFlg;
	protected DataInputStream inputStream;
	protected DataOutputStream outputStream;
	protected PrivateNetSocket privateNetSocket;
	protected InetSocketAddress remoteAddr;

	WebSocketHandler(WrapperSocket socket) {
		this.socket = socket;
		this.executeFlg = true;
		this.inputStream = this.socket.getInputStream();
		this.outputStream = this.socket.getOutputStream();
	}

	public void setExecuteFlg(boolean executeFlg) {
		this.executeFlg = executeFlg;
	}

	public abstract void sendData(byte[] data);

	void setRemoteSocketAddr(PrivateNetSocket privateNetSocket, InetSocketAddress remoteAddr) {
		this.privateNetSocket = privateNetSocket;
		this.remoteAddr = remoteAddr;
	}
}
