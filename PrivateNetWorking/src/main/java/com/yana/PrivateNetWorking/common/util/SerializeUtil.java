package com.yana.PrivateNetWorking.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerializeUtil {
	public static byte[] changeBytes(Object object) {
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(object);
			oos.flush();
			return bos.toByteArray();
		} catch(IOException e) {
			return new byte[0];
		}
	}
}
