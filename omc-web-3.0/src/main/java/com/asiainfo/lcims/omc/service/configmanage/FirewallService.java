package com.asiainfo.lcims.omc.service.configmanage;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.*;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.AreaDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdEquipmentDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdFactoryDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdFirewallDao;
import com.asiainfo.lcims.omc.persistence.system.MdParamDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 防火墙设备管理
 */
@Service(value = "firewallService")
public class FirewallService {
    private static final Logger LOG = LoggerFactory.getLogger(FirewallService.class);

    @Autowired
    private MdParamDAO mdParamDAO;

    @Autowired
    private AreaDAO areaDAO;

    @Autowired
    private MdFirewallDao mdFirewallDao;

    @Resource(name = "mdMenuDataListener")
    private MdMenuDataListener mdMenuDataListener;

    @Autowired
    private CommonInit commonInit;

    @Autowired
    private MdEquipmentDAO mdEquipmentDAO;

    @Autowired
    private MdFactoryDAO mdFactoryDAO;

    @Autowired
    private OperateHisService operateHisService;

    /**
     * ip类型下拉菜单接口
     * @param mdParam
     * @return
     */
    public List<MdParam> getParamByType(MdParam mdParam) {
        return mdParamDAO.getParamByType(mdParam.getType().toString());
    }

    /**
     * 属地名称下拉菜单接口
     * @param roleid
     * @return
     */
    public List<MdArea> getByRoleMdAreaList(String roleid){
        List<MdArea> areaList = areaDAO.getByRoleMdAreaList(roleid);
        return areaList;
    }

    public List<MdEquipmentModel> getMdEquipList(MdEquipmentModel equipment) {
        List<MdEquipmentModel> equipmentList = mdEquipmentDAO.getMdEquipment(equipment);
        return equipmentList;
    }

    public List<MdFactory> getMdFactoryList(MdFactory mdFactory) {
        List<MdFactory> mdFactoryList = mdFactoryDAO.getMdFactory(mdFactory);
        return mdFactoryList;
    }

    /**
     * 表格数据
     * @param mdFirewall
     * @return
     */
    public List<MdFirewall> getFirewallList(MdFirewall mdFirewall) {
        return mdFirewallDao.getFirewall(mdFirewall);
    }

    public List<MdFirewall> getBdNasByRoleList(String roleid,MdFirewall mdFirewall) {
//        return mdFirewallDao.getBdNasByRoleList(roleid,mdFirewall);
        return null;
    }

    /**
     * 新增
     * @param mdFirewall
     * @return
     */
    public WebResult addFirewall(MdFirewall mdFirewall) {
        WebResult result = new WebResult(false, "新增失败");
        String uuid = IDGenerateUtil.getUuid();
        mdFirewall.setId(uuid);
        int addResult = mdFirewallDao.insert(mdFirewall);
        if (1 == addResult) {
            // 刷新BRAS缓存
//            commonInit.loadBdNasInfo();
            // 加载菜单
//            mdMenuDataListener.loadMenuList();
            // 添加告警规则
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("operationType", Constant.DYNAMICTYPE_BRAS);
//            map.put("dynamicType", Constant.OPERATIONTYPE_ADD);
//            map.put("operationId", uuid);
//            alarmRuleManageService.dimensionModify(map);
//            // 用户日志记录
//            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_FIREWALL, "新增数据[BRAS:"
//                    + bdNas.getNas_name() + "]");

            result = new WebResult(true, "新增成功");
        } else {
            result = new WebResult(false, "新增失败");
        }
        return result;
    }

    /**
     * 修改
     * @param mdFirewall
     * @return
     */
    public WebResult modifyFirewall(MdFirewall mdFirewall) {
        WebResult result = new WebResult(false, "修改失败");

//        String firewallName = "";
//        List<MdFirewall> mdFirewallList = CommonInit.getBdNasList();
//        for (BdNas bras : bdNasList) {
//            if (bras.getId().equals(mdFirewall.getId())) {
//                firewallName = bras.getNas_name();
//            }
//        }

        MdFirewall m = mdFirewallDao.getSingleFirewall(mdFirewall.getId());
//        String preDimensionId = m.getArea_no();
        int updateResult = mdFirewallDao.update(mdFirewall);
        if (1 == updateResult) {
//            // 刷新BRAS缓存
//            commonInit.loadBdNasInfo();
//            // 加载菜单
//            mdMenuDataListener.loadMenuList();
//            // 修改告警规则
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("operationType", Constant.DYNAMICTYPE_BRAS);
//            map.put("dynamicType", Constant.OPERATIONTYPE_MODIFY);
//            map.put("operationId", mdFirewall.getId());
//            map.put("preDimensionId", preDimensionId);
//            map.put("curDimensionId", mdFirewall.getArea_no());
//            alarmRuleManageService.dimensionModify(map);
//            // 用户日志记录
//            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_FIREWALL, "修改数据[BRAS:"
//                    + firewallName + "-->" + mdFirewall.getFactoryname() + "]");

            result = new WebResult(true, "修改成功");
        } else {
            result = new WebResult(false, "修改失败");
        }
        return result;
    }

    /**
     * 删除
     * @param firewallidArray
     * @return
     */
    public WebResult deleteFirewall(String[] firewallidArray) {
        WebResult result = null;
        List<String> delSuccessList = new ArrayList<String>();
        List<String> delFailList = new ArrayList<String>();
        String message = "";
        if (firewallidArray != null && firewallidArray.length != 0) {
            for (String id : firewallidArray) {
                MdFirewall mdFirewall = mdFirewallDao.getSingleFirewall(id);
                String firewallName = mdFirewall.getFirewall_name();
                int deleteResult = mdFirewallDao.delete(id);
                if (1 == deleteResult) {
//                    // 刷新BRAS缓存
//                    commonInit.loadBdNasInfo();
//                    // 加载菜单
//                    mdMenuDataListener.loadMenuList();
//                    // 删除告警规则
//                    Map<String, Object> map = new HashMap<String, Object>();
//                    map.put("operationType", Constant.DYNAMICTYPE_BRAS);
//                    map.put("dynamicType", Constant.OPERATIONTYPE_DELETE);
//                    map.put("operationId", id);
//                    alarmRuleManageService.dimensionModify(map);
                    delSuccessList.add(firewallName);
                } else {
                    delFailList.add(firewallName);
                }
            }
        }
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "条防火墙设备删除成功" + delSuccessList + "。";
            // 刷新BRAS缓存
            commonInit.loadBdNasInfo();
//            // 用户日志记录
//            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_FIREWALL, "删除数据[BRAS:"
//                    + delSuccessList);
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "条防火墙设备删除失败" + delFailList
                    + "。删除失败的防火墙设备请先解绑对应主机。";
        }
        result = new WebResult(true, message);
        return result;
    }

    /**
     * 导出
     * @param mdFirewallList
     * @param response
     * @param request
     * @return
     */
    public WebResult exportExcel(List<MdFirewall> mdFirewallList, HttpServletResponse response,
                                 HttpServletRequest request) {
        WebResult result = new WebResult(false, "导出失败");
        List<MdFirewallExcel> mdFirewallExcels = transferFirewall(mdFirewallList);
        String[][] datas = toArray(mdFirewallExcels);
        String title = "FIREWALL_EXPORT";
        String querytime = currentDate();
        String[] fields = { "设备名称", "设备IP", "设备型号", "厂家名称", "属地名称", "IP类型", "端口","用户名","密码","文件采集路径","采集结果路径","描述" };
        ExcelUtil.downloadExcelFile(title, querytime, fields, request, response, datas);
        result = new WebResult(true, "导出成功！");
        return result;
    }

    public List<MdFirewallExcel> transferFirewall(List<MdFirewall> mdFirewallList) {
        List<MdFirewallExcel> mdFirewallExcels = new ArrayList<>();
        for (MdFirewall mdFirewall : mdFirewallList) {
            MdFirewallExcel mdFirewallExcel = new MdFirewallExcel();
            mdFirewallExcel.setFirewall_name(mdFirewall.getFirewall_name());
            mdFirewallExcel.setFirewall_ip(mdFirewall.getFirewall_ip());
            mdFirewallExcel.setModelname(mdFirewall.getModelname());
            mdFirewallExcel.setFactoryname(mdFirewall.getFactoryname());
            mdFirewallExcel.setAreaname(mdFirewall.getAreaname());
            mdFirewallExcel.setIptype(mdFirewall.getIptype());
            mdFirewallExcel.setPort(mdFirewall.getPort());
            mdFirewallExcel.setUser_name(mdFirewall.getUser_name());
            mdFirewallExcel.setPassword(mdFirewall.getPassword());
            mdFirewallExcel.setFile_path(mdFirewall.getFile_path());
            mdFirewallExcel.setResult_path(mdFirewall.getResult_path());
            mdFirewallExcel.setDescription(mdFirewall.getDescription());
            mdFirewallExcels.add(mdFirewallExcel);
        }
        return mdFirewallExcels;
    }

    public String[][] toArray(List<MdFirewallExcel> mdFirewallExcelList) {
        String[][] datas = new String[mdFirewallExcelList.size()][12];
        for (int i = 0; i < mdFirewallExcelList.size(); i++) {
            MdFirewallExcel mdFirewallExcel = mdFirewallExcelList.get(i);
            datas[i][0] = mdFirewallExcel.getFirewall_name();
            datas[i][1] = mdFirewallExcel.getFirewall_ip();
            datas[i][2] = mdFirewallExcel.getModelname();
            datas[i][3] = mdFirewallExcel.getFactoryname();
            datas[i][4] = mdFirewallExcel.getAreaname();
            datas[i][5] = mdFirewallExcel.getIptype();
            datas[i][6] = mdFirewallExcel.getPort();
            datas[i][7] = mdFirewallExcel.getUser_name();
            datas[i][8] = mdFirewallExcel.getPassword();
            datas[i][9] = mdFirewallExcel.getFile_path();
            datas[i][10] = mdFirewallExcel.getResult_path();
            datas[i][11] = mdFirewallExcel.getDescription();
        }
        return datas;
    }

    public String currentDate() {
        DateTools dateTools = new DateTools("yyyy-MM-dd");
        String currentDate = dateTools.getCurrentDate();
        return currentDate;
    }

    /**
     * 设置属地
     * @param firewallids
     * @param areano
     * @return
     */
    public WebResult setFirewallArea(String firewallids, String areano) {
        String[] firewallidsArr = firewallids.split(",");
        String message = "修改成功";
        try {
            for (String id : firewallidsArr) {
                MdFirewall m = mdFirewallDao.getSingleFirewall(id);
                String preDimensionId = m.getArea_no();
                int result = mdFirewallDao.updateAreaNo(id, areano);
                if (1 == result) {
//                    // 刷新BRAS缓存
//                    commonInit.loadBdNasInfo();
//                    // 加载菜单
//                    mdMenuDataListener.loadMenuList();
//                    // 修改告警规则
//                    Map<String, Object> map = new HashMap<String, Object>();
//                    map.put("operationType", Constant.DYNAMICTYPE_BRAS);
//                    map.put("dynamicType", Constant.OPERATIONTYPE_MODIFY);
//                    map.put("operationId", id);
//                    map.put("preDimensionId", preDimensionId);
//                    map.put("curDimensionId", areano);
//                    alarmRuleManageService.dimensionModify(map);
                }
            }
        } catch (Exception e) {
            message = "修改异常";
        }
        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_FIREWALL, "设置属地成功！");
        WebResult result = new WebResult(true, message);
        return result;
    }

    /**
     * 同步
     * @return
     */
    public WebResult refreshFirewall() {
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
                    List<MdFirewall> list = mdFirewallDao.queryNotMatchIp(metricid);
                    LOG.info("查询未匹配的防火墙 ip 信息 length:" + list.size());
                    String area_no = CommonInit.BUSCONF.getStringValue("bras_unknow_area", "1000");
                    for (MdFirewall mdFirewall : list) {
                        mdFirewall.setId(IDGenerateUtil.getUuid());
                        // 设置默认属地： 未知属地
                        mdFirewall.setArea_no(area_no);
                        LOG.info("insert start "+mdFirewall.getFirewall_ip());
                        int result = mdFirewallDao.insert(mdFirewall);
                        LOG.info("insert end "+mdFirewall.getFirewall_ip());
                        if (1 == result) {
//                            // 刷新BRAS缓存
//                            commonInit.loadBdNasInfo();
//                            // 加载菜单
//                            mdMenuDataListener.loadMenuList();
//                            // 添加告警规则
//                            Map<String, Object> map = new HashMap<String, Object>();
//                            map.put("operationType", Constant.DYNAMICTYPE_BRAS);
//                            map.put("dynamicType", Constant.OPERATIONTYPE_ADD);
//                            map.put("operationId", mdFirewall.getId());
//                            LOG.info("refresh alarm rule start "+mdFirewall.getFirewall_ip());
//                            alarmRuleManageService.dimensionModify(map);
//                            LOG.info("refresh alarm rule end "+mdFirewall.getFirewall_ip());
                        }
                    }
                }
            }
            // 用户日志记录
            LOG.info("log  start ========");
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_FIREWALL, "同步防火墙成功！");
            LOG.info("log  end ========");
        } catch (Exception e) {
            LOG.error("同步防火墙 fail", e);
        }
        WebResult result = new WebResult(true, message);
        return result;
    }
}
