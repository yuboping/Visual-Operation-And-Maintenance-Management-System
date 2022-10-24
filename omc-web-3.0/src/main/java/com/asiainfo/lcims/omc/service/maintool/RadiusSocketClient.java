package com.asiainfo.lcims.omc.service.maintool;
import java.io.*;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class RadiusSocketClient {
    private static final Logger LOG = LoggerFactory.getLogger(RadiusSocketClient.class);
    private Socket clientSocket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public RadiusSocketClient(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            clientSocket.setSoTimeout(20000);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            LOG.error("Can't connect to the Radius Server host!");
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * 向服务端发送数据.
     *
     * @param data
     */
    public void sendMsg2Server(String data) {
        try {
            byte[] cbuf = data.getBytes();
            out.write(cbuf);
            out.flush();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    // 从服务端接收数据.
    @SuppressWarnings("unused")
    public String getMsgFromServer() {
        byte[] b = new byte[8192];
        int len = 0;
        int slen = 0;
        String temp = "";
        try {
            slen = in.read(b);
            if(slen != -1) {
                temp = new String(b, 0, slen);
            }
        } catch (Exception e) {
            LOG.error("Getting message from the Radius server failed!", e);
        }
        return temp;
    }

    // 关闭对象.
    public void close() {
        try {
            clientSocket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
