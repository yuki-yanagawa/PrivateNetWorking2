package com.yana.PrivateNetWorking.CentralRouter.intervalcommunication;

import java.net.InetSocketAddress;

public class FurtuerActivateResult {
	private static FurtuerActivateResult furtuerActivateResult = new FurtuerActivateResult();
	private byte[] responseData;
	private InetSocketAddress targetSockAddr;
	private FurtuerActivateResult() {
		
	}

	public static void initSetTargetData(InetSocketAddress targetSockAddr) {
		synchronized (furtuerActivateResult) {
			furtuerActivateResult.responseData = new byte[0];
			furtuerActivateResult.targetSockAddr = targetSockAddr;
		}
	}

	public static byte[] getResult() {
		synchronized (furtuerActivateResult) {
			if(furtuerActivateResult.responseData.length == 0) {
				furtuerActivateResult.waitInterval();
			}
			return furtuerActivateResult.responseData;
		}
	}

	public static void setResult(byte[] responseData, InetSocketAddress socketAddress) {
		synchronized (furtuerActivateResult) {
			if(!isTargetSocketAddr(socketAddress)) {
				return;
			}
			furtuerActivateResult.responseData = responseData;
			furtuerActivateResult.notifyAll();
		}
	}

	private static boolean isTargetSocketAddr(InetSocketAddress socketAddress) {
		return socketAddress.getHostName().equals(furtuerActivateResult.targetSockAddr)
				&& socketAddress.getPort() == furtuerActivateResult.targetSockAddr.getPort();
	}

	public void waitInterval() {
		try {
			furtuerActivateResult.wait(5000);
		} catch(InterruptedException e) {
			
		}
	}
}
