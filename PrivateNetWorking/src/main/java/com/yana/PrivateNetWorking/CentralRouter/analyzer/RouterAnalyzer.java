package com.yana.PrivateNetWorking.CentralRouter.analyzer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import com.yana.PrivateNetWorking.CentralRouter.action.ActionSelector;
import com.yana.PrivateNetWorking.CentralRouter.action.IAction;
import com.yana.PrivateNetWorking.CentralRouter.keygen.KeyGenerateHelper;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommandDecider;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;
import com.yana.privateNetSocket2.PrivateNetSocket;
import com.yana.privateNetSocket2.packet.PrivateNetPacket;

public class RouterAnalyzer implements IAnalyzer {
	private PrivateNetSocket privateNetSocket;
	private boolean isRuning = true;
	private MemberCache memberCache = MemberCache.getInstance();

	@Override
	public void run() {
		while(isRuning) {
			System.out.println("Wait : " + Thread.currentThread().getName());
			PrivateNetPacket  privateNetPacket = privateNetSocket.takePrivateNetPacketData();
			System.out.println("Get : " + Thread.currentThread().getName());
			byte[] packetData = privateNetPacket.getPacketData();
			InetSocketAddress socketAddr = privateNetPacket.getSocketAddress();
			analyzePacket(packetData, socketAddr);
		}
	}

	@Override
	public void setPrivateNetSocket(PrivateNetSocket privateNetSocket) {
		this.privateNetSocket = privateNetSocket;
	}

	private void analyzePacket(byte[] packetData, InetSocketAddress socketAddress) {
		String[] requetLines = new String(packetData, CharsetUtil.charSet()).split(CommunicationDefnition.LINE_SPARATOR);
		CommunicationCommand communication = CommunicationCommandDecider.dicede(requetLines[0]);
		IAction action = null;
		switch(communication) {
		case JOIN:
			LoggerUtil.info("JOIN : " + socketAddress);
			String joinUserName = "unknown";
			if(requetLines[1].trim().startsWith(CommunicationDefnition.KEY_USER_NAME)) {
				joinUserName = requetLines[1].split(CommunicationDefnition.KEY_VAL_SEPALATOR)[1].trim();
			}
			memberCache.registMember(socketAddress, joinUserName);
			responseJoinAck(socketAddress);
			notifyUpdateMember();
			break;
		case REQUEST_COMMONKEY:
			LoggerUtil.info("REQUEST_COMMONKEY : " + socketAddress);
			action = ActionSelector.select(CommunicationCommand.REQUEST_COMMONKEY, privateNetSocket, socketAddress);
			break;
		case DISCONNECT:
			LoggerUtil.info("DISCONNECT : " + socketAddress);
			action = ActionSelector.select(CommunicationCommand.DISCONNECT, privateNetSocket, socketAddress);
			break;
		case NO_RESPONSE_USER:
			break;
		default:
			break;
		}
		if(action != null) {
			action.execute(requetLines);
		}
	}

	private void responseJoinAck(InetSocketAddress socketAddress) {
		StringBuilder sb = new StringBuilder();
		sb.append(CommunicationDefnition.SUBJ_JOIN_ACK);
		sb.append(CommunicationDefnition.LINE_SPARATOR);
		sb.append(CommunicationDefnition.KEY_SERVER_CERT);
		sb.append(CommunicationDefnition.KEY_VAL_SEPALATOR);
		sb.append(KeyGenerateHelper.getX509Certificate());
		sb.append(CommunicationDefnition.LINE_SPARATOR);
		byte[] sendData = sb.toString().getBytes(CharsetUtil.charSet());
		try {
			privateNetSocket.sendData(sendData, socketAddress);
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
	}

	private void notifyUpdateMember() {
		List<String> tmpcodeList = memberCache.getDisplayStyleAndIdentityCode();
		String memberList = tmpcodeList.get(0);
		String displayMemberList = tmpcodeList.get(1);
		StringBuilder sb = new StringBuilder();
		sb.append(CommunicationDefnition.SUBJ_UPDATE_MEMBER).append(CommunicationDefnition.LINE_SPARATOR)
			.append(CommunicationDefnition.KEY_MEMBERLIST)
			.append(CommunicationDefnition.KEY_VAL_SEPALATOR)
			.append(displayMemberList).append(CommunicationDefnition.LINE_SPARATOR);
		byte[] sendData = sb.toString().getBytes(CharsetUtil.charSet());
		String[] ipAndPorts = memberList.split(",");
		for(String ipAndPort : ipAndPorts) {
			String ipAddr = ipAndPort.split(":")[0].trim();
			int port = Integer.parseInt(ipAndPort.split(":")[1].trim());
			InetSocketAddress inetSocketAddress = new InetSocketAddress(ipAddr, port);
			try {
				privateNetSocket.sendData(sendData, inetSocketAddress);
			} catch(IOException e) {
				LoggerUtil.warn(e);
			}
		}
	}
}
