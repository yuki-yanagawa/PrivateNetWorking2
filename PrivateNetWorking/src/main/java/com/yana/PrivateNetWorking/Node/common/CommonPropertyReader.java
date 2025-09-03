package com.yana.PrivateNetWorking.Node.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CommonPropertyReader {
	private static final String PROP_PATH = "conf/common.properties";
	private static CommonPropertyReader commonPropertyReader = new CommonPropertyReader();
	private Properties properties;
	private CommonPropertyReader() {
		try(FileInputStream fis = new FileInputStream(PROP_PATH)) {
			properties.load(fis);
		} catch(IOException e) {
			
		}
	}

	public CommonPropertyReader getInstance() {
		return commonPropertyReader;
	}

	public String getValue(String key) {
		return properties.get(key).toString();
	}
}
