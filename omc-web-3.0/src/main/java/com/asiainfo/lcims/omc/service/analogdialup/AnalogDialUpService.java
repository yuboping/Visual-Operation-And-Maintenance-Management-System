package com.asiainfo.lcims.omc.service.analogdialup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.lcbmi.password.PasswordException;
import com.asiainfo.lcims.lcbmi.password.PwdDES3;
import com.asiainfo.lcims.omc.analogdialup.AnalogDialUpQuartzManager;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.analogdialup.AnalogDialUp;
import com.asiainfo.lcims.omc.persistence.analogdialup.AnalogDialUpDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.page.Page;

@Service(value = "analogDialUpService")
public class AnalogDialUpService {
    private static final Logger LOG = LoggerFactory.getLogger(AnalogDialUpService.class);

    @Inject
    private AnalogDialUpDAO analogDialUpDAO;

    @Autowired
    private OperateHisService operateHisService;

    public List<AnalogDialUp> getAnalogDialUpList(AnalogDialUp analogDialUp) {
        List<AnalogDialUp> AnalogDialUpList = analogDialUpDAO.getAnalogDialUpList(analogDialUp);
        for (AnalogDialUp analogDial : AnalogDialUpList) {
            String cron_erp = analogDial.getCron_erp();
            String min_rate = cron_erp.substring(4, cron_erp.length() - 8);
            analogDial.setMin_rate(min_rate);
            analogDial.setMin_rate_name("每" + min_rate + "分钟执行一次");
        }
        return AnalogDialUpList;
    }

    public Page getAnalogDialUpResultPage(AnalogDialUp analogDialUp, Page page) {
        // 查询总数
        int totalCount = analogDialUpDAO.getAnalogDialUpResultTotalCount(analogDialUp);
        page.setTotalCount(totalCount);
        List<AnalogDialUp> analogDialUpResultList = new ArrayList<AnalogDialUp>();
        // 查询分页数据
        if (totalCount > 0) {
            analogDialUpResultList = analogDialUpDAO.getAnalogDialUpResultPage(analogDialUp, page);
        }
        for (AnalogDialUp analogDialUpResult : analogDialUpResultList) {
            String cron_erp = analogDialUpResult.getCron_erp();
            String min_rate = cron_erp.substring(4, cron_erp.length() - 8);
            analogDialUpResult.setMin_rate(min_rate);
            analogDialUpResult.setMin_rate_name("每" + min_rate + "分钟执行一次");
        }
        page.setPageList(analogDialUpResultList);
        return page;
    }

    public WebResult addAnalogDialUp(AnalogDialUp analogDialUp) {
        String uuid = IDGenerateUtil.getUuid();
        analogDialUp.setId(uuid);
        if (null != analogDialUp.getHost_id() && !("").equals(analogDialUp.getHost_id())
                && null != analogDialUp.getMin_rate() && !("").equals(analogDialUp.getMin_rate())
                && null != analogDialUp.getUsername() && !("").equals(analogDialUp.getUsername())
                && null != analogDialUp.getPassword() && !("").equals(analogDialUp.getPassword())) {
            try {
                analogDialUp.setCron_erp("0 0/" + analogDialUp.getMin_rate() + " * * * ?");
                String password = analogDialUp.getPassword();
                // 密码加盐加密
                PwdDES3 pwd = new PwdDES3();
                password = password + Constant.PASSWORD_SALT;
                String encryptPassword = pwd.encryptPassword(password);
                analogDialUp.setPassword(encryptPassword);
                int addResult = analogDialUpDAO.insert(analogDialUp);
                if (1 == addResult) {
                    AnalogDialUp addAnalogDialUp = analogDialUpDAO.getAnalogDialUp(uuid);
                    // 用户日志记录
                    operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ANALOG_DIAL_UP,
                            "新增数据[模拟拨测主机IP:" + addAnalogDialUp.getHost_ip() + "]");
                    // 添加模拟拨测定时任务
                    AnalogDialUpQuartzManager.getInstance().addJob(addAnalogDialUp);
                    return new WebResult(true, "成功", uuid);
                } else {
                    return new WebResult(false, "失败");
                }
            } catch (SchedulerException | PasswordException e) {
                LOG.error("AnalogDialUpService addAnalogDialUp Exception:{}", e);
                return new WebResult(false, "失败");
            }
        } else {
            return new WebResult(false, "失败");
        }
    }

    public WebResult modifyAnalogDialUp(AnalogDialUp analogDialUp) {
        if (null != analogDialUp.getId() && !("").equals(analogDialUp.getId())
                && null != analogDialUp.getHost_id() && !("").equals(analogDialUp.getHost_id())
                && null != analogDialUp.getMin_rate() && !("").equals(analogDialUp.getMin_rate())
                && null != analogDialUp.getUsername() && !("").equals(analogDialUp.getUsername())
                && null != analogDialUp.getPassword() && !("").equals(analogDialUp.getPassword())) {
            try {
                analogDialUp.setCron_erp("0 0/" + analogDialUp.getMin_rate() + " * * * ?");
                String password = analogDialUp.getPassword();
                // 密码加盐加密
                PwdDES3 pwd = new PwdDES3();
                password = password + Constant.PASSWORD_SALT;
                String encryptPassword = pwd.encryptPassword(password);
                analogDialUp.setPassword(encryptPassword);
                int updateResult = analogDialUpDAO.update(analogDialUp);
                if (1 == updateResult) {
                    AnalogDialUp modifyAnalogDialUp = analogDialUpDAO
                            .getAnalogDialUp(analogDialUp.getId());
                    // 修改模拟拨测定时任务
                    AnalogDialUpQuartzManager.getInstance().modifyJob(modifyAnalogDialUp);
                    // 用户日志记录
                    operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ANALOG_DIAL_UP,
                            "修改数据[模拟拨测主机IP:" + modifyAnalogDialUp.getHost_ip() + "]");
                    return new WebResult(true, "成功");
                } else {
                    return new WebResult(false, "失败");
                }
            } catch (SchedulerException | PasswordException e) {
                LOG.error("AnalogDialUpService modifyAnalogDialUp Exception:{}", e);
                return new WebResult(false, "失败");
            }
        } else {
            return new WebResult(false, "失败");
        }
    }

    public WebResult deleteAnalogDialUp(String[] analogDialUpIdArray) {
        int deleteSuccess = 0;
        List<String> delSuccessList = new ArrayList<String>();
        int deleteFail = 0;
        List<String> delFailList = new ArrayList<String>();
        String message = "";
        if (analogDialUpIdArray != null && analogDialUpIdArray.length != 0) {
            for (String id : analogDialUpIdArray) {
                try {
                    AnalogDialUp analogDialUp = analogDialUpDAO.getAnalogDialUp(id);
                    String host_ip = analogDialUp.getHost_ip();
                    int deleteResult = analogDialUpDAO.delete(id);
                    if (1 == deleteResult) {
                        // 删除模拟拨测定时任务
                        AnalogDialUpQuartzManager.getInstance().deleteJob(analogDialUp);
                        delSuccessList.add(host_ip);
                        deleteSuccess++;
                    } else {
                        delFailList.add(host_ip);
                        deleteFail++;
                    }
                } catch (SchedulerException e) {
                    LOG.error("AnalogDialUpService deleteAnalogDialUp Exception:{}", e);
                }
            }
        }
        if (deleteSuccess > 0) {
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ANALOG_DIAL_UP,
                    "删除数据[模拟拨测主机:" + delSuccessList);
            message = message + deleteSuccess + "条模拟拨测删除成功" + delSuccessList + "。";
        }
        if (deleteFail > 0) {
            message = message + deleteFail + "条模拟拨测删除失败" + delFailList + "。";
        }
        return new WebResult(true, message);
    }

    public List<Map<String, Object>> getReturncodeName() {
        return analogDialUpDAO.getReturncodeName();
    }
}
