package com.yana.PrivateNetWorking.Node.localServer.model.responseDataAnalyze;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import com.yana.PrivateNetWorking.Node.keyStore.NodeKeyStore;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json.DefaultJsonObject;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json.JsonObject;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.key.PrivateNetWorkKeyDef;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

public class JoinResponseDataAnalyzer {
	private byte[] routerResponseRawData;
	private JsonObject responseJsonData;
	public JoinResponseDataAnalyzer(byte[] routerResponseRawData) {
		this.routerResponseRawData = routerResponseRawData;
	}

	public boolean analyze() {
		String responseDataStr = new String(routerResponseRawData, CharsetUtil.charSet());
		String[] responseDataLines = responseDataStr.split(CommunicationDefnition.LINE_SPARATOR);
		if(!responseDataLines[0].trim().equals(CommunicationDefnition.SUBJ_JOIN_ACK)) {
			return false;
		}
		if(responseDataLines.length != 2) {
			return false;
		}
		String[] certificateSettings = responseDataLines[1].split(CommunicationDefnition.KEY_VAL_SEPALATOR);
		if(!certificateSettings[0].trim().equals(CommunicationDefnition.KEY_SERVER_CERT)) {
			return false;
		}
		X509Certificate certificate = null;
		byte[] certBytes = Base64.getDecoder().decode(certificateSettings[1].trim());
		try(ByteArrayInputStream bis = new ByteArrayInputStream(certBytes)) {
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			Certificate tmpCertificate = factory.generateCertificate(bis);
			if(!(tmpCertificate instanceof X509Certificate)) {
				return false;
			}
			certificate = (X509Certificate)tmpCertificate;
		} catch(CertificateException e) {
			return false;
		} catch(IOException e) {
			return false;
		}
		if(!checkIssure(certificate)) {
			return false;
		}
		PublicKey publicKey = certificate.getPublicKey();
		if(!PrivateNetWorkKeyDef.KEY_ALGO.equals(publicKey.getAlgorithm())) {
			return false;
		}
		NodeKeyStore.setRouterPubKey(publicKey);
		this.responseJsonData = new DefaultJsonObject("{\"JOIN_ACK\" : \"OK\"}");
		return true;
	}

	private boolean checkIssure(X509Certificate certificate) {
		System.out.println("Issure : " + certificate.getIssuerDN());
		return true;
	}

	public JsonObject getResponseJsonData() {
		return responseJsonData;
	}
}
