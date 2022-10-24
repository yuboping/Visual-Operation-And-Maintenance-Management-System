package com.asiainfo.lcims.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName: HttpUtils
 * @Description: (Http请求类)
 * @author yubp@asiainfo-sec.com
 * @date
 *
 */
public class HttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    public static String post(String url, String content, String charset) {
        String rsp = "";
        HttpURLConnection conn = null;
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        try {
            LOG.info("post 请求信息:url={" + url + "},params={" + content + "}");
            URL u = new URL(url);
            if (url.toLowerCase().startsWith("https://")) {
                // setHttpsConnection();
            }
            long startTime = System.nanoTime();
            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", String.valueOf(content.length()));
            // conn.setRequestProperty("Apitoken",
            // "{\"name\":\"mobile-shh\",\"sig\":\"shh-cmc-token-in-server-side\"}");
            // conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "close");
            conn.setConnectTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            writer = new OutputStreamWriter(conn.getOutputStream(), charset);
            writer.write(content);
            writer.flush();
            int code = conn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException(
                        "HTTP POST Request Failed with Error code : " + code);
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            StringBuffer sb = new StringBuffer();
            String temp;
            while ((temp = reader.readLine()) != null) {
                sb.append(temp).append('\n');
            }
            rsp = sb.toString();
            long endTime = System.nanoTime();
            LOG.info("post请求耗时：" + (endTime - startTime) + "ns");
            LOG.info("post 返回信息：url={" + url + "},rsp={" + rsp + "}");

        } catch (Exception e) {
            LOG.error("post调用远程接口异常：url={" + url + "},e={" + e + "}");
            rsp = "";
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LOG.error("post接口异常：e={" + e + "}");
                }
            }

            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.error("post接口异常：e={" + e + "}");
                }
            }

            if (null != conn) {
                conn.disconnect();
            }

        }
        return rsp;
    }

    // public static void main(String[] args) {
    // Map<String, Object> map = new HashMap<String, Object>();
    // map.put("step", 60);
    // map.put("start_time", "1541655860");
    // ArrayList<String> hostnameslist = new ArrayList<String>();
    // hostnameslist.add("10.1.198.90");
    // hostnameslist.add("10.1.198.34");
    // hostnameslist.add("10.1.198.137");
    // map.put("hostnames", hostnameslist);
    // map.put("end_time", 1541657672);
    // ArrayList<String> counterslist = new ArrayList<String>();
    // counterslist.add("radius_access_accept_count");
    // counterslist.add("radius_access_reject_count");
    // map.put("counters", counterslist);
    // map.put("consol_fun", "AVERAGE");
    // JSONObject mapjson = JSONObject.parseObject(JSON.toJSONString(map));
    // String url_197 = "http://10.21.37.197:9004/api/v1/graph/history";
    // // String url = "http://localhost:9006/api/v1/graph/history";
    // String rsp = post(url_197, mapjson.toString(), "utf-8");
    // System.out.println(rsp);
    // }
}
