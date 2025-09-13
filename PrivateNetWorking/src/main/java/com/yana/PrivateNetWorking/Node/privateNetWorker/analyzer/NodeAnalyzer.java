package com.yana.PrivateNetWorking.Node.privateNetWorker.analyzer;

import java.net.InetSocketAddress;
import java.util.Arrays;

import com.yana.PrivateNetWorking.Node.keyStore.NodeKeyStore;
import com.yana.PrivateNetWorking.Node.privateNetWorker.accept.Acceptor;
import com.yana.PrivateNetWorking.Node.privateNetWorker.autoResponse.AutoResponseSelector;
import com.yana.PrivateNetWorking.Node.privateNetWorker.autoResponse.IAutoResponse;
import com.yana.PrivateNetWorking.Node.privateNetWorker.collectFile.CollectFileData;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.CommandOperator;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.ICommand;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.result.CommandResultSelector;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.result.ICommandResult;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommandDecider;
import com.yana.PrivateNetWorking.common.key.PrivateNetWorkCommonKey;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;
import com.yana.privateNetSocket2.PrivateNetSocket;
import com.yana.privateNetSocket2.packet.PrivateNetPacket;

class NodeAnalyzer implements INodeAnalyzer {
	private static final byte CRBYTE = 13;
	private static final byte LFBYTE = 10;
	private PrivateNetSocket privateNetSocket;
	private boolean isRuning = true;
	private Acceptor acceptor;

	@Override
	public void setPrivateNetSocket(PrivateNetSocket privateNetSocket) {
		this.privateNetSocket = privateNetSocket;
		this.acceptor = Acceptor.getInstance();
	}

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

	private void analyzePacket(byte[] packetData, InetSocketAddress socketAddress) {
		int headerIndex = searchCRLF(packetData);
		String headerStr = new String(Arrays.copyOf(packetData, headerIndex),CharsetUtil.charSet());
		CommunicationCommand communication = CommunicationCommandDecider.dicede(headerStr);
		ICommandResult commandResult = null;
		switch(communication) {
		case JOIN_ACK:
			commandResult = CommandResultSelector.selectCommandResult(CommunicationCommand.JOIN);
			//common key get action
			requestCommonKeys();
			break;
		case REQUEST_COMMONKEY_ACK:
			commandResult = CommandResultSelector.selectCommandResult(CommunicationCommand.REQUEST_COMMONKEY);
			break;
		case UPDATE_MEMBER:
			// Acceptor
			acceptor.addResponseData(communication, packetData);
			//UpdateMemberCheckCommandResult.setLatestUpdateUsers(packetData);
			break;
		case REQUSER_COMMON_COM:
			decrypt(packetData, socketAddress, headerIndex);
			break;
		case ACTIVATE:
			autoResponse(packetData, socketAddress, communication);
			break;
		default:
			break;
		}
		if(commandResult != null) {
			commandResult.settigResopnseData(packetData);
		}
	}

	private void analyzeCommonPacket(byte[] packetData, InetSocketAddress socketAddress) {
		int headerIndex = searchCRLF(packetData);
		String headerStr = new String(Arrays.copyOf(packetData, headerIndex),CharsetUtil.charSet());
		CommunicationCommand communication = CommunicationCommandDecider.dicede(headerStr);
		ICommandResult commandResult = null;
		switch(communication) {
		case REQUSET_DIRLIST:
			// Auto Response
			autoResponse(packetData, socketAddress, communication);
			break;
		case REQUSET_DIRLIST_ACK:
			commandResult = CommandResultSelector.selectCommandResult(CommunicationCommand.REQUSET_DIRLIST);
			break;
		case REQUEST_FILE:
			// Auto Response
			autoResponse(packetData, socketAddress, communication);
			break;
		case REQUEST_FILE_ACK:
			collectFileData(packetData);
			// Acceptor
			acceptor.addResponseData(communication, packetData);
			break;
		default:
			break;
		}
		if(commandResult != null) {
			commandResult.settigResopnseData(packetData);
		}
	}

	private int searchCRLF(byte[] packetData) {
		for(int i = 0; i < packetData.length; i++) {
			if(packetData[i] != CRBYTE) {
				continue;
			}
			if(i + 1 >= packetData.length) {
				continue;
			}
			if(packetData[i + 1] == LFBYTE) {
				return i;
			}
		}
		return -1;
	}

	private void requestCommonKeys() {
		ICommand command = CommandOperator.command(CommunicationCommand.REQUEST_COMMONKEY);
		command.execute(null);
	}

	private void autoResponse(byte[] packetData, InetSocketAddress socketAddress, CommunicationCommand communication) {
		IAutoResponse autoResponse = AutoResponseSelector.select(this.privateNetSocket, communication, packetData, socketAddress);
		autoResponse.execute();
	}

	private void decrypt(byte[] packetData, InetSocketAddress socketAddress, int headerIndex) {
		byte[] cypryptBytes = Arrays.copyOfRange(packetData, headerIndex + 2, packetData.length);
		PrivateNetWorkCommonKey privateNetWorkCommonKey = NodeKeyStore.getPrivateNetworkCommonKey();
		byte[] newPacketData = privateNetWorkCommonKey.decrypt(cypryptBytes);
		if(newPacketData.length == 0) {
			return;
		}
		analyzeCommonPacket(newPacketData, socketAddress);
	}

	private void collectFileData(byte[] packetData) {
		new CollectFileData(packetData).execute();
	}
}
