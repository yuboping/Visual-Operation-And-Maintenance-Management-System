package com.asiainfo.lcims.omc.service.configmanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdProcess;
import com.asiainfo.lcims.omc.persistence.configmanage.MdProcessDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.page.Page;

@Service(value = "processManageService")
public class ProcessManageService {
    @Inject
    private MdProcessDAO mdProcessDAO;

    @Autowired
    private OperateHisService operateHisService;

    /**
     * 
     * @Title: getMdProcessPage
     * @Description: TODO(获取进程信息)
     * @param @return 参数
     * @return List<MdProcess> 返回类型
     * @throws
     */
    public Page getMdProcessPage(MdProcess mdProcess, Page page) {
        // 查询总数
        int totalCount = mdProcessDAO.getMdProcessTotalCount(mdProcess);
        page.setTotalCount(totalCount);
        List<MdProcess> mdProcessList = new ArrayList<MdProcess>();
        // 查询分页数据
        if (totalCount > 0) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("mdProcess", mdProcess);
            parameters.put("page", page);
            mdProcessList = mdProcessDAO.getMdProcessPage(mdProcess, page);
        }
        page.setPageList(mdProcessList);
        return page;
    }

    /**
     *
     * @Title: addMdProcess
     * @Description: TODO(新增进程)
     * @param @param mdProcess
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult addMdProcess(MdProcess mdProcess) {
        if (null != mdProcess.getProcess_name() && !("").equals(mdProcess.getProcess_name())
                && null != mdProcess.getProcess_key() && !("").equals(mdProcess.getProcess_key())) {
            String uuid = IDGenerateUtil.getUuid();
            mdProcess.setProcess_id(uuid);
            Date date = new Date();
            mdProcess.setCreate_time(date);
            mdProcess.setUpdate_time(date);
            int addResult = mdProcessDAO.insert(mdProcess);
            if (1 == addResult) {
                // 用户日志记录
                operateHisService.insertOperateHistory(Constant.OPERATE_HIS_PROCESS_MANAGE,
                        "新增数据[进程:" + mdProcess.getProcess_name() + "]");
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
     * @Title: addMdProcess
     * @Description: TODO(修改进程)
     * @param @param mdProcess
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult modifyMdProcess(MdProcess mdProcess) {
        if (null != mdProcess.getProcess_name() && !("").equals(mdProcess.getProcess_name())
                && null != mdProcess.getProcess_key() && !("").equals(mdProcess.getProcess_key())) {
            MdProcess hisMdProcess = new MdProcess();
            hisMdProcess.setProcess_id(mdProcess.getProcess_id());
            hisMdProcess = mdProcessDAO.getMdProcess(hisMdProcess).get(0);
            mdProcess.setUpdate_time(new Date());
            int updateResult = mdProcessDAO.update(mdProcess);
            if (1 == updateResult) {
                // 用户日志记录
                operateHisService.insertOperateHistory(
                        Constant.OPERATE_HIS_PROCESS_MANAGE,
                        "修改数据[进程:" + hisMdProcess.getProcess_name() + "-->"
                                + mdProcess.getProcess_name() + "]");
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
     * @Title: deleteMdProcess
     * @Description: TODO(批量删除进程)
     * @param @param processidArray
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult deleteMdProcess(String[] processidArray) {
        int deleteSuccess = 0;
        List<String> delSuccessList = new ArrayList<String>();
        int deleteFail = 0;
        List<String> delFailList = new ArrayList<String>();
        String message = "";
        if (processidArray != null && processidArray.length != 0) {
            for (String id : processidArray) {
                MdProcess mdProcess = new MdProcess();
                mdProcess.setProcess_id(id);
                List<MdProcess> mdMetricList = mdProcessDAO.getMdProcess(mdProcess);
                String processName = null;
                if (mdMetricList != null) {
                    processName = mdMetricList.get(0).getProcess_name();
                }
                int deleteResult = mdProcessDAO.delete(id);
                if (1 == deleteResult) {
                    delSuccessList.add(processName);
                    deleteSuccess++;
                } else {
                    delFailList.add(processName);
                    deleteFail++;
                }
            }
        }
        if (deleteSuccess > 0) {
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_PROCESS_MANAGE, "删除数据[进程:"
                    + delSuccessList);
            message = message + deleteSuccess + "条进程删除成功" + delSuccessList + "。";
        }

        if (deleteFail > 0) {
            message = message + deleteFail + "条进程删除失败" + delFailList + "。删除失败的进程请先解绑对应主机。";
        }
        return new WebResult(true, message);
    }

    /**
     * 查询所有进程信息
     * 
     * @return
     */
    public List<MdProcess> getAllMdProcess() {
        List<MdProcess> mdProcessList = mdProcessDAO.getAllMdProcess();
        return mdProcessList;
    }
}
