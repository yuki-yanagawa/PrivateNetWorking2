package com.yana.PrivateNetWorking.CentralRouter.keygen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import com.yana.PrivateNetWorking.CentralRouter.cert.RouterCertPassManager;
import com.yana.PrivateNetWorking.CentralRouter.prop.RouterProperties;
import com.yana.PrivateNetWorking.common.key.PrivateNetWorkCommonKey;

public class KeyGenerateHelper {
	private static final KeyGenerateHelper KEY_GENERATE_HELPER = new KeyGenerateHelper();
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private PrivateNetWorkCommonKey privateNetWorkCommonKey;
	private RouterProperties routerProperties;
	private KeyStore keyStore;
	private X509Certificate x509certificate;

	private KeyGenerateHelper() {
		routerProperties = RouterProperties.getInstance();
	}

	public static KeyGenerateHelper getInstance() {
		return KEY_GENERATE_HELPER;
	}

	public void createKeyStore() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		File inputTargetFile = Paths.get(routerProperties.getProperty("certDir") + "router.pk12").toFile();
		try(FileInputStream fis = new FileInputStream(inputTargetFile)) {
			keyStore.load(fis, RouterCertPassManager.getPass().toCharArray());
		}
		File outputTargetFile = Paths.get(routerProperties.getProperty("keysDir") + "keyStore").toFile();
		try(FileOutputStream fos = new FileOutputStream(outputTargetFile)) {
			keyStore.store(fos, RouterCertPassManager.getPass().toCharArray());
		}
	}

	public void setKeysFromKeyStroe() throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		File inputTargetFile = Paths.get(routerProperties.getProperty("keysDir") + "keyStore").toFile();
		try(FileInputStream fis = new FileInputStream(inputTargetFile)) {
			keyStore.load(fis, RouterCertPassManager.getPass().toCharArray());
		}
		Key tmpKey = keyStore.getKey("1", RouterCertPassManager.getPass().toCharArray());
		if(!(tmpKey instanceof PrivateKey)) {
			throw new Exception("Failed create privatekey");
		}
		privateKey = (PrivateKey)privateKey;

		X509Certificate x509Certificate = (X509Certificate)keyStore.getCertificate("1");
		this.x509certificate = x509Certificate;
		this.publicKey = x509Certificate.getPublicKey();
		this.privateNetWorkCommonKey = PrivateNetWorkCommonKeyCreator.create();
	}

	public void createKeyPairs() throws NoSuchAlgorithmException {
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		KeyPair keyPair = keygen.genKeyPair();
		privateKey = keyPair.getPrivate();
		publicKey = keyPair.getPublic();
	}

	public static String getX509Certificate() {
		return KEY_GENERATE_HELPER._getX509Certificate();
	}

	private String _getX509Certificate() {
		try {
			return Base64.getEncoder().encodeToString(this.x509certificate.getEncoded());
		} catch(CertificateEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static PrivateNetWorkCommonKey getPrivateNetWorkCommonKey() {
		return KEY_GENERATE_HELPER._getPrivateNetWorkCommonKey();
	}

	private PrivateNetWorkCommonKey _getPrivateNetWorkCommonKey() {
		return privateNetWorkCommonKey;
	}
}
