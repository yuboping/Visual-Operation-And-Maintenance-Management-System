package com.asiainfo.lcims.omc.report.util;

import java.util.List;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.model.MdCollCycleTime;
import com.asiainfo.lcims.omc.alarm.model.MdMetric;
import com.asiainfo.lcims.omc.report.conf.UploadConf;
import com.asiainfo.lcims.omc.report.model.FileTarget;
import com.asiainfo.lcims.omc.report.model.UploadProtocol;
import com.asiainfo.lcims.omc.report.protocol.FileUpload;
import com.asiainfo.lcims.omc.report.protocol.ProtocolDetail;
import com.asiainfo.lcims.omc.report.protocol.ftp.FTPconfig;
import com.asiainfo.lcims.omc.report.protocol.sftp.SFTPconfig;
import com.asiainfo.lcims.util.ConstantUtil;

public class UploadUtil {
    private static final Logger LOG = LoggerFactory.make();
    private static UploadConf uploadConf = new UploadConf();
    /**
     * 返回指标当前周期时间
     * @param metric
     * @param cycleTimelist
     * @return
     */
    public static String getCurrenttime(MdMetric metric, List<MdCollCycleTime> cycleTimelist) {
        for (MdCollCycleTime cycleTime : cycleTimelist) {
            if (metric.getCycle_id().intValue() == cycleTime.getCycleid().intValue()) {
                return cycleTime.getCurrenttime();
            }
        }
        return null;
    }
    
    public static void uploadFileTarget (FileTarget target){
        UploadProtocol protocolConf = uploadConf.getUploadProtocol();
        ProtocolDetail protocol = null;
        if(protocolConf.getProtocol().equals(ConstantUtil.SFTP)){
            protocol = new SFTPconfig();
        }else if(protocolConf.getProtocol().equals(ConstantUtil.FTP)){
            protocol = new FTPconfig();
        }else{
            LOG.error("uploadconfig protocol is undefine");
            return;
        }
        protocol.setLoginName(protocolConf.getLoginName());
        protocol.setPassword(protocolConf.getPassword());
        protocol.setServerIP(protocolConf.getServerIp());
        protocol.setServerPort(protocolConf.getServerPort());
        protocol.setAddSuffix(protocolConf.isAddSuffix());
        protocol.setSuffix(protocolConf.getSuffix());
        boolean successFlag = FileUpload.uploadOne(target, protocol);
        if(successFlag){
            LOG.info("upload file[" + target.getLocalFile() + "]" + " to [" + protocol.getServerIP()
            + ":" + target.getUpload_dir() + "] success ");
            target.getLocalFile().delete();
        }
    }

    public static UploadConf getUploadConf() {
        return uploadConf;
    }
}
