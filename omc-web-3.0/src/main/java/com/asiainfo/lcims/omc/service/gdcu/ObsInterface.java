package com.asiainfo.lcims.omc.service.gdcu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 与obs运维接口
 * 
 */
public class ObsInterface {
    private static final Logger log = LoggerFactory.getLogger(ObsInterface.class);
    private static final int TIMEOUT = 60000;
    Socket clientSocket = null;
    DataOutputStream out = null;
    DataInputStream in = null;

    public ObsInterface(String ip, int port) throws UnknownHostException, IOException {
        log.info("ip : {}, port : {}", ip, port);
        clientSocket = new Socket(ip, port);
        clientSocket.setSoTimeout(TIMEOUT);
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());
    }

    // 向服务端发送数据.
    public void sendMsg2Server(String data) throws IOException {
        data = data + "\n";
        log.debug("send to server data : {}", data);
        out.write(data.getBytes());
        out.flush();
    }

    /**
     * 从输入流读取数据。
     *
     * @return
     * @throws Exception
     */
    public String getMsgFromServer() throws Exception {
        String strLineInfo = "";
        boolean blSucc = false;
        do {
            byte[] b = new byte[81920];
            String tmp = null;
            int count = 0;
            while ((count=in.read(b))>0) {
            	tmp = new String(b);
                if (tmp == null || tmp.trim().equals("")) {
                    blSucc = true;
                } else if (tmp.indexOf("\n") >= 0) {
                    blSucc = true;
                    strLineInfo += tmp.substring(0, tmp.indexOf("\n"));
                    if(tmp.substring(tmp.indexOf("\n")) != null){
                    	strLineInfo += '}';
                    }
                    log.info("strLineInfo is : {}", strLineInfo);
                    break;
                } else {
                    strLineInfo += tmp.trim();
                }
			}
            
        } while (!blSucc);
//        if (!blSucc) {
//            throw new Exception("Read receive error!");
//        }
        return strLineInfo;
    }

    public void close() {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }
}
