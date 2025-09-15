package com.yana.PrivateNetWorking.Node.privateNetWorker;

import java.net.InetSocketAddress;

public class WrapAcceptPrivateNetPacketData {
	private InetSocketAddress acceptSocketAddress;
	private byte[] acceptPacketData;

	public WrapAcceptPrivateNetPacketData(InetSocketAddress acceptSocketAddress, byte[] acceptPacketData) {
		this.acceptSocketAddress = acceptSocketAddress;
		this.acceptPacketData = acceptPacketData;
	}

	public InetSocketAddress getAcceptSocketAddress() {
		return this.acceptSocketAddress;
	}

	public byte[] getAcceptPacketData() {
		return this.acceptPacketData;
	}
}
