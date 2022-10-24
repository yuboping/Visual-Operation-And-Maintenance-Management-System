package com.asiainfo.lcims.omc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title:FileUtil Description:文件读写工具类
 * 
 */
public class FileUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 
     * <p>
     * Description:读文件
     * </p>
     * 
     * @param path
     *            读取文件目录
     * @param code
     *            读取文件编码
     * @return
     */
    public static String readFile(String path, String code) {
        FileInputStream is = null;
        BufferedReader br = null;
        File file = null;
        StringBuffer result = new StringBuffer();
        try {
            file = new File(path);
            is = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(is, code));
            // 判断文件是否存在
            if (file.exists()) {
                String line = "";
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (Exception e) {
            result.append("-1");
            LOG.error("readFile method file read failure: {}", e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(br);
        }
        return result.toString();
    }

    /**
     * 
     * <p>
     * Description:写文件
     * </p>
     * 
     * @param path
     *            写入文件目录
     * @param content
     *            写入文件内容
     * @param code
     *            写入文件编码
     * @return
     */
    public static String writeFile(String path, String content, String code) {
        File file = null;
        FileOutputStream out = null;
        OutputStreamWriter os = null;
        BufferedWriter writer = null;
        String result = "";
        try {
            file = new File(path);
            out = new FileOutputStream(file);
            os = new OutputStreamWriter(out, code);
            writer = new BufferedWriter(os);
            writer.write(content + "\n");
            result = "0";
        } catch (Exception e) {
            result = "-1";
            LOG.error("writeFile method File write failed: {}", e.getMessage());
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(os);
        }
        return result;
    }
}