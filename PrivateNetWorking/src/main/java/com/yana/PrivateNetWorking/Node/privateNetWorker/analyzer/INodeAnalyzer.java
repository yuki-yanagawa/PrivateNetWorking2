package com.yana.PrivateNetWorking.Node.privateNetWorker.analyzer;

import com.yana.privateNetSocket2.PrivateNetSocket;

public interface INodeAnalyzer extends Runnable {
	public void setPrivateNetSocket(PrivateNetSocket privateNetSocket);
}
