package com.yana.PrivateNetWorking.CentralRouter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yana.PrivateNetWorking.CentralRouter.analyzer.IAnalyzer;
import com.yana.PrivateNetWorking.CentralRouter.analyzer.RouterAnalyzerFactory;
import com.yana.PrivateNetWorking.CentralRouter.cert.RouterCertCreator;
import com.yana.PrivateNetWorking.CentralRouter.keygen.KeyGenerateHelper;
import com.yana.PrivateNetWorking.CentralRouter.prop.RouterProperties;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.privateNetSocket2.PrivateNetSocket;
import com.yana.privateNetSocket2.exception.PrivateNetSocketException;

public class CentralRouterEntry {
	public static void main(String[] args) {
		setSystemProperties();
		PrivateNetSocket privateNetSocket = null;
		try {
			privateNetSocket = PrivateNetSocket.createPrivateNetSocket(9898);
			RouterProperties.initSetting();
			LoggerUtil.setLogger("CentralRouterLogger", "CentralRouter.log");
			boolean isCreateKey = Boolean.parseBoolean(System.getProperty("com.yana.PrivateNetWorking.CenralRouterCreateKey"));
			KeyGenerateHelper keyGenerateHelper = KeyGenerateHelper.getInstance();
			if(isCreateKey || !Paths.get("keys/keyStore").toFile().exists()) {
				RouterCertCreator.create();
				keyGenerateHelper.createKeyStore();
			}
			keyGenerateHelper.setKeysFromKeyStroe();
		} catch(PrivateNetSocketException e) {
			close(privateNetSocket);
			System.exit(-1);
		} catch(Exception e) {
			close(privateNetSocket);
			LoggerUtil.warn(e);
			System.exit(-1);
		}
		int recvierCount = 5;
		ExecutorService analyzerService = Executors.newFixedThreadPool(recvierCount);
		launchReciver(analyzerService, privateNetSocket, recvierCount);

//		ExecutorService senderService = Executors.newFixedThreadPool(1);
//		launchSender(senderService);
		
		Runtime.getRuntime().addShutdownHook(new ShutDownEvent(privateNetSocket, analyzerService));
	}

	private static void close(PrivateNetSocket privateNetSocket) {
		if(privateNetSocket == null) {
			return;
		}
		try {
			privateNetSocket.close();
		} catch(IOException e) {
			
		}
	}

	private static void setSystemProperties() {
		setDefaultSystemProperty("com.yana.PrivateNetWorking.CenralRouterCreateKey", "fales");
		setDefaultSystemProperty("com.yana.PrivateNetWorking.CenralRouterPass", "routerPass");
		setDefaultSystemProperty("logOutConsole", "true");
	}

	private static void setDefaultSystemProperty(String key, String value) {
		if(System.getProperty(key) != null) {
			return;
		}
		System.setProperty(key, value);
	}

	private static void launchReciver(ExecutorService recivers, PrivateNetSocket privateNetSocket, int reciverThreadCount) {
		for(int i = 0; i < reciverThreadCount; i++) {
			IAnalyzer analyzer = RouterAnalyzerFactory.createRouterReciver();
			analyzer.setPrivateNetSocket(privateNetSocket);
			recivers.execute(analyzer);
		}
	}

	private static void launchSender(ExecutorService senders) {
//		senders.execute(command);
	}

	private static class ShutDownEvent extends Thread {
		PrivateNetSocket privateNetSocket;
		ExecutorService analyzerService;
		ShutDownEvent(PrivateNetSocket privateNetSocket, ExecutorService analyzerService) {
			this.privateNetSocket = privateNetSocket;
			this.analyzerService = analyzerService;
		}

		@Override
		public void run() {
			close(privateNetSocket);
			analyzerService.shutdown();
			System.out.println("END TEST!!!");
		}
	}
}
