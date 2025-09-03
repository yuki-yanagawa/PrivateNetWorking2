package com.yana.PrivateNetWorking.Node.keygen;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.yana.PrivateNetWorking.Node.keyStore.NodeKeyStore;

public class KeyGenratorHelper {

	public static void createKeys() throws KeyGenerateException {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();
			NodeKeyStore.setNodePrivKey(privateKey);
			NodeKeyStore.setNodePublicKey(publicKey);
		} catch(Exception e) {
			throw new KeyGenerateException(e.getMessage());
		}
	}
}
