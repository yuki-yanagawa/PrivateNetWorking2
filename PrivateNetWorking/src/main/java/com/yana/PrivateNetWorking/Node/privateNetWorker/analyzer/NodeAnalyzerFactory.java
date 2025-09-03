package com.yana.PrivateNetWorking.Node.privateNetWorker.analyzer;

public class NodeAnalyzerFactory {
	public static INodeAnalyzer createNodeAnalyzer() {
		return new NodeAnalyzer();
	}
}
