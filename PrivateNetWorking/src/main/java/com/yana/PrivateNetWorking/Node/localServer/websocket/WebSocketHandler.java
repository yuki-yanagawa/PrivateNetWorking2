package com.yana.PrivateNetWorking.Node.localServer.websocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

import com.yana.PrivateNetWorking.Node.localServer.socket.WrapperSocket;
import com.yana.PrivateNetWorking.Node.shutDownIF.InvokeShutDownEvent;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;

class WebSocketHandler implements Runnable {
	private WrapperSocket socket;
	private boolean executeFlg;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	WebSocketHandler(WrapperSocket socket) {
		this.socket = socket;
		this.executeFlg = true;
		this.inputStream = this.socket.getInputStream();
		this.outputStream = this.socket.getOutputStream();
	}
	@Override
	public void run() {
		try {
			socket.setSoTimeout(3000);
			while(executeFlg) {
				try {
					byte[] readData = WebSocketMessageUtil.messageDecoder(inputStream);
				} catch(SocketTimeoutException e) {
				}
			}
		} catch(IOException e) {
			LoggerUtil.warn(e);
		} finally {
			try {
				socket.close();
			} catch(IOException e) {
				
			}
			InvokeShutDownEvent.getInstance().invoke();
		}
	}

	public synchronized void sendData(byte[] data) {
		byte[] sendData = WebSocketMessageUtil.createSendMessage(data);
		try {
			outputStream.write(sendData);
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
	}

	public void setExecuteFlg(boolean executeFlg) {
		this.executeFlg = executeFlg;
	}
}
