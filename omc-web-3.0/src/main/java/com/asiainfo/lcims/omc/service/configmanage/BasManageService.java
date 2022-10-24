package com.asiainfo.lcims.omc.service.configmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdBras;
import com.asiainfo.lcims.omc.model.configmanage.MdEquipmentModel;
import com.asiainfo.lcims.omc.model.configmanage.MdFactory;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.AreaDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.BdNasDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdEquipmentDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdFactoryDAO;
import com.asiainfo.lcims.omc.persistence.system.MdParamDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.ExcelUtil;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

/**
 * BRAS管理的Service类
 * 
 * @author zhujiansheng
 * @date 2018年8月8日 下午3:18:17
 * @version V1.0
 */
@Service(value = "basManageService")
public class BasManageService {
    private static final Logger LOG = LoggerFactory.getLogger(BasManageService.class);
    @Autowired
    private BdNasDAO bdNasDAO;

    @Autowired
    private MdParamDAO mdParamDAO;

    @Autowired
    private MdEquipmentDAO mdEquipmentDAO;

    @Autowired
    private MdFactoryDAO mdFactoryDAO;

    @Autowired
    private AreaDAO areaDAO;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    @Resource(name = "mdMenuDataListener")
    private MdMenuDataListener mdMenuDataListener;

    @Autowired
    private OperateHisService operateHisService;

    @Autowired
    private CommonInit commonInit;

    public List<BdNas> getBdNasList(BdNas bdNas) {
        return bdNasDAO.getBdNas(bdNas);
    }

    public List<MdArea> getByRoleMdAreaList(String roleid) {
        List<MdArea> areaList = areaDAO.getByRoleMdAreaList(roleid);
        return areaList;
    }

    public List<BdNas> getBdNasByRoleList(String roleid, BdNas bdNas) {
        return bdNasDAO.getBdNasByRoleList(roleid, bdNas);
    }

    public List<MdEquipmentModel> getMdEquipList(MdEquipmentModel equipment) {
        List<MdEquipmentModel> equipmentList = mdEquipmentDAO.getMdEquipment(equipment);
        return equipmentList;
    }

    public List<MdParam> getParamByType(MdParam mdParam) {
        return mdParamDAO.getParamByType(mdParam.getType().toString());
    }

    public List<MdFactory> getMdFactoryList(MdFactory mdFactory) {
        List<MdFactory> mdFactoryList = mdFactoryDAO.getMdFactory(mdFactory);
        return mdFactoryList;
    }

    public List<MdArea> getMdAreaList(MdArea mdArea) {
        List<MdArea> areaList = areaDAO.getAll();
        return areaList;
    }

    public WebResult addBdNas(BdNas bdNas) {
        WebResult result = new WebResult(false, "新增失败");
        String uuid = IDGenerateUtil.getUuid();
        bdNas.setId(uuid);
        int addResult = bdNasDAO.insert(bdNas);
        if (1 == addResult) {
            // 刷新BRAS缓存
            commonInit.loadBdNasInfo();
            // 加载菜单
            mdMenuDataListener.loadMenuList();
            // 添加告警规则
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operationType", Constant.DYNAMICTYPE_BRAS);
            map.put("dynamicType", Constant.OPERATIONTYPE_ADD);
            map.put("operationId", uuid);
            alarmRuleManageService.dimensionModify(map);
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_BRAS_MANAGE,
                    "新增数据[BRAS:" + bdNas.getNas_name() + "]");

            result = new WebResult(true, "新增成功");
        } else {
            result = new WebResult(false, "新增失败");
        }
        return result;
    }

    public WebResult modifyBdNas(BdNas bdNas) {
        WebResult result = new WebResult(false, "修改失败");

        String brasName = "";
        List<BdNas> bdNasList = CommonInit.getBdNasList();
        for (BdNas bras : bdNasList) {
            if (bras.getId().equals(bdNas.getId())) {
                brasName = bras.getNas_name();
            }
        }

        BdNas b = bdNasDAO.getSingleBras(bdNas.getId());
        String preDimensionId = b.getArea_no();
        int updateResult = bdNasDAO.update(bdNas);
        if (1 == updateResult) {
            // 刷新BRAS缓存
            commonInit.loadBdNasInfo();
            // 加载菜单
            mdMenuDataListener.loadMenuList();
            // 修改告警规则
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operationType", Constant.DYNAMICTYPE_BRAS);
            map.put("dynamicType", Constant.OPERATIONTYPE_MODIFY);
            map.put("operationId", bdNas.getId());
            map.put("preDimensionId", preDimensionId);
            map.put("curDimensionId", bdNas.getArea_no());
            alarmRuleManageService.dimensionModify(map);
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_BRAS_MANAGE,
                    "修改数据[BRAS:" + brasName + "-->" + bdNas.getNas_name() + "]");

            result = new WebResult(true, "修改成功");
        } else {
            result = new WebResult(false, "修改失败");
        }
        return result;
    }

    public WebResult deleteBdNas(String[] basidArray) {
        WebResult result = null;
        List<String> delSuccessList = new ArrayList<String>();
        List<String> delFailList = new ArrayList<String>();
        String message = "";
        if (basidArray != null && basidArray.length != 0) {
            for (String id : basidArray) {
                BdNas bdNas = bdNasDAO.getSingleBras(id);
                String basName = bdNas.getNas_name();
                int deleteResult = bdNasDAO.delete(id);
                if (1 == deleteResult) {
                    // 刷新BRAS缓存
                    commonInit.loadBdNasInfo();
                    // 加载菜单
                    mdMenuDataListener.loadMenuList();
                    // 删除告警规则
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("operationType", Constant.DYNAMICTYPE_BRAS);
                    map.put("dynamicType", Constant.OPERATIONTYPE_DELETE);
                    map.put("operationId", id);
                    alarmRuleManageService.dimensionModify(map);
                    delSuccessList.add(basName);
                } else {
                    delFailList.add(basName);
                }
            }
        }
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "条BRAS删除成功" + delSuccessList + "。";
            // 刷新BRAS缓存
            commonInit.loadBdNasInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_BRAS_MANAGE,
                    "删除数据[BRAS:" + delSuccessList);
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "条BRAS删除失败" + delFailList
                    + "。删除失败的BRAS请先解绑对应主机。";
        }
        result = new WebResult(true, message);
        return result;
    }

    public WebResult exportExcel(List<BdNas> bdNasList, HttpServletResponse response,
            HttpServletRequest request) {
        WebResult result = new WebResult(false, "导出失败");
        List<MdBras> mdBrasList = transferBdNas(bdNasList);
        String[][] datas = toArray(mdBrasList);
        String title = "BRAS_EXPORT";
        String querytime = currentDate();
        String[] fields = { "设备名称", "设备IP", "设备型号", "厂家名称", "属地名称", "IP类型", "描述" };
        ExcelUtil.downloadExcelFile(title, querytime, fields, request, response, datas);
        result = new WebResult(true, "导出成功！");
        return result;
    }

    public List<MdBras> transferBdNas(List<BdNas> bdNasList) {
        List<MdBras> mdBrasList = new ArrayList<>();
        for (BdNas bdNas : bdNasList) {
            MdBras mdBras = new MdBras();
            mdBras.setNas_name(bdNas.getNas_name());
            mdBras.setNas_ip(bdNas.getNas_ip());
            mdBras.setModelname(bdNas.getModelname());
            mdBras.setFactoryname(bdNas.getFactoryname());
            mdBras.setAreaname(bdNas.getAreaname());
            mdBras.setIptype(bdNas.getIptype());
            mdBras.setDescription(bdNas.getDescription());
            mdBrasList.add(mdBras);
        }
        return mdBrasList;
    }

    public String[][] toArray(List<MdBras> mdBrasList) {
        String[][] datas = new String[mdBrasList.size()][7];
        for (int i = 0; i < mdBrasList.size(); i++) {
            MdBras mdBras = mdBrasList.get(i);
            datas[i][0] = mdBras.getNas_name();
            datas[i][1] = mdBras.getNas_ip();
            datas[i][2] = mdBras.getModelname();
            datas[i][3] = mdBras.getFactoryname();
            datas[i][4] = mdBras.getAreaname();
            datas[i][5] = mdBras.getIptype();
            datas[i][6] = mdBras.getDescription();
        }
        return datas;
    }

    public String currentDate() {
        DateTools dateTools = new DateTools("yyyy-MM-dd");
        String currentDate = dateTools.getCurrentDate();
        return currentDate;
    }

    public WebResult refreshBras() {
        String mericIdentity = CommonInit.BUSCONF.getStringValue("bras_synchronous_metric_name");
        String message = "匹配成功";
        try {
            if (ToolsUtils.StringIsNull(mericIdentity)) {
                message = "未配置匹配所需指标标识";
                LOG.info(message);
            } else {
                String metricid = CommonInit.getMetricIdByMetricIdentity(mericIdentity);
                if (ToolsUtils.StringIsNull(metricid)) {
                    message = "指标表中无标识为 [" + mericIdentity + "] 的指标";
                    LOG.info(message);
                } else {
                    // 查询为匹配的bras ip 信息
                    List<BdNas> list = bdNasDAO.queryNotMatchNasIp(metricid);
                    LOG.info("查询未匹配的bras ip 信息 bas length:" + list.size());
                    String area_no = CommonInit.BUSCONF.getStringValue("bras_unknow_area", "1000");
                    for (BdNas bdNas : list) {
                        bdNas.setId(IDGenerateUtil.getUuid());
                        // 设置默认属地： 未知属地
                        bdNas.setArea_no(area_no);
                        LOG.info("insert bas start " + bdNas.getNas_ip());
                        int result = bdNasDAO.insert(bdNas);
                        LOG.info("insert bas end " + bdNas.getNas_ip());
                        if (1 == result) {
                            // 刷新BRAS缓存
                            commonInit.loadBdNasInfo();
                            // 加载菜单
                            mdMenuDataListener.loadMenuList();
                            // 添加告警规则
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("operationType", Constant.DYNAMICTYPE_BRAS);
                            map.put("dynamicType", Constant.OPERATIONTYPE_ADD);
                            map.put("operationId", bdNas.getId());
                            LOG.info("refresh alarm rule start " + bdNas.getNas_ip());
                            alarmRuleManageService.dimensionModify(map);
                            LOG.info("refresh alarm rule end " + bdNas.getNas_ip());
                        }
                    }
                }
            }
            // 用户日志记录
            LOG.info("log  start ========");
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_BRAS_MANAGE, "同步BRAS成功！");
            LOG.info("log  end ========");
        } catch (Exception e) {
            LOG.error("同步bas fail", e);
        }
        WebResult result = new WebResult(true, message);
        return result;
    }

    public WebResult setBrasArea(String brasids, String areano) {
        String[] brasidArr = brasids.split(",");
        String message = "修改成功";
        try {
            for (String id : brasidArr) {
                BdNas b = bdNasDAO.getSingleBras(id);
                String preDimensionId = b.getArea_no();
                int result = bdNasDAO.updateAreaNo(id, areano);
                if (1 == result) {
                    // 刷新BRAS缓存
                    commonInit.loadBdNasInfo();
                    // 加载菜单
                    mdMenuDataListener.loadMenuList();
                    // 修改告警规则
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("operationType", Constant.DYNAMICTYPE_BRAS);
                    map.put("dynamicType", Constant.OPERATIONTYPE_MODIFY);
                    map.put("operationId", id);
                    map.put("preDimensionId", preDimensionId);
                    map.put("curDimensionId", areano);
                    alarmRuleManageService.dimensionModify(map);
                }
            }
        } catch (Exception e) {
            message = "修改异常";
        }
        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_BRAS_MANAGE, "设置属地成功！");
        WebResult result = new WebResult(true, message);
        return result;
    }

    public Map<String, String> syncPWG(BdNas bdNas) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            switch (bdNas.getOtype()) {
            case "1":// 新增
                if ("success".equals(selectEquipmentId(bdNas, result))) {
                    addPWG(bdNas, result);
                }
                break;
            case "2":// 删除
                deletePWG(bdNas, result);
                break;
            case "3":// 修改
                if ("success".equals(selectEquipmentId(bdNas, result))) {
                    updatePWG(bdNas, result);
                }
                break;
            default:
                result.put("ret", "1");
                result.put("desc", "Otype error.");
                break;
            }
        } catch (Exception e) {
            result.put("ret", "1");
            result.put("desc", "SyncPWG parameter error.");
            LOG.error("syncPWG,error:{}", e);
        }
        return result;
    }

    public void addPWG(BdNas bdNas, Map<String, String> result) {
        BdNas originalBdNas = null;
        try {
            originalBdNas = bdNasDAO.getBdNasByIp(bdNas.getNas_ip());
        } catch (Exception e) {
            result.put("ret", "1");
            result.put("desc", "Ip repeat, error.");
            LOG.error("BdNasIp repeat,error:{}", e);
        }
        if (originalBdNas == null) {
            MdArea mdArea = null;
            try {
                mdArea = areaDAO.getAreabyAreano(bdNas.getArea_no());
            } catch (Exception e) {
                result.put("ret", "1");
                result.put("desc", "Areano repeat, error.");
                LOG.error("Areano repeat,error:{}", e);
            }
            if (mdArea != null) {
                String uuid = IDGenerateUtil.getUuid();
                bdNas.setId(uuid);
                int addResult = bdNasDAO.insert(bdNas);
                if (1 == addResult) {
                    // 刷新BRAS缓存
                    commonInit.loadBdNasInfo();
                    // 添加告警规则
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("operationType", Constant.DYNAMICTYPE_BRAS);
                    map.put("dynamicType", Constant.OPERATIONTYPE_ADD);
                    map.put("operationId", uuid);
                    alarmRuleManageService.dimensionModify(map);
                    result.put("ret", "0");
                    result.put("desc", "Success.");
                } else {
                    result.put("ret", "1");
                    result.put("desc", "Insert error.");
                }
            } else {
                result.put("ret", "1");
                result.put("desc", "Areano, error.");
            }
        } else {
            result.put("ret", "1");
            result.put("desc", "There is already PWG, error.");
        }
    }

    public void deletePWG(BdNas bdNas, Map<String, String> result) {
        BdNas originalBdNas = null;
        try {
            originalBdNas = bdNasDAO.getBdNasByIp(bdNas.getNas_ip());
        } catch (Exception e) {
            result.put("ret", "1");
            result.put("desc", "Ip repeat, error.");
            LOG.error("BdNasIp repeat,error:{}", e);
        }
        if (originalBdNas != null) {
            int deleteResult = bdNasDAO.delete(originalBdNas.getId());
            if (1 == deleteResult) {
                // 刷新BRAS缓存
                commonInit.loadBdNasInfo();
                // 删除告警规则
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("operationType", Constant.DYNAMICTYPE_BRAS);
                map.put("dynamicType", Constant.OPERATIONTYPE_DELETE);
                map.put("operationId", originalBdNas.getId());
                alarmRuleManageService.dimensionModify(map);
                result.put("ret", "0");
                result.put("desc", "Success.");
            } else {
                result.put("ret", "1");
                result.put("desc", "Delete error,unbind the corresponding host first.");
            }
        } else {
            result.put("ret", "0");
            result.put("desc", "Success.");
        }
    }

    public void updatePWG(BdNas bdNas, Map<String, String> result) {
        BdNas originalBdNas = null;
        try {
            originalBdNas = bdNasDAO.getBdNasByIp(bdNas.getNas_ip());
        } catch (Exception e) {
            result.put("ret", "1");
            result.put("desc", "Ip repeat, error.");
            LOG.error("BdNasIp repeat,error:{}", e);
        }
        if (originalBdNas != null) {
            MdArea mdArea = null;
            try {
                mdArea = areaDAO.getAreabyAreano(bdNas.getArea_no());
            } catch (Exception e) {
                result.put("ret", "1");
                result.put("desc", "Areano repeat, error.");
                LOG.error("Areano repeat,error:{}", e);
            }
            if (mdArea != null) {
                bdNas.setId(originalBdNas.getId());
                int updateResult = bdNasDAO.update(bdNas);
                if (1 == updateResult) {
                    // 刷新BRAS缓存
                    commonInit.loadBdNasInfo();
                    // 修改告警规则
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("operationType", Constant.DYNAMICTYPE_BRAS);
                    map.put("dynamicType", Constant.OPERATIONTYPE_MODIFY);
                    map.put("operationId", bdNas.getId());
                    map.put("preDimensionId", originalBdNas.getArea_no());
                    map.put("curDimensionId", bdNas.getArea_no());
                    alarmRuleManageService.dimensionModify(map);
                    result.put("ret", "0");
                    result.put("desc", "Success.");
                } else {
                    result.put("ret", "1");
                    result.put("desc", "Update error.");
                }
            } else {
                result.put("ret", "1");
                result.put("desc", "Areano, error.");
            }
        } else {
            result.put("ret", "1");
            result.put("desc", "There is no PWG here, error.");
        }
    }

    public String selectEquipmentId(BdNas bdNas, Map<String, String> result) {
        try {
            MdFactory mdFactory = mdFactoryDAO.getFactoryByName(bdNas.getFactory_name());
            MdEquipmentModel mdEquipmentModel = mdEquipmentDAO
                    .getEquipmentByName(bdNas.getEquip_name(), mdFactory.getId());
            bdNas.setEquip_id(mdEquipmentModel.getId());
            return "success";
        } catch (Exception e) {
            result.put("ret", "1");
            result.put("desc", "Factory or equipment name error.");
            LOG.error("Factory or equipment name error:{}", e);
            return "error";
        }
    }
}
