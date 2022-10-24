package com.asiainfo.lcims.omc.report.service;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.model.MdCollCycleTime;
import com.asiainfo.lcims.omc.report.conf.UploadConf;
import com.asiainfo.lcims.omc.report.dao.NRMResourceDataDAO;
import com.asiainfo.lcims.omc.report.dto.XmlFileContent;
import com.asiainfo.lcims.omc.report.model.FileTarget;
import com.asiainfo.lcims.omc.report.model.NRMResourceData;
import com.asiainfo.lcims.util.DateTools;
import com.asiainfo.lcims.util.FileOperate;
import com.asiainfo.lcims.util.XmlUtil;
import com.asiainfo.lcims.util.ZipUtil;

public class NfvPMUploadService extends AbstractUploadService {

    private static final Logger LOG = LoggerFactory.getLogger(XmlFileContent.class);

    private static final Long BATCH_NUM = 610000L;

    public static final String PERFORM_FILE_FORMAT = "yyyyMMddHHmmss";

    public static final String STIME_FORMAT = "yyyy-MM-dd HH:mm";

    public static final String RES_DIR_FORMAT = "yyyyMMdd";

    @Override
    public FileTarget makeUploadTarget(UploadConf conf, List<MdCollCycleTime> cycleTimelist) {
        // 获取资源数据
        String stimeStr = getStime();
        List<NRMResourceData> nrmDataList = NRMResourceDataDAO.getNRMResourceData();
        FileTarget target = createUploadTarget(conf, nrmDataList, stimeStr);
        return target;
    }

    private FileTarget createUploadTarget(UploadConf conf, List<NRMResourceData> nrmDataList,
            String stime) {
        int dbSize = nrmDataList.size();
        LOG.info("omc data list size is : {}", dbSize);
        FileTarget targetList = new FileTarget();
        String localDir = conf.getUploadProtocol().getLocalDir();
        String fileName = "";
        if (dbSize <= BATCH_NUM) {
            fileName = conf.getUploadProtocol().getResourceFileNamePrefix()
                    + uploadFileTime(stime, STIME_FORMAT, PERFORM_FILE_FORMAT) + ".xml.tmp";
            String localName = localDir + File.separator + fileName;
            LOG.info("tmp file name is:[{}]", localName);
            File file = FileOperate.createLocalFile(localName);
            // 生成xml.tmp格式的文件
            writeFile(file, nrmDataList);

            File zipFile = tmp2ZipFile(fileName, localDir, file);

            targetList.setLocalFile(zipFile);
            String uploadDir = conf.getUploadProtocol().getUploadDirPM() + File.separator
                    + getCurrentTime(RES_DIR_FORMAT);
            LOG.info("resource data upload directory is:[{}]", uploadDir);
            targetList.setUpload_dir(uploadDir);
        } else {
            List<FileTarget> fileTargetList = new ArrayList<>();
            long pageCount = 0; // 页数
            if (dbSize % BATCH_NUM == 0) {
                pageCount = dbSize / BATCH_NUM;
            } else {
                pageCount = dbSize / BATCH_NUM + 1;
            }
            for (int i = 1; i <= pageCount; i++) {
                FileTarget target = new FileTarget();
                String seq = String.format("%03d", i);
                fileName = conf.getUploadProtocol().getResourceFileNamePrefix()
                        + uploadFileTime(stime, STIME_FORMAT, PERFORM_FILE_FORMAT) + "-" + seq
                        + ".xml.tmp";
                String localName = localDir + File.separator + fileName;
                LOG.info("tmp file name is:[{}]", localName);
                File file = FileOperate.createLocalFile(localName);

                List<NRMResourceData> dataList = NRMResourceDataDAO
                        .getLimitNRMResourceData((i - 1) * BATCH_NUM, BATCH_NUM);
                // 生成xml.tmp格式的文件
                writeFile(file, dataList);

                File zipFile = tmp2ZipFile(fileName, localDir, file);

                target.setLocalFile(zipFile);
                String uploadDir = conf.getUploadProtocol().getUploadDir() + File.separator
                        + getCurrentTime(RES_DIR_FORMAT);
                LOG.info("resource data upload directory is:[{}]", uploadDir);
                target.setUpload_dir(uploadDir);
                fileTargetList.add(target);
            }
            targetList.setFileTargetList(fileTargetList);
        }
        return targetList;
    }

    private File tmp2ZipFile(String fileName, String localDir, File file) {
        String filePreName = FilenameUtils.getBaseName(fileName);
        String newFileName = localDir + File.separator + filePreName;
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

    private void writeFile(File file, List<NRMResourceData> nrmDataList) {
        LOG.info("start to create local file:[{}]", file.getAbsolutePath());
        UploadConf conf = new UploadConf();

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("DataFile");
        XmlFileContent.rootElement(root);

        Element fileHeader = root.addElement("FileHeader");
        XmlFileContent.headerElement(fileHeader, conf);

        Element objectsElement = root.addElement("Objects");
        objectsElement.addElement("ObjectType").addText(conf.getUploadProtocol().getObjectType());
        Element fieldName = objectsElement.addElement("FieldName");
        List<String> columnNames = getFieldNames();
        XmlFileContent.fieldNameElement(fieldName, columnNames);

        Element fieldValue = objectsElement.addElement("FieldValue");
        XmlFileContent.fieldValueElement(fieldValue, columnNames, nrmDataList, conf);

        OutputFormat format = XmlUtil.setPrettyPrint();
        XmlUtil.saveXml(document, file.getAbsolutePath(), format);
        LOG.info("local file:[{}] create succeeded", file.getAbsolutePath());
    }

    public List<String> getFieldNames() {
        Field[] declaredFields = NRMResourceData.class.getDeclaredFields();
        List<String> list = new ArrayList<>();
        for (Field field : declaredFields) {
            String name = field.getName();
            list.add(name);
        }
        return list;
    }

    public String uploadFileTime(String stime, String oldDateFormat, String newDateFormat) {
        DateTools dateTools = new DateTools(oldDateFormat);
        long millis = dateTools.getFormatTime(stime);
        DateTime dateTime = new DateTime(millis);
        String fileTime = dateTime.toString(newDateFormat);
        return fileTime;
    }

    public String getCurrentTime(String dateFormat) {
        DateTools dateTools = new DateTools(dateFormat);
        String currentTime = dateTools.getCurrentDate();
        return currentTime;
    }

    private String getStime() {
        DateTools dateTools = new DateTools("yyyy-MM-dd HH:mm");
        String currentHour = dateTools.getCurrentHour();
        String stime = "";
        int hour = Integer.parseInt(currentHour);
        DateTools noonDateTools = null;
        if (hour >= 12) {
            noonDateTools = new DateTools("yyyy-MM-dd 12:00");
        } else {
            noonDateTools = new DateTools("yyyy-MM-dd 00:00");
        }
        stime = noonDateTools.getCurrentDate();
        return stime;
    }

}
