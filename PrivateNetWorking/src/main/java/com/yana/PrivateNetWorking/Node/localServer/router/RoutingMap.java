package com.yana.PrivateNetWorking.Node.localServer.router;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.requet.RequestDataDefnition.METHOD;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;

class RoutingMap {
	private static final Path ROUTING_PATH = Paths.get("conf/routing.txt");

	private static RoutingMap instance;
	private Map<METHOD, Map<String, String>> routingMap = new HashMap<>();
	private RoutingMap(Map<METHOD, Map<String, String>> routingMap) {
		this.routingMap = routingMap;
	}
	static synchronized void init() {
		if(instance != null) {
			return;
		}
		try(BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(ROUTING_PATH.toFile())))) {
			Map<METHOD, Map<String, String>> routingMap = new HashMap<>();
			String readLine = null;
			while((readLine = buf.readLine()) != null) {
				String[] readLines = readLine.split("\\s+");
				METHOD m = searchMethod(readLines[0].trim().toUpperCase());
				if(readLines.length != 3 || m == null) {
					continue;
				}
				Map<String, String> innerMap = routingMap.computeIfAbsent(m, (key) -> new HashMap<>());			
				innerMap.put(readLines[1].trim(), readLines[2].trim());
			}
			instance = new RoutingMap(routingMap);
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
	}

	private static METHOD searchMethod(String method) {
		for(METHOD m : METHOD.values()) {
			if(m.name().equals(method)) {
				return m;
			}
		}
		return null;
	}

	static String getValue(METHOD method, String requestPath) {
		Map<String, String> inner = instance.routingMap.get(method);
		return inner.get(requestPath);
	}
}
