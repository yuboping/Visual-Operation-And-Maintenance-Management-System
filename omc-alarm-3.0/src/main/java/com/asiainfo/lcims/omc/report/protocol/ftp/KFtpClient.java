package com.asiainfo.lcims.omc.report.protocol.ftp;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.asiainfo.lcims.omc.report.protocol.ExecuteException;
import com.asiainfo.lcims.omc.report.protocol.KTransferFileClient;
import com.asiainfo.lcims.omc.report.protocol.LoginException;
import com.asiainfo.lcims.omc.report.protocol.LogoutException;
import com.asiainfo.lcims.util.FileOperate;

public class KFtpClient extends KTransferFileClient {
    final static private Logger LOGGER = LoggerFactory.getLogger(KFtpClient.class);
    private FTPClient ftpClient;

    public KFtpClient(FTPconfig config) {
        super(config);
    }

    @Override
    public String pwd() {
        try {
            String pwd = ftpClient.printWorkingDirectory();
            if (pwd == null)
                pwd = "";
            return pwd;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return "";
        }
    }

    @Override
    public boolean cd(String path) {
        try {
            return ftpClient.changeWorkingDirectory(path);
        } catch (IOException e) {
            LOGGER.error(e.getMessage() + " path" + path, e);
            return false;
        }
    }

    @Override
    public boolean lcd(String path) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mkdir(String path) {
        try {
            return ftpClient.makeDirectory(path);
        } catch (IOException e) {
            LOGGER.error(e.getMessage() + " path" + path, e);
            return false;
        }
    }

    @Override
    public boolean isFileExist(String path, String fileName) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isDirExist(String path, String dirName) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean upload(String localFilePath, String remoteFilePath) throws Exception {
        File localFile = new File(localFilePath);
        BufferedInputStream inStream = null;
        boolean success = false;
        try {
            this.ftpClient.changeWorkingDirectory(remoteFilePath);// ??????????????????
            inStream = new BufferedInputStream(new FileInputStream(localFile));
            success = ftpClient.storeFile(localFile.getName(), inStream);
            return success;
        } catch (Exception e) {
            LOGGER.error("??????" + localFile + "????????????", e);
            throw new Exception(e.getMessage());
        } finally {
            close(inStream);
        }
    }

    private void close(Closeable in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                LOGGER.error("close error", e);
            }
        }
    }

    @Override
    public OutputStream upload(String fileName) throws Exception {
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        return ftpClient.storeUniqueFileStream(fileName);
    }

    /**
     * remotePath??????????????? +?????????
     * localFileDir???????????????
     */
    @Override
    public long download(String remotePath, String localFileDir) throws Exception {
    	String fileName = null;
    	String remoteDir = null;
    	String localFilePath = null;
    	if(!remotePath.endsWith("/")){
    		int a = remotePath.lastIndexOf("/");
    		fileName = remotePath.substring(a+1, remotePath.length());
    		remoteDir = remotePath.substring(0, a+1);
    		if(!localFileDir.endsWith("/"))
    			localFilePath = localFileDir + "/" + fileName;
    		else
    			localFilePath = localFileDir + fileName;
    	}else{
    		remoteDir = remotePath;
    		localFilePath = localFileDir;
    	}
        File localfile = new File(localFilePath);
        FileOutputStream outStream = null;
        boolean success = false;
        try {
            ftpClient.changeWorkingDirectory(remoteDir);
            outStream = new FileOutputStream(localfile);
            success = ftpClient.retrieveFile(localfile.getName(), outStream);
            if (success == true) {
                outStream.flush();
                return localfile.length();
            } else {
                return 0L;
            }
        } catch (Exception e) {
            LOGGER.error(remoteDir + "????????????");
            throw new Exception(e.getMessage());
        } finally {
            close(outStream);
        }
    }

    @Override
    public long download(String remoteFilePath, String localFilePath, long offset)
            throws Exception {
        FileOperate.createLocalFile(localFilePath);
        ftpClient.setRestartOffset(offset);
        File localFile = new File(localFilePath);
        try (OutputStream out = new FileOutputStream(localFile)) {
            boolean flag = ftpClient.retrieveFile(remoteFilePath, out);
            if (flag) {
                File tempFile = new File(localFilePath);
                return offset + tempFile.length();
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return offset;
    }

    @Override
    public boolean deleteFile(String path) {
        try {
            return ftpClient.deleteFile(path);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean renameFile(String oldPath, String newPath) {
        try {
            return ftpClient.rename(oldPath, newPath);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void login() throws LoginException {
        ftpClient = new FTPClient();
        FTPClientConfig ftpClientConfig = new FTPClientConfig();
        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.configure(ftpClientConfig);
        try {
            ftpClient.connect(getConfig().getServerIP(), getConfig().getServerPort());
            int reply = this.ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.ftpClient.disconnect();
                throw new LoginException("connect to " + getConfig().getServerPort() + " error");
            }
            if (ftpClient.login(getConfig().getLoginName(), getConfig().getPassword())) {
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.setBufferSize(1024 * 2);
                ftpClient.setDataTimeout(30 * 1000);
            } else {
                throw new LoginException("login to " + getConfig().getServerPort() + " error");
            }
        } catch (Exception e) {
            throw new LoginException("connect to " + getConfig().getServerPort() + " error");
        }

    }

    @Override
    public void logout() throws LogoutException {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception ex) {
            LOGGER.error("logout error:", ex);
            throw new LogoutException(ex.getMessage());
        }
    }

    @Override
    public void active_test() throws ExecuteException {
        try {
            ftpClient.noop();
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ExecuteException(ex.getMessage());
        }
    }

    /***
     * @???????????????
     * @param localDirectory
     *            ???????????????
     * @param remoteDirectoryPath
     *            Ftp ???????????????
     * @throws Exception
     */
    public boolean uploadDirectory(String localDirectory, String remoteDirectoryPath)
            throws Exception {
        File src = new File(localDirectory);
        try {
            remoteDirectoryPath = remoteDirectoryPath + "/" + src.getName() + "/";
            this.ftpClient.makeDirectory(remoteDirectoryPath);
        } catch (IOException e) {
            LOGGER.info(remoteDirectoryPath + "??????????????????");
        }
        File[] allFile = src.listFiles();
        for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
            if (!allFile[currentFile].isDirectory()) {
                String srcName = allFile[currentFile].getPath().toString();
                upload(srcName, remoteDirectoryPath);
            }
        }
        for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
            if (allFile[currentFile].isDirectory()) {
                uploadDirectory(allFile[currentFile].getPath().toString(), remoteDirectoryPath);
            }
        }
        return true;
    }

    /***
     * @???????????????
     * @param localDirectoryPath????????????
     * @param remoteDirectory
     *            ???????????????
     */
    public boolean downLoadDirectory(String localDirectoryPath, String remoteDirectory) {
        try {
            String fileName = new File(remoteDirectory).getName();
            localDirectoryPath = localDirectoryPath + fileName + "/";
            new File(localDirectoryPath).mkdirs();
            FTPFile[] allFile = this.ftpClient.listFiles(remoteDirectory);
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (!allFile[currentFile].isDirectory()) {
                    download(remoteDirectory,
                            localDirectoryPath + "/" + allFile[currentFile].getName());
                }
            }
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (allFile[currentFile].isDirectory()) {
                    String strremoteDirectoryPath = remoteDirectory + "/"
                            + allFile[currentFile].getName();
                    downLoadDirectory(localDirectoryPath, strremoteDirectoryPath);
                }
            }
        } catch (Exception e) {
            LOGGER.info("?????????????????????");
            return false;
        }
        return true;
    }

	@Override
    public List<String> listNames(String path) {
		try {
            return Arrays.asList(this.ftpClient.listNames(path));
		} catch (IOException e) {
			LOGGER.info("??????ftp????????? ???????????????????????? ??? ");
            return Collections.emptyList();
		}
	}

	@Override
	public List<String> getFileList(String path) {
		List<String> list = new ArrayList<String>();
		return listAllFilenamesUnderDir(list,path);
	}
	
	/**
	 * ?????????????????????????????????????????????????????????????????????????????????
	 * @param nameList
	 * @param dirPath
	 * @return
	 */
	private List<String> listAllFilenamesUnderDir(List<String> nameList, String dirPath) {
		if (dirPath == null) {
			LOGGER.warn("????????????????????????null ??? " + dirPath);
			return Collections.emptyList();
		}
		if(!dirPath.endsWith("/")){
			dirPath = dirPath + "/";
		}
		LOGGER.info("???????????????=dirPath="+ dirPath);
		try {
			FTPFile[] ftpFiles = null; 
			ftpClient.enterLocalPassiveMode();
//			ftpClient.configure(new FTPClientConfig("com.asiainfo.logtrace.core.protocol.ftp.UnixFTPEntryParser"));
			ftpFiles = ftpClient.listFiles(dirPath);
			if(ftpFiles==null)
				LOGGER.info("??????????????????");
			else
				LOGGER.info("????????????====="+ ftpFiles.length);
			for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
				 FTPFile file = ftpFiles[i];
				 if (file.isFile()) {
					 nameList.add(dirPath+file.getName());
				} else if (file.isDirectory()) {
					listAllFilenamesUnderDir(nameList, dirPath + file.getName() + "/");
				}
			 }
		} catch (IOException e) {
			LOGGER.warn("ftp???????????? ??? " + e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
        return nameList;
    }
}
