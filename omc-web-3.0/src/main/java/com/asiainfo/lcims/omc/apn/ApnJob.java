package com.asiainfo.lcims.omc.apn;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.model.apn.ApnSingleData;
import com.asiainfo.lcims.omc.model.apn.MdApnRecord;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.apn.MdApnRecordDAO;
import com.asiainfo.lcims.omc.redis.OmcRedisUtil;
import com.asiainfo.lcims.omc.util.DateUtil;

public class ApnJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(ApnJob.class);

    private static Boolean isNeedFresh = false;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        MdApnRecordDAO apnRecordDAO = (MdApnRecordDAO) context.getMergedJobDataMap()
                .get("mdApnRecordDAO");
        LOG.info("start excute apn add data");
        if (isNeedFresh) {
            CommonInit commoninit = (CommonInit) context.getMergedJobDataMap().get("commoninit");
            // 更新缓存数据
            commoninit.loadApnInfo();
            LOG.info("Fresh loadApnInfo success");
            isNeedFresh = false;
        }
        List<MdApnRecord> mdApnRecordList = CommonInit.getMdApnRecordList();
        long time = System.currentTimeMillis() / 1000;
        // long time = 1594370200;
        LOG.info("millisTime : {}", time);
        for (MdApnRecord mdApnRecord : mdApnRecordList) {
            ApnSingleData apnData = new ApnSingleData();
            String apnName = mdApnRecord.getApn();
            String lowerApnName = StringUtils.lowerCase(apnName);
            int totalSum = 0;
            for (long i = 0; i < 60; i++) {
                long everySecond = time - i;
                String apnKey = lowerApnName + "," + String.valueOf(everySecond);
                String apnValue = OmcRedisUtil.getValueByKey(apnKey);
                int value = string2Int(apnValue);
                totalSum = value + totalSum;
            }
            LOG.debug("{},=== {}", apnName, totalSum);
            apnData.setItem(apnName);
            apnData.setMvalue(totalSum);
            long millistime = time * 1000;
            String stime = DateUtil.stampToDate(millistime, DateUtil.C_TIME_PATTON_DEFAULT);
            apnData.setStime(stime);
            apnData.setMetricId("ce986a8e43f940f0af6acdcd5b6e9a5b");
            apnRecordDAO.insertMetricDataSingle(apnData);
        }
    }

    public int string2Int(String value) {
        if (value == null) {
            return 0;
        }
        try {
            int intValue = Integer.parseInt(value);
            return intValue;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0;
        }
    }

    public static Boolean getIsNeedFresh() {
        return isNeedFresh;
    }

    public static void setIsNeedFresh(Boolean isNeedFresh) {
        ApnJob.isNeedFresh = isNeedFresh;
    }

}