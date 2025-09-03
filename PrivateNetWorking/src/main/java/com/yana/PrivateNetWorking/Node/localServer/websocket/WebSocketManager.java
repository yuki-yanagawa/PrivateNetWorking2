package com.yana.PrivateNetWorking.Node.localServer.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.yana.PrivateNetWorking.Node.localServer.socket.WrapperSocket;

public class WebSocketManager {
	private static final String WEB_SOCK_ACT1 = "/connectWebSocket";
	private static final String WEB_SOCK_ACT2 = "";
	private static ExecutorService executorService = Executors.newFixedThreadPool(2);
	private static Map<String, WebSocketHandler> webSocketMap = new HashMap<>();
	public static synchronized void connectUpgrade(WrapperSocket socket, String requestPath) {
		requestPath = requestPath.trim();
		if(!WEB_SOCK_ACT1.equals(requestPath) && !WEB_SOCK_ACT2.equals(requestPath)) {
			return;
		}
		WebSocketHandler webSocketHandler = new WebSocketHandler(socket);
		webSocketMap.put(requestPath, webSocketHandler);
		executorService.execute(webSocketHandler);
	}

	public static void notifyUpdateInfo(byte[] data) {
		WebSocketHandler handler = webSocketMap.get(WEB_SOCK_ACT1);
		if(handler == null) {
			return;
		}
		handler.sendData(data);
	}

	public static void cloesEvent() {
		executorService.shutdown();
		for(WebSocketHandler handler : webSocketMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList())) {
			handler.setExecuteFlg(false);
		}
	}
}
