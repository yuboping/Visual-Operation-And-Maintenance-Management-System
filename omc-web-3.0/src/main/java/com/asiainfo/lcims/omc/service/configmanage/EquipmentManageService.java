package com.asiainfo.lcims.omc.service.configmanage;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdEquipmentModel;
import com.asiainfo.lcims.omc.model.configmanage.MdFactory;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.MdEquipmentDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

/**
 * 设置型号管理Service类
 * 
 * @author zhujiansheng
 * @date 2018年7月25日 下午5:15:28
 * @version V1.0
 */
@Service
public class EquipmentManageService {

    @Autowired
    private MdEquipmentDAO mdEquipmentDAO;

    @Autowired
    private OperateHisService operateHisService;

    @Autowired
    private CommonInit commonInit;

    public List<MdFactory> getFactoryList() {
        return mdEquipmentDAO.getMdFactoryList();
    }

    public List<MdEquipmentModel> getEquipmentList(MdEquipmentModel mdEquipmentModel) {
        List<MdEquipmentModel> mdEquipment = mdEquipmentDAO.getMdEquipment(mdEquipmentModel);
        return mdEquipment;
    }

    public WebResult addMdEquipment(MdEquipmentModel mdEquip) {
        WebResult result = new WebResult(false, "新增失败");
        String uuid = IDGenerateUtil.getUuid();
        mdEquip.setId(uuid);
        int addResult = mdEquipmentDAO.insert(mdEquip);
        if (addResult == 1) {
            result = new WebResult(true, "新增成功");
            // 刷新设备型号缓存
            commonInit.loadMdEquipmentInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_EQUIPMENT_MANAGE,
                    "新增数据[设备型号:" + mdEquip.getModel_name() + "]");
        } else {
            result = new WebResult(false, "新增失败");
        }
        return result;
    }

    public WebResult modifyMdEquipment(MdEquipmentModel mdEquip) {
        WebResult result = new WebResult(false, "修改失败");
        
        List<MdEquipmentModel> mdEquipmentList = CommonInit.getMdEquipmentModelList();
        String equipmentName = "";
        for (MdEquipmentModel mdEquipmentModel : mdEquipmentList) {
            if (mdEquipmentModel.getId().equals(mdEquip.getId())) {
                equipmentName = mdEquipmentModel.getModel_name();
            }
        }
        
        int updateResult = mdEquipmentDAO.update(mdEquip);
        if (updateResult == 1) {
            result = new WebResult(true, "修改成功");
            // 刷新设备型号缓存
            commonInit.loadMdEquipmentInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_EQUIPMENT_MANAGE,
                    "修改数据[设备型号名:" + equipmentName + "-->" + mdEquip.getModel_name() + "]");
        } else {
            result = new WebResult(false, "修改失败");
        }
        return result;
    }

    public WebResult deleteMdEquipment(String[] equipidArray) {
        WebResult result = null;
        List<String> delSuccessList = new LinkedList<String>();
        List<String> delFailList = new LinkedList<String>();
        String message = "";
        if (equipidArray != null && equipidArray.length > 0) {
            for (String equipid : equipidArray) {
                MdEquipmentModel equipment = mdEquipmentDAO.getOneEquipment(equipid);
                int delResult = mdEquipmentDAO.delete(equipid);
                String modelName = equipment.getModel_name();
                if (delResult == 1) {
                    delSuccessList.add(modelName);
                } else {
                    delFailList.add(modelName);
                }
            }
        }
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "台设备型号删除成功" + delSuccessList + "。";
            // 刷新设备型号缓存
            commonInit.loadMdEquipmentInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_EQUIPMENT_MANAGE,
                    "删除数据[设备型号:" + delSuccessList);
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "台设备型号删除失败" + delFailList
                    + "。删除失败的设备型号请先解绑BRAS。";
        }
        result = new WebResult(true, message);
        return result;
    }
}
