package com.yana.PrivateNetWorking.CentralRouter.analyzer;

public class RouterAnalyzerFactory {
	public static IAnalyzer createRouterReciver() {
		return new RouterAnalyzer();
	}
}
