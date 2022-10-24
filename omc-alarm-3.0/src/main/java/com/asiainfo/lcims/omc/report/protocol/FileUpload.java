package com.asiainfo.lcims.omc.report.protocol;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.report.model.FileTarget;
import com.asiainfo.lcims.omc.report.protocol.ftp.FTPconfig;
import com.asiainfo.lcims.omc.report.protocol.ftp.KFtpClient;
import com.asiainfo.lcims.omc.report.protocol.sftp.KSFTPClient;
import com.asiainfo.lcims.omc.report.protocol.sftp.SFTPconfig;
import com.asiainfo.lcims.util.TimeTools;

/**
 * 文件上传
 *
 */
public class FileUpload {
    private static final Logger LOG = LoggerFactory.getLogger(FileUpload.class);

    /**
     * 上传单个文件到ftp服务器目标地址
     * 
     * @param target
     * @param conf
     * @return 返回是否上传成功
     */
    public static boolean uploadOne(FileTarget target, ProtocolDetail conf) {
        boolean success = false;
        KTransferFileClient client = createClient(conf);
        if (!login(client)) {
            return success;
        }
        target.setUpload_start(TimeTools.getTimestamp());
        createRemotePath(target.getUpload_dir(), client);
        success = uploadFile(client, target.getLocalFile(), target.getUpload_dir());
        if (success) {
            if (LOG.isDebugEnabled()) {
                LOG.info("upload file[" + target.getLocalFile() + "]" + " to [" + conf.getServerIP()
                        + ":" + target.getUpload_dir() + "] success ");
            }
            target.setUpload_complete(TimeTools.getTimestamp());
            //关闭连接
            close(client);
        } else {
            LOG.warn("upload file[" + target.getLocalFile() + "]" + " to [" + conf.getServerIP()
                    + ":" + target.getUpload_dir() + "] FAIL ");
        }
        return success;
    }

    /**
     * 将文件列表中所有文件上传到指定远程主机路径
     * 
     * @param filesList
     *            文件列表
     * @param remotePath
     *            远程路径
     * @param protocol
     *            协议详细配置
     * @return 返回成功上传文件列表
     */
    public static List<FileTarget> uploadAll(List<FileTarget> targetList, ProtocolDetail conf) {
        List<FileTarget> success = new ArrayList<>();
        KTransferFileClient client = createClient(conf);
        if (!login(client)) {
            return success;
        }
        for (FileTarget target : targetList) {
            try {
                if (!target.getLocalFile().exists()) {
                    LOG.warn("file[" + target.getLocalFile() + "]" + " not exists");
                    continue;
                }
                target.setUpload_start(TimeTools.getTimestamp());
                createRemotePath(target.getUpload_dir(), client);
                if (uploadFile(client, target.getLocalFile(), target.getUpload_dir())) {
                    success.add(target);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("upload file[" + target.getLocalFile() + "]" + " to ["
                                + conf.getServerIP() + ":" + target.getUpload_dir() + "] success ");
                    }
                }
                target.setUpload_complete(TimeTools.getTimestamp());
            } catch (Exception e) {
                LOG.error("upload file[" + target.getLocalFile() + "]" + " to ["
                        + conf.getServerIP() + ":" + target.getUpload_dir() + "] failure ");
            }
        }
        close(client);
        return success;
    }

    protected static boolean login(KTransferFileClient client) {
        int count = 0;
        do {
            try {
                client.login();
                return true;
            } catch (LoginException e) {
                LOG.error("connect to " + client.getConfig().getServerIP() + " failure");
            }
            count++;
        } while (count <= client.getConfig().getRetry());
        return false;
    }

    protected static void close(KTransferFileClient client) {
        if (client != null) {
            try {
                client.logout();
            } catch (LogoutException e) {
                LOG.error("disconnect to " + client.getConfig().getServerIP() + " failure");
            }
        }
    }

    /**
     * 上传文件
     * 
     * @param client
     *            上传客户端
     * @param file
     *            文件
     * @param remotePath
     *            远程路径
     * @param retry
     *            失败时重试次数
     * @return
     */
    private static boolean uploadFile(KTransferFileClient client, File file, String remotePath) {
        int count = 0;
        do {
            try {
                if (client.upload(file.getPath(), remotePath)) {
                    //是否要上传.ok文件
                    if (client.getConfig().isUploadOk()) {
                        File ok = new File(file + ".ok");
                        if(!ok.exists()) {
                            if(ok.createNewFile()) {
                                LOG.info("createNewFile["+file+".ok] is success.");
                            }
                        }
                        client.upload(ok.getPath(), remotePath);
                    }
                    
                    //文件上传成功后是否追加文件名后缀
                    if(client.getConfig().isAddSuffix()) {
                        String oldPath = remotePath+"/"+file.getName();
                        String newPath = remotePath+"/"+file.getName()+"."+client.getConfig().getSuffix();
                        client.renameFile(oldPath, newPath);
                    }
                    
                    return true;
                }
            } catch (Exception e) {
                LOG.error("文件上传失败", e);
            }
            count++;
            if (count <= client.getConfig().getRetry()) {
                login(client);
            }
        } while (count <= client.getConfig().getRetry());
        return false;
    }

    /**
     * 校验远程路径，若目录不存在则创建目录
     * 
     * @param remotePath
     * @param client
     */
    private static void createRemotePath(String remotePath, KTransferFileClient client) {
        client.cd("/");
        Path path = Paths.get(remotePath);
        
//        if (!client.cd(path.toString())) {
//            client.mkdir(path.toString());
//            client.cd(path.toString());
//        }
        path.forEach(e -> {
            if (!client.cd(e.toString())) {
                client.mkdir(e.toString());
                client.cd(e.toString());
            }
        });
    }

    protected static KTransferFileClient createClient(ProtocolDetail protocol) {
        KTransferFileClient client = null;
        switch (protocol.getType()) {
        case SFTP:
            SFTPconfig config = (SFTPconfig) protocol;
            client = new KSFTPClient(config);
            break;
        case FTP:
            FTPconfig ftpconfig = (FTPconfig) protocol;
            client = new KFtpClient(ftpconfig);
            break;
        default:
            break;
        }
        return client;
    }
}
