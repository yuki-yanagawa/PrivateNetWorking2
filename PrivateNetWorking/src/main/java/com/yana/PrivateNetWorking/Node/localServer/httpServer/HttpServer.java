package com.yana.PrivateNetWorking.Node.localServer.httpServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

import com.yana.PrivateNetWorking.Node.localServer.socket.WrapperSocket;

class HttpServer implements IHttpServer{
	private static int MAX_PORT_NO = 65535;
	private static final Path PROP_PATH = Paths.get("conf/localHttpServer.properties");
	private static final String PROP_PROT_KEY = "defaultPort";
	private Properties prop;
	private ServerSocket svrSock;
	HttpServer(ServerSocket svrSock, Properties prop) {
		this.svrSock = svrSock;
		this.prop = prop;
	}

	static Optional<HttpServer> createServer() throws IOException {
		Properties prop = new Properties();
		try(FileInputStream fis = new FileInputStream(PROP_PATH.toFile())) {
			prop.load(fis);
		}
		ServerSocket serverSocket = new ServerSocket();
		int tmpPort = Integer.parseInt(prop.getProperty(PROP_PROT_KEY));
		for(int i = 0; i < MAX_PORT_NO; i++) {
			tmpPort += i;
			try {
				serverSocket.bind(new InetSocketAddress(tmpPort));
				return Optional.of(new HttpServer(serverSocket, prop));
			} catch(IOException e) {
				try {
					Thread.sleep(20);
				} catch(InterruptedException ie) {
					
				}
				continue;
			}
		}
		serverSocket.close();
		return Optional.empty();
	}

	@Override
	public WrapperSocket await() throws IOException {
		Socket socket = svrSock.accept();
		return WrapperSocket.create(socket);
	}

	@Override
	public int getLocalPort() {
		return svrSock.getLocalPort();
	}
}
