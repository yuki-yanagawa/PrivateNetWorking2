package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;
import com.yana.privateNetSocket2.PrivateNetSocket;

class CommandPrivateNetHelper {
	private static final Path PROP_PATH = Paths.get("conf/NodePrivateWorkerSetting.properties");
	private PrivateNetSocket privateNetSocket;
	private InetSocketAddress routerAddr;
	private InetSocketAddress activeUserAddr;
	private Properties prop;
	private CommandPrivateNetHelper(PrivateNetSocket privateNetSocket, InetSocketAddress routerAddr, Properties prop) {
		this.privateNetSocket = privateNetSocket;
		this.routerAddr = routerAddr;
		this.prop = prop;
	}

	static CommandPrivateNetHelper createNetCommandHelper(PrivateNetSocket privateNetSocket) {
		Properties prop = new Properties();
		try(FileInputStream fis = new FileInputStream(PROP_PATH.toFile())) {
			prop.load(fis);
		} catch(IOException e) {
			return null;
		}
		String addr = prop.get("centralRouterAddress").toString();
		int port = Integer.parseInt(prop.get("centralRouterPort").toString());
		InetSocketAddress inetSocketAddress = new InetSocketAddress(addr, port);
		return new CommandPrivateNetHelper(privateNetSocket, inetSocketAddress, prop);
	}

	void sendMessage(String message, InetSocketAddress socketAddress) {
		try {
			privateNetSocket.sendData(message.getBytes(CharsetUtil.charSet()), socketAddress);
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
	}

	void sendMessage(byte[] rawData, InetSocketAddress socketAddress) {
		try {
			privateNetSocket.sendData(rawData, socketAddress);
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
	}

	void sendMessageToRouter(String message) {
		try {
			privateNetSocket.sendData(message.getBytes(CharsetUtil.charSet()), routerAddr);
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
	}
}
