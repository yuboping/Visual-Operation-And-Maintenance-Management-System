package com.asiainfo.lcims.omc.report.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.model.Host;
import com.asiainfo.lcims.omc.alarm.model.MdCollCycleTime;
import com.asiainfo.lcims.omc.alarm.model.MdMetric;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.omc.report.conf.UploadConf;
import com.asiainfo.lcims.omc.report.dao.ReportMetricDataDAO;
import com.asiainfo.lcims.omc.report.model.FileTarget;
import com.asiainfo.lcims.omc.report.model.ReportChartData;
import com.asiainfo.lcims.omc.report.model.UploadProtocol;
import com.asiainfo.lcims.omc.report.util.UploadUtil;
import com.asiainfo.lcims.util.DateTools;
import com.asiainfo.lcims.util.FileOperate;
import com.asiainfo.lcims.util.ToolsUtils;
import com.asiainfo.lcims.util.ZipUtil;

public class NfvUploadService extends AbstractUploadService {

    private static final Logger LOG = LoggerFactory.getLogger(NfvUploadService.class);

    private Map<String, String> mapVal = new HashMap<>();

    private List<String> metricidList = new ArrayList<>();

    private static final String PERFORM_FILE_FORMAT = "yyyyMMddHHmmss";

    private static final String STIME_FORMAT = "yyyy-MM-dd HH:mm";

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String PER_DIR_FORMAT = "yyyyMMddHH";

    @Override
    public FileTarget makeUploadTarget(UploadConf conf, List<MdCollCycleTime> cycleTimelist) {
        // 获取性能数据
        // 查询指标值信息
        String metricIdentitys = conf.getUploadMetricIdentitys();
        String[] metricIdentityArr = metricIdentitys.split(",");
        String stime = "";
        for (String metric_identity : metricIdentityArr) {
            MdMetric metric = InitParam.getMetricByIdentity(metric_identity);
            // 查询指标采集值当前周期值信息
            stime = UploadUtil.getCurrenttime(metric, cycleTimelist);
            if (StringUtils.isBlank(stime)) {
                stime = UploadUtil.getCurrenttime(metric, cycleTimelist);
            }
            List<ReportChartData> datas = ReportMetricDataDAO.getSimpleData(metric.getId(), stime);
            putMapVal(datas);
            metricidList.add(metric.getId());
        }
        String startTime = uploadFileTime(stime, STIME_FORMAT, TIMESTAMP_FORMAT);
        FileTarget pmTarget = createUploadTarget(startTime, conf);
        return pmTarget;
    }

    private FileTarget createUploadTarget(String time, UploadConf conf) {
        FileTarget target = new FileTarget();
        // GD-PM-MME-SCTPASSOC-B001-V1.1.0-20151227000000-15-001.csv
        String fileName = conf.getUploadProtocol().getPerformFileNamePrefix()
                + uploadFileTime(time, TIMESTAMP_FORMAT, PERFORM_FILE_FORMAT)
                + "-05.csv.tmp";
        String localDir = conf.getUploadProtocol().getLocalDir();
        String localName = localDir + "/" + fileName;
        LOG.info("tmp file name is:[{}]", localName);
        File file = FileOperate.createLocalFile(localName);
        // 生成csv.tmp格式的文件
        writeFile(file, time, conf);

        File zipFile = tmp2ZipFile(fileName, localDir, file);
        target.setLocalFile(zipFile);
        String uploadDir = conf.getUploadProtocol().getUploadDir() + "/"
                + getCurrentTime(PER_DIR_FORMAT);
        LOG.info("perform data upload directory is:[{}]", uploadDir);
        target.setUpload_dir(uploadDir);
        return target;
    }

    /**
     * 把tmp文件压缩为zip格式的文件
     * 
     * @param fileName
     * @param localDir
     * @param file
     * @return
     */
    private File tmp2ZipFile(String fileName, String localDir, File file) {
        String filePreName = FilenameUtils.getBaseName(fileName);
        String newFileName = localDir + "/" + filePreName;
        File newFile = new File(newFileName);
        boolean renameFlag = file.renameTo(newFile);
        if (renameFlag) {
            LOG.info("file:[{}] create succeeded", newFileName);
        } else {
            LOG.error("file:[{}] create failed", newFileName);
        }
        File zipFile = ZipUtil.compressZip(newFileName + ".zip", newFileName);
        FileUtils.deleteQuietly(newFile);
        LOG.info("delete file:[{}] succeeded", newFileName);
        return zipFile;
    }

    private void writeFile(File file, String time, UploadConf conf) {
        // 查询所有主机信息
        List<Host> hostlist = InitParam.getHosts();
        int size = hostlist.size();
        FileWriter fileWriter = null;
        BufferedWriter bw = null;
        try {
            fileWriter = new FileWriter(file);
            bw = new BufferedWriter(fileWriter);
            bw.write(publicInfo(conf));
            bw.write("startTime|hostname|addr|memory|cpu|disk\n");
            for (int i = 0; i < size; i++) {
                String content = makeContent(hostlist.get(i), time);
                bw.write(content);
            }
            bw.flush();
        } catch (IOException e) {
            LOG.error("create file error", e);
        } finally {
            IOUtils.closeQuietly(fileWriter);
            IOUtils.closeQuietly(bw);
        }
    }

    private String publicInfo(UploadConf conf) {
        String currentTime = getCurrentTime(TIMESTAMP_FORMAT);
        UploadProtocol protocol = conf.getUploadProtocol();
        String vendorName = protocol.getVendorName();
        String elementType = protocol.getElementType();
        String pmVersion = protocol.getPmVersion();
        String objectType = protocol.getObjectType();
        String info = "TimeStamp=" + currentTime + "|TimeZone=UTC+8|Period=5|VendorName="
                + vendorName + "|ElementType=" + elementType + "|PmVersion=" + pmVersion
                + "|ObjectType=" + objectType + "\n";
        LOG.info("public information : {}", info);
        return info;
    }

    private String makeContent(Host host, String time) {
        String content = time + "|" + host.getHostname() + "|" + host.getAddr();
        for (String metricId : metricidList) {
            content = content + "|" + getMapVal(host.getHostid() + "_" + metricId);
        }
        content = content + "\n";
        return content;
    }

    private String getMapVal(String key) {
        String value = mapVal.get(key);
        if (StringUtils.isBlank(value)) {
            return "0";
        }
        return value + "%";
    }

    private void putMapVal(List<ReportChartData> datas) {
        if (ToolsUtils.ListIsNull(datas)) {
            return;
        }
        for (ReportChartData reportChartData : datas) {
            mapVal.put(reportChartData.getMark() + "_" + reportChartData.getMetricid(),
                    reportChartData.getValue());
        }
    }

    private String uploadFileTime(String stime, String oldDateFormat, String newDateFormat) {
        DateTools dateTools = new DateTools(oldDateFormat);
        long millis = dateTools.getFormatTime(stime);
        DateTime dateTime = new DateTime(millis);
        String fileTime = dateTime.toString(newDateFormat);
        return fileTime;
    }

    private String getCurrentTime(String dateFormat) {
        DateTools dateTools = new DateTools(dateFormat);
        String currentTime = dateTools.getCurrentDate();
        return currentTime;
    }

}
