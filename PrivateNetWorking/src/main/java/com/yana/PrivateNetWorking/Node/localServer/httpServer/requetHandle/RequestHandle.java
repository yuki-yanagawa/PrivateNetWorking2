package com.yana.PrivateNetWorking.Node.localServer.httpServer.requetHandle;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.requet.RequestData;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseData;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseDataCreator;
import com.yana.PrivateNetWorking.Node.localServer.router.Routing;
import com.yana.PrivateNetWorking.Node.localServer.socket.WrapperSocket;
import com.yana.PrivateNetWorking.Node.localServer.websocket.WebSocketManager;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;

class RequestHandle {
	private String threadName;
	private int readBufSize;
	private ByteBuffer buffer;
	RequestHandle(String threadName) {
		this.threadName = threadName;
		this.readBufSize = 2048;
	}

	void response(WrapperSocket sock) {
		byte[] readBuf = new byte[this.readBufSize];
		boolean isConnectKeep = false;
		RequestData requestData = null;
		try {
			DataInputStream is = sock.getInputStream();
			DataOutputStream os = sock.getOutputStream();
			sock.setSoTimeout(5000);
			try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
				int size = -1;
				boolean isOneceContinue = true;
				while(true) {
					try {
						size = is.read(readBuf);
						if(size == 0 && isOneceContinue) {
							isOneceContinue = false;
							Thread.sleep(10);
							continue;
						}
						if(size <= 0) {
							break;
						}
						bos.write(Arrays.copyOf(readBuf, size));
						if(size < this.readBufSize) {
							break;
						}
					} catch(SocketTimeoutException e) {
						break;
					} catch(InterruptedException e) {
						continue;
					}
				}
				byte[] reqBytes = bos.toByteArray();
				if(reqBytes.length == 0) {
					return;
				}
				requestData = RequestData.createRequestdata(bos.toByteArray());
			}
			ResponseData responseData = null;
			if(requestData == null) {
				responseData = ResponseDataCreator.createNotPageFound();
			} else {
				responseData = Routing.invoke(requestData);
			}
			if(responseData == null) {
				responseData = ResponseDataCreator.createNotPageFound();
			}
			os.write(responseData.changeByteArraysData());
			os.flush();
			isConnectKeep = responseData.getIsConnectKeep();
		} catch(IOException e) {
			LoggerUtil.warn(e);
		} finally {
			if(isConnectKeep && requestData != null) {
				WebSocketManager.connectUpgrade(sock, requestData.getRequestPath());
			} else {
				try {
					sock.close();
				} catch(IOException e) {
					LoggerUtil.warn(e);
				};
			}
		}
	}
}
