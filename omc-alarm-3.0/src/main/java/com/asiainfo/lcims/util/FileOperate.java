package com.asiainfo.lcims.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

/**
 * 文件大小，行数等各种属性计算
 */
public class FileOperate {
    private static final Logger LOG = LoggerFactory.getLogger(FileOperate.class);

    /**
     * 得到文件行数
     * 
     * @param file
     * @return
     */
    public static int getLineNum(File file) {
        if (!file.exists() || !file.isFile()) {
            return 0;
        }
        int lines = 0;
        try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
            if (reader != null) {
                reader.skip(file.length());
                lines = reader.getLineNumber();
            }
        } catch (Exception e) {
            LOG.error("get file lineNum error:" + file.getPath(), e);
        }
        return lines;
    }

    /**
     * 文件大小,向上取整（单位:KB）
     * 
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        Double fileSize = new Double("0");
        if (file.exists() && file.isFile()) {
            fileSize = Math.ceil(file.length() / (double) 1024);
        }
        return fileSize.longValue();
    }

    /**
     * 文件MD5值
     * 
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        try (FileInputStream in = new FileInputStream(file);) {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0,
                    file.length());
            digest = MessageDigest.getInstance("MD5");
            digest.update(byteBuffer);
            Cleaner cl = ((DirectBuffer) byteBuffer).cleaner();
            if (cl != null) {
                cl.clean();
            }
        } catch (Exception e) {
            LOG.error("get file MD5 error:" + file.getPath(), e);
            return null;
        }
        return toHex(digest.digest());
    }

    public static String toHex(byte[] buffer) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString();
    }

    /**
     * .gz文件解压
     * 
     * @param sourcedir
     * @return
     */
    public static File gunzipFile(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        File outFile = new File(file.getPath().substring(0, file.getPath().lastIndexOf('.')));
        try (GZIPInputStream gzin = new GZIPInputStream(new FileInputStream(file));
                FileOutputStream fout = new FileOutputStream(outFile)) {
            int num;
            byte[] buf = new byte[8192];
            while ((num = gzin.read(buf, 0, buf.length)) != -1) {
                fout.write(buf, 0, num);
            }
            fout.flush();
        } catch (Exception ex) {
            LOG.error("unzip file [" + file.getPath() + "] error", ex);
            return null;
        }
        return outFile;
    }

    /**
     * gzip方式压缩文件
     * 
     * @param file
     * @return
     */
    public static File gzipFile(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        File outFile = new File(file.getPath() + ".gz");
        try (FileInputStream in = new FileInputStream(file);
                GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(outFile))) {
            byte[] buf = new byte[8192];
            int num = 0;
            while ((num = in.read(buf, 0, buf.length)) != -1) {
                out.write(buf, 0, num);
            }
            out.flush();
        } catch (IOException e) {
            LOG.error("zip file [" + file.getPath() + "] error", e);
            return null;
        }
        return outFile;
    }

    public static File createLocalFile(String fileName) {
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

}
