package com.yana.PrivateNetWorking.CentralRouter.cert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RouterCertCreator {
	private static final String CERT_DIR_PATH = "cert/";

	public static void create() throws IOException {
		String password = RouterCertPassManager.getPass();
		StringBuilder sb = new StringBuilder();
		String certDirPath = CERT_DIR_PATH.replaceAll("/", "\\\\");
		sb.append("cmd /c ").append(certDirPath).append("certcreate.bat ").append(password);
		Process process = Runtime.getRuntime().exec(sb.toString());
		try(BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			while(buf.readLine() != null) {
			}
		}
		if(process.exitValue() != 0) {
			throw new IOException("create key bat error");
		}
	}

//	public X509Certificate createCertificate() {
//		X509CertSelector x509CertSelector = new X509CertSelector();
//		x509CertSelector.setIssuer(new X500Principal("/C=JP/ST=Miyagi/O=TestOrg/CN=localhost"));
//		x509CertSelector.setSubject(new X500Principal("/C=JP/ST=Miyagi/O=TestOrg/CN=localhost"));
//		x509CertSelector.setSubjectPublicKey(publicKey);
//		return x509CertSelector.getCertificate();
//	}
}
