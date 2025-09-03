package com.yana.PrivateNetWorking.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;

public class DeserializeUtil {
	public static Optional<Object> changeObject(byte[] bytes) {
		try(ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis)) {
			return Optional.of(ois.readObject());
		} catch(IOException e) {
			return Optional.empty();
		} catch(ClassNotFoundException e) {
			return Optional.empty();
		}
	}
}
