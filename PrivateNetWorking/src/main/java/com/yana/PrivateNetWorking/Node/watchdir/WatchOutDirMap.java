package com.yana.PrivateNetWorking.Node.watchdir;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WatchOutDirMap {
	private static Map<String, WatchFile> outfileMap = new ConcurrentHashMap<>();

	static synchronized void regist(String filename, WatchFile watchFile) {
		outfileMap.put(filename, watchFile);
	}

	static synchronized void update(String filename) {
		
	}

	static byte[] getWatchFileData(String filename) {
		return new byte[0];
	}

	static synchronized WatchFile removeWatchFileData(String filename) {
		return outfileMap.remove(filename);
	}

	static synchronized Map<String, WatchFile> getOutfileMap() {
		return outfileMap;
	}
}
