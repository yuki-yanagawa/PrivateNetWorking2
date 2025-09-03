package com.yana.PrivateNetWorking.Node.watchdir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.yana.PrivateNetWorking.common.logger.LoggerUtil;

public class WatchDirManager {
	private static final Path OUT_DIR = Paths.get("outdir");
	private static final Path COLLECT_DIR = Paths.get("collectdir");

	private static final WatchOutDirWorker WATCH_OUT_DIR_WORKER = new WatchOutDirWorker(OUT_DIR);

	public static void monitorStart() {
		for(File f : OUT_DIR.toFile().listFiles()) {
			if(f.isDirectory()) {
				continue;
			}
			WatchOutDirMap.regist(f.getName(), new WatchFile(f.getName(), OUT_DIR));
		}
		WATCH_OUT_DIR_WORKER.start();
	}

	//filename=XXXX:filesize=YYY
	public static List<String> getOutFileList() {
		Map<String, WatchFile> tmpMap = WatchOutDirMap.getOutfileMap();
		return tmpMap.entrySet().stream().map(e -> {
			String fileName = e.getKey();
			long filezie = e.getValue().getFileSize();
			if(filezie < 0) {
				return null;
			}
			return "filename=" + fileName + ":filesize=" + String.valueOf(filezie);
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}
}
