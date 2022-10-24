package com.asiainfo.lcims.omc.service.configmanage;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdSoundAlarm;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.SoundAlarmManageDAO;
import com.asiainfo.lcims.omc.persistence.system.MdParamDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

@Service
public class SoundAlarmManageService {

    @Autowired
    private SoundAlarmManageDAO soundAlarmManageDAO;

    @Autowired
    private OperateHisService operateHisService;

    @Autowired
    private CommonInit commonInit;

    @Autowired
    private MdParamDAO mdParamDAO;

    public List<MdParam> getParamByType(MdParam mdParam) {
        return mdParamDAO.getParamByType(mdParam.getType().toString());
    }

    public List<MdSoundAlarm> getMdNodeList(MdSoundAlarm mdSoundAlarm) {
        return soundAlarmManageDAO.getMdSoundAlarm(mdSoundAlarm);
    }

    public WebResult addMdSoundAlarm(MdSoundAlarm mdSoundAlarm) {
        WebResult result = new WebResult(false, "新增失败");
        String uuid = IDGenerateUtil.getUuid();
        mdSoundAlarm.setId(uuid);
        int addResult = soundAlarmManageDAO.insert(mdSoundAlarm);
        if (addResult == 1) {
            result = new WebResult(true, "新增成功");
            // 刷新声音告警缓存
            commonInit.loadSoundAlarmList();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_SOUND_ALARM_MANAGE,
                    "新增数据[声音告警名称:" + mdSoundAlarm.getSound_name() + "]");
        } else {
            result = new WebResult(false, "新增失败");
        }
        return result;
    }

    public WebResult modifyMdSoundAlarm(MdSoundAlarm mdSoundAlarm) {
        WebResult result = new WebResult(false, "修改失败");

        List<MdSoundAlarm> mdSoundAlarmList = CommonInit.getMdSoundAlarmList();
        String soundAlarmName = "";
        for (MdSoundAlarm soundAlarm : mdSoundAlarmList) {
            if (soundAlarm.getId().equals(mdSoundAlarm.getId())) {
                soundAlarmName = soundAlarm.getSound_name();
            }
        }

        int updateResult = soundAlarmManageDAO.update(mdSoundAlarm);
        if (updateResult == 1) {
            result = new WebResult(true, "修改成功");
            // 刷新声音告警缓存
            commonInit.loadSoundAlarmList();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_FACTORY_MANAGE,
                    "修改数据[声音告警名称:" + soundAlarmName + "-->" + mdSoundAlarm.getSound_name() + "]");
        } else {
            result = new WebResult(false, "修改失败");
        }
        return result;
    }

    public WebResult deleteMdSoundAlarm(String[] soundAlarmIds) {
        WebResult result = null;
        List<String> delSuccessList = new LinkedList<String>();
        List<String> delFailList = new LinkedList<String>();
        String message = "";
        if (soundAlarmIds != null && soundAlarmIds.length > 0) {
            for (String soundAlarmId : soundAlarmIds) {
                MdSoundAlarm soundAlarm = soundAlarmManageDAO.getOneSoundAlarm(soundAlarmId);
                String soundName = soundAlarm.getSound_name();
                int delResult = soundAlarmManageDAO.delete(soundAlarmId);
                if (delResult == 1) {
                    delSuccessList.add(soundName);
                } else {
                    delFailList.add(soundName);
                }
            }
        }
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "条声音告警删除成功" + delSuccessList + "。";
            // 刷新声音告警缓存
            commonInit.loadSoundAlarmList();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_FACTORY_MANAGE,
                    "删除数据[声音告警:" + delSuccessList);
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "条声音告警删除失败" + delFailList + "。";
        }
        result = new WebResult(true, message);
        return result;
    }
}
