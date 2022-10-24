package com.asiainfo.lcims.omc.service.configmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdMetricType;
import com.asiainfo.lcims.omc.model.system.MdCollCycle;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.MdMetricDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdMetricTypeDAO;
import com.asiainfo.lcims.omc.persistence.system.MdCollCycleDAO;
import com.asiainfo.lcims.omc.persistence.system.MdParamDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

@Service(value = "metricManageService")
public class MetricManageService {
    @Inject
    private MdMetricDAO mdMetricDAO;

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
    public List<MdMetric> getMdMetricList(MdMetric mdMetric) {
        return mdMetricDAO.getMdMetric(mdMetric);
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
    public WebResult addMdMetric(MdMetric mdMetric) {
        String uuid = IDGenerateUtil.getUuid();
        mdMetric.setId(uuid);
        if (null != mdMetric.getMetric_identity() && !("").equals(mdMetric.getMetric_identity())
                && null != mdMetric.getMetric_name() && !("").equals(mdMetric.getMetric_name())
                && null != mdMetric.getScript() && !("").equals(mdMetric.getScript())) {
            int addResult = mdMetricDAO.insert(mdMetric);
            if (1 == addResult) {
                // 更新缓存数据
                commoninit.loadMetricInfos();
                // 用户日志记录
                operateHisService.insertOperateHistory(Constant.OPERATE_HIS_METRIC_MANAGE,
                        "新增数据[指标:" + mdMetric.getMetric_name() + "]");
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
    public WebResult modifyMdMetric(MdMetric mdMetric) {
        if (null != mdMetric.getMetric_identity() && !("").equals(mdMetric.getMetric_identity())
                && null != mdMetric.getMetric_name() && !("").equals(mdMetric.getMetric_name())
                && null != mdMetric.getScript() && !("").equals(mdMetric.getScript())) {
            MdMetric hisMdMetric = new MdMetric();
            hisMdMetric.setId(mdMetric.getId());
            hisMdMetric = mdMetricDAO.getMdMetric(hisMdMetric).get(0);
            int updateResult = mdMetricDAO.update(mdMetric);
            if (1 == updateResult) {
                // 更新缓存数据
                commoninit.loadMetricInfos();
                // 用户日志记录
                operateHisService.insertOperateHistory(
                        Constant.OPERATE_HIS_METRIC_MANAGE,
                        "修改数据[指标:" + hisMdMetric.getMetric_name() + "-->"
                                + mdMetric.getMetric_name() + "]");
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
    public WebResult deleteMdMetric(String[] metricidArray) {
        int deleteSuccess = 0;
        List<String> delSuccessList = new ArrayList<String>();
        int deleteFail = 0;
        List<String> delFailList = new ArrayList<String>();
        String message = "";
        if (metricidArray != null && metricidArray.length != 0) {
            for (String id : metricidArray) {
                MdMetric mdMetric = new MdMetric();
                mdMetric.setId(id);
                List<MdMetric> mdMetricList = mdMetricDAO.getMdMetric(mdMetric);
                String metricName = mdMetricList.get(0).getMetric_name();
                int deleteResult = mdMetricDAO.delete(id);
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
            message = message + deleteSuccess + "条指标删除成功" + delSuccessList + "。";
        }
        if (deleteFail > 0) {
            message = message + deleteFail + "条指标删除失败" + delFailList + "。删除失败的指标请先解绑对应主机。";
        }
        return new WebResult(true, message);
    }
}
