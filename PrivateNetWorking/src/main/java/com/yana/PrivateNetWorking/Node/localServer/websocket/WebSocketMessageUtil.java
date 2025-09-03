package com.yana.PrivateNetWorking.Node.localServer.websocket;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.yana.PrivateNetWorking.common.logger.LoggerUtil;

class WebSocketMessageUtil {
	static byte[] messageDecoder(DataInputStream dis) throws IOException {
		byte first = dis.readByte();
		byte opCode = (byte) (first & 0x0F);

		// Opcode is connection closed
		if(opCode == 8) {
			throw new IOException("Connection is closed");
		}

		byte second = dis.readByte();
		// Mask Check From client mask is necessary
		boolean masked = ((second & 0x80) != 0);
		if(!masked) {
			throw new IOException("Mask is not setting");
		}
	
		long length = second & 0x7F;
		if (length == 126) {
			length = dis.readUnsignedShort();
		} else if (length == 127) {
			length = dis.readLong();
		}

		// Read mask
		byte[] mask = new byte[4];
		dis.read(mask);
		// decoder
		byte[] encodedCharArray = new byte[(int) length];
		dis.read(encodedCharArray);
		// decode byte
		byte[] decodeByteArray = new byte[(int) length];
		for (int i = 0; i < encodedCharArray.length; i++) {
			decodeByteArray[i] = (byte)(encodedCharArray[i] ^ mask[i % 4]);
		}
		return decodeByteArray;
	}

	static byte[] createSendMessage(byte[] data) {
		int headLen = 1;
		if(data.length < 126) {
			headLen += 1;
		} else if(data.length <= (Short.MAX_VALUE - Short.MIN_VALUE)) {
			headLen += 3;
		} else if (data.length <= Long.MAX_VALUE) {
			headLen += 9;
		}
		try(ByteArrayOutputStream byteArray = new ByteArrayOutputStream((data.length + headLen));
			DataOutputStream dos = new DataOutputStream(byteArray)) {
			//0x81 = -127;
			dos.writeByte(0x81);
			if(headLen == 2) {
				dos.writeByte(data.length);
			} else if(headLen == 4) {
				dos.writeByte(126);
				dos.writeShort(data.length);
			} else if(headLen == 10) {
				dos.writeByte(127);
				dos.writeLong(data.length);
			}
			dos.write(data);
			dos.flush();
			return byteArray.toByteArray();
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
		return new byte[0];
	}
}
