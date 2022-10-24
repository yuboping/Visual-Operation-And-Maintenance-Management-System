package com.asiainfo.lcims.omc.service.configmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdMetricType;
import com.asiainfo.lcims.omc.persistence.configmanage.MdMetricTypeDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.page.Page;

@Service(value = "metricTypeManageService")
public class MetricTypeManageService {
    @Inject
    private MdMetricTypeDAO mdMetricTyepDAO;

    @Autowired
    private OperateHisService operateHisService;

    /**
     * 
     * @Title: getAllMetricManage
     * @Description: TODO(获取指标类型信息)
     * @param @return 参数
     * @return List<MdMetricType> 返回类型
     * @throws
     */
    public Page getMdMetricTypePage(MdMetricType mdMetricTyep, Page page) {
        // 查询总数
        int totalCount = mdMetricTyepDAO.getMdMetricTypeTotalCount(mdMetricTyep);
        page.setTotalCount(totalCount);
        List<MdMetricType> mdMetricTypeList = new ArrayList<MdMetricType>();
        // 查询分页数据
        if (totalCount > 0) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("mdMetricTyep", mdMetricTyep);
            parameters.put("page", page);
            mdMetricTypeList = mdMetricTyepDAO.getMdMetricTypePage(mdMetricTyep, page);
        }
        page.setPageList(mdMetricTypeList);
        return page;
    }

    /**
     *
     * @Title: addMdMetricType
     * @Description: TODO(新增指标类型)
     * @param @param mdMetricTyep
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult addMdMetricType(MdMetricType mdMetricTyep) {
        if (null != mdMetricTyep.getMetric_type_name()
                && !("").equals(mdMetricTyep.getMetric_type_name())) {
            String uuid = IDGenerateUtil.getUuid();
            mdMetricTyep.setId(uuid);
            int addResult = mdMetricTyepDAO.insert(mdMetricTyep);
            if (1 == addResult) {
                // 用户日志记录
                operateHisService.insertOperateHistory(Constant.OPERATE_HIS_METRICTYPE_MANAGE,
                        "新增数据[指标类型:" + mdMetricTyep.getMetric_type_name() + "]");
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
     * @Title: addMdMetricType
     * @Description: TODO(修改指标类型)
     * @param @param mdMetricTyep
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult modifyMdMetricType(MdMetricType mdMetricTyep) {
        if (null != mdMetricTyep.getMetric_type_name()
                && !("").equals(mdMetricTyep.getMetric_type_name())) {
            MdMetricType hisMdMetricType = new MdMetricType();
            hisMdMetricType.setId(mdMetricTyep.getId());
            hisMdMetricType = mdMetricTyepDAO.getMdMetricType(hisMdMetricType).get(0);
            int updateResult = mdMetricTyepDAO.update(mdMetricTyep);
            if (1 == updateResult) {
                // 用户日志记录
                operateHisService.insertOperateHistory(
                        Constant.OPERATE_HIS_METRICTYPE_MANAGE,
                        "修改数据[指标类型:" + hisMdMetricType.getMetric_type_name() + "-->"
                                + mdMetricTyep.getMetric_type_name() + "]");
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
     * @Title: deleteMdMetricType
     * @Description: TODO(批量删除指标类型)
     * @param @param metricTypeidArray
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult deleteMdMetricType(String[] metricTypeidArray) {
        int deleteSuccess = 0;
        List<String> delSuccessList = new ArrayList<String>();
        int deleteFail = 0;
        List<String> delFailList = new ArrayList<String>();
        String message = "";
        if (metricTypeidArray != null && metricTypeidArray.length != 0) {
            for (String id : metricTypeidArray) {
                MdMetricType mdMetricType = new MdMetricType();
                mdMetricType.setId(id);
                List<MdMetricType> mdMetricList = mdMetricTyepDAO.getMdMetricType(mdMetricType);
                String metricTypeName = mdMetricList.get(0).getMetric_type_name();
                int deleteResult = mdMetricTyepDAO.delete(id);
                if (1 == deleteResult) {
                    delSuccessList.add(metricTypeName);
                    deleteSuccess++;
                } else {
                    delFailList.add(metricTypeName);
                    deleteFail++;
                }
            }
        }
        if (deleteSuccess > 0) {
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_METRICTYPE_MANAGE,
                    "删除数据[指标类型:" + delSuccessList);
            message = message + deleteSuccess + "条指标类型删除成功" + delSuccessList + "。";
        }

        if (deleteFail > 0) {
            message = message + deleteFail + "条指标类型删除失败" + delFailList + "。删除失败的指标类型请先解绑对应指标。";
        }
        return new WebResult(true, message);
    }
}
