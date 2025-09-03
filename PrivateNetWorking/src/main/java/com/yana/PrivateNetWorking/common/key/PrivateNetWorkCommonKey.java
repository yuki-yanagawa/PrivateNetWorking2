package com.yana.PrivateNetWorking.common.key;

import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

public class PrivateNetWorkCommonKey implements Serializable {
	private static final long serialVersionUID = -2563108017716793530L;

	private SecretKey secretKey;
	private String transformation;
	private byte[] iv;
	private transient Cipher cipher;
	private transient IvParameterSpec ivParameterSpec;
	public PrivateNetWorkCommonKey(SecretKey secretKey, String transformation, byte[] iv) {
		this.secretKey = secretKey;
		this.transformation = transformation;
		this.iv = iv;
	}

	public PrivateNetWorkCommonKey(SecretKey secretKey, String transformation, byte[] iv, Cipher cipher, IvParameterSpec ivParameterSpec) {
		this.secretKey = secretKey;
		this.transformation = transformation;
		this.iv = iv;
		this.cipher = cipher;
		this.ivParameterSpec = ivParameterSpec;
	}

	public void init() {
		try {
			cipher = Cipher.getInstance(transformation);
			ivParameterSpec = new IvParameterSpec(iv);
		} catch(NoSuchAlgorithmException e) {
			LoggerUtil.warn(e);
		} catch(NoSuchPaddingException e) {
			LoggerUtil.warn(e);
		}
	}

	public String getTransFormation() {
		return this.transformation;
	}

	public synchronized byte[] encrypt(String message) {
		byte[] messageData = message.getBytes(CharsetUtil.charSet());
		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
			return this.cipher.doFinal(messageData);
		} catch(InvalidKeyException e) {
			LoggerUtil.warn(e);
		} catch(InvalidAlgorithmParameterException e) {
			LoggerUtil.warn(e);
		} catch(BadPaddingException e) {
			LoggerUtil.warn(e);
		} catch(IllegalBlockSizeException e) {
			LoggerUtil.warn(e);
		}
		return new byte[0];
	}

	public synchronized byte[] encrypt(byte[] message) {
		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
			return this.cipher.doFinal(message);
		} catch(InvalidKeyException e) {
			LoggerUtil.warn(e);
		} catch(InvalidAlgorithmParameterException e) {
			LoggerUtil.warn(e);
		} catch(BadPaddingException e) {
			LoggerUtil.warn(e);
		} catch(IllegalBlockSizeException e) {
			LoggerUtil.warn(e);
		}
		return new byte[0];
	}

	public synchronized byte[] decrypt(byte[] readData) {
		try {
			this.cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
			return this.cipher.doFinal(readData);
		} catch(InvalidKeyException e) {
			LoggerUtil.warn(e);
		} catch(InvalidAlgorithmParameterException e) {
			LoggerUtil.warn(e);
		} catch(BadPaddingException e) {
			LoggerUtil.warn(e);
		} catch(IllegalBlockSizeException e) {
			LoggerUtil.warn(e);
		}
		return new byte[0];
	}
}
