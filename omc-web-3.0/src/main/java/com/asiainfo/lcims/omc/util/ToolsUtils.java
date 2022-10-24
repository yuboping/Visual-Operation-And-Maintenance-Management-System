package com.asiainfo.lcims.omc.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolsUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ToolsUtils.class);

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean StringIsNull(String str){
    	if(null==str || "".equals(str))
    		return true;
    	return false;
    }
    
    public static boolean ListIsNull(List<?> list){
    	if(null==list || list.isEmpty())
    		return true;
    	return false;
    }
    
    /**
     * 返回各浏览器下载文件名
     * @param request
     * @param filename
     * @return
     */
    public static String getDownFileName(HttpServletRequest request,String filename){
        String userAgent = request.getHeader("User-Agent");
        // 针对IE或者以IE为内核的浏览器
        try {
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")
                    || userAgent.contains("Edge")|| userAgent.contains("Safari")) {  
                    filename = java.net.URLEncoder.encode(filename, "UTF-8");
            } else {
                // 非IE浏览器的处理
                filename = new String(filename.getBytes(), "ISO-8859-1");  
            }
            return filename;
        } catch (UnsupportedEncodingException e) {
            LOG.error("ie exploer file name error, {}", e);
        }
        return filename;
    }
    
    public boolean isKeyInMap(Map<String, String > map,String key){
        if(map.containsKey(key)){
            return true;
        }
        return false;
    }
    
    public static boolean isSyslogAddr(String ipAddress) {
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)" + ":\\d+";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

}
