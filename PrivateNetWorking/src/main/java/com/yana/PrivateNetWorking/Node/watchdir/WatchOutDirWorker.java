package com.yana.PrivateNetWorking.Node.watchdir;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import com.yana.PrivateNetWorking.common.logger.LoggerUtil;

class WatchOutDirWorker extends Thread {
	private Path targetPath;
	private boolean executeFlg = true;
	WatchOutDirWorker(Path targetPath) {
		this.targetPath = targetPath;
	}

	Thread getThread() {
		return Thread.currentThread();
	}

	public void run() {
		try(WatchService ws = FileSystems.getDefault().newWatchService()) {
			targetPath.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
			while(executeFlg) {
				try {
					WatchKey key = ws.take();
					for(WatchEvent<?> event : key.pollEvents()) {
						Kind<?> kind = event.kind();
						if(kind == StandardWatchEventKinds.ENTRY_CREATE) {
							WatchOutDirMap.regist(event.context().toString(), new WatchFile(event.context().toString(), targetPath));
						}
						if(kind == StandardWatchEventKinds.ENTRY_DELETE) {
							WatchOutDirMap.removeWatchFileData(event.context().toString());
						}
						if(kind == StandardWatchEventKinds.ENTRY_MODIFY) {
							WatchOutDirMap.update(event.context().toString());
						}
					}
				} catch(InterruptedException e) {
					
				}
			}
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
	}
}
