package com.asiainfo.lcims.omc.service.ais;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.ais.AisGroupModel;
import com.asiainfo.lcims.omc.model.ais.AisScheduleModel;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.ais.AisGroupDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.StringUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.*;

@Service("aisGroupManageService")
public class AisGroupManageService {

    private static final Logger LOG = LoggerFactory.getLogger(AisGroupManageService.class);


    @Inject
    AisGroupDAO aisGroupDAO;

    @Resource(name = "operateHisService")
    OperateHisService operateHisService;

    public List<AisGroupModel> getAisGroupList(AisGroupModel aisGroup) {
        List<AisGroupModel> list = aisGroupDAO.getAisGroupList(aisGroup);
        return list;
    }

    public WebResult addInfo(AisGroupModel aisGroup) {
        WebResult result = addValid(aisGroup);
        if (result.operFail()) {
            return result;
        }
        String group_id = IDGenerateUtil.getUuid();
        aisGroup.setGroup_id(group_id);
        aisGroup.setStatus(1);
        int num = 0;
        try {
            num = aisGroupDAO.insert(aisGroup);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        if (num == 0) {
            return new WebResult(false, "新增失败!");
        }

        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AIS_GROUP_MANAGE,
                "新增数据[巡检组:" + aisGroup.getGroup_name() + "]");
        return new WebResult(true, "成功!");
    }

    private WebResult addValid(AisGroupModel aisGroup) {
        if (StringUtils.isEmpty(aisGroup.getGroup_name())) {
            return new WebResult(false, "巡检组名称为空!");
        }
        int aisGroupCount = aisGroupDAO.getAisGroupCount(aisGroup);
        if (aisGroupCount > 0){
            return new WebResult(false, "巡检组名称重复，请修改巡检组名称!", "repeat");
        }
        return new WebResult(true, "");
    }

    public WebResult modifyInfo(AisGroupModel aisGroup) {
        WebResult result = modifyValid(aisGroup);
        if (result.operFail()) {
            return result;
        }

        AisGroupModel aisGroupModel = aisGroupDAO.getAisGroupById(aisGroup.getGroup_id());

        int num = 0;
        try {
            num = aisGroupDAO.update(aisGroup);
        } catch (Exception e) {
            LOG.error("error:", e);
        }
        if (num == 0) {
            return new WebResult(false, "修改失败!");
        }

        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AIS_GROUP_MANAGE, "修改数据[巡检组名:"
                + aisGroupModel.getGroup_name() + "-->" + aisGroup.getGroup_name() + "]");
        return new WebResult(true, "成功!");
    }

    private WebResult modifyValid(AisGroupModel aisGroup) {
        if (StringUtils.isEmpty(aisGroup.getGroup_name())) {
            return new WebResult(false, "巡检组名称为空!");
        }
        List<AisGroupModel> list = aisGroupDAO.getAllAisGroup();
        for (AisGroupModel info : list) {
            if (!info.getGroup_id().equals(aisGroup.getGroup_id()) && info.getGroup_name().equals(aisGroup.getGroup_name())) {
                return new WebResult(false, "城市名称已存在!", "repeat");
            }
        }

        return new WebResult(true, "");
    }

    public WebResult deleteInfo(String[] aisGroupArray) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> delSuccessList = new ArrayList<String>();
        List<String> delFailList = new ArrayList<String>();
        String delFailInfo = "";

        if (aisGroupArray != null && aisGroupArray.length != 0) {
            for (String group_id : aisGroupArray) {
                WebResult result = deleteValid(group_id);
                AisGroupModel aisGroupModel = aisGroupDAO.getAisGroupById(group_id);
                if (result.operFail()) {
                    delFailInfo = delFailInfo + "[巡检组:{" + aisGroupModel.getGroup_name() +"} "+ result.getMessage() +"],";
//                    delFailInfo = "使用中的巡检组请先与巡检相关模块解除关联再进行删除操作。";
                    delFailList.add(aisGroupModel.getGroup_name());
                    continue;
                }
                int deleteResult = aisGroupDAO.deleteById(group_id);
                if (1 == deleteResult) {
                    delSuccessList.add(aisGroupModel.getGroup_name());
                } else {
                    delFailList.add(aisGroupModel.getGroup_name());
                }
            }
        }
        if(delFailInfo.length() > 0){
            delFailInfo = delFailInfo.substring(0,delFailInfo.length() -1);
        }
        String message = "";
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "条巡检组信息删除成功" + delSuccessList + "。";
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "条巡检组信息删除失败" + delFailList + "。提示:" + delFailInfo
                    + "。";
        }
        if (ToolsUtils.StringIsNull(message)) {
            message = "删除失败！";
        }
        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AIS_GROUP_MANAGE, "删除巡检组:"
                + delSuccessList);
        return new WebResult(true, message);
    }

    private WebResult deleteValid(String group_id) {
        boolean deleteFlag = true;
        int aisMetricCount = aisGroupDAO.getAisGroupMetricById(group_id);
        if(aisMetricCount > 0){
            deleteFlag = false;
            return new WebResult(deleteFlag, "请先解除巡检组指标与巡检组关联");
        }

        List<String> aisIdsList = new ArrayList();
        List<AisScheduleModel> aisScheduleModelList = aisGroupDAO.getAisScheduleListById();
        for(AisScheduleModel aisScheduleModel : aisScheduleModelList){
            String ids = aisScheduleModel.getGroup_ids();
            String[] idsArray  = ids.split(",");
            aisIdsList.addAll(Arrays.asList(idsArray));
        }
        if (aisIdsList != null && aisIdsList.size() > 0) {
            for (String id : aisIdsList) {
                if (group_id.equals(id)) {
                    deleteFlag = false;
                    return new WebResult(deleteFlag, "请先解除巡检组计划与巡检组关联");
                }
            }
        }
        return new WebResult(deleteFlag, "");
    }
}
