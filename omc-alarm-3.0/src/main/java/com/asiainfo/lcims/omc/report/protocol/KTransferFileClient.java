package com.asiainfo.lcims.omc.report.protocol;

import java.io.OutputStream;
import java.util.List;

public abstract class KTransferFileClient extends KClient {

    private ProtocolDetail config;
    
    public ProtocolDetail getConfig() {
        return config;
    }
    
    public KTransferFileClient(ProtocolDetail config) {
        this.config = config;
    }

    public abstract String pwd();

    public abstract boolean cd(String path);

    public abstract boolean lcd(String path);

    public abstract boolean mkdir(String path);

    public abstract boolean isFileExist(String path, String fileName) throws Exception;

    public abstract boolean isDirExist(String path, String dirName) throws Exception;

    public abstract boolean upload(String localFilePath, String remoteFilePath) throws Exception;

    public abstract OutputStream upload(String fileName) throws Exception;

    public abstract long download(String remoteFilePath, String localFilePath) throws Exception;

    public abstract long download(String remoteFilePath, String localFilePath, long offset)
            throws Exception;

    public abstract boolean deleteFile(String path);

    public abstract boolean renameFile(String oldPath, String newPath);
    
    public abstract List<String> listNames(String path);
    /** 返回path路径下的所有文件的绝对路径的list*/
    public abstract List<String>getFileList(String path);
}
