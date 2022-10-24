package com.asiainfo.lcims.omc.util.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketClient {
    private static final Logger LOG = LoggerFactory.getLogger(SocketClient.class);
    private final int timeout = 20000;
    private Socket socket;

    public SocketClient(String host, int port) {
        try {
            LOG.info("SocketClient host:" + host + ";port:" + port + ".");
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            LOG.error("SocketClient UnknownHostException error:", e);
        } catch (IOException e) {
            LOG.error("SocketClient IOException error:", e);
        }
    }

    // 无终止符
    public String sendMsg(String sendMsg, String charset) {
        String resultMsg = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            LOG.info("SocketClient sendMsg:" + sendMsg + ".");
            socket.setSoTimeout(timeout);
            // 获取输出流，向服务端发送数据
            os = socket.getOutputStream();
            // 传输字符，一般情况下必须指定字符编码
            os.write(sendMsg.getBytes(charset));
            // 此处需要关闭输出，否则服务端的读操作将一直循环下去
            socket.shutdownOutput();
            is = socket.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bt = new byte[256];
            int n = -1;
            // 当客户端关闭其输出流时，read会返回-1
            while ((n = is.read(bt)) != -1) {
                baos.write(bt, 0, n);
            }
            resultMsg = new String(baos.toByteArray(), charset);
            // 关闭输入
            socket.shutdownInput();
        } catch (IOException e) {
            LOG.error("SocketClient sendMsg error:", e);
        } finally {
            try {
                if (null != socket) {
                    socket.close();
                }
            } catch (IOException e) {
                LOG.error("SocketClient sendMsg error:", e);
            }
        }
        LOG.info("SocketClient sendMsg:" + sendMsg + ";resultMsg:" + resultMsg + ".");
        return resultMsg;
    }

    // 有终止符
    public String sendMsg(String sendMsg, String charset, String delimiterstr) {
        String resultMsg = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            LOG.info("SocketClient sendMsg:" + sendMsg + ".");
            sendMsg = sendMsg + delimiterstr;
            LOG.info("SocketClient sendMsg add delimiterstr:" + sendMsg + ".");
            socket.setSoTimeout(timeout);
            // 获取输出流，向服务端发送数据
            os = socket.getOutputStream();
            // 传输字符，一般情况下必须指定字符编码
            os.write(sendMsg.getBytes(charset));
            // 此处需要关闭输出，否则服务端的读操作将一直循环下去
            socket.shutdownOutput();
            long startTime = System.currentTimeMillis();
            while (true) {
                long endTime = System.currentTimeMillis();
                if ((endTime - startTime) > timeout) {
                    LOG.error("SocketClient sendMsg timeout error exceed millis:" + timeout);
                    break;
                }
                is = socket.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] bt = new byte[256];
                int n = -1;
                // 当客户端关闭其输出流时，read会返回-1
                while ((n = is.read(bt)) != -1) {
                    baos.write(bt, 0, n);
                }
                resultMsg = new String(baos.toByteArray(), charset);
                if (resultMsg.contains(delimiterstr)) {
                    LOG.info("SocketClient resultMsg :" + sendMsg + ".");
                    resultMsg = resultMsg.substring(0, resultMsg.length() - delimiterstr.length());
                    LOG.info("SocketClient resultMsg reduce delimiterstr:" + sendMsg + ".");
                    break;
                }
            }
            // 关闭输入
            socket.shutdownInput();
        } catch (IOException e) {
            LOG.error("SocketClient sendMsg error:", e);
        } finally {
            try {
                if (null != socket) {
                    socket.close();
                }
            } catch (IOException e) {
                LOG.error("SocketClient sendMsg error:", e);
            }
        }
        LOG.info("SocketClient sendMsg:" + sendMsg + ";resultMsg:" + resultMsg + ".");
        return resultMsg;
    }

    // public static void main(String[] args) {
    // SocketClient s = new SocketClient("127.0.0.1", 8888);
    // String e1 = s.sendMsg("SimuDialUp UV6heOe3S0SYoSb12Z ", "UTF-8", "\r\n");
    // String e = s.sendMsg("SimuDialUp UV6heOe3S0SYoSb12Z ", "UTF-8");
    // System.out.println("start1");
    // System.out.println(e1);
    // System.out.println("end1");
    // System.out.println("start2");
    // System.out.println(e);
    // System.out.println("end2");
    // }
}
