package com.asiainfo.lcims.omc.service.configmanage;

import com.asiainfo.lcims.omc.boot.JettyServerConf;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.*;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.persistence.system.MdParamDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service(value = "collectManageService")
public class CollectManageService {

    public static final JettyServerConf conf = new JettyServerConf();
    private static final Logger LOG = LoggerFactory.getLogger(HostService.class);

    @Autowired
    private MdParamDAO mdParamDAO;

    @Autowired
    private CollectManageDAO collectManageDAO;

    @Autowired
    private MdNodeDAO mdNodeDAO;

    @Autowired
    private MdHostProcessDAO mdHostProcessDAO;

    @Inject
    private MdBusinessHostDAO mdBusinessHostDAO;

    @Resource(name = "mdMenuDataListener")
    private MdMenuDataListener mdMenuDataListener;

    @Autowired
    private CommonInit commonInit;

    @Autowired
    private OperateHisService operateHisService;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    private static final String PARAM_TYPE_HOSTTYPE = "3";

    public List<MdNode> getNodeList() {
        List<MdNode> nodeList = mdNodeDAO.getMdNodeList();
        return nodeList;
    }

    public WebResult addInfo(MonHost host) {
        WebResult result = addValid(host);
        if (result.operFail()) {
            return result;
        }
        int num = 0;
        try {
            host.setStatus(1);
            host.setHostid(IDGenerateUtil.getUuid());
            num = collectManageDAO.insert(host);
            // 加载菜单
            mdMenuDataListener.loadMenuList();
            // 采集机新增时调用刷新缓存信息
            commonInit.refreshMdHost();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_SERVER_MANAGE, "新增数据[服务器:"
                    + host.getHostname() + "]");
            // 添加告警规则
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operationType", Constant.DYNAMICTYPE_HOST);
            map.put("dynamicType", Constant.OPERATIONTYPE_ADD);
            map.put("operationId", host.getHostid());
            alarmRuleManageService.dimensionModify(map);
        } catch (Exception e) {
            LOG.error("insert host error, reason : {}", e);
        }
        if (num == 0) {
            return new WebResult(false, "新增失败!");
        }
        return new WebResult(true, host.getHostid());
    }

    // 修改采集机
    public WebResult modifyInfo(MonHost host) {
        WebResult result = modifyValid(host);
        if (result.operFail()) {
            return result;
        }
        int num = 0;

        List<MonHost> monHostList = CommonInit.getMonHostList();
        String monHostName = "";
        String preDimensionId = "";
        for (MonHost monHost : monHostList) {
            if (monHost.getHostid().equals(host.getHostid())) {
                monHostName = monHost.getHostname();
            }
            if (monHost.getHostid().equals(host.getHostid())) {
                preDimensionId = monHost.getNodeid();
            }
        }

        try {
            num = collectManageDAO.update(host);
            // 加载菜单
            mdMenuDataListener.loadMenuList();
            // 采集机修改时调用刷新缓存信息
            commonInit.refreshMdHost();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_SERVER_MANAGE, "修改数据[服务器:"
                    + monHostName + "-->" + host.getHostname() + "]");

            // 修改告警规则
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operationType", Constant.DYNAMICTYPE_HOST);
            map.put("dynamicType", Constant.OPERATIONTYPE_MODIFY);
            map.put("operationId", host.getHostid());
            map.put("preDimensionId", preDimensionId);
            map.put("curDimensionId", host.getNodeid());
            alarmRuleManageService.dimensionModify(map);
        } catch (Exception e) {
            LOG.error("update host error, reason {}", e);
        }
        if (num == 0) {
            return new WebResult(false, "修改失败!");
        }
        return new WebResult(true, "修改成功!");
    }

    public List<MonHost> getRepeatHost(String addr, String hostname) {
        List<MonHost> monHosts = collectManageDAO.getRepeatHost(addr, hostname);
        return monHosts;
    }

    @Transactional
    public WebResult deleteInfo(String[] hostidArray) {
        WebResult webResult = null;
        List<String> delSuccessList = new LinkedList<String>();
        List<String> delFailList = new LinkedList<String>();
        String message = "";
        if (hostidArray != null && hostidArray.length > 0) {
            for (String hostid : hostidArray) {
                MonHost monHost = collectManageDAO.getHostById(hostid);
                String hostname = monHost.getHostname();
                int deleteResult = collectManageDAO.deleteByHostId(hostid);
                if (1 == deleteResult) {
                    int deleteHostProcess = mdHostProcessDAO.deleteByHostId(hostid);
                    int deleteBusinessHost = mdBusinessHostDAO.deleteByHostId(hostid);
                    // 加载菜单
                    mdMenuDataListener.loadMenuList();
                    // 采集机删除时调用刷新缓存信息
                    commonInit.refreshMdHost();
                    // 删除告警规则
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("operationType", Constant.DYNAMICTYPE_HOST);
                    map.put("dynamicType", Constant.OPERATIONTYPE_DELETE);
                    map.put("operationId", hostid);
                    alarmRuleManageService.dimensionModify(map);
                    LOG.info("hostid : {},delete host process [{}],delete business host [{}]",
                            hostid, ((deleteHostProcess == 0) ? "failed" : "succeed"),
                            ((deleteBusinessHost == 0) ? "failed" : "succeed"));
                    delSuccessList.add(hostname);
                } else {
                    delFailList.add(hostname);
                }
            }
        }
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "台采集机删除成功，采集机名：" + delSuccessList + "。";
            // 采集机修改时调用刷新缓存信息
            commonInit.refreshMdHost();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_SERVER_MANAGE, "删除数据[采集机:"
                    + delSuccessList);
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "台采集机删除失败，采集机名：" + delFailList
                    + "。删除失败请先解绑对应指标。";
        }
        webResult = new WebResult(true, message);
        return webResult;
    }

    private WebResult addValid(MonHost host) {
        List<MonHost> list = collectManageDAO.getAllHost();
        for (MonHost h : list) {
            if (h.getAddr().equals(host.getAddr())) {
                return new WebResult(false, "采集机IP已存在!");
            }
            if (h.getHostname().equals(host.getHostname())) {
                return new WebResult(false, "采集机名称已存在!");
            }
        }
        return new WebResult(true, "");
    }

    private WebResult modifyValid(MonHost host) {
        List<MonHost> list = collectManageDAO.getAllHost();
        for (MonHost h : list) {
            if (!h.getHostid().equals(host.getHostid()) && h.getAddr().equals(host.getAddr())) {
                return new WebResult(false, "采集机IP已存在!");
            }
            if (!h.getHostid().equals(host.getHostid())
                    && h.getHostname().equals(host.getHostname())) {
                return new WebResult(false, "采集机名称已存在!");
            }
        }
        return new WebResult(true, "");
    }

    public List<MdParam> getHostType() {
        List<MdParam> list = mdParamDAO.getParamByType(PARAM_TYPE_HOSTTYPE);
        return list;
    }

    public List<MonHost> getAllHost() {
        List<MonHost> list = collectManageDAO.getAllHost();
        return list;
    }

    public List<MonHost> getHost(String hostname, String addr, String nodeid) {
        List<MonHost> list = collectManageDAO.getHost(hostname, addr, nodeid);
        return list;
    }

    /**
     * 根据hostid查询单个采集机，显示详情
     *
     * @param hostid
     * @return
     */
    public MonHost getHostById(String hostid) {
        MonHost host = new MonHost();
        if (conf.getAutoDeploySwitch()) {
            host = collectManageDAO.getHostByIdAutodeploy(hostid);
        } else {
            host = collectManageDAO.getHostById(hostid);
        }
        return host;
    }
}
