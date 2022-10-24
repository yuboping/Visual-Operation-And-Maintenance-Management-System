package com.asiainfo.lcims.omc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.boot.MainServer;

public class ReadFile {
    public static final String PROVINCE = MainServer.conf.getProvince();
    public static final String DEFAULT = "develop";
    private static final Logger LOG = LoggerFactory.make();

    public static String readByFilename(String filename) {
        BufferedReader reader = null;
        String laststr = "";
        try {
            reader = getInputStream(filename);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (Exception e) {
            LOG.error("读取文件失败:" + filename, e);
        } finally {
            close(reader);
        }
        return laststr;
    }

    public static List<String> readListByFilename(String filename) {
        BufferedReader reader = null;
        List<String> list = new ArrayList<String>();
        try {
            reader = getInputStream(filename);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                list.add(tempString);
            }
            reader.close();
        } catch (Exception e) {
            LOG.error("读取文件失败:" + filename, e);
        } finally {
            close(reader);
        }
        return list;
    }

    private static void close(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
    }

    private static BufferedReader getInputStream(String filename)
            throws UnsupportedEncodingException {
        BufferedReader reader;
        ClassLoader loader = ReadFile.class.getClassLoader();
        InputStream in = loader.getResourceAsStream(PROVINCE + "/" + filename);
        if (in == null) {
            in = loader.getResourceAsStream(DEFAULT + "/" + filename);
        }
        InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
        reader = new BufferedReader(inputStreamReader);
        return reader;
    }

    public static String getPath(String filename) {
        ClassLoader loader = ReadFile.class.getClassLoader();
        URL file = loader.getResource(PROVINCE + "/" + filename);
        if (file == null) {
            file = loader.getResource(DEFAULT + "/" + filename);
        }
        return file == null ? filename : file.getPath();
    }

    public static String getFileExistFolder(String filename) {
        ClassLoader loader = ReadFile.class.getClassLoader();
        URL file = loader.getResource(PROVINCE + "/" + filename);
        if (file != null) {
            return PROVINCE;
        }
        file = loader.getResource(DEFAULT + "/" + filename);
        if (file != null) {
            return DEFAULT;
        }
        return "";
    }

}
