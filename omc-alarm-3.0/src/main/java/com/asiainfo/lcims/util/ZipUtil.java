package com.asiainfo.lcims.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhujiansheng
 * @date 2020年5月28日 上午11:01:23
 * @version V1.0
 */
public class ZipUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ZipUtil.class);

    /**
     * 压缩Zip文件
     * 
     * @param destFileName
     *            压缩成Zip的目标文件
     * @param srcFileName
     *            源文件
     * @return
     */
    public static File compressZip(String destFileName, String srcFileName) {
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        FileOutputStream fos = null;
        ZipOutputStream zout = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(destFile);
            zout = new ZipOutputStream(fos);
            fis = new FileInputStream(srcFile);
            String fileName = srcFile.getName();
            ZipEntry entry = new ZipEntry(fileName);
            zout.putNextEntry(entry);

            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fis.read(buffer)) != -1) {
                zout.write(buffer, 0, len);
            }
            zout.closeEntry();
            LOG.info("zip file:{} compress succeeded", destFile);
        } catch (Exception e) {
            LOG.error("compress zip file error :", e);
        } finally {
            IOUtils.closeQuietly(zout);
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(fis);
        }
        return destFile;
    }

}
