package com.asiainfo.lcims.omc.timecleaning;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.timecleaning.MdTimeCleaning;
import com.asiainfo.lcims.omc.persistence.timecleaning.MdTimeCleaningDao;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.QuartzConstant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import java.util.List;

public class TimeCleaningJob implements Job {
    private static final Logger logger = LoggerFactory.make();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        MdTimeCleaningDao mdTimeCleaningDao = (MdTimeCleaningDao) context.getMergedJobDataMap().get("mdTimeCleaningDao");

        logger.info("start excute cleaning data");

        List<MdTimeCleaning> mdTimeCleaningList = mdTimeCleaningDao.selectAllTimeCleaning();

        for(MdTimeCleaning mdTimeCleaning : mdTimeCleaningList){

            String tableName = mdTimeCleaning.getClean_table_name();

            String deleteTime = DateUtil.getFormatTime(mdTimeCleaning.getClean_interval(), QuartzConstant.DATE_FORMATE_NORMAL);

            mdTimeCleaning.setDelete_time(deleteTime);
            //需判断类型 根据类型决定是否拼接表名
            if(QuartzConstant.SPECIAL_TABLE_TYPE.equals(mdTimeCleaning.getType())){
                //获取月格式数据
                String monthFormat = DateUtil.getFormatTime(mdTimeCleaning.getClean_interval(), QuartzConstant.DATE_FORMATE_MONTH);
                //获取日格式数据
                String dayFormat = DateUtil.getFormatTime(mdTimeCleaning.getClean_interval(), QuartzConstant.DATE_FORMATE_DAY);
                tableName = tableName + QuartzConstant.SPLIT_FORMAT + monthFormat + QuartzConstant.SPLIT_FORMAT + dayFormat;
                deleteTime = DateUtil.getFormatTime(mdTimeCleaning.getClean_interval()-1, QuartzConstant.DATE_FORMATE_NORMAL);
                mdTimeCleaning.setDelete_time(deleteTime);
                mdTimeCleaning.setClean_table_name(tableName);
            }
            mdTimeCleaningDao.deleteNormalTableData(mdTimeCleaning);
        }
    }
}
