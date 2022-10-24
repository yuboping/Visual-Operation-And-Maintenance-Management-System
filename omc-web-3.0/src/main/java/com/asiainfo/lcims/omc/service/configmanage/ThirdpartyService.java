package com.asiainfo.lcims.omc.service.configmanage;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.*;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.*;
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
import java.util.List;

@Service(value = "thirdpartyService")
public class ThirdpartyService {
    private static final Logger LOG = LoggerFactory.getLogger(ThirdpartyService.class);

    @Autowired
    private MdParamDAO mdParamDAO;

    @Autowired
    private AreaDAO areaDAO;

    @Autowired
    private MdThirdpartyDao mdThirdpartyDao;

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
     * @param mdThirdparty
     * @return
     */
    public List<MdThirdparty> getThirdpartyList(MdThirdparty mdThirdparty) {
        return mdThirdpartyDao.getThirdparty(mdThirdparty);
    }

    public List<MdThirdparty> getThirdpartyByRoleList(String roleid,MdThirdparty mdThirdparty) {
//        return mdFirewallDao.getBdNasByRoleList(roleid,mdFirewall);
        return null;
    }

    /**
     * 新增
     * @param mdThirdparty
     * @return
     */
    public WebResult addThirdparty(MdThirdparty mdThirdparty) {
        WebResult result = new WebResult(false, "新增失败");
        String uuid = IDGenerateUtil.getUuid();
        mdThirdparty.setId(uuid);
        int addResult = mdThirdpartyDao.insert(mdThirdparty);
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
     * @param mdThirdparty
     * @return
     */
    public WebResult modifyThirdparty(MdThirdparty mdThirdparty) {
        WebResult result = new WebResult(false, "修改失败");

//        String firewallName = "";
//        List<MdFirewall> mdFirewallList = CommonInit.getBdNasList();
//        for (BdNas bras : bdNasList) {
//            if (bras.getId().equals(mdFirewall.getId())) {
//                firewallName = bras.getNas_name();
//            }
//        }
        MdThirdparty m = mdThirdpartyDao.getSingleThirdparty(mdThirdparty.getId());
//        String preDimensionId = m.getArea_no();
        int updateResult = mdThirdpartyDao.update(mdThirdparty);
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
     * @param thirdpartyidArray
     * @return
     */
    public WebResult deleteThirdparty(String[] thirdpartyidArray) {
        WebResult result = null;
        List<String> delSuccessList = new ArrayList<String>();
        List<String> delFailList = new ArrayList<String>();
        String message = "";
        if (thirdpartyidArray != null && thirdpartyidArray.length != 0) {
            for (String id : thirdpartyidArray) {
                MdThirdparty mdThirdparty = mdThirdpartyDao.getSingleThirdparty(id);
                String thirdpartyName = mdThirdparty.getThirdparty_name();
                int deleteResult = mdThirdpartyDao.delete(id);
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
                    delSuccessList.add(thirdpartyName);
                } else {
                    delFailList.add(thirdpartyName);
                }
            }
        }
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "条第三方接口设备删除成功" + delSuccessList + "。";
            // 刷新BRAS缓存
            commonInit.loadBdNasInfo();
//            // 用户日志记录
//            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_FIREWALL, "删除数据[BRAS:"
//                    + delSuccessList);
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "条第三方接口设备删除失败" + delFailList
                    + "。删除失败的第三方接口设备请先解绑对应主机。";
        }
        result = new WebResult(true, message);
        return result;
    }

    /**
     * 导出
     * @param mdThirdpartyList
     * @param response
     * @param request
     * @return
     */
    public WebResult exportExcel(List<MdThirdparty> mdThirdpartyList, HttpServletResponse response,
                                 HttpServletRequest request) {
        WebResult result = new WebResult(false, "导出失败");
        List<MdThirdpartyExcel> mdThirdpartyExcels = transferThirdparty(mdThirdpartyList);
        String[][] datas = toArray(mdThirdpartyExcels);
        String title = "THIRDPARTY_EXPORT";
        String querytime = currentDate();
        String[] fields = { "设备名称", "设备IP", "设备型号", "厂家名称", "属地名称", "IP类型", "端口","用户名","密码","文件路径","描述" };
        ExcelUtil.downloadExcelFile(title, querytime, fields, request, response, datas);
        result = new WebResult(true, "导出成功！");
        return result;
    }

    public List<MdThirdpartyExcel> transferThirdparty(List<MdThirdparty> mdThirdpartyList) {
        List<MdThirdpartyExcel> mdThirdpartyExcels = new ArrayList<>();
        for (MdThirdparty mdThirdparty : mdThirdpartyList) {
            MdThirdpartyExcel mdThirdpartyExcel = new MdThirdpartyExcel();
            mdThirdpartyExcel.setThirdparty_name(mdThirdparty.getThirdparty_name());
            mdThirdpartyExcel.setThirdparty_ip(mdThirdparty.getThirdparty_ip());
            mdThirdpartyExcel.setModelname(mdThirdparty.getModelname());
            mdThirdpartyExcel.setFactoryname(mdThirdparty.getFactoryname());
            mdThirdpartyExcel.setAreaname(mdThirdparty.getAreaname());
            mdThirdpartyExcel.setIptype(mdThirdparty.getIptype());
            mdThirdpartyExcel.setPort(mdThirdparty.getPort());
            mdThirdpartyExcel.setUser_name(mdThirdparty.getUser_name());
            mdThirdpartyExcel.setPassword(mdThirdparty.getPassword());
            mdThirdpartyExcel.setFile_path(mdThirdparty.getFile_path());
            mdThirdpartyExcel.setDescription(mdThirdparty.getDescription());
            mdThirdpartyExcels.add(mdThirdpartyExcel);
        }
        return mdThirdpartyExcels;
    }

    public String[][] toArray(List<MdThirdpartyExcel> mdThirdpartyExcelList) {
        String[][] datas = new String[mdThirdpartyExcelList.size()][11];
        for (int i = 0; i < mdThirdpartyExcelList.size(); i++) {
            MdThirdpartyExcel mdThirdpartyExcel = mdThirdpartyExcelList.get(i);
            datas[i][0] = mdThirdpartyExcel.getThirdparty_name();
            datas[i][1] = mdThirdpartyExcel.getThirdparty_ip();
            datas[i][2] = mdThirdpartyExcel.getModelname();
            datas[i][3] = mdThirdpartyExcel.getFactoryname();
            datas[i][4] = mdThirdpartyExcel.getAreaname();
            datas[i][5] = mdThirdpartyExcel.getIptype();
            datas[i][6] = mdThirdpartyExcel.getPort();
            datas[i][7] = mdThirdpartyExcel.getUser_name();
            datas[i][8] = mdThirdpartyExcel.getPassword();
            datas[i][9] = mdThirdpartyExcel.getFile_path();
            datas[i][10] = mdThirdpartyExcel.getDescription();
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
     * @param thirdpartyids
     * @param areano
     * @return
     */
    public WebResult setThirdpartyArea(String thirdpartyids, String areano) {
        String[] thirdpartyidsArr = thirdpartyids.split(",");
        String message = "修改成功";
        try {
            for (String id : thirdpartyidsArr) {
                MdThirdparty m = mdThirdpartyDao.getSingleThirdparty(id);
                String preDimensionId = m.getArea_no();
                int result = mdThirdpartyDao.updateAreaNo(id, areano);
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
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_THIRDPARTY, "设置属地成功！");
        WebResult result = new WebResult(true, message);
        return result;
    }

    /**
     * 同步
     * @return
     */
    public WebResult refreshThirdparty() {
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
                    List<MdThirdparty> list = mdThirdpartyDao.queryNotMatchIp(metricid);
                    LOG.info("查询未匹配的第三方接口设备 ip 信息 length:" + list.size());
                    String area_no = CommonInit.BUSCONF.getStringValue("bras_unknow_area", "1000");
                    for (MdThirdparty mdThirdparty : list) {
                        mdThirdparty.setId(IDGenerateUtil.getUuid());
                        // 设置默认属地： 未知属地
                        mdThirdparty.setArea_no(area_no);
                        LOG.info("insert start "+mdThirdparty.getThirdparty_ip());
                        int result = mdThirdpartyDao.insert(mdThirdparty);
                        LOG.info("insert end "+mdThirdparty.getThirdparty_ip());
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
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_THIRDPARTY, "同步第三方接口设备成功！");
            LOG.info("log  end ========");
        } catch (Exception e) {
            LOG.error("第三方接口设备 fail", e);
        }
        WebResult result = new WebResult(true, message);
        return result;
    }
}
