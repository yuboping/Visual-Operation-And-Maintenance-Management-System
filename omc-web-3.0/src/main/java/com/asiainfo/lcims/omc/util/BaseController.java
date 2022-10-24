package com.asiainfo.lcims.omc.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.filter.XssHttpServletRequest;

/**
 * 
 * <p>
 * Title:BaseController
 * </p>
 * <p>
 * Description:控制器基类
 * </p>
 * <p>
 * Company
 * </p>
 * 
 * @author yuboping
 * @date
 */
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    public Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, String[]> reqMap = request.getParameterMap();
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.putAll(getIpAddr(request));
        for (Entry<String, String[]> m : reqMap.entrySet()) {
            String key = m.getKey();
            Object[] obj = (Object[]) reqMap.get(key);
            String value = obj[0].toString();
            if (!ToolsUtils.StringIsNull(value)) {
                value = XssHttpServletRequest.xssEncode(value);
            }
            resultMap.put(key, (obj.length > 1) ? obj : value);
        }
        return resultMap;
    }

    public Map<String, Object> iPLocal() {
        Map<String, Object> params = new HashMap<String, Object>();
        InetAddress ia;
        try {
            ia = InetAddress.getLocalHost();
            params.put("localIp", ia.getHostAddress());
            params.put("ipN", ia.getLocalHost());
        } catch (UnknownHostException e) {
            logger.error("ip error:" + e.getMessage(), e);
        }
        return params;
    }

    public Map<String, Object> getIpAddr(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        params.put("ip", ip);
        return params;
    }

    public void writetoclient(String content, HttpServletResponse httpServletResponse) {
        httpServletResponse.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = httpServletResponse.getWriter();
            writer.print(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.error("IOException:", e);
        }
    }

    public void exportFile(HttpServletRequest request, HttpServletResponse response, String path,
            String fileName) {
        FileInputStream in = null;
        ServletOutputStream os = null;
        HSSFWorkbook wb = null;
        try {
            in = new FileInputStream(path + fileName);
            wb = new HSSFWorkbook(in);
            fileName = ToolsUtils.getDownFileName(request, fileName);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-disposition",
                    String.format("attachment; filename=\"%s\"", fileName));
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            os = response.getOutputStream();
            wb.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error("", e);
            try {
                response.sendRedirect("/error");
            } catch (IOException e1) {
                logger.error("", e1);
            }
        } finally {
            close(in, os, wb);
        }
    }

    public void exportFileTXT(HttpServletRequest request, HttpServletResponse response, String path,
            String fileName) {
        FileInputStream in = null;
        ServletOutputStream os = null;
        try {
            in = new FileInputStream(path + fileName);
            byte[] result = new byte[in.available()];
            if (in.read(result) > 0) {
                fileName = ToolsUtils.getDownFileName(request, fileName);
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                response.setContentType("text/plain; charset=utf-8");
                os = response.getOutputStream();
                os.write(result);
                os.flush();
            }
        } catch (Exception e) {
            logger.error("", e);
            try {
                response.sendRedirect("/error");
            } catch (IOException e1) {
                logger.error("", e1);
            }
        } finally {
            try {
                if (in != null)
                    in.close();
                if (os != null)
                    os.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    public void close(FileInputStream in, ServletOutputStream os, HSSFWorkbook wb) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        if (wb != null) {
            try {
                wb.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    public Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}