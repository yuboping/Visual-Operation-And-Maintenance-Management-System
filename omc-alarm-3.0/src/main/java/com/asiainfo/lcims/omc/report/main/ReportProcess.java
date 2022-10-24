package com.asiainfo.lcims.omc.report.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.business.AlarmService;
import com.asiainfo.lcims.omc.alarm.model.MdCollCycleTime;
import com.asiainfo.lcims.omc.report.conf.UploadConf;
import com.asiainfo.lcims.omc.report.model.FileTarget;
import com.asiainfo.lcims.omc.report.model.UploadProtocol;
import com.asiainfo.lcims.omc.report.protocol.FileUpload;
import com.asiainfo.lcims.omc.report.protocol.ProtocolDetail;
import com.asiainfo.lcims.omc.report.protocol.ftp.FTPconfig;
import com.asiainfo.lcims.omc.report.protocol.sftp.SFTPconfig;
import com.asiainfo.lcims.omc.report.service.AbstractUploadService;
import com.asiainfo.lcims.omc.report.service.GdcuUploadService;
import com.asiainfo.lcims.omc.report.service.GscmSBUploadService;
import com.asiainfo.lcims.omc.report.service.GscmUploadService;
import com.asiainfo.lcims.omc.report.service.JscmUploadService;
import com.asiainfo.lcims.omc.report.service.NfvPMUploadService;
import com.asiainfo.lcims.omc.report.service.NfvUploadService;
import com.asiainfo.lcims.util.ConstantUtil;
import com.asiainfo.lcims.util.DateTools;
import com.asiainfo.lcims.util.ProviceUtill;

public class ReportProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ReportProcess.class);

    private List<MdCollCycleTime> cycleTimelist;

    private UploadConf conf = new UploadConf();

    public ReportProcess(){}

    public ReportProcess(List<MdCollCycleTime> cycleTimelist){
        this.cycleTimelist = cycleTimelist;
    }
    
    public void startProcess() {
        // 省份标识
        String province = AlarmService.conf.getProvince();
        AbstractUploadService uploadService = switchBusinessClass(province);
        if (uploadService == null) {
            return;
        }
        // 上报操作
        FileTarget target = uploadService.makeUploadTarget(conf, cycleTimelist);
        if (target != null) {
            List<FileTarget> fileTargetList = target.getFileTargetList();
            // 判断是不是GscmSBUploadService
            if (uploadService instanceof GscmSBUploadService) {
                if (fileTargetList.size() > 0) {
                    for (FileTarget fileTarget : fileTargetList) {
                        uploadFileTargetSB(fileTarget);
                    }
                } else {
                    uploadFileTargetSB(target);
                }
            } else {
                if (fileTargetList.size() > 0) {
                    for (FileTarget fileTarget : fileTargetList) {
                        uploadFileTarget(fileTarget);
                    }
                } else {
                    uploadFileTarget(target);
                }
            }
        }
    }
    
    private AbstractUploadService switchBusinessClass (String province){
        AbstractUploadService uploadService = null;
        switch (province) {
        case ProviceUtill.PROVINCE_JSCM:
            uploadService = new JscmUploadService();
            break;
        case ProviceUtill.PROVINCE_GDCU:
            uploadService = new GdcuUploadService();
            break;
        case ProviceUtill.PROVINCE_NFV:
            uploadService = new NfvUploadService();
            // 生成资源文件
            uploadPMData();
            break;
        case ProviceUtill.PROVINCE_GSCM:
            // 判断文件有没有生成,如果存在不执行
            // conf.getUploadProtocol().getLocalDir()/GS_NRTM_NKFmonth_JS_"+stime.substring(0,
            // 6)+"_P01_END"+".csv
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
            String filePath = conf.getUploadProtocol().getLocalDir() + "/GS_NRTM_NKFmonth_JS_"
                    + dateFormat.format(date) + "_P01_END.csv";
            File file = new File(filePath);
            if (!file.exists()) {
                // 不存在执行下面的代码
                AbstractUploadService process = new GscmUploadService();
                // 上报操作
                FileTarget target = process.makeUploadTarget(conf, cycleTimelist);
                if (target != null) {
                    List<FileTarget> fileTargetList = target.getFileTargetList();
                    if (fileTargetList.size() > 0) {
                        for (FileTarget fileTarget : fileTargetList) {
                            uploadFileTarget(fileTarget);
                        }
                    } else {
                        uploadFileTarget(target);
                    }
                }

            }
            uploadService = new GscmSBUploadService();
            break;
        default:
            break;
        }
        return uploadService;
    }
    
    private void uploadPMData() {
        DateTools dateTools = new DateTools("HHmm");
        String currentDate = dateTools.getCurrentDate();
        LOG.info("current date is {}", currentDate);
        if (StringUtils.equals(currentDate, "0000") || StringUtils.equals(currentDate, "1200")) {
            NfvPMUploadService uploadService = new NfvPMUploadService();
            FileTarget target = uploadService.makeUploadTarget(conf, cycleTimelist);
            if (target != null) {
                List<FileTarget> fileTargetList = target.getFileTargetList();
                if (fileTargetList.size() > 0) {
                    for (FileTarget fileTarget : fileTargetList) {
                        uploadFileTarget(fileTarget);
                    }
                } else {
                    uploadFileTarget(target);
                }
            }
        }
    }

    private void uploadFileTarget(FileTarget target){
        UploadProtocol protocolConf = conf.getUploadProtocol();
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
            //本地删除
            //target.getLocalFile().delete();
        }
    }

    private void uploadFileTargetSB(FileTarget target){
        UploadProtocol protocolConf = conf.getUploadProtocol();
        ProtocolDetail protocol = null;
        if(protocolConf.getProtocol().equals(ConstantUtil.SFTP)){
            protocol = new SFTPconfig();
        }else if(protocolConf.getProtocol().equals(ConstantUtil.FTP)){
            protocol = new FTPconfig();
        }else{
            LOG.error("uploadconfig protocol is undefine");
            return;
        }
        protocol.setLoginName(protocolConf.getLoginNameSB());
        protocol.setPassword(protocolConf.getPasswordSB());
        protocol.setServerIP(protocolConf.getServerIpSB());
        protocol.setServerPort(protocolConf.getServerPort());
        protocol.setAddSuffix(protocolConf.isAddSuffix());
        protocol.setSuffix(protocolConf.getSuffix());
        boolean successFlag = FileUpload.uploadOne(target, protocol);
        if(successFlag){
            LOG.info("upload file[" + target.getLocalFile() + "]" + " to [" + protocol.getServerIP()
                    + ":" + target.getUpload_dir() + "] success ");
            //本地删除
            //target.getLocalFile().delete();
        }
    }
    
    public List<MdCollCycleTime> getCycleTimelist() {
        return cycleTimelist;
    }

    public void setCycleTimelist(List<MdCollCycleTime> cycleTimelist) {
        this.cycleTimelist = cycleTimelist;
    }
    
}
