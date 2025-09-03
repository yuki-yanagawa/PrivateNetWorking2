package com.yana.PrivateNetWorking.Node.localServer.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class WrapperSocket implements AutoCloseable {
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private WrapperSocket(Socket socket, DataInputStream inputStream, DataOutputStream outputStream) {
		this.socket = socket;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	public static WrapperSocket create(Socket socket) throws IOException {
		DataInputStream inputStream = new DataInputStream(socket.getInputStream());
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		return new WrapperSocket(socket, inputStream, outputStream);
	}

	public DataInputStream getInputStream() {
		return inputStream;
	}

	public DataOutputStream getOutputStream() {
		return outputStream;
	}

	public void setSoTimeout(int timeout) throws SocketException {
		socket.setSoTimeout(timeout);
	}

	@Override
	public void close() throws IOException {
		if(this.inputStream != null) {
			this.inputStream.close();
		}
		if(this.outputStream != null) {
			this.outputStream.close();
		}
		if(this.socket != null) {
			this.socket.close();
		}
	}

}
