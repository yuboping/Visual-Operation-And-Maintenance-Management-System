package com.asiainfo.lcims.omc.report.service;

import java.util.List;

import com.asiainfo.lcims.omc.alarm.model.MdCollCycleTime;
import com.asiainfo.lcims.omc.report.conf.UploadConf;
import com.asiainfo.lcims.omc.report.model.FileTarget;

public abstract class AbstractUploadService{
    
    /**
     * 生成上报文件
     * @param conf
     */
    public abstract FileTarget makeUploadTarget(UploadConf conf, List<MdCollCycleTime> cycleTimelist);
}
