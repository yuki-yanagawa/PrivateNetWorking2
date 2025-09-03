package com.yana.PrivateNetWorking.Node.privateNetWorker;

import com.yana.privateNetSocket2.PrivateNetSocket;
import com.yana.privateNetSocket2.exception.PrivateNetSocketException;

public class PrivateNetSocketCreator {
	private static int START_PORT = 9050;
	private static int MAX_PORT = 9060;
	public static PrivateNetSocket create() {
		int tmpPort = START_PORT;
		PrivateNetSocket privateNetSocket = null;
		while(true) {
			try {
				privateNetSocket = PrivateNetSocket.createPrivateNetSocket(tmpPort);
				break;
			} catch(PrivateNetSocketException e) {
				tmpPort++;
				if(MAX_PORT < tmpPort) {
					privateNetSocket = null;
					break;
				}
				try {
					Thread.sleep(100);
				} catch(InterruptedException ie) {
				}
				continue;
			}
		}
		return privateNetSocket;
	}
}
