package com.asiainfo.lcims.omc.alarm.sms.hncm;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * @Author: YuChao
 * @Date: 2019/10/15 16:48
 */
public class SMSSendClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMSSendClient.class);

    private static final String SMS_CHARSET = "GBK";
    private static final String SMS_SPLIT = "|||";
    private static final String SMS_SPLIT_REF = "\\|\\|\\|";
    private static final String SMS_SEND_PARAM = "0";

    /**
     *
     * @param ip
     * @param port
     * @param timeout
     * @param message (18539958228|||0|||XW+File+Exist!!!|||0|||0|||0 )
     * @return
     */
    public static int send(String ip, int port, int timeout, String phone, String message) {
        LOGGER.info("request phone, message:[{}, {}]", phone, message);
        String smsMessage = concatSocketMessage(phone, message);
        int code = -1;
        Socket socket = null;
        PrintWriter out = null;
        DataInputStream inputStream = null;
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(timeout);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), SMS_CHARSET)), true);
            inputStream = new DataInputStream(socket.getInputStream());
            out.print(smsMessage);
            out.flush();
            byte[] bytes = new byte[8192];
            int read = inputStream.read(bytes);
            String resonse = new String(bytes, 0, read);
            LOGGER.info("response[{}]", resonse);
            String[] respArr = resonse.split(SMS_SPLIT_REF);
            if (respArr.length >= 2) {
                code = Integer.parseInt(respArr[0]);
            }
        } catch (Exception ex) {
            LOGGER.error("send fail", ex);
            code = -1;
        } finally {
            IOUtils.closeQuietly(socket);
        }
        return code;
    }

    /**
     * 拼接SMS请求内容
     * @param phone
     * @param message
     * @return
     */
    private static String concatSocketMessage(String phone, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(phone).append(SMS_SPLIT);
        sb.append(SMS_SEND_PARAM).append(SMS_SPLIT);
        sb.append(message).append(SMS_SPLIT);
        sb.append(SMS_SEND_PARAM).append(SMS_SPLIT);
        sb.append(SMS_SEND_PARAM).append(SMS_SPLIT);
        sb.append(SMS_SEND_PARAM);
        return sb.toString();
    }

    public static void main(String[] args) {
        SMSSendClient.send("127.0.0.1", 6666, 5000, "15251866986", "test0001");
    }
}
