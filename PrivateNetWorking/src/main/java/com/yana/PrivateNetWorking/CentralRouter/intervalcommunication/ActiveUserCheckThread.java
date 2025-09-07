package com.yana.PrivateNetWorking.CentralRouter.intervalcommunication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import com.yana.PrivateNetWorking.CentralRouter.analyzer.MemberCache;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;
import com.yana.privateNetSocket2.PrivateNetSocket;

public class ActiveUserCheckThread extends Thread {
	private PrivateNetSocket privateNetSocket;
	private boolean executeFlg;
	private int intervalSec;
	private Thread thread;
	public ActiveUserCheckThread(PrivateNetSocket privateNetSocket) {
		this.privateNetSocket = privateNetSocket;
		this.executeFlg = true;
		this.intervalSec = 180;
	}

	public void run() {
		//Thread setting
		this.thread = Thread.currentThread();
		while(this.executeFlg) {
			activeUserCheck();
			intervalWait();
		}
	}

	private void activeUserCheck() {
		List<String> tmpUserList = MemberCache.getInstance().getDisplayStyleAndIdentityCode();
		String displayMemberList = tmpUserList.get(1);
		StringBuilder sb = new StringBuilder();
		sb.append(CommunicationDefnition.SUBJ_ACTIVATE).append(CommunicationDefnition.LINE_SPARATOR);
		byte[] sendData = sb.toString().getBytes(CharsetUtil.charSet());
		boolean isNeedNotify = false;
		for(String userData : displayMemberList.split(",")) {
			String[] tmps = userData.split("\\s+");
			String username = tmps[0].replaceAll("username:", "");
			String[] tmpAddr = tmps[1].split(":");
			String ipAddr = tmpAddr[0].trim();
			int port = Integer.parseInt(tmpAddr[1].trim());
			InetSocketAddress inetSocketAddress = new InetSocketAddress(ipAddr, port);
			byte[] responseData = sendActiveCheckMessage(inetSocketAddress, sendData);
			if(responseData.length == 0 || isActivate(responseData)) {
				isNeedNotify = MemberCache.getInstance().deActivateUser(inetSocketAddress, username) || isNeedNotify;
			} else {
				MemberCache.getInstance().checkedActivateUser(inetSocketAddress, username);
			}
		}
		if(isNeedNotify) {
			
		}
	}

	private boolean isActivate(byte[] responseData) {
		String str = new String(responseData, CharsetUtil.charSet());
		return str.split(CommunicationDefnition.LINE_SPARATOR)[0].trim().equals(CommunicationDefnition.SUBJ_ACTIVATE_ACK);
	}

	private byte[] sendActiveCheckMessage(InetSocketAddress socketAddress, byte[] sendData) {
		FurtuerActivateResult.initSetTargetData(socketAddress);
		try {
			privateNetSocket.sendData(sendData, socketAddress);
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
		return FurtuerActivateResult.getResult();
	}

	private void intervalWait() {
		try {
			Thread.sleep(this.intervalSec * 1000);
		} catch(InterruptedException e) {
			//e.printStackTrace();
		}
	}

	public void closeEvent() {
		this.executeFlg = false;
		this.thread.interrupt();
	}
}
