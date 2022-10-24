package com.asiainfo.lcims.omc.service.ais;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.ais.AisGroupMetricModel;
import com.asiainfo.lcims.omc.model.ais.AisGroupModel;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.persistence.ais.AisGroupDAO;
import com.asiainfo.lcims.omc.persistence.ais.AisGroupMetricDAO;
import com.asiainfo.lcims.omc.persistence.monitor.PageChartDAO;
import com.asiainfo.lcims.omc.service.configmanage.AlarmRuleManageService;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.service.system.MenuService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

@Service("aisGroupMetricManageService")
public class AisGroupMetricManageService {

    private static final Logger LOG = LoggerFactory.getLogger(AisGroupMetricManageService.class);

    @Inject
    AisGroupMetricDAO aisGroupMetricDAO;

    @Inject
    AisGroupDAO aisGroupDAO;

    @Resource(name = "operateHisService")
    OperateHisService operateHisService;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    @Resource(name = "menuService")
    MenuService menuService;

    @Inject
    private PageChartDAO pageChartDAO;

    /**
     * 
     * @Title: getAisGroupList
     * @Description: TODO(获取巡检组)
     * @param @return 参数
     * @return List<AisGroupModel> 返回类型
     * @throws
     */
    public List<AisGroupModel> getAisGroupList() {
        List<AisGroupModel> list = aisGroupDAO.getAllAisGroup();
        return list;
    }

    /**
     * 
     * @Title: getAisGroupMetricList
     * @Description: TODO(获取巡检组指标列表)
     * @param @param map
     * @param @return 参数
     * @return List<AisGroupMetricModel> 返回类型
     * @throws
     */
    public List<AisGroupMetricModel> getAisGroupMetricList(Map<String, Object> map) {
        AisGroupMetricModel aisGroupMetricModel = new AisGroupMetricModel();
        String wholeUrl = alarmRuleManageService.getWholeUrl(map);
        if (wholeUrl != null && !wholeUrl.equals("")) {
            aisGroupMetricModel.setUrl(wholeUrl);
        }
        String mdMenuId = "";
        String chart_name = "";
        String metric_id = "";
        String group_id = "";
        String group_metric_id = "";
        String attr = "";
        if (map.get("mdMenuId") != null) {
            mdMenuId = map.get("mdMenuId").toString();
            if (!mdMenuId.equals("")) {
                MdMenu mdMenu = menuService.getMdMenuById(mdMenuId);
                aisGroupMetricModel.setName(mdMenu.getName());
            }
        }
        if (map.get("group_id") != null) {
            group_id = map.get("group_id").toString();
            if (!group_id.equals("")) {
                aisGroupMetricModel.setGroup_id(group_id);
            }
        }
        if (map.get("group_metric_id") != null) {
            group_metric_id = map.get("group_metric_id").toString();
            if (!group_metric_id.equals("")) {
                aisGroupMetricModel.setGroup_metric_id(group_metric_id);
            }
        }
        if (map.get("metric_id") != null) {
            metric_id = map.get("metric_id").toString();
            if (!metric_id.equals("")) {
                aisGroupMetricModel.setMetric_id(metric_id);
            }
        }
        if (map.get("attr") != null) {
            attr = map.get("attr").toString();
            if (!attr.equals("")) {
                aisGroupMetricModel.setAttr(attr);
            }
        }
        if (map.get("chart_name") != null) {
            chart_name = map.get("chart_name").toString();
            if (!chart_name.equals("")) {
                aisGroupMetricModel.setChart_name(chart_name);
            }
        }
        List<AisGroupMetricModel> aisGroupMetricModelList = aisGroupMetricDAO
                .getAisGroupMetricModel(aisGroupMetricModel);
        for (AisGroupMetricModel groupMetricModel : aisGroupMetricModelList) {
            String URL = groupMetricModel.getUrl();
            String businesslinkurl = URL.split(ConstantUtill.URL_SPLIT)[0];
            groupMetricModel.setBusinesslinkurl(businesslinkurl);
            MdMenu mdMenu = menuService.getTwoLevelMdMenu(businesslinkurl);
            String module = mdMenu.getId();
            groupMetricModel.setModule(module);
            // 获取模块名称
            groupMetricModel.setModule_name(mdMenu.getShow_name());
            int dimension_type = groupMetricModel.getDimension_type();
            String dimension_type_name = alarmRuleManageService
                    .getDimensionTypeName(dimension_type);
            groupMetricModel.setDimension_type_name(dimension_type_name);
            List<Map<String, Object>> monitorTargetOneLevelList = alarmRuleManageService
                    .getMonitorTargetOneLevelList(module);
            String businesslinkoneurl = businesslinkurl;
            if (dimension_type == Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE
                    || dimension_type == Constant.DIMENSIONTYPE_NODE_ALL_AREA_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_SINGLE
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_ALL_BRAS_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_SINGLE
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_ALL_NODE_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_SINGLE) {
                String s[] = businesslinkurl.split("/");
                int length = s.length;
                int lastlength = 0;
                if (length > 0) {
                    lastlength = s[length - 1].length() + 1;
                }
                businesslinkoneurl = businesslinkurl.substring(0, businesslinkurl.length()
                        - lastlength);
            }
            String monitortarget1 = alarmRuleManageService.getMonitorTargetOne(businesslinkoneurl,
                    dimension_type, monitorTargetOneLevelList);
            groupMetricModel.setMonitortarget1(monitortarget1);
            if (!monitortarget1.equals("")) {
                List<Map<String, Object>> monitorTargetThreeLevelList = alarmRuleManageService
                        .getMonitorTargetThreeLevelList(monitortarget1);
                String monitortarget3 = alarmRuleManageService.getMonitorTargetThree(
                        businesslinkurl, dimension_type, monitorTargetThreeLevelList);
                groupMetricModel.setMonitortarget3(monitortarget3);
            }
            // 获取维度1名称
            String dimension1 = groupMetricModel.getDimension1();
            String dimension1_name = alarmRuleManageService.getDynamicNameById(dimension1);
            groupMetricModel.setDimension1_name(dimension1_name);
            // 获取维度2名称
            String dimension2 = groupMetricModel.getDimension2();
            String dimension2_name = alarmRuleManageService.getDynamicNameById(dimension2);
            groupMetricModel.setDimension2_name(dimension2_name);
        }
        return aisGroupMetricModelList;
    }

    /**
     * 
     * @Title: addAisGroupMetric
     * @Description: TODO(新增巡检组指标)
     * @param @param map
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    public WebResult addAisGroupMetric(Map<String, Object> map) {
        String URL = alarmRuleManageService.getWholeUrl(map);
        String uuid = IDGenerateUtil.getUuid();
        AisGroupMetricModel aisGroupMetricModel = new AisGroupMetricModel();
        String metric_id = "";
        String chart_name = "";
        String attr = "";
        String dimension1 = "";
        String dimension2 = "";
        String group_id = "";
        if (map.get("metric_id") != null) {
            metric_id = map.get("metric_id").toString();
        }
        if (map.get("attr") != null) {
            attr = map.get("attr").toString();
        }
        if (map.get("attr1") != null) {
            dimension1 = map.get("attr1").toString();
        }
        if (map.get("attr2") != null) {
            dimension2 = map.get("attr2").toString();
        }
        if (map.get("group_id") != null) {
            group_id = map.get("group_id").toString();
        }
        aisGroupMetricModel.setUrl(URL);
        aisGroupMetricModel.setMetric_id(metric_id);
        aisGroupMetricModel.setAttr(attr);
        aisGroupMetricModel.setDimension1(dimension1);
        aisGroupMetricModel.setDimension2(dimension2);
        aisGroupMetricModel.setDimension3("");
        aisGroupMetricModel.setGroup_id(group_id);
        aisGroupMetricModel.setGroup_metric_id(uuid);
        String businesslinkurl = URL.split(ConstantUtill.URL_SPLIT)[0];
        MdMenu mdMenu = menuService.getTwoLevelMdMenu(businesslinkurl);
        aisGroupMetricModel.setName(mdMenu.getName());
        int dimension_type = alarmRuleManageService.judgeDimensionType(URL);
        aisGroupMetricModel.setDimension_type(dimension_type);
        if (map.get("chart_name") != null) {
            chart_name = map.get("chart_name").toString();
        } else {
            List<Map<String, Object>> ml = pageChartDAO.getChartNamePageChart(URL, metric_id);
            if (ml == null || ml.size() == 0) {
                ml = pageChartDAO.getChartNamePageChart(businesslinkurl, metric_id);
            }
            for (Map<String, Object> m : ml) {
                chart_name = chart_name + "," + m.get("CHART_NAME").toString();
            }
            if (chart_name.length() > 0) {
                chart_name = chart_name.substring(1);
            }
        }
        aisGroupMetricModel.setChart_name(chart_name);
        aisGroupMetricModel.setCreate_time(new Date());
        aisGroupMetricModel.setUpdate_time(new Date());
        int addResult = aisGroupMetricDAO.insertAisGroupMetricModel(aisGroupMetricModel);
        if (1 == addResult) {
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AIS_GROUP_METRIC_MANAGE,
                    "新增数据[巡检组指标: URL:" + aisGroupMetricModel.getUrl() + " 组ID:"
                            + aisGroupMetricModel.getGroup_id() + "]");
            return new WebResult(true, "成功", uuid);
        } else {
            return new WebResult(false, "失败");
        }
    }

    /**
     * 
     * @Title: getAisGroupMetricDetailList
     * @Description: TODO(获取巡检组指标明细)
     * @param @param map
     * @param @return 参数
     * @return List<AisGroupMetricModel> 返回类型
     * @throws
     */
    public List<AisGroupMetricModel> getAisGroupMetricDetailList(Map<String, Object> map) {
        AisGroupMetricModel aisGroupMetricModel = new AisGroupMetricModel();
        String group_metric_id = "";
        if (map.get("group_metric_id") != null) {
            group_metric_id = map.get("group_metric_id").toString();
            if (!group_metric_id.equals("")) {
                aisGroupMetricModel.setGroup_metric_id(group_metric_id);
            }
        }
        String group_id = "";
        if (map.get("group_id") != null) {
            group_id = map.get("group_id").toString();
            if (!group_id.equals("")) {
                aisGroupMetricModel.setGroup_id(group_id);
            }
        }
        List<AisGroupMetricModel> aisGroupMetricDetailList = new ArrayList<AisGroupMetricModel>();
        List<AisGroupMetricModel> aisGroupMetricModelList = aisGroupMetricDAO
                .getAisGroupMetricModel(aisGroupMetricModel);
        if (aisGroupMetricModelList != null && aisGroupMetricModelList.size() > 0) {
            for (AisGroupMetricModel aisGroupMetric : aisGroupMetricModelList) {
                String businesslinkurl = aisGroupMetric.getUrl().split(ConstantUtill.URL_SPLIT)[0];
                String metric_id = aisGroupMetric.getMetric_id();
                Map<String, Object> monitorTargetMap = alarmRuleManageService.getMonitorTargetMap(
                        businesslinkurl, aisGroupMetric.getDimension_type());
                monitorTargetMap.put("attr1", aisGroupMetric.getDimension1());
                monitorTargetMap.put("attr2", aisGroupMetric.getDimension2());
                List<Map<String, Object>> dimensionList = alarmRuleManageService
                        .getDimensionList(monitorTargetMap);
                if (dimensionList != null && dimensionList.size() > 0) {
                    for (Map<String, Object> m : dimensionList) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> list = (List<Map<String, Object>>) m.get("list");
                        if (list != null && list.size() > 0) {
                            for (Map<String, Object> listmap : list) {
                                AisGroupMetricModel aisGroupMetricDetail = new AisGroupMetricModel();
                                aisGroupMetricDetail.setGroup_id(aisGroupMetric.getGroup_id());
                                aisGroupMetricDetail.setGroup_metric_id(aisGroupMetric
                                        .getGroup_metric_id());
                                aisGroupMetricDetail.setName(aisGroupMetric.getName());
                                aisGroupMetricDetail.setUrl(aisGroupMetric.getUrl());
                                aisGroupMetricDetail
                                        .setMetric_name(aisGroupMetric.getMetric_name());
                                aisGroupMetricDetail.setDimension_type(aisGroupMetric
                                        .getDimension_type());
                                aisGroupMetricDetail.setChart_name(aisGroupMetric.getChart_name());
                                aisGroupMetricDetail.setMetric_id(aisGroupMetric.getMetric_id());
                                aisGroupMetricDetail.setAttr(aisGroupMetric.getAttr());
                                aisGroupMetricDetail.setDimension3(aisGroupMetric.getDimension3());
                                aisGroupMetricDetail.setCreate_time(new Date());
                                aisGroupMetricDetail.setUpdate_time(new Date());
                                String attr1 = m.get("dimension1").toString();
                                aisGroupMetricDetail.setDimension1(attr1);
                                String mdAlarmRuleDetailDimension1URL = businesslinkurl
                                        + ConstantUtill.URL_SPLIT + "/" + attr1;
                                aisGroupMetricDetail.setUrl(mdAlarmRuleDetailDimension1URL);
                                String attr2 = listmap.get("dimension2").toString();
                                aisGroupMetricDetail.setDimension2(attr2);
                                String mdAlarmRuleDetailDimension2URL = businesslinkurl
                                        + ConstantUtill.URL_SPLIT + "/" + attr1 + "/" + attr2;
                                aisGroupMetricDetail.setUrl(mdAlarmRuleDetailDimension2URL);
                                boolean judgeMetricIsExist = alarmRuleManageService
                                        .judgeMetricIsExist(mdAlarmRuleDetailDimension2URL,
                                                metric_id);
                                if (judgeMetricIsExist) {
                                    aisGroupMetricDetailList.add(aisGroupMetricDetail);
                                }
                            }
                        } else {
                            AisGroupMetricModel aisGroupMetricDetail = new AisGroupMetricModel();
                            aisGroupMetricDetail.setGroup_id(aisGroupMetric.getGroup_id());
                            aisGroupMetricDetail.setGroup_metric_id(aisGroupMetric
                                    .getGroup_metric_id());
                            aisGroupMetricDetail.setName(aisGroupMetric.getName());
                            aisGroupMetricDetail.setUrl(aisGroupMetric.getUrl());
                            aisGroupMetricDetail.setDimension_type(aisGroupMetric
                                    .getDimension_type());
                            aisGroupMetricDetail.setChart_name(aisGroupMetric.getChart_name());
                            aisGroupMetricDetail.setMetric_name(aisGroupMetric.getMetric_name());
                            aisGroupMetricDetail.setMetric_id(aisGroupMetric.getMetric_id());
                            aisGroupMetricDetail.setAttr(aisGroupMetric.getAttr());
                            aisGroupMetricDetail.setDimension3(aisGroupMetric.getDimension3());
                            aisGroupMetricDetail.setCreate_time(new Date());
                            aisGroupMetricDetail.setUpdate_time(new Date());
                            String attr1 = m.get("dimension1").toString();
                            aisGroupMetricDetail.setDimension1(attr1);
                            String mdAlarmRuleDetailDimension1URL = businesslinkurl
                                    + ConstantUtill.URL_SPLIT + "/" + attr1;
                            aisGroupMetricDetail.setUrl(mdAlarmRuleDetailDimension1URL);
                            aisGroupMetricDetail.setDimension2("");
                            boolean judgeMetricIsExist = alarmRuleManageService.judgeMetricIsExist(
                                    mdAlarmRuleDetailDimension1URL, metric_id);
                            if (judgeMetricIsExist) {
                                aisGroupMetricDetailList.add(aisGroupMetricDetail);
                            }
                        }
                    }
                } else {
                    aisGroupMetricDetailList.add(aisGroupMetric);
                }
            }
        }
        return aisGroupMetricDetailList;
    }

    /**
     * 
     * @Title: modifyAisGroupMetric
     * @Description: TODO(修改巡检组指标)
     * @param @param map
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    public WebResult modifyAisGroupMetric(Map<String, Object> map) {
        AisGroupMetricModel aisGroupMetricModel = new AisGroupMetricModel();
        String group_metric_id = "";
        String group_id = "";
        if (map.get("group_metric_id") != null) {
            group_metric_id = map.get("group_metric_id").toString();
        }
        if (map.get("group_id") != null) {
            group_id = map.get("group_id").toString();
        }
        AisGroupMetricModel hisAisGroupMetricModel = new AisGroupMetricModel();
        hisAisGroupMetricModel.setGroup_metric_id(group_metric_id);
        hisAisGroupMetricModel = aisGroupMetricDAO.getAisGroupMetricModel(hisAisGroupMetricModel)
                .get(0);
        aisGroupMetricModel.setGroup_metric_id(group_metric_id);
        aisGroupMetricModel.setGroup_id(group_id);
        aisGroupMetricModel.setUpdate_time(new Date());
        int updateResult = aisGroupMetricDAO.update(aisGroupMetricModel);
        if (1 == updateResult) {
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AIS_GROUP_METRIC_MANAGE,
                    "修改数据[巡检组指标Group_id:" + hisAisGroupMetricModel.getGroup_id() + "-->"
                            + aisGroupMetricModel.getGroup_id() + "]");
            return new WebResult(true, "成功");
        } else {
            return new WebResult(false, "失败");
        }
    }

    /**
     * 
     * @Title: deleteAisGroupMetric
     * @Description: TODO(删除巡检组指标)
     * @param @param groupmetricidArray
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    public WebResult deleteAisGroupMetric(String[] groupmetricidArray) {
        int deleteSuccess = 0;
        int deleteFail = 0;
        List<String> delSuccessList = new ArrayList<String>();
        String message = "";
        if (groupmetricidArray != null && groupmetricidArray.length != 0) {
            for (String id : groupmetricidArray) {
                AisGroupMetricModel aisGroupMetricModel = new AisGroupMetricModel();
                aisGroupMetricModel.setGroup_metric_id(id);
                aisGroupMetricModel = aisGroupMetricDAO.getAisGroupMetricModel(aisGroupMetricModel)
                        .get(0);
                int deleteResult = aisGroupMetricDAO.delete(id);
                if (1 == deleteResult) {
                    delSuccessList.add(aisGroupMetricModel.getGroup_metric_id());
                    deleteSuccess++;
                } else {
                    deleteFail++;
                }
            }
        }
        if (deleteSuccess > 0) {
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AIS_GROUP_METRIC_MANAGE,
                    "删除数据[巡检组指标:" + delSuccessList);
            message = message + deleteSuccess + "条巡检组指标删除成功。";
        }
        if (deleteFail > 0) {
            message = message + deleteFail + "条巡检组指标删除失败。";
        }
        return new WebResult(true, message);
    }
}
