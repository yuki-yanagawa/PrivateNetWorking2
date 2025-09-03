package com.yana.PrivateNetWorking.Node;

import java.io.IOException;

import com.yana.PrivateNetWorking.Node.gui.GuiController;
import com.yana.PrivateNetWorking.Node.keygen.KeyGenerateException;
import com.yana.PrivateNetWorking.Node.keygen.KeyGenratorHelper;
import com.yana.PrivateNetWorking.Node.localServer.router.Routing;
import com.yana.PrivateNetWorking.Node.localServer.websocket.WebSocketManager;
import com.yana.PrivateNetWorking.Node.watchdir.WatchDirManager;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.privateNetSocket2.PrivateNetSocket;

public class NodeEntry {
	public static void main(String[] args) {
//		GuiMain.start();
		setSystemProperties();
		try {
			Class.forName("com.yana.PrivateNetWorking.Node.localServer.websocket.WebSocketManager");
		} catch(ClassNotFoundException e) {
			System.exit(-1);
		}
		try {
			LoggerUtil.setLogger("NodeEntryLogger", "NodeEntry.log");
		} catch(IOException e) {
			System.exit(-1);
		}
		try {
			KeyGenratorHelper.createKeys();
		} catch(KeyGenerateException e) {
			System.exit(-1);
		}
		Routing.setUp();

		WatchDirManager.monitorStart();

		GuiController guiController = GuiController.displayOpen();
		Runtime.getRuntime().addShutdownHook(new ShutDownThread(guiController));
	}

	private static class ShutDownThread extends Thread {
		private GuiController guiController;
		ShutDownThread(GuiController guiController) {
			this.guiController = guiController;
		}
		@Override
		public void run() {
			PrivateNetSocket socket = guiController.getPrivateNetSocket();
			if(socket != null) {
				try {
					socket.close();
				} catch(IOException e) {
				}
			}
			WebSocketManager.cloesEvent();
			System.out.println("END!!!!");
		}
	}

	private static void setSystemProperties() {
		setDefaultSystemProperty("logOutConsole", "true");
	}

	private static void setDefaultSystemProperty(String key, String value) {
		if(System.getProperty(key) != null) {
			return;
		}
		System.setProperty(key, value);
	}
}
