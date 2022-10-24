package com.asiainfo.lcims.omc.apn;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.model.apn.MdApnRecord;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.apn.MdApnRecordDAO;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

public class AddApnJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(AddApnJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Boolean isAddNeedFresh = false;
        MdApnRecordDAO apnRecordDAO = (MdApnRecordDAO) context.getMergedJobDataMap()
                .get("mdApnRecordDAO");
        LOG.info("start excute add apn");
        List<MdApnRecord> mdApnRecordList = CommonInit.getMdApnRecordList();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date time = cal.getTime();
        String tableSuffix = DateUtil.getFormatDate(
                new SimpleDateFormat(DateUtil.C_TIME_PATTON_DEFAULT).format(time),
                DateUtil.C_TIME_PATTON_DEFAULT, DateUtil.TABLE_SUFFIX);
        String tableName = Constant.METRIC_DATA_MULTI + tableSuffix;
        List<MdApnRecord> addList = apnRecordDAO.getAddApn(tableName);

        for (MdApnRecord add : addList) {
            Boolean isExist = false;
            for (MdApnRecord mdApnRecord : mdApnRecordList) {
                if (add.getApn().equals(mdApnRecord.getApn())) {
                    isExist = true;
                }
            }
            if (!isExist) {
                MdApnRecord insertMdApnRecord = new MdApnRecord();
                insertMdApnRecord.setApnid(IDGenerateUtil.getUuid());
                insertMdApnRecord.setApn(add.getApn());
                apnRecordDAO.insert(insertMdApnRecord);
                isAddNeedFresh = true;
            }
        }
        // CommonInit commoninit = (CommonInit)
        // context.getMergedJobDataMap().get("commoninit");
        // 更新缓存数据
        // commoninit.loadApnInfo();
        if (isAddNeedFresh) {
            ApnJob.setIsNeedFresh(true);
        }
    }
}