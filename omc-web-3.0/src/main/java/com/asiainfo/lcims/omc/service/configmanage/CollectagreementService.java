package com.asiainfo.lcims.omc.service.configmanage;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdCollectAgreement;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdMetricType;
import com.asiainfo.lcims.omc.model.system.MdCollCycle;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.CollectagreementDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdMetricDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdMetricTypeDAO;
import com.asiainfo.lcims.omc.persistence.system.MdCollCycleDAO;
import com.asiainfo.lcims.omc.persistence.system.MdParamDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.google.inject.internal.asm.$Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "collectagreementService")
public class CollectagreementService {

    @Inject
    private CollectagreementDAO collectagreementDAO;

    @Inject
    private MdCollCycleDAO mdCollCycleDAO;

    @Inject
    private MdParamDAO mdParamDAO;

    @Inject
    private MdMetricTypeDAO mdMetricTypeDAO;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    @Resource(name = "commoninit")
    CommonInit commoninit;

    @Autowired
    private OperateHisService operateHisService;

    /**
     *
     * @Title: getAllMetricManage
     * @Description: TODO(获取指标信息)
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    public List<MdCollectAgreement> getCollectAgreementList(MdCollectAgreement mdCollectAgreement) {
        return collectagreementDAO.getMdCollectAgreement(mdCollectAgreement);
    }

    /**
     *
     * @Title: getMdCollCycleList
     * @Description: TODO(获取采集周期)
     * @param @return 参数
     * @return List<MdCollCycle> 返回类型
     * @throws
     */
    public List<MdCollCycle> getMdCollCycleList() {
        return mdCollCycleDAO.getMdCollCycle();
    }

    /**
     *
     * @Title: getParamByType
     * @Description: TODO(获取脚本返回类型)
     * @param @param mdParam
     * @param @return 参数
     * @return List<MdParam> 返回类型
     * @throws
     */
    public List<MdParam> getParamByType(MdParam mdParam) {
        return mdParamDAO.getParamByType(mdParam.getType().toString());
    }

    /**
     *
     * @Title: getMdMetricTypeList
     * @Description: TODO(获取指标类型)
     * @param @return 参数
     * @return List<MdMetricType> 返回类型
     * @throws
     */
    public List<MdMetricType> getMdMetricTypeList() {
        return mdMetricTypeDAO.getMdMetricType(new MdMetricType());
    }

    /**
     *
     * @Title: addMdMetric
     * @Description: TODO(新增指标)
     * @param @param mdMetric
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult addMdCollectAgreement(MdCollectAgreement mdCollectAgreement) {
        String uuid = IDGenerateUtil.getUuid();
        mdCollectAgreement.setId(uuid);
        if (null != mdCollectAgreement.getProtocol_identity() && !("").equals(mdCollectAgreement.getProtocol_identity())
                && null != mdCollectAgreement.getProtocol_name() && !("").equals(mdCollectAgreement.getProtocol_name())
                && null != mdCollectAgreement.getScript() && !("").equals(mdCollectAgreement.getScript())) {
            int addResult = collectagreementDAO.insert(mdCollectAgreement);
            if (1 == addResult) {
                // 更新缓存数据
                commoninit.loadMetricInfos();
                // 用户日志记录
                operateHisService.insertOperateHistory(Constant.OPERATE_HIS_METRIC_MANAGE,
                        "新增数据[指标:" + mdCollectAgreement.getProtocol_name() + "]");
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
     * @Title: addMdMetric
     * @Description: TODO(修改指标)
     * @param @param mdMetric
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult modifyMdCollectAgreement(MdCollectAgreement mdCollectAgreement) {
        if (null != mdCollectAgreement.getProtocol_identity() && !("").equals(mdCollectAgreement.getProtocol_identity())
                && null != mdCollectAgreement.getProtocol_name() && !("").equals(mdCollectAgreement.getProtocol_name())
                && null != mdCollectAgreement.getScript() && !("").equals(mdCollectAgreement.getScript())) {
            MdCollectAgreement hisMdCollectAgreement = new MdCollectAgreement();
            hisMdCollectAgreement.setId(mdCollectAgreement.getId());
            hisMdCollectAgreement = collectagreementDAO.getMdCollectAgreement(hisMdCollectAgreement).get(0);
            int updateResult = collectagreementDAO.update(mdCollectAgreement);
            if (1 == updateResult) {
                // 更新缓存数据
                commoninit.loadMetricInfos();
                // 用户日志记录
                operateHisService.insertOperateHistory(
                        Constant.OPERATE_HIS_METRIC_MANAGE,
                        "修改数据[指标:" + hisMdCollectAgreement.getProtocol_name() + "-->"
                                + mdCollectAgreement.getProtocol_name() + "]");
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
     * @Title: deleteMdMetric
     * @Description: TODO(批量删除指标)
     * @param @param metricidArray
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult deleteMdCollectAgreement(String[] protocolidArray) {
        int deleteSuccess = 0;
        List<String> delSuccessList = new ArrayList<String>();
        int deleteFail = 0;
        List<String> delFailList = new ArrayList<String>();
        String message = "";
        if (protocolidArray != null && protocolidArray.length != 0) {
            for (String id : protocolidArray) {
                MdCollectAgreement mdCollectAgreement = new MdCollectAgreement();
                mdCollectAgreement.setId(id);
                List<MdCollectAgreement> mdMetricList = collectagreementDAO.getMdCollectAgreement(mdCollectAgreement);
                String metricName = mdMetricList.get(0).getProtocol_name();
                int deleteResult = collectagreementDAO.delete(id);
                if (1 == deleteResult) {
                    // 更新缓存数据
                    commoninit.loadMetricInfos();
                    // 删除告警规则
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("operationType", Constant.DYNAMICTYPE_METRIC);
                    map.put("dynamicType", Constant.OPERATIONTYPE_DELETE);
                    map.put("operationId", id);
                    alarmRuleManageService.dimensionModify(map);
                    delSuccessList.add(metricName);
                    deleteSuccess++;
                } else {
                    delFailList.add(metricName);
                    deleteFail++;
                }
            }
        }
        if (deleteSuccess > 0) {
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_METRIC_MANAGE, "删除数据[指标:"
                    + delSuccessList);
            message = message + deleteSuccess + "条插件删除成功" + delSuccessList + "。";
        }
        if (deleteFail > 0) {
            message = message + deleteFail + "条插件删除失败" + delFailList + "。删除失败的插件请先解绑对应主机。";
        }
        return new WebResult(true, message);
    }
}
