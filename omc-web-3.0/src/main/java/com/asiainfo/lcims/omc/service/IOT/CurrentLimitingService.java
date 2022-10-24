package com.asiainfo.lcims.omc.service.IOT;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.apn.MdApnLimitRule;
import com.asiainfo.lcims.omc.model.apn.MdApnRecord;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.IOT.CurrentLimitingDAO;
import com.asiainfo.lcims.omc.service.configmanage.AlarmRuleManageService;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

@Service(value = "currentLimitingService")
public class CurrentLimitingService {

    @Inject
    private CurrentLimitingDAO currentLimitingDAO;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    @Resource(name = "commoninit")
    CommonInit commoninit;

    @Autowired
    private OperateHisService operateHisService;


    /**
     *
     * @Title: getApnList
     * @param @return 参数
     * @return List<MdApnLimitRule> 返回类型
     * @throws
     */
    public List<MdApnLimitRule> getApnList(MdApnLimitRule mdApnLimitRule) {
        return currentLimitingDAO.getApnList(mdApnLimitRule);
    }

    /**
     *
     * @Title: addMdMetric
     * @Description: TODO(新增)
     * @param @param mdMetric
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult addApn(MdApnLimitRule mdApnLimitRule) {
        String uuid = IDGenerateUtil.getUuid();
        mdApnLimitRule.setRuleId(uuid);
        if (null != mdApnLimitRule.getApnId() && !("").equals(mdApnLimitRule.getLimit_cycle())
                && null != mdApnLimitRule.getAuth_value() && !("").equals(mdApnLimitRule.getLog_value())
                && null != mdApnLimitRule.getRecord_value() && !("").equals(mdApnLimitRule.getDay_value())) {
            int addResult = currentLimitingDAO.addApn(mdApnLimitRule);
            if (1 == addResult) {
                // 更新缓存数据
                commoninit.loadMetricInfos();
                alarmRuleManageService.updateApnAlarmRuleDetail(mdApnLimitRule.getApnId(),mdApnLimitRule.getDay_value());
                // 用户日志记录
                operateHisService.insertOperateHistory(Constant.IOT_APN,
                        "新增数据[APN名称:" + mdApnLimitRule.getApnId() + ",限流周期:" + mdApnLimitRule.getLimit_cycle() + ", " +
                                "认证限流阀值:" + mdApnLimitRule.getAuth_value() + ",日志限流阀值:" + mdApnLimitRule.getLog_value() + "," +
                                "记录次数限流阀值:" + mdApnLimitRule.getRecord_value() + ",每日限流阀值:" + mdApnLimitRule.getDay_value() + "]");
                return new WebResult(true, "成功", uuid);
            } else {
                return new WebResult(false, "失败");
            }
        } else {
            return new WebResult(false, "失败");
        }
    }

    /**
     *
     * @Title: getApnRecord
     * 获取apn记录表
     * @param @return 参数
     * @return List<MdApnLimitRule> 返回类型
     * @throws
     */
    public List<MdApnRecord> getApnRecord() {
        return CommonInit.getMdApnRecordList();
    }

    /**
     *
     * @Title: modifyApn
     * @Description: TODO(修改)
     * @param @param mdMetric
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult modifyApn(MdApnLimitRule mdApnLimitRule) {
        if (null != mdApnLimitRule.getApnId() && !("").equals(mdApnLimitRule.getLimit_cycle())
                && null != mdApnLimitRule.getAuth_value() && !("").equals(mdApnLimitRule.getLog_value())
                && null != mdApnLimitRule.getRecord_value() && !("").equals(mdApnLimitRule.getDay_value())) {
            MdApnLimitRule hisMdApn = new MdApnLimitRule();
            hisMdApn.setApnId(mdApnLimitRule.getApnId());
            hisMdApn = currentLimitingDAO.getApnList(hisMdApn).get(0);
            int updateResult = currentLimitingDAO.update(mdApnLimitRule);
            if (1 == updateResult) {
                // 更新缓存数据
                commoninit.loadMetricInfos();
                alarmRuleManageService.updateApnAlarmRuleDetail(mdApnLimitRule.getApnId(),mdApnLimitRule.getDay_value());
                // 用户日志记录
                operateHisService.insertOperateHistory(
                        Constant.IOT_APN,
                        "修改数据[APN名称:" + hisMdApn.getApnId() + "-->" + mdApnLimitRule.getApnId()
                                + "限流周期" + hisMdApn.getLimit_cycle() + "-->" + mdApnLimitRule.getLimit_cycle()
                                + "认证限流阀值" + hisMdApn.getAuth_value() + "-->" + mdApnLimitRule.getAuth_value()
                                + "日志限流阀值" + hisMdApn.getLog_value() + "-->" + mdApnLimitRule.getLog_value()
                                + "记录次数限流阀值" + hisMdApn.getRecord_value() + "-->" + mdApnLimitRule.getRecord_value()
                                + "每日限流阀值" + hisMdApn.getDay_value() + "-->" + mdApnLimitRule.getDay_value() + "]");
                return new WebResult(true, "成功");
            } else {
                return new WebResult(false, "失败");
            }
        } else {
            return new WebResult(false, "失败");
        }
    }

    /**
     *
     * @Title: deleteApn
     * @Description: TODO(批量删除)
     * @param @param metricidArray
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult deleteApn(String[] apnIdArray) {
        int deleteSuccess = 0;
        List<String> delSuccessList = new ArrayList<String>();
        int deleteFail = 0;
        List<String> delFailList = new ArrayList<String>();
        String message = "";
        if (apnIdArray != null && apnIdArray.length != 0) {
            for (String id : apnIdArray) {
                MdApnLimitRule mdApnLimitRule = new MdApnLimitRule();
                mdApnLimitRule.setApnId(id);
                List<MdApnLimitRule> mdApnLimitRuleList = currentLimitingDAO.getApnList(mdApnLimitRule);
                String apnName = mdApnLimitRuleList.get(0).getApnId();
                int deleteResult = currentLimitingDAO.delete(id);
                if (1 == deleteResult) {
                    // 更新缓存数据
                    commoninit.loadMetricInfos();
                    alarmRuleManageService.updateApnAlarmRuleDetail(apnName,"0");
                    delSuccessList.add(apnName);
                    deleteSuccess++;
                } else {
                    delFailList.add(apnName);
                    deleteFail++;
                }
            }
        }
        if (deleteSuccess > 0) {
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.IOT_APN, "删除数据[apn:"
                    + delSuccessList);
            message = message + deleteSuccess + "条apn删除成功" + delSuccessList + "。";
        }
        if (deleteFail > 0) {
            message = message + deleteFail + "条apn删除失败" + delFailList + "。删除失败的apn请先解绑对应主机。";
        }
        return new WebResult(true, message);
    }
}
