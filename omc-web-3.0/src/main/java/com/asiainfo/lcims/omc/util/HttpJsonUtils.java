package com.asiainfo.lcims.omc.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpJsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpJsonUtils.class);

    public static String post(String url, String content) {
        String response = "";
        HttpURLConnection connection = null;
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        try {
            LOG.info("post url={}, params={}", url, content);
            URL u = new URL(url);
            String lowerUrl = StringUtils.lowerCase(url);
            if (StringUtils.startsWith(lowerUrl, "https://")) {

            }
            long startTime = System.currentTimeMillis();
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", String.valueOf(content.length()));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(30000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(content);
            writer.flush();
            int code = connection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("HTTP POST Request Failed with Error code : " + code);
            }
            reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String temp;
            while ((temp = reader.readLine()) != null) {
                buffer.append(temp).append('\n');
            }
            response = String.valueOf(buffer);
            long endTime = System.currentTimeMillis();
            LOG.info("post cost time ：{} ns", (endTime - startTime));
            LOG.info("post response ：url= {}, rsp={}", url, response);

        } catch (Exception e) {
            LOG.error("post call error, url " + url, e);
            response = "";
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(reader);
            if (null != connection) {
                connection.disconnect();
            }
        }
        return response;
    }

}
