package com.asiainfo.lcims.omc.service.apn;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.apn.ApnJob;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.apn.ApnOperate;
import com.asiainfo.lcims.omc.model.apn.ApnResult;
import com.asiainfo.lcims.omc.model.apn.LimitResp;
import com.asiainfo.lcims.omc.model.apn.MdApnRecord;
import com.asiainfo.lcims.omc.model.configmanage.MdAlarmRule;
import com.asiainfo.lcims.omc.model.configmanage.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.apn.ApnOperateDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdAlarmRuleDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdAlarmRuleDetailDAO;
import com.asiainfo.lcims.omc.service.IOT.CurrentLimitingService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

@Service
public class ApnOperateService {

    private static final Logger LOG = LoggerFactory.getLogger(ApnOperateService.class);

    @Autowired
    private ApnOperateDAO apnOperateDAO;

    @Autowired
    private CommonInit commoninit;

    @Autowired
    private MdAlarmRuleDAO mdAlarmRuleDAO;

    @Autowired
    private MdAlarmRuleDetailDAO mdAlarmRuleDetailDAO;

    @Autowired
    private LimitStrategyService limitStrategyService;

    @Autowired
    private CurrentLimitingService currentLimitingService;

    public ApnResult operateApn(ApnOperate apnOperate) {
        String otype = apnOperate.getOtype();
        String apn = apnOperate.getApn();
        ApnResult apnResult = new ApnResult();
        Boolean isAddNeedFresh = false;
        try {
            if (StringUtils.equals(otype, Constant.APN_ADD)) {
                LOG.info("apn [{}] operate type is add.", apn);
                String uuid = IDGenerateUtil.getUuid();
                String updateTime = apnOperate.getUpdateTime();
                MdApnRecord apnRecord = apnOperateDAO.getApnRecordByName(apn);
                if (apnRecord != null) {
                    apnResult.setOtype(otype);
                    apnResult.setApn(apn);
                    apnResult.setRet(Constant.APN_FAILED);
                    apnResult.setDesc("APN已经存在，新增失败");
                } else {
                    MdApnRecord mdApnRecord = new MdApnRecord();
                    mdApnRecord.setApnid(uuid);
                    mdApnRecord.setApn(apn);
                    mdApnRecord.setUpdate_time(updateTime);
                    int addResult = apnOperateDAO.addApn(mdApnRecord);
                    if (addResult == 1) {
                        isAddNeedFresh = true;
                        apnResult.setOtype(otype);
                        apnResult.setApn(apn);
                        apnResult.setRet(Constant.APN_SUCCESS);
                        apnResult.setDesc("成功");
                    } else {
                        apnResult.setOtype(otype);
                        apnResult.setApn(apn);
                        apnResult.setRet(Constant.APN_FAILED);
                        apnResult.setDesc("APN新增失败");
                    }
                }
                LOG.info("apn [{}] operate add result is {}.", apn, apnResult.getDesc());
            } else if (StringUtils.equals(otype, Constant.APN_DELETE)) {
                LOG.info("apn [{}] operate type is delete.", apn);
                int deleteResult = apnOperateDAO.deleteApn(apn);
                if (deleteResult == 1) {
                    isAddNeedFresh = true;
                    apnResult.setOtype(otype);
                    apnResult.setApn(apn);
                    apnResult.setRet(Constant.APN_SUCCESS);
                    apnResult.setDesc("成功");
                } else {
                    apnResult.setOtype(otype);
                    apnResult.setApn(apn);
                    apnResult.setRet(Constant.APN_FAILED);
                    apnResult.setDesc("APN删除失败");
                }
                LOG.info("apn [{}] operate delete result is {}.", apn, apnResult.getDesc());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            apnResult.setOtype(otype);
            apnResult.setApn(apn);
            apnResult.setRet(Constant.APN_FAILED);
            apnResult.setDesc("APN操作失败");
        }
        if (isAddNeedFresh) {
            ApnJob.setIsNeedFresh(true);
        }
        return apnResult;
    }

    public ApnResult manageApn(ApnOperate apnOperate) {
        String otype = apnOperate.getOtype();
        String apn = apnOperate.getApn();
        ApnResult apnResult = new ApnResult();
        try {
            if (StringUtils.equals(otype, Constant.APN_ADD)) {
                LOG.info("apn operate type is add.");
                String uuid = IDGenerateUtil.getUuid();
                String updateTime = apnOperate.getUpdateTime();
                MdApnRecord apnRecord = CommonInit.getApnByName(apn);
                if (apnRecord != null) {
                    apnResult.setOtype(otype);
                    apnResult.setApn(apn);
                    apnResult.setRet(Constant.APN_FAILED);
                    apnResult.setDesc("APN已经存在，新增失败");
                } else {
                    MdApnRecord mdApnRecord = new MdApnRecord();
                    mdApnRecord.setApnid(uuid);
                    mdApnRecord.setApn(apn);
                    mdApnRecord.setUpdate_time(updateTime);
                    int addResult = apnOperateDAO.addApn(mdApnRecord);
                    if (addResult == 1) {
                        // 更新缓存数据
                        commoninit.loadApnInfo();
                        // 增加告警规则细表
                        addApnAlarmRuleDetail(uuid);
                        apnResult.setOtype(otype);
                        apnResult.setApn(apn);
                        apnResult.setRet(Constant.APN_SUCCESS);
                        apnResult.setDesc("成功");
                    } else {
                        apnResult.setOtype(otype);
                        apnResult.setApn(apn);
                        apnResult.setRet(Constant.APN_FAILED);
                        apnResult.setDesc("APN新增失败");
                    }
                }
                LOG.info("apn operate add result is {}.", apnResult.getRet());
            } else if (StringUtils.equals(otype, Constant.APN_DELETE)) {
                LOG.info("apn operate type is delete.");
                MdApnRecord apnRecord = apnOperateDAO.getApnIdByName(apn);
                int deleteResult = apnOperateDAO.deleteApn(apn);
                if (deleteResult == 1) {
                    // 更新缓存数据
                    commoninit.loadApnInfo();
                    // 删除告警规则细表
                    deleteApnAlarmRuleDetail(apnRecord);
                    // 调用AAA的接口取消限流
                    String[] apnArray = new String[] { apnRecord.getApnid() };
                    LimitResp deleteLimitResult = limitStrategyService.deleteLimitStrategy(apnArray,
                            Constant.LIMIT_VALID_UNOPEN);
                    String ret = deleteLimitResult.getRet();
                    if (StringUtils.equals(ret, "0")) {
                        WebResult webResult = currentLimitingService.deleteApn(apnArray);
                        LOG.info("delete apn limit rule : {}", webResult.getMessage());
                    }
                    apnResult.setOtype(otype);
                    apnResult.setApn(apn);
                    apnResult.setRet(Constant.APN_SUCCESS);
                    apnResult.setDesc("成功");
                } else {
                    apnResult.setOtype(otype);
                    apnResult.setApn(apn);
                    apnResult.setRet(Constant.APN_FAILED);
                    apnResult.setDesc("APN删除失败");
                }
                LOG.info("apn operate delete result is {}.", apnResult.getRet());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            apnResult.setOtype(otype);
            apnResult.setApn(apn);
            apnResult.setRet(Constant.APN_FAILED);
            apnResult.setDesc("APN操作失败");
        }
        return apnResult;
    }

    public void deleteApnAlarmRuleDetail(MdApnRecord apnRecord) {
        String apnId = apnRecord.getApnid();
        String limitAlarmRuleUrl = CommonInit.BUSCONF.getApnLimitThresholdAlarmRuleUrl();
        String apnUrl = StringUtils.replace(limitAlarmRuleUrl, "#{apn_id}", apnId);
        String metricId = CommonInit.BUSCONF.getApnLimitThresholdMetricId();
        MdAlarmRuleDetail alarmRuleDetail = new MdAlarmRuleDetail();
        alarmRuleDetail.setUrl(apnUrl);
        alarmRuleDetail.setMetric_id(metricId);
        int deleteRetValue = mdAlarmRuleDetailDAO.deleteApnAlarmDetailRule(alarmRuleDetail);
        LOG.info("url [{}] delete table result is : {}", apnUrl, deleteRetValue);
    }

    public void addApnAlarmRuleDetail(String uuid) {
        String limitAlarmRuleUrl = CommonInit.BUSCONF.getApnLimitThresholdAlarmRuleUrl();
        String[] urlArr = StringUtils.split(limitAlarmRuleUrl, "--");
        String apnUrl = urlArr[0];
        String metricId = CommonInit.BUSCONF.getApnLimitThresholdMetricId();
        MdAlarmRule mdAlarmRule = new MdAlarmRule();
        mdAlarmRule.setUrl(apnUrl);
        mdAlarmRule.setMetric_id(metricId);
        List<MdAlarmRule> mdAlarmRuleList = mdAlarmRuleDAO.getMdAlarmRule(mdAlarmRule);
        if (mdAlarmRuleList != null && !mdAlarmRuleList.isEmpty()) {
            MdAlarmRule alarmRule = mdAlarmRuleList.get(0);
            MdAlarmRuleDetail alarmRuleDetail = copyProperties(alarmRule);
            String alarmId = IDGenerateUtil.getUuid();
            alarmRuleDetail.setAlarm_id(alarmId);
            String url = alarmRule.getUrl() + "--/" + uuid;
            alarmRuleDetail.setUrl(url);
            alarmRuleDetail.setDimension1(uuid);
            int addRetValue = mdAlarmRuleDetailDAO.insertMdAlarmRuleDetail(alarmRuleDetail);
            LOG.info("url [{}] add table result is : {}", url, addRetValue);
        }
    }

    public MdAlarmRuleDetail copyProperties(MdAlarmRule alarmRule) {
        MdAlarmRuleDetail alarmRuleDetail = new MdAlarmRuleDetail();
        try {
            BeanUtils.copyProperties(alarmRuleDetail, alarmRule);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return alarmRuleDetail;
        }
        return alarmRuleDetail;
    }

}
