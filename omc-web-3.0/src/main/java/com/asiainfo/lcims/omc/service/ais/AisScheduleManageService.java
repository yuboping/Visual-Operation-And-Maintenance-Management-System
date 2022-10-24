package com.asiainfo.lcims.omc.service.ais;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.ais.AisGroupModel;
import com.asiainfo.lcims.omc.model.ais.AisScheduleModel;
import com.asiainfo.lcims.omc.persistence.ais.AisGroupDAO;
import com.asiainfo.lcims.omc.persistence.ais.AisScheduleDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("aisScheduleManageService")
public class AisScheduleManageService {

    private static final Logger LOG = LoggerFactory.getLogger(AisScheduleManageService.class);


    @Inject
    AisScheduleDAO aisScheduleDAO;

    @Resource(name = "scheduleService")
    private ScheduleService service;

    @Resource(name = "operateHisService")
    OperateHisService operateHisService;

    public List<AisScheduleModel> getAisScheduleList(AisScheduleModel aisScheduleModel) {
        List<AisScheduleModel> list = aisScheduleDAO.getAisScheduleList(aisScheduleModel);
        List<AisScheduleModel> aisScheduleModelList = new ArrayList<>();
        if(!list.isEmpty()) {
            for (AisScheduleModel aisSchedule : list) {
                String[] groupids = aisSchedule.getGroup_ids().split(",");
                String groupidstring = "";
                for (String groupid : groupids) {
                    groupidstring = groupidstring + "'" + groupid + "',";
                }
                groupidstring = groupidstring.substring(0, groupidstring.length() - 1);
                aisSchedule.setGroup_ids(groupidstring);
                List<AisGroupModel> aisGroupModelList = aisScheduleDAO.getAisGroupNameList(aisSchedule);
                String groupnamestring = "";
                if (!aisGroupModelList.isEmpty()) {
                    for (AisGroupModel aisGroupModel : aisGroupModelList) {
                        groupnamestring = groupnamestring + aisGroupModel.getGroup_name() + ",";
                    }
                    groupnamestring = groupnamestring.substring(0, groupnamestring.length() - 1);
                    aisSchedule.setGroup_ids(groupnamestring);
                    aisScheduleModelList.add(aisSchedule);
                }
            }
        }
        return aisScheduleModelList;
    }

    public WebResult addInfo(AisScheduleModel aisScheduleModel) {
        WebResult result = addValid(aisScheduleModel);
        if (result.operFail()) {
            return result;
        }
        String schedule_id = IDGenerateUtil.getUuid();
        aisScheduleModel.setId(schedule_id);

        int num = 0;
        try {
            num = aisScheduleDAO.insert(aisScheduleModel);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        if (num == 0) {
            return new WebResult(false, "新增失败!");
        }

        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AIS_GROUP_MANAGE,
                "新增数据[巡检计划:" + aisScheduleModel.getTitle() + "]");
        return new WebResult(true, "成功!");
    }

    private WebResult addValid(AisScheduleModel aisScheduleModel) {
        if (StringUtils.isEmpty(aisScheduleModel.getTitle())) {
            return new WebResult(false, "巡检计划标题为空!");
        }
        return new WebResult(true, "");
    }

    public WebResult modifyInfo(AisScheduleModel aisScheduleModel) {
        WebResult result = modifyValid(aisScheduleModel);
        if (result.operFail()) {
            return result;
        }

        AisScheduleModel aisSchedule = aisScheduleDAO.getAisScheduleById(aisScheduleModel.getId());

        int num = 0;
        try {
            num = aisScheduleDAO.update(aisScheduleModel);
        } catch (Exception e) {
            LOG.error("error:", e);
        }
        if (num == 0) {
            return new WebResult(false, "修改失败!");
        }

        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AIS_GROUP_MANAGE, "修改数据[巡检计划标题:"
                + aisSchedule.getTitle() + "-->" + aisScheduleModel.getTitle() + "]");
        return new WebResult(true, "成功!");
    }

    private WebResult modifyValid(AisScheduleModel aisScheduleModel) {
        if (StringUtils.isEmpty(aisScheduleModel.getTitle())) {
            return new WebResult(false, "巡检计划标题为空!");
        }
        return new WebResult(true, "");
    }

    public WebResult deleteInfo(String[] aisschedulelist) {
        Map<String, Object> map = new HashMap<>();
        List<String> delSuccessList = new ArrayList<>();
        List<String> delFailList = new ArrayList<>();

        if (aisschedulelist != null && aisschedulelist.length != 0) {
            for (String schedule_id : aisschedulelist) {
                WebResult result = deleteValid(schedule_id);
                AisScheduleModel aisScheduleModel = aisScheduleDAO.getAisScheduleById(schedule_id);
                if (result.operFail()) {
                    delFailList.add(aisScheduleModel.getTitle());
                    continue;
                }
                int deleteResult = service.delSchedule(schedule_id);
                if (1 == deleteResult) {
                    delSuccessList.add(aisScheduleModel.getTitle());
                } else {
                    delFailList.add(aisScheduleModel.getTitle());
                }
            }
        }
        String message = "";
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "条巡检计划删除成功" + delSuccessList + "。";
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "条巡检计划删除失败" + delFailList
                    + "。";
        }
        if (ToolsUtils.StringIsNull(message)) {
            message = "删除失败！";
        }
        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_AIS_GROUP_MANAGE, "删除巡检计划:"
                + delSuccessList);
        return new WebResult(true, message);
    }

    private WebResult deleteValid(String schedule_id) {
        boolean deleteFlag = true;
//        int aisMetricCount = aisScheduleDAO.getAisGroupMetricById(schedule_id);
//        if(aisMetricCount > 0){
//            deleteFlag = false;
//            return new WebResult(deleteFlag, "请先解除巡检组指标与巡检组关联");
//        }
//        int aisScheduleCount = aisScheduleDAO.getAisGroupMetricById(schedule_id);
//        if(aisScheduleCount > 0){
//            deleteFlag = false;
//            return new WebResult(deleteFlag, "请先解除巡检组计划与巡检组关联");
//        }
        return new WebResult(deleteFlag, "");
    }
}
