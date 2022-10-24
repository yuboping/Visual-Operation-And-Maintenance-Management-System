package com.asiainfo.lcims.omc.persistence.apn.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.model.apn.ApnSingleData;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateUtil;

public class MdApnRecordDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(MdApnRecordDAOImpl.class);

    public String insertMetricDataSingle(Map<String, Object> parameters) {
        ApnSingleData apnSingleData = (ApnSingleData) parameters.get("apnSingleData");
        String metricId = apnSingleData.getMetricId();
        String item = apnSingleData.getItem();
        int mvalue = apnSingleData.getMvalue();
        String stime = apnSingleData.getStime();
        String tableSuffix = DateUtil.getFormatDate(stime, DateUtil.C_TIME_PATTON_DEFAULT,
                DateUtil.TABLE_SUFFIX);
        String tableName = Constant.METRIC_DATA_SINGLE + tableSuffix;
        StringBuffer buffer = new StringBuffer();
        buffer.append("INSERT INTO ").append(tableName).append(
                " (HOST_ID,METRIC_ID,MVALUE,ITEM,STIME,CREATE_TIME) VALUES('")
                .append("1").append("','").append(metricId).append("','").append(mvalue)
                .append("','").append(item).append("','").append(stime).append("','").append(stime)
                .append("')");
        String sql = buffer.toString();
        LOG.debug("apn insert single table sql : {}", sql);
        return sql;
    }
}
