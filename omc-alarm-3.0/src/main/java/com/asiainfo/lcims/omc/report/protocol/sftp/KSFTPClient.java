package com.asiainfo.lcims.omc.report.protocol.sftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.report.protocol.ExecuteException;
import com.asiainfo.lcims.omc.report.protocol.KTransferFileClient;
import com.asiainfo.lcims.omc.report.protocol.LoginException;
import com.asiainfo.lcims.omc.report.protocol.LogoutException;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class KSFTPClient extends KTransferFileClient {

	final static private Logger LOGGER = LoggerFactory.getLogger(KSFTPClient.class);

	protected JSch jsch;
	protected Session session;
	protected ChannelSftp channel;

	public KSFTPClient(SFTPconfig config) {
		super(config);
	}

	@Override
	public void login() throws LoginException {
		try {
			jsch = new JSch();
			session = jsch.getSession(getConfig().getLoginName(), getConfig().getServerIP(), getConfig().getServerPort());
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(getConfig().getPassword());
			SFTPconfig conf = (SFTPconfig) getConfig();
			session.connect(conf.getSessionTimeout());
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect(conf.getChannelTimeout());
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new LoginException(ex.getMessage());
		}
	}

	@Override
	public void logout() throws LogoutException {
		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}

	@Override
	public void active_test() throws ExecuteException {
		try {
			channel.pwd();
		} catch (SftpException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new ExecuteException(ex.getMessage());
		}

	}

	@Override
	public String pwd() {
		try {
			return channel.pwd();
		} catch (SftpException e) {
			LOGGER.error(e.getMessage(), e);
			return "";
		}
	}

	@Override
	public boolean cd(String path) {
		try {
			channel.cd(path);
		} catch (SftpException ex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean lcd(String path) {
		try {
			channel.lcd(path);
		} catch (SftpException ex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean mkdir(String path) {
		try {
			channel.mkdir(path);
		} catch (SftpException ex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isFileExist(String path, String fileName) throws Exception {
		this.channel.setTerminalMode("binary".getBytes());
		@SuppressWarnings("unchecked")
		Vector<LsEntry> vector = (Vector<LsEntry>) channel.ls(path);
		if (vector == null || vector.isEmpty()) {
			return false;
		}
		for (LsEntry entry : vector) {
			if (!entry.getAttrs().isDir() && entry.getFilename().equals(fileName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDirExist(String path, String dirName) throws Exception {
		this.channel.setTerminalMode("binary".getBytes());
		@SuppressWarnings("unchecked")
		Vector<LsEntry> vector = (Vector<LsEntry>) channel.ls(path);
		if (vector == null || vector.isEmpty()) {
			return false;
		}
		for (LsEntry entry : vector) {
			if (entry.getAttrs().isDir() && entry.getFilename().equals(dirName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean upload(String localFilePath, String remoteFilePath) throws Exception {
		try {
			channel.put(localFilePath, remoteFilePath);
		} catch (SftpException ex) {
			return false;
		}
		return true;
	}

	@Override
	public OutputStream upload(String fileName) throws Exception {
		return channel.put(fileName);
	}

	@Override
	public long download(String remoteFilePath, String localFilePath) throws Exception {
		channel.get(remoteFilePath, localFilePath);
		File tempFile = new File(localFilePath);
		return tempFile.length();
	}

	@Override
	public long download(String remoteFilePath, String localFilePath, long offset) throws Exception {
		FileOutputStream fos = new FileOutputStream(localFilePath);
		byte[] buffer = new byte[8192];
		boolean flag = false;
		long skipCount = 0, readCount = 0, availableCount = 0;
		try {
			InputStream remoteIS = channel.get(remoteFilePath);
			readCount = remoteIS.read(buffer);
			boolean needContinueSkip = true;
			if (readCount > 0) {
				if (readCount < offset) {
					skipCount += readCount;
				} else {
					skipCount += offset;
					fos.write(buffer, (int) offset, (int) (readCount - offset));
					needContinueSkip = false;
				}
			}
			if (needContinueSkip) {
			    long skips = 0;
				while ((availableCount = remoteIS.available()) > 0) {
					if (skipCount + availableCount < offset) {
					    skips = remoteIS.skip(availableCount);
						skipCount += availableCount;
					} else {
					    skips = remoteIS.skip(offset - skipCount);
						skipCount += (offset - skipCount);
						break;
					}
				}
			}
			while ((readCount = remoteIS.read(buffer)) > 0) {
				fos.write(buffer, 0, (int) readCount);
			}
			flag = true;
		} catch (SftpException ex) {
			throw new Exception(ex.getMessage());
		} finally {
			fos.close();
		}
		if (flag) {
			File tempFile = new File(localFilePath);
			return offset + tempFile.length();
		}
		return offset;
	}

	@Override
	public boolean deleteFile(String path) {
		try {
			channel.rm(path);
		} catch (SftpException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean renameFile(String oldPath, String newPath) {
		try {
			channel.rename(oldPath, newPath);
		} catch (SftpException e) {
			return false;
		}
		return true;
	}

	@Override
    public List<String> listNames(String path) {
        List<String> list = new ArrayList<String>();
        SftpATTRS dirStat = null;
        try {
            dirStat = this.channel.stat(path);
        } catch (SftpException e) {
        }
        if (dirStat == null) {
            LOGGER.warn("指定的文件路径下不存在文件 ！ " + path);
            return list;
        }
		try {
			this.channel.setTerminalMode("binary".getBytes());
			@SuppressWarnings("unchecked")
			Vector<LsEntry> vector = (Vector<LsEntry>) channel.ls(path);
            if (vector == null || vector.isEmpty()) {
                return list;
            }
            for (int i = 0; i < vector.size(); i++) {
                String name = vector.get(i).getFilename();
                if (!".".equals(name) && !"..".equals(name)) {
                    list.add(name);
                }
			}
		} catch (SftpException e) {
			LOGGER.error(e.getMessage(), e);
		}
        return list;
	}

	/**
	 * 	获取一个path下所有文件的绝对路径的方法
	 */
	@Override
	public List<String> getFileList(String path) {
		List<String> list = new ArrayList<String>();
		return listAllFilenamesUnderDir(list, path);
	}
	
	/**
	 * 通过迭代的方法，获取一个目录层级下面的所有文件的文件名
	 * @param nameList
	 * @param dirPath
	 * @return
	 */
	@SuppressWarnings("unchecked")
    private List<String> listAllFilenamesUnderDir(List<String> nameList, String dirPath) {
        SftpATTRS dirStat = null;
        try {
            dirStat = this.channel.stat(dirPath);
        } catch (SftpException e) {
        }
        if (dirStat == null) {
            LOGGER.warn("指定的文件路径下不存在文件 ！ " + dirPath);
            return Collections.emptyList();
        }
        if (!dirStat.isDir()) {
            nameList.add(dirPath);
        } else {
            try {
                Vector<LsEntry> v = this.channel.ls(dirPath);
                for (LsEntry entry : v) {
                    String entryFilename = entry.getFilename();
                    if (".".equals(entryFilename) || "..".equals(entryFilename)) {
                        continue;
                    }
                    listAllFilenamesUnderDir(nameList, dirPath + "/" + entryFilename);
                }
            } catch (SftpException e) {
            }
        }
        return nameList;
    }

}