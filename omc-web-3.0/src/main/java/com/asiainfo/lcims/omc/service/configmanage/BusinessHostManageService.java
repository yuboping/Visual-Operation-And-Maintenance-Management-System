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

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdBusinessHost;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.configmanage.MdBusinessHostDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.service.system.MenuService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.page.Page;

@Service(value = "businessHostManageService")
public class BusinessHostManageService {
    private static final Logger LOG = LoggerFactory.getLogger(BusinessHostManageService.class);
    @Inject
    private MdBusinessHostDAO mdBusinessHostDAO;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "menuService")
    MenuService menuService;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    @Autowired
    private OperateHisService operateHisService;

    /**
     * 
     * @Title: getMdBusinessHostPage
     * @Description: TODO(获取业务关联主机配置信息)
     * @param @return 参数
     * @return List<MdBusinessHost> 返回类型
     * @throws
     */
    public Page getMdBusinessHostPage(MdBusinessHost mdBusinessHost, Page page) {
        // 查询总数
        int totalCount = mdBusinessHostDAO.getMdBusinessHostTotalCount(mdBusinessHost);
        page.setTotalCount(totalCount);
        List<MdBusinessHost> mdBusinessHostList = new ArrayList<MdBusinessHost>();
        // 查询分页数据
        if (totalCount > 0) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("mdBusinessHost", mdBusinessHost);
            parameters.put("page", page);
            mdBusinessHostList = mdBusinessHostDAO.getMdBusinessHostPage(mdBusinessHost, page);
            // 查询主机动态菜单
            List<MdMenu> mdMenuHostList = menuService.getMdMenuHostList();
            for (MdBusinessHost mh : mdBusinessHostList) {
                for (MdMenu m : mdMenuHostList) {
                    if (mh.getName().equals(m.getName())) {
                        mh.setBusiness_link(m.getBusiness_link());
                    }
                }
            }
        }
        page.setPageList(mdBusinessHostList);
        return page;
    }
    
    public List<MdBusinessHost> getMdBusinessHostListByHostId(String hostid){
        List<MdBusinessHost> mdBusinessHostList = mdBusinessHostDAO.getMdBusinessHostListByHostId(hostid);
        return mdBusinessHostList;
    }
    
    /**
     *
     * @Title: addMdBusinessHost
     * @Description: TODO(新增业务关联主机配置)
     * @param @param mdBusinessHost
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult addMdBusinessHost(MdBusinessHost mdBusinessHost, boolean isLoadMenu) {
        String uuid = IDGenerateUtil.getUuid();
        mdBusinessHost.setId(uuid);
        int addResult = mdBusinessHostDAO.insert(mdBusinessHost);
        if (1 == addResult) {
            // 加载菜单
            if(isLoadMenu){
                mdMenuDataListener.loadMenuList();
                // 用户日志记录
                operateHisService.insertOperateHistory(Constant.OPERATE_HIS_BUSINESSHOST_MANAGE,
                        "新增数据[业务关联主机:" + mdBusinessHost.getName() + "]");
            }
            // 添加告警规则
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operationType", Constant.DYNAMICTYPE_BUSINESSHOST);
            map.put("dynamicType", Constant.OPERATIONTYPE_ADD);
            map.put("operationId", mdBusinessHost.getHostid());
            alarmRuleManageService.dimensionModify(map);
            return new WebResult(true, "成功", uuid);
        } else {
            return new WebResult(false, "失败");
        }
    }
    
    /**
     *
     * @Title: addMdBusinessHost
     * @Description: TODO(修改业务关联主机配置)
     * @param @param mdBusinessHost
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult modifyMdBusinessHost(MdBusinessHost mdBusinessHost) {
        MdBusinessHost bh = new MdBusinessHost();
        bh.setId(mdBusinessHost.getId());
        List<MdBusinessHost> mdBusinessHostList = mdBusinessHostDAO.getMdBusinessHost(bh);
        MdBusinessHost businessHost = mdBusinessHostList.get(0);
        int updateResult = mdBusinessHostDAO.update(mdBusinessHost);
        if (1 == updateResult) {
            // 加载菜单
            mdMenuDataListener.loadMenuList();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_BUSINESSHOST_MANAGE,
                    "修改数据[业务关联主机:" + businessHost.getName() + "-->" + mdBusinessHost.getName()
                            + "]");
            // 更新告警规则
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operationType", Constant.DYNAMICTYPE_BUSINESSHOST);
            map.put("dynamicType", Constant.OPERATIONTYPE_MODIFY);
            map.put("operationId", businessHost.getHostid());
            map.put("preDimensionId", businessHost.getName());
            alarmRuleManageService.dimensionModify(map);
            return new WebResult(true, "成功");
        } else {
            return new WebResult(false, "失败");
        }
    }

    /**
     *
     * @Title: deleteMdBusinessHost
     * @Description: TODO(批量删除业务关联主机配置)
     * @param @param businessHostidArray
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult deleteMdBusinessHost(String[] businessHostidArray, boolean isLoadMenu) {
        int deleteSuccess = 0;
        int deleteFail = 0;
        String message = "";
        List<String> delSuccessList = new ArrayList<String>();
        if (businessHostidArray != null && businessHostidArray.length != 0) {
            for (String id : businessHostidArray) {
                MdBusinessHost mdBusinessHost = new MdBusinessHost();
                mdBusinessHost.setId(id);
                List<MdBusinessHost> mdBusinessHostList = mdBusinessHostDAO
                        .getMdBusinessHost(mdBusinessHost);
                MdBusinessHost businessHost = mdBusinessHostList.get(0);
                int deleteResult = mdBusinessHostDAO.delete(id);
                if (1 == deleteResult) {
                    // 加载菜单
                    if(isLoadMenu){
                        mdMenuDataListener.loadMenuList();
                    }
                    // 删除告警规则
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("operationType", Constant.DYNAMICTYPE_BUSINESSHOST);
                    map.put("dynamicType", Constant.OPERATIONTYPE_DELETE);
                    map.put("operationId", businessHost.getHostid());
                    map.put("preDimensionId", businessHost.getName());
                    alarmRuleManageService.dimensionModify(map);
                    delSuccessList.add(businessHost.getName());
                    deleteSuccess++;
                } else {
                    deleteFail++;
                }
            }
        }
        if (deleteSuccess > 0) {
            // 用户日志记录
            if(isLoadMenu){
                operateHisService.insertOperateHistory(Constant.OPERATE_HIS_BUSINESSHOST_MANAGE,
                        "删除数据[业务关联主机:" + delSuccessList);
            }
            message = message + deleteSuccess + "条业务关联主机配置删除成功 。";
        }

        if (deleteFail > 0) {
            message = message + deleteFail + "条业务关联主机配置删除失败。";
        }
        return new WebResult(true, message);
    }
}
