package com.asiainfo.lcims.omc.protocol.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http客户端
 */
public class HttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);
    
    private static String contentTypeXml = "application/xml";
    private static String contentTypeJson = "application/json";
    /**
     * GET方式发送http请求并读取回应
     *
     */
    public static String sendGetForXml(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url + "?" + param);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("Pragma:", "no-cache");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "text/html");
            conn.connect();
            result = readInput(conn);
        } catch (Exception e) {
            LOG.error("发送GET请求出现异常！", e);
        } finally {
            close(in);
        }
        return result;
    }

    /**
     * POST方式发送http请求并读取回应
     *
     */
    public static String sendPostForXml(String url, String param) throws Exception {
        HttpURLConnection conn = sendPost(url, param, contentTypeXml);
        return readInput(conn);
    }

    /**
     * POST方式发送http请求并读取回应，重试3次
     *
     */
    public static String sendPostForXml(String url, String param, int retry) {
        String ret = null;
        int count = 0;
        do {
            try {
                HttpURLConnection conn = sendPost(url, param, contentTypeXml);
                return ret = readInput(conn);
            } catch (Exception e) {
                LOG.error("请求[" + url + "]发送失败", e);
            }
            count++;
        } while (count <= retry);
        return ret;
    }

    /**
     * POST方式发送http请求，无回应信息
     *
     */
    public static void sendPostNoRespForXml(String url, String param) throws Exception {
        HttpURLConnection conn = sendPost(url, param, contentTypeXml);
        if (conn.getResponseCode() != 200) {
            throw new Exception("http请求发送失败");
        }
    }

    /**
     * POST方式发送http请求，无回应信息，重试3次
     *
     */
    public static void sendPostNoRespForXml(String url, String param, int retry) {
        int count = 0;
        do {
            try {
                HttpURLConnection conn = sendPost(url, param, contentTypeXml);
                if (conn.getResponseCode() != 200) {
                    throw new Exception("http请求发送失败");
                }
                return;
            } catch (Exception e) {
                LOG.error("请求[" + url + "]发送失败", e);
            }
            count++;
        } while (count <= retry);
    }

    private static HttpURLConnection sendPost(String url, String param, String contentType) throws Exception {
        OutputStreamWriter out = null;
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", contentType+";charset=UTF-8");
//            conn.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.connect();
            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(param);
            out.flush();
        } catch (Exception e) {
            throw new Exception("发送请求出现异常！" + e.getMessage(), e);
        } finally {
            close(out);
        }
        return conn;
    }

    private static String readInput(HttpURLConnection conn) throws IOException {
        String result = getStringFromInputStream(conn.getInputStream());
        return result;
    }

    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String result = os.toString();
        os.close();
        if (result == null)
            result = "";
        return result;
    }

    private static void close(Closeable in) {
        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    /**
     * GET方式发送http请求并读取回应
     * 
     * @param url
     * @param param
     * @return
     */
    public static String sendGetForJson(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url + "?" + param);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("Pragma:", "no-cache");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", contentTypeJson);
            conn.connect();
            result = readInput(conn);
        } catch (Exception e) {
            LOG.error("发送GET请求出现异常！", e);
        } finally {
            close(in);
        }
        return result;
    }

    /**
     * POST方式发送http请求并读取回应
     *
     */
    public static String sendPostForJson(String url, String param) {
        HttpURLConnection conn;
        String rt = null;
        try {
            conn = sendPost(url, param, contentTypeJson);
            rt = readInput(conn);
        } catch (Exception e) {
            LOG.error("sendPostForJson fail", e);
        }
        return rt;
    }
}
