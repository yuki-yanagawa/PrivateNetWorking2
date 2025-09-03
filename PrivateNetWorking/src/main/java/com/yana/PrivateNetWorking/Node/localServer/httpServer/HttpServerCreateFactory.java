package com.yana.PrivateNetWorking.Node.localServer.httpServer;

import java.io.IOException;
import java.util.Optional;

public class HttpServerCreateFactory {
	public static IHttpServer createHttpServer() throws IOException {
		Optional<HttpServer> optSrv = HttpServer.createServer();
		if(optSrv.isPresent()) {
			return optSrv.get();
		}
		throw new IOException("create server error");
	}
}
