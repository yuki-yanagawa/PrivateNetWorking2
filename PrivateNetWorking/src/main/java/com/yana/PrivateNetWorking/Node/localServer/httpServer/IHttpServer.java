package com.yana.PrivateNetWorking.Node.localServer.httpServer;

import java.io.IOException;
import java.net.Socket;

import com.yana.PrivateNetWorking.Node.localServer.socket.WrapperSocket;

public interface IHttpServer {
	public WrapperSocket await() throws IOException;
	public int getLocalPort();
}
