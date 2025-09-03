package com.yana.PrivateNetWorking.CentralRouter.prop;

import java.util.Properties;

public class RouterProperties {
	public static RouterProperties routerProperties;
	private Properties prop;
	private RouterProperties() {
		prop = new Properties();
	}

	public static synchronized void initSetting() {
		routerProperties = new RouterProperties();
		routerProperties.setProperty("certDir", "cert/");
		routerProperties.setProperty("keysDir", "keys/");
	}

	public static RouterProperties getInstance() {
		return routerProperties;
	}

	public void setProperty(String key, String value) {
		prop.setProperty(key, value);
	}

	public String getProperty(String key) {
		return prop.getProperty(key);
	}
}
