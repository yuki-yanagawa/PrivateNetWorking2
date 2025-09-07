package com.yana.PrivateNetWorking.Node.watchdir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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

	public void updateFileInfo() {
		try {
			this.filesize = Files.size(this.path);
		} catch(IOException e) {
			this.filesize = -1;
			LoggerUtil.warn(e);
		}
	}

	public byte[] getFileData() {
		byte[] readBuff = new byte[4096];
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
			FileInputStream fis = new FileInputStream(this.path.toFile())) {
			int readSize = -1;
			while((readSize = fis.read(readBuff)) > 0) {
				if(readSize < readBuff.length) {
					bos.write(Arrays.copyOf(readBuff, readSize));
				} else {
					bos.write(readBuff);
				}
			}
			bos.flush();
			return bos.toByteArray();
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
		return new byte[0];
	}
}
