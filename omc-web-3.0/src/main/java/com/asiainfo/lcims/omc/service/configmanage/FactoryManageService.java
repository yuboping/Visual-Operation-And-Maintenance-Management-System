package com.asiainfo.lcims.omc.service.configmanage;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdFactory;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.MdFactoryDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

/**
 * 工厂管理Service类
 * 
 * @author zhujiansheng
 * @date 2018年7月25日 上午10:16:25
 * @version V1.0
 */
@Service
public class FactoryManageService {

    @Autowired
    private MdFactoryDAO mdFactoryDAO;

    @Autowired
    private OperateHisService operateHisService;

    @Autowired
    private CommonInit commonInit;

    public List<MdFactory> getMdFactoryList(MdFactory mdFactory) {
        return mdFactoryDAO.getMdFactory(mdFactory);
    }

    public WebResult addMdFactory(MdFactory mdFactory) {
        WebResult result = new WebResult(false, "新增失败");
        String uuid = IDGenerateUtil.getUuid();
        mdFactory.setId(uuid);
        int addResult = mdFactoryDAO.insert(mdFactory);
        if (addResult == 1) {
            result = new WebResult(true, "新增成功");
            // 刷新厂家缓存
            commonInit.loadMdFactoryInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_FACTORY_MANAGE,
                    "新增数据[厂家:" + mdFactory.getFactory_name() + "]");
        } else {
            result = new WebResult(false, "新增失败");
        }
        return result;
    }

    public WebResult modifyMdFactory(MdFactory mdFactory) {
        WebResult result = new WebResult(false, "修改失败");

        List<MdFactory> mdFactoryList = CommonInit.getMdFactoryList();
        String factoryName = "";
        for (MdFactory factory : mdFactoryList) {
            if (factory.getId().equals(mdFactory.getId())) {
                factoryName = factory.getFactory_name();
            }
        }

        int updateResult = mdFactoryDAO.update(mdFactory);
        if (updateResult == 1) {
            result = new WebResult(true, "修改成功");
            // 刷新厂家缓存
            commonInit.loadMdFactoryInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_FACTORY_MANAGE,
                    "修改数据[厂家名:" + factoryName + "-->" + mdFactory.getFactory_name() + "]");
        } else {
            result = new WebResult(false, "修改失败");
        }
        return result;
    }

    public WebResult deleteMdFactory(String[] factoryIds) {
        WebResult result = null;
        List<String> delSuccessList = new LinkedList<String>();
        List<String> delFailList = new LinkedList<String>();
        String message = "";
        if (factoryIds != null && factoryIds.length > 0) {
            for (String factoryId : factoryIds) {
                MdFactory factory = mdFactoryDAO.getOneFactory(factoryId);
                String factoryName = factory.getFactory_name();
                int delResult = mdFactoryDAO.delete(factoryId);
                if (delResult == 1) {
                    delSuccessList.add(factoryName);
                } else {
                    delFailList.add(factoryName);
                }
            }
        }
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "条厂家删除成功" + delSuccessList + "。";
            // 刷新厂家缓存
            commonInit.loadMdFactoryInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_FACTORY_MANAGE,
                    "删除数据[厂家:" + delSuccessList);
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "条厂家删除失败" + delFailList
                    + "。删除失败的厂家请先删除对应设备。";
        }
        result = new WebResult(true, message);
        return result;
    }
}
