package com.asiainfo.lcims.omc.report.main;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.model.MdCollCycleTime;
import com.asiainfo.lcims.omc.alarm.param.InitParam;

public class MainServer {
    
    private static final Logger LOG = LoggerFactory.getLogger(MainServer.class);

    public static void main(String[] args) {
        LOG.info("-----------report start-----------------");
        InitParam.init();
        List<MdCollCycleTime> cycleTimelist = InitParam.getAllCollCycleTimes(null, null);
        ReportProcess process = new ReportProcess(cycleTimelist);
        process.startProcess();
        LOG.info("-----------report end-----------------");
    }
    
}
