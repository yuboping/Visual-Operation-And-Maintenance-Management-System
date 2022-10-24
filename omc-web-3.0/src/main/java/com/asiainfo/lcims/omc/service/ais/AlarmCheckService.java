package com.asiainfo.lcims.omc.service.ais;

import java.util.List;

import javax.inject.Inject;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.MetricDataModel;
import com.asiainfo.lcims.omc.model.TimeCell;
import com.asiainfo.lcims.omc.persistence.ais.INSThresholdDAO;
import com.asiainfo.lcims.omc.persistence.monitor.MetricDataDAO;
import com.asiainfo.lcims.omc.persistence.po.ais.INSThreshold;
import com.asiainfo.lcims.omc.service.param.RuleValue;
import com.asiainfo.lcims.omc.util.TimeControl;

@Service("alarmCheckService")
public class AlarmCheckService {
    private static final Logger logger = LoggerFactory.make();
    @Inject
    private INSThresholdDAO thresholdDAO;
    
}
