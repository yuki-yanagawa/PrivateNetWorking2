package com.yana.PrivateNetWorking.CentralRouter.keygen;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.yana.PrivateNetWorking.common.key.PrivateNetWorkCommonKey;

public class PrivateNetWorkCommonKeyCreator {
	public static PrivateNetWorkCommonKey create()  throws NoSuchAlgorithmException, NoSuchPaddingException{
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = new SecureRandom();
		keyGenerator.init(128, secureRandom);
		byte[] ivBytes = new byte[16];
		secureRandom.nextBytes(ivBytes);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
		SecretKey secretKey = keyGenerator.generateKey();
		String transformation = "AES/CBC/PKCS5Padding";
		Cipher cipher = Cipher.getInstance(transformation);
		return new PrivateNetWorkCommonKey(secretKey, transformation, ivBytes, cipher, ivParameterSpec);
	}
}
