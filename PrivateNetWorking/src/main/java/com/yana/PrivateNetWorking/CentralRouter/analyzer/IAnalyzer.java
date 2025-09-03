package com.yana.PrivateNetWorking.CentralRouter.analyzer;

import com.yana.privateNetSocket2.PrivateNetSocket;

public interface IAnalyzer extends Runnable {
	public void setPrivateNetSocket(PrivateNetSocket privateNetSocket);
}
