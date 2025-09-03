package com.yana.PrivateNetWorking.Node.localServer.httpServer.response;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

class ResponseBodyCache {
	private static Map<String, ResponseBody> resposeBodyCacheMap = new HashMap<>();

	static ResponseBody getResponseBody(String key) {
		ResponseBody responseBody = null;
		synchronized (resposeBodyCacheMap) {
			responseBody = resposeBodyCacheMap.get(key);
			resposeBodyCacheMap.notifyAll();
		}
		if(responseBody == null) {
			return null;
		}
		long updateTime = responseBody.getLastUpdateTime();
		if(Paths.get(key).toFile().lastModified() > updateTime) {
			return null;
		}
		return responseBody;
	}

	static void addResponseBody(String key, ResponseBody responseBody) {
		synchronized (resposeBodyCacheMap) {
			resposeBodyCacheMap.put(key, responseBody);
			resposeBodyCacheMap.notifyAll();
		}
	}
}
