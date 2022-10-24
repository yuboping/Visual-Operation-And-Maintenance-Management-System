package com.asiainfo.lcims.omc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * @author wangchun
 * @data 20160615
 * 
 */
public class SFTPChannel {

    private static final Logger LOG = LoggerFactory.make();

    /**
     * 连接sftp服务器
     * 
     * @param host
     *            主机
     * @param port
     *            端口
     * @param username
     *            用户名
     * @param password
     *            密码
     * @return
     */
    public Session connect(String host, int port, String username, String password) {
        Session sshSession = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            LOG.info("Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            LOG.info("Session connected.");
            LOG.info("Opening Channel.");
        } catch (Exception e) {
            LOG.error("connect sftp error, reason : {}", e);
        }
        return sshSession;
    }

    /**
     * 上传文件
     * 
     * @param directory
     *            上传的目录
     * @param uploadFile
     *            要上传的文件
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        FileInputStream fileInputStream = null;
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            fileInputStream = new FileInputStream(file);
            sftp.put(fileInputStream, file.getName());
        } catch (Exception e) {
            LOG.error("upload file error, reason : {}", e);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    /**
     * 下载文件
     * 
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveFile
     *            存在本地的路径
     * @param sftp
     */
    public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            sftp.get(downloadFile, new FileOutputStream(file));
        } catch (Exception e) {
            LOG.error("download file error, reason : {}", e);
        }
    }

    /**
     * 删除文件
     * 
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            LOG.error("delete file error, reason : {}", e);
        }
    }
}
