package com.yana.PrivateNetWorking.Node.localServer.controller;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseData;

public class FileReadController extends AbstractController {
	private static String JSDIR = "javascript";
	private static String CSSDIR = "css";
	public ResponseData readFileJS() {
		String requestPath = requestData.getRequestPath();
		if(!requestPath.startsWith(JSDIR) && !requestPath.startsWith("/" + JSDIR)) {
			if(requestPath.startsWith("/")) {
				requestPath = JSDIR + requestPath;
			} else {
				requestPath = JSDIR + "/" + requestPath;
			}
		}
		return render(requestPath);
	}

	public ResponseData readFileCSS() {
		String requestPath = requestData.getRequestPath();
		if(!requestPath.startsWith(CSSDIR) && !requestPath.startsWith("/" + CSSDIR)) {
			if(requestPath.startsWith("/")) {
				requestPath = CSSDIR + requestPath;
			} else {
				requestPath = CSSDIR + "/" + requestPath;
			}
		}
		return render(requestPath);
	}
}
