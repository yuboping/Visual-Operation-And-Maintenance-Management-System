package com.asiainfo.lcims.omc.service.configmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.AreaDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.BdNasDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ToolsUtils;

@Service(value = "areaService")
public class AreaService {

    private static final Logger LOG = LoggerFactory.getLogger(AreaService.class);

    @Inject
    private AreaDAO areaDAO;
    @Inject
    private BdNasDAO nasDAO;
    @Resource(name = "operateHisService")
    OperateHisService operateHisService;
    @Autowired
    private CommonInit commonInit;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    @Resource(name = "mdMenuDataListener")
    private MdMenuDataListener mdMenuDataListener;

    public WebResult addInfo(MdArea area) {
        WebResult result = addValid(area);
        if (result.operFail()) {
            return result;
        }

        int num = 0;
        try {
            String maxId = areaDAO.getMaxId();
            area.setAreano("00" + mkAreano(maxId));
            area.setAreacode("00");
            num = areaDAO.insert(area);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        if (num == 0) {
            return new WebResult(false, "新增失败!");
        }
        // 刷新地市缓存
        commonInit.loadAreaInfo();
        // 加载菜单
        mdMenuDataListener.loadMenuList();
        // 添加告警规则
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("operationType", Constant.DYNAMICTYPE_AREA);
        map.put("dynamicType", Constant.OPERATIONTYPE_ADD);
        map.put("operationId", area.getAreano());
        alarmRuleManageService.dimensionModify(map);
        LOG.info("新增数据为:" + area.toString() + ",成功数目为" + num);
        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AREA_MANAGE,
                "新增数据[属地:" + area.getName() + "]");
        return new WebResult(true, "成功!");
    }

    private String mkAreano(String id) {
        String temp = id == null ? "1" : id;
        if (temp.contains(".")){
            temp = temp.substring(0, temp.indexOf("."));
        }
        if (temp.length() < 2) {
            temp = "0" + temp;
        }
        return temp;
    }

    private WebResult addValid(MdArea area) {
        if (StringUtils.isEmpty(area.getName())) {
            return new WebResult(false, "参数有误!");
        }
        List<MdArea> list = areaDAO.getAll();
        for (MdArea info : list) {
            if (info.getName().equals(area.getName())) {
                return new WebResult(false, "城市名称已存在!", "repeat");
            }
        }
        return new WebResult(true, "");
    }

    public WebResult modifyInfo(MdArea area) {
        WebResult result = modifyValid(area);
        if (result.operFail()) {
            return result;
        }

        List<MdArea> mdAreaList = CommonInit.getMdAreaList();
        String areaname = null;
        for (MdArea trans : mdAreaList) {
            if (trans.getAreano().equals(area.getAreano())) {
                areaname = trans.getName();
            }
        }

        int num = 0;
        try {
            num = areaDAO.update(area);
        } catch (Exception e) {
        }
        if (num == 0) {
            return new WebResult(false, "修改失败!");
        }
        // 刷新地市缓存
        commonInit.loadAreaInfo();
        // 加载菜单
        mdMenuDataListener.loadMenuList();
        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AREA_MANAGE, "修改数据[属地名:"
                + areaname + "-->" + area.getName() + "]");
        return new WebResult(true, "成功!");
    }

    private WebResult modifyValid(MdArea area) {
        if (StringUtils.isEmpty(area.getName())) {
            return new WebResult(false, "参数有误!");
        }
        List<MdArea> list = areaDAO.getAll();
        for (MdArea info : list) {
            if (!info.getAreano().equals(area.getAreano()) && info.getName().equals(area.getName())) {
                return new WebResult(false, "城市名称已存在!", "repeat");
            }
        }
        return new WebResult(true, "");
    }

    public WebResult deleteInfo(String[] areaArray) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> delSuccessList = new ArrayList<String>();
        List<String> delFailList = new ArrayList<String>();

        if (areaArray != null && areaArray.length != 0) {
            for (String areano : areaArray) {
                WebResult result = deleteValid(areano);
                MdArea mdArea = areaDAO.getAreabyAreano(areano);
                if (result.operFail()) {
                    delFailList.add(mdArea.getName());
                    continue;
                }
                int deleteResult = areaDAO.deleteById(areano);
                if (1 == deleteResult) {
                    // 刷新地市缓存
                    commonInit.loadAreaInfo();
                    // 加载菜单
                    mdMenuDataListener.loadMenuList();
                    // 删除告警规则
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("operationType", Constant.DYNAMICTYPE_AREA);
                    m.put("dynamicType", Constant.OPERATIONTYPE_DELETE);
                    m.put("operationId", areano);
                    alarmRuleManageService.dimensionModify(m);
                    delSuccessList.add(mdArea.getName());
                } else {
                    delFailList.add(mdArea.getName());
                }
            }
        }
        String message = "";
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "条属地信息删除成功" + delSuccessList + "。";
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "条属地信息删除失败" + delFailList
                    + "。删除失败的属地信息请先解绑对应bas设备。";
        }
        if (ToolsUtils.StringIsNull(message)) {
            message = "删除失败！";
        }
        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AREA_MANAGE, "删除属地:"
                + delSuccessList);
        return new WebResult(true, message);
    }

    private WebResult deleteValid(String areano) {
        if ("00".equals(areano)) {
            return new WebResult(false, "不能删除省中心!");
        }
        List<BdNas> list = nasDAO.getBdNasWithAreaid(areano);
        if (!list.isEmpty()) {
            return new WebResult(false, "属地下关联有设备主机!");
        }
        return new WebResult(true, "");
    }

    public List<MdArea> getArea(String areano, String name) {
        List<MdArea> list = areaDAO.getArea(areano, name);
        return list;
    }

    public List<MdArea> getAllAreaList() {
        List<MdArea> list = areaDAO.getAll();
        return list;
    }
}
