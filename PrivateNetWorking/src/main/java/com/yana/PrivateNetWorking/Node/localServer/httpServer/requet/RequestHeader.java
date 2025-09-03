package com.yana.PrivateNetWorking.Node.localServer.httpServer.requet;

import java.util.HashMap;
import java.util.Map;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.requet.RequestDataDefnition.METHOD;

public class RequestHeader {
	private METHOD method;
	private String requestPath;
	private String protocol;
	private Map<String, String> requestMap;
	
	private RequestHeader(METHOD method, String requestPath, String protocol, Map<String, String> requestMap) {
		this.method = method;
		this.requestPath = requestPath;
		this.protocol = protocol;
		this.requestMap = requestMap;
	}

	static RequestHeader createRequestHeader(String[] textBases) {
		String head = textBases[0];
		String[] heads = head.split("\\s+");
		METHOD method = searchMethod(heads[0].trim());
		if(method == null) {
			return null;
		}
		String requestData = heads[1].trim();
		String protocol = heads[2].trim();

		Map<String, String> map = new HashMap<>();
		for(int i = 1; i < textBases.length; i++) {
			if(!textBases[i].contains(RequestDataDefnition.HTTP_REQUEST_HEAD_KEYVAL_SEPARATOR)) {
				break;
			}
			String[] keyVals = textBases[i].split(RequestDataDefnition.HTTP_REQUEST_HEAD_KEYVAL_SEPARATOR);
			map.put(keyVals[0].trim().toUpperCase(), keyVals[1].trim());
		}
		return new RequestHeader(method, requestData, protocol, map);
	}

	private static METHOD searchMethod(String method) {
		String target = method.toUpperCase();
		for(METHOD m : METHOD.values()) {
			if(m.name().equals(target)) {
				return m;
			}
		}
		return null;
	}

	String getReqeustHeaderValue(String key, boolean isNeedUpperCase) {
		if(isNeedUpperCase) {
			key = key.toUpperCase();
		}
		return getReqeustHeaderValue(key);
	}

	private String getReqeustHeaderValue(String key) {
		return requestMap.get(key.toUpperCase());
	}

	METHOD getRequestMethod() {
		return method;
	}

	String getRequestPath() {
		return requestPath;
	}

	String getProtocol() {
		return protocol;
	}
}
