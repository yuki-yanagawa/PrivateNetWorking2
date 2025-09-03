package com.yana.PrivateNetWorking.CentralRouter.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.yana.PrivateNetWorking.CentralRouter.keygen.KeyGenerateHelper;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.key.PrivateNetWorkCommonKey;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;
import com.yana.PrivateNetWorking.common.util.SerializeUtil;
import com.yana.privateNetSocket2.PrivateNetSocket;

class RequestCommonKeyAction implements IAction {
	private PrivateNetSocket socket;
	private InetSocketAddress socketAddress;
	RequestCommonKeyAction(PrivateNetSocket socket, InetSocketAddress socketAddress) {
		this.socket = socket;
		this.socketAddress = socketAddress;
	}
	@Override
	public void execute(String[] requetLines) {
		// user check is need ??

		String[] keyAndVal = requetLines[1].split(CommunicationDefnition.KEY_VAL_SEPALATOR);
		String val = keyAndVal[1].trim();
		byte[] pubKbytes = Base64.getDecoder().decode(val);
		byte[] retBytes = new byte[0];
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubKbytes);
			PublicKey key = kf.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			PrivateNetWorkCommonKey commonKey = KeyGenerateHelper.getPrivateNetWorkCommonKey();
			byte[] serialize = SerializeUtil.changeBytes(commonKey);
			ByteBuffer buffer = ByteBuffer.wrap(serialize);
			try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
				while(buffer.hasRemaining()) {
					byte[] readData = null;
					if(buffer.remaining() >= 245) {
						readData = new byte[245];
					} else {
						readData = new byte[buffer.remaining()];
					}
					buffer.get(readData);
					byte[] tmp = cipher.doFinal(readData);
					bos.write(tmp);
				}
				retBytes = bos.toByteArray();
			} catch(IOException e) {
				LoggerUtil.warn(e);
			}
		} catch(NoSuchAlgorithmException e) {
			LoggerUtil.warn(e);
		} catch(InvalidKeySpecException e) {
			LoggerUtil.warn(e);
		} catch(NoSuchPaddingException e) {
			LoggerUtil.warn(e);
		} catch(InvalidKeyException e) {
			LoggerUtil.warn(e);
		} catch(BadPaddingException e) {
			LoggerUtil.warn(e);
		} catch(IllegalBlockSizeException e) {
			LoggerUtil.warn(e);
		}
		if(retBytes.length == 0) {
			return;
		}

		String retStr = Base64.getEncoder().encodeToString(retBytes);
		StringBuilder sb = new StringBuilder();
		sb.append(CommunicationDefnition.SUBJ_REQUEST_COMMONKEY_ACK).append(CommunicationDefnition.LINE_SPARATOR)
			.append(CommunicationDefnition.KEY_COMK).append(CommunicationDefnition.KEY_VAL_SEPALATOR).append(retStr)
			.append(CommunicationDefnition.LINE_SPARATOR);
		try {
			socket.sendData(sb.toString().getBytes(CharsetUtil.charSet()), socketAddress);
		} catch(IOException e) {
			LoggerUtil.warn(e);
		}
	}
}
