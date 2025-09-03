package com.yana.PrivateNetWorking.Node.localServer.httpServer.requet;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RequestDataDefnition {
	public static Charset CHARSET = StandardCharsets.UTF_8;
	public static String HTTP_PLOTO_SEPARATOR = "\r\n";
	public static String HTTP_REQUEST_HEAD_KEYVAL_SEPARATOR = ":";

	public enum METHOD {
		GET, POST, PUT, DELETE,
	}
}
