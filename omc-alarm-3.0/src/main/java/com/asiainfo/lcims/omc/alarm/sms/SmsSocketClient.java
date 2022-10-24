package com.asiainfo.lcims.omc.alarm.sms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsSocketClient {
	private static final Logger log = LoggerFactory.getLogger(SmsSender.class);
	Socket clientSocket = null;
	DataOutputStream out = null;
	DataInputStream in = null;

	public SmsSocketClient() {
	}

	public SmsSocketClient(String paramString, int paramInt) throws UnknownHostException, IOException {
		this.clientSocket = new Socket(paramString, paramInt);
		this.out = new DataOutputStream(this.clientSocket.getOutputStream());
		this.in = new DataInputStream(this.clientSocket.getInputStream());
	}

	public void sendMsg2Server(String paramString) throws IOException {
		byte[] arrayOfByte = paramString.getBytes();
		this.out.write(arrayOfByte);
		this.out.flush();
	}

	public String getMsgFromServer() throws IOException {
		byte[] arrayOfByte = new byte[8192];
		String str = "";
		int count = this.in.read(arrayOfByte);
		if(count>0){
		    str = new String(arrayOfByte).trim();
		}
		return str;
	}

	public void close() {
		try {
			this.clientSocket.close();
			this.out.close();
			this.in.close();
		} catch (IOException localIOException) {
		}
	}

	public void test(String paramString1, int paramInt, String paramString2) throws IOException {
		SmsSocketClient localSocketClient = new SmsSocketClient(paramString1, paramInt);
		localSocketClient.sendMsg2Server(paramString2 + "\n\n");
		String str = localSocketClient.getMsgFromServer();
		log.info("receive:" +str);
		try {
			if (localSocketClient != null)
				localSocketClient.close();
		} catch (Exception localException) {
		}
	}

	public static void main(String[] paramArrayOfString) {
		try {
			new SmsSocketClient().test(paramArrayOfString[0], Integer.parseInt(paramArrayOfString[1]),
					paramArrayOfString[2]);
		} catch (Exception localException) {
		}
	}
}