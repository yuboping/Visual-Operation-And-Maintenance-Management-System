package com.asiainfo.lcims.omc.persistence.timecleaning;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.timecleaning.MdTimeCleaning;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import org.slf4j.Logger;

import java.util.Map;

public class SqlCleaningProvider {

    private static final Logger LOG = LoggerFactory.make();

    /**
     * 告警列表展示
     *
     * @param parameters
     * @return
     */
    public String deleteNormalTableData(Map<String, Object> parameters) {
        MdTimeCleaning timeCleaning = (MdTimeCleaning) parameters.get("timeCleaning");
        StringBuffer strb = new StringBuffer();
        strb.append("DELETE FROM "+ timeCleaning.getClean_table_name() + " WHERE ")
                .append(DbSqlUtil.getTimeSql(timeCleaning.getClean_column_name()) + " <= ")
                .append("'" +timeCleaning.getDelete_time() + "'");
        LOG.debug("deleteNormalTableData sql = {}", strb.toString());
        return strb.toString();
    }

}
