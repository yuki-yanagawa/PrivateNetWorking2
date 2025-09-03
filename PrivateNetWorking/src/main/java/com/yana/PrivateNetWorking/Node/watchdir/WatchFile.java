package com.yana.PrivateNetWorking.Node.watchdir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.yana.PrivateNetWorking.common.logger.LoggerUtil;

public class WatchFile {
	private static final String FILE_SEPARATOR = File.separator;
	private Path path;
	private long filesize;
	public WatchFile(String filename, Path parentPath) {
		this.path = Paths.get(parentPath.toAbsolutePath().toString() + File.separator + filename);
		try {
			this.filesize = Files.size(this.path);
		} catch(IOException e) {
			this.filesize = -1;
			LoggerUtil.warn(e);
		}
	}

	public byte[] readData() {
		return new byte[0];
	}

	public long getFileSize() {
		return filesize;
	}
}
