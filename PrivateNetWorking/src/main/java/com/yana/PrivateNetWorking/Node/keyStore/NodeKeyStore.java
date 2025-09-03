package com.yana.PrivateNetWorking.Node.keyStore;

import java.security.PrivateKey;
import java.security.PublicKey;

import com.yana.PrivateNetWorking.common.key.PrivateNetWorkCommonKey;

public class NodeKeyStore {
	private static NodeKeyStore nodeKeyStore = new NodeKeyStore();
	private static Object routerPubLck = new Object();
	private static Object nodePubLck = new Object();
	private static Object nodePrivLck = new Object();
	private static Object commonKeyLck = new Object();
	private PublicKey routerPubKey;
	private PublicKey nodePubKey;
	private PrivateKey nodePrivKey;
	private PrivateNetWorkCommonKey privateNetWorkCommonKey;
	private NodeKeyStore() {
	}

	public static void setRouterPubKey(PublicKey routerPubKey) {
		synchronized (routerPubLck) {
			nodeKeyStore.routerPubKey = routerPubKey;
		}
	}

	public static PublicKey getRouterPubKey() {
		synchronized (routerPubLck) {
			return nodeKeyStore.routerPubKey;
		}
	}

	public static void setNodePublicKey(PublicKey publicKey) {
		synchronized (nodePubLck) {
			nodeKeyStore.nodePubKey = publicKey;
		}
	}

	public static PublicKey getNodePublicKey() {
		synchronized (nodePubLck) {
			return nodeKeyStore.nodePubKey;
		}
	}

	public static void setNodePrivKey(PrivateKey privKey) {
		synchronized (nodePrivLck) {
			nodeKeyStore.nodePrivKey = privKey;
		}
	}

	public static PrivateKey getPrivateKey() {
		synchronized (nodePrivLck) {
			return nodeKeyStore.nodePrivKey;
		}
	}

	public static void setPrivateNetworkCommonKey(PrivateNetWorkCommonKey key) {
		synchronized (commonKeyLck) {
			nodeKeyStore.privateNetWorkCommonKey = key;
		}
	}

	public static PrivateNetWorkCommonKey getPrivateNetworkCommonKey() {
		synchronized (commonKeyLck) {
			return nodeKeyStore.privateNetWorkCommonKey;
		}
	}
}
