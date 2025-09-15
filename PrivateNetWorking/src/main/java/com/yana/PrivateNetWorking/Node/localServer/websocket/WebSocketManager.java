package com.yana.PrivateNetWorking.Node.localServer.websocket;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.yana.PrivateNetWorking.Node.localServer.socket.WrapperSocket;
import com.yana.privateNetSocket2.PrivateNetSocket;

public class WebSocketManager {
	private enum CONNECT_TYPE {
		OBSERVER, CALL, NONE
	}
	private static final String WEB_SOCK_ACT1 = "/connectWebSocket";
	private static final String WEB_SOCK_ACT2 = "/connectCallSetting";
	private static ExecutorService executorService = Executors.newFixedThreadPool(2);
	private static Map<CONNECT_TYPE, WebSocketHandler> webSocketMap = new HashMap<>();
	public static synchronized void connectUpgrade(WrapperSocket socket, String requestPath) {
		CONNECT_TYPE type = CONNECT_TYPE.NONE;
		requestPath = requestPath.trim();
		WebSocketHandler handler = null;
		if(WEB_SOCK_ACT1.equals(requestPath)) {
			type = CONNECT_TYPE.OBSERVER;
			handler = new ObservHandler(socket);
		}
		if(WEB_SOCK_ACT2.equals(requestPath)) {
			type = CONNECT_TYPE.CALL;
			handler = new CallHandler(socket);
		}
		if(type == CONNECT_TYPE.NONE) {
			return;
		}
		if(webSocketMap.containsKey(type)) {
			return;
		}
		webSocketMap.put(type, handler);
		executorService.execute(handler);
	}

	public static void notifyUpdateInfo(byte[] data) {
		WebSocketHandler handler = webSocketMap.get(CONNECT_TYPE.OBSERVER);
		if(handler == null) {
			return;
		}
		handler.sendData(data);
	}

	public static void setPrivateCallTargetAddr(PrivateNetSocket privateNetSocket, InetSocketAddress remoteAddr) {
		WebSocketHandler handler = webSocketMap.get(CONNECT_TYPE.CALL);
		handler.setRemoteSocketAddr(privateNetSocket, remoteAddr);
	}

	public static void requestDelete() {
		synchronized (webSocketMap) {
			webSocketMap.remove(CONNECT_TYPE.CALL);
		}
	}

	public static void sendCallingDataFromRemote(byte[] data) {
		WebSocketHandler handler = webSocketMap.get(CONNECT_TYPE.CALL);
		handler.sendData(data);
	}

	public static void cloesEvent() {
		executorService.shutdown();
		for(WebSocketHandler handler : webSocketMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList())) {
			handler.setExecuteFlg(false);
		}
	}
}
