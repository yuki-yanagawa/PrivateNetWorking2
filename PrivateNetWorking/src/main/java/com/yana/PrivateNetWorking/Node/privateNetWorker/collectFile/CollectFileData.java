package com.yana.PrivateNetWorking.Node.privateNetWorker.collectFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

public class CollectFileData {
	private static final byte CRBYTE = 13;
	private static final byte LFBYTE = 10;
	private static final Path COLLECT_PATH = Paths.get("collectdir");

	private byte[] responseData;
	public CollectFileData(byte[] responseData) {
		this.responseData = responseData;
	}

	public void execute() {
		int crlfLen = CommunicationDefnition.LINE_SPARATOR.length();
		int index = searchCRLF(0, responseData);
		//String dataHeader = new String(Arrays.copyOf(responseData, index), CharsetUtil.charSet());
		int fileNameLasIndex = searchCRLF(index + crlfLen, responseData);
		String fileNameLine = new String(Arrays.copyOfRange(responseData, index + crlfLen, fileNameLasIndex), CharsetUtil.charSet());
		String fileName = fileNameLine.split(CommunicationDefnition.KEY_VAL_SEPALATOR)[1].trim();
		byte searchByte = CommunicationDefnition.KEY_VAL_SEPALATOR.getBytes()[0];
		int separateIndex = -1;
		for(int i = fileNameLasIndex + crlfLen; i < responseData.length; i++) {
			if(searchByte == responseData[i]) {
				separateIndex = i;
				break;
			}
		}
		if(separateIndex == -1) {
			return;
		}
		byte[] fileDataBytes = Arrays.copyOfRange(responseData, separateIndex + 1, responseData.length - crlfLen);
		String collectFile = COLLECT_PATH.toAbsolutePath() + File.separator + fileName;
		try(FileOutputStream fos = new FileOutputStream(collectFile)) {
			fos.write(fileDataBytes);
			fos.flush();
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
	}

	private int searchCRLF(int startIndex, byte[] packetData) {
		for(int i = startIndex; i < packetData.length; i++) {
			if(packetData[i] != CRBYTE) {
				continue;
			}
			if(i + 1 >= packetData.length) {
				continue;
			}
			if(packetData[i + 1] == LFBYTE) {
				return i;
			}
		}
		return -1;
	}
}
