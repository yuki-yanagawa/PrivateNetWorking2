package com.yana.PrivateNetWorking.CentralRouter.cert;

public class RouterCertPassManager {
	public static String getPass() {
		return System.getProperty("com.yana.PrivateNetWorking.CenralRouterPass");
	}
}
