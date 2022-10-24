package com.asiainfo.lcims.omc.agentserver.service.business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import org.slf4j.Logger;

public class SocketClient {
    private static final Logger log = LoggerFactory.make();
    public static int port = 55533;
    
    public static void send_to_server_1() throws IOException {
        Socket socket = null;
        BufferedReader br = null;
        PrintWriter write = null;
        DataOutputStream  dataOut = null;
        String message = "1|||874b36b1-6e6d-4d66-8aba-16ec49599ef9|||1.1.1.1;2.2.2.2;3.3.3.3;4.4.4.4\n";
        try {
            socket = new Socket("127.0.0.1", port);
            dataOut = new DataOutputStream(socket.getOutputStream());
            dataOut.write(message.getBytes());
            dataOut.flush();
            message = "ppppppp\n";
            dataOut.write(message.getBytes());
            dataOut.flush();
            
            message = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n";
            dataOut.write(message.getBytes());
            dataOut.flush();

        }catch (Exception e) {
            log.error("Exception:",e);
        } finally {
            if (null != socket) {
                socket.close();
            }
        }
    }
     
    
    public static void send_to_server_2() throws IOException {
        Socket s = null;
        try {
             s = new Socket("127.0.0.1",port);
            //构建IO
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            //向服务器端发送一条消息
            bw.write("测试客户端和服务器通信，服务器接收到消息返回到客户端\n");
            bw.flush();

            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            boolean flag = true;
            while (flag){
                len = is.read(bytes);
                System.out.println(len);
                if(len == -1) {
                    break;
                }else {
                    sb.append(new String(bytes, 0, len, "UTF-8"));
                }
            }
         } catch (IOException e) {
            log.error("Exception:",e);
         } finally {
            if (null != s) {
                s.close();
            }
        }
    }

}
