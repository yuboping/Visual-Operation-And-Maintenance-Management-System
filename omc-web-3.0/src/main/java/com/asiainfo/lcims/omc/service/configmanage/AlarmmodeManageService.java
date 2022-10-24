package com.asiainfo.lcims.omc.service.configmanage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdAlarmMode;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.AlarmmodeManageDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdAlarmRuleDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

/**
 * 告警方式管理Service类
 * 
 * @author zhujiansheng
 * @date 2018年8月30日 上午11:18:30
 * @version V1.0
 */
@Service
public class AlarmmodeManageService {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmmodeManageService.class);

    @Autowired
    private AlarmmodeManageDAO alarmmodeManageDAO;

    @Autowired
    private MdAlarmRuleDAO mdAlarmRuleDAO;

    @Autowired
    private OperateHisService operateHisService;

    @Autowired
    private CommonInit commonInit;

    public List<MdParam> getAlarmModeType() {
        List<MdParam> modeTypeList = alarmmodeManageDAO.getParamByType("4");
        return modeTypeList;
    }

    public List<MdAlarmMode> getMdAlarmModeList(MdAlarmMode mdAlarmMode) {
        List<MdAlarmMode> alarmModeList = alarmmodeManageDAO.getMdAlarmMode(mdAlarmMode);
        return alarmModeList;
    }

    public WebResult addMdAlarmMode(MdAlarmMode mdAlarmMode) {
        WebResult result = null;
        String uuid = IDGenerateUtil.getUuid();
        mdAlarmMode.setModeid(uuid);
        int addResult = alarmmodeManageDAO.insert(mdAlarmMode);
        if (addResult == 1) {
            // 刷新告警方式缓存
            commonInit.loadMdAlarmModeInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ALARMMODE_MANAGE,
                    "新增数据[告警方式:" + mdAlarmMode.getModename() + "]");
            result = new WebResult(true, "新增成功");
        } else {
            result = new WebResult(false, "新增失败");
        }
        return result;
    }

    public WebResult modifyMdAlarmMode(MdAlarmMode mdAlarmMode) {
        WebResult result = null;

        String alarmModeName = "";
        List<MdAlarmMode> mdAlarmModeList = CommonInit.getMdAlarmModeList();
        for (MdAlarmMode alarmMode : mdAlarmModeList) {
            if (alarmMode.getModeid().equals(mdAlarmMode.getModeid())) {
                alarmModeName = alarmMode.getModename();
            }
        }

        int updateResult = alarmmodeManageDAO.update(mdAlarmMode);
        if (updateResult == 1) {
            // 刷新告警方式缓存
            commonInit.loadMdAlarmModeInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ALARMMODE_MANAGE,
                    "修改数据[告警方式:" + alarmModeName + "-->" + mdAlarmMode.getModename() + "]");
            result = new WebResult(true, "修改成功");
        } else {
            result = new WebResult(false, "修改失败");
        }
        return result;
    }

    public WebResult deleteMdAlarmMode(String[] alarmmodeArray) {
        WebResult result = null;
        List<String> delSuccessList = new LinkedList<String>();
        List<String> delFailList = new LinkedList<String>();
        String message = "";
        if (alarmmodeArray != null && alarmmodeArray.length > 0) {
            for (String alarmmodeId : alarmmodeArray) {
                MdAlarmMode mdAlarmMode = alarmmodeManageDAO.getModeName(alarmmodeId);
                String modeName = mdAlarmMode.getModename();
                List<String> allModes = mdAlarmRuleDAO.getAllModes();
                String modes = transModes(allModes);
                LOG.debug("modes is : {}", modes);
                int delResult = alarmmodeManageDAO.delete(modes, alarmmodeId);
                if (delResult == 1) {
                    delSuccessList.add(modeName);
                } else {
                    delFailList.add(modeName);
                }
            }
        }
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "条告警方式删除成功" + delSuccessList + "。";
            // 刷新告警方式缓存
            commonInit.loadMdAlarmModeInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ALARMMODE_MANAGE,
                    "删除数据[告警方式:" + delSuccessList);
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "条告警方式删除失败" + delFailList
                    + "，删除失败的告警方式已被告警规则使用。";
        }
        result = new WebResult(true, message);
        return result;
    }

    public String transModes(List<String> allModes) {
        Set<String> modeSet = new HashSet<>();
        for (String modes : allModes) {
            LOG.debug("one of allModes is : {}", modes);
            String[] modeArr = StringUtils.split(modes, "#");
            if (modeArr != null) {
                List<String> tempList = Arrays.asList(modeArr);
                modeSet.addAll(tempList);
            }
        }
        String modeStr = StringUtils.join(modeSet, "','");
        modeStr = "'" + modeStr + "'";
        return modeStr;
    }
}
