package com.asiainfo.lcims.omc.brasipcollect;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.BdNasDAO;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import java.util.List;


public class BrasIpCollectJob implements Job {
    private static final Logger logger = LoggerFactory.make();
    
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        BdNasDAO nasDao = (BdNasDAO) context.getMergedJobDataMap().get("nasDao");
        CommonInit commoninit = (CommonInit) context.getMergedJobDataMap().get("commoninit");
        logger.info("start excute shcm brasip add data");
        // 查询新增的brasip信息 
        MdMetric metric = CommonInit.getMetricByIdentity("sh_auth_fail_reason");
        if(null!=metric) {
            List<BdNas> list = nasDao.queryNotMatchNasIpForShcm(metric.getId());
            if(!ToolsUtils.ListIsNull(list)) {
                String area_no = CommonInit.BUSCONF.getStringValue("bras_unknow_area", "1000");
                for (BdNas bdNas : list) {
                    bdNas.setId(IDGenerateUtil.getUuid());
                    // 设置默认属地： 未知属地
                    bdNas.setArea_no(area_no);
                    logger.info("insert bas start "+bdNas.getNas_ip());
                    nasDao.insert(bdNas);
                }
                commoninit.refreshBdNas();
            } else {
                logger.info("start excute shcm brasip [braslist is null]");
            }
        }else {
            logger.info("start excute shcm brasip [businessMetric not exist]");
        }
    }
}
