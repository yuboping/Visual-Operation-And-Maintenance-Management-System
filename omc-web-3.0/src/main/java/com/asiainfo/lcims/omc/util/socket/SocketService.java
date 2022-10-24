package com.asiainfo.lcims.omc.util.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService {
    private final static String MESSAGE = "{serialno} {ERROR} {err}\r\n";

    public static void main(String[] args) throws InterruptedException {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8888);
            System.out.println("server started");
            Socket socket = null;

            while (true) {
                // 通过accept获取一个Socket实例并建立一客户段的连接
                socket = serverSocket.accept();

                System.out.println("read a message from stream");
                // 获取输入流，读取客户端传输的数据
                InputStream is = socket.getInputStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] bt = new byte[256];
                int n = -1;
                // 当客户端关闭其输出流时，read会返回-1
                while ((n = is.read(bt)) != -1) {
                    baos.write(bt, 0, n);
                }
                // 关闭输入
                socket.shutdownInput();

                // 传输字符，一般情况下必须指定字符编码
                System.out.println("message:" + new String(baos.toByteArray(), "UTF-8"));

                System.out.println("return a message to client");

                System.out.println("message:" + MESSAGE);
                // 获取输出流，向客户端返回数据
                OutputStream os = socket.getOutputStream();

                os.write("".getBytes("UTF-8"));

                Thread.sleep(5000);

                os.write(MESSAGE.getBytes("UTF-8"));

                // 此处需要关闭输出，否则客户端的读操作将一直循环下去
                socket.shutdownOutput();

                // 关闭socket
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (null != serverSocket) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            System.out.println("finished...");
        }
    }
}
