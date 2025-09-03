package com.yana.PrivateNetWorking.Node.localServer.controller;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseData;

public class TestController extends AbstractController {
	public ResponseData main() {
		return render("html/index.html");
	}
}
