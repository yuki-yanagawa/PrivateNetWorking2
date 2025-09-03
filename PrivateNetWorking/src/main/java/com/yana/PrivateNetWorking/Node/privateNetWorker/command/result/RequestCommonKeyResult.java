package com.yana.PrivateNetWorking.Node.privateNetWorker.command.result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.yana.PrivateNetWorking.Node.keyStore.NodeKeyStore;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.key.PrivateNetWorkCommonKey;
import com.yana.PrivateNetWorking.common.logger.LoggerUtil;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;
import com.yana.PrivateNetWorking.common.util.DeserializeUtil;

class RequestCommonKeyResult implements ICommandResult {

	@Override
	public boolean settigResopnseData(byte[] reseponsePacketData) {
		String[] lines = new String(reseponsePacketData, CharsetUtil.charSet()).split(CommunicationDefnition.LINE_SPARATOR);
		String val = lines[1].split(CommunicationDefnition.KEY_VAL_SEPALATOR)[1].trim();
		byte[] deos = Base64.getDecoder().decode(val);
		ByteBuffer byteBuf = ByteBuffer.wrap(deos);
		PrivateKey priv = NodeKeyStore.getPrivateKey();
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, priv);
			byte[] readData;
			while(byteBuf.hasRemaining()) {
				readData = new byte[256];
				byteBuf.get(readData);
				bos.write(cipher.doFinal(readData));
			}
			byte[] retBytes = bos.toByteArray();
			Optional<Object> retObjOpt = DeserializeUtil.changeObject(retBytes);
			if(!retObjOpt.isPresent()) {
				return false;
			}
			Object retObj = retObjOpt.get();
			if(!(retObj instanceof PrivateNetWorkCommonKey)) {
				return false;
			}
			PrivateNetWorkCommonKey privateNetWorkCommonKey = (PrivateNetWorkCommonKey)retObj;
			System.out.println("Check !!!! : " + privateNetWorkCommonKey.getTransFormation());
			NodeKeyStore.setPrivateNetworkCommonKey(privateNetWorkCommonKey);
		} catch(NoSuchAlgorithmException e) {
			LoggerUtil.warn(e);
			return false;
		} catch(NoSuchPaddingException e) {
			LoggerUtil.warn(e);
			return false;
		} catch(InvalidKeyException e) {
			LoggerUtil.warn(e);
			return false;
		} catch(BadPaddingException e) {
			LoggerUtil.warn(e);
			return false;
		} catch(IllegalBlockSizeException e) {
			LoggerUtil.warn(e);
			return false;
		} catch(IOException e) {
			LoggerUtil.warn(e);
			return false;
		}
		return true;
	}

}
