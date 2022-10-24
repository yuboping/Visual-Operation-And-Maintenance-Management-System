package com.asiainfo.lcims.omc.service.ems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.lcims.omc.model.configmanage.MdBusinessHost;
import com.asiainfo.lcims.omc.model.configmanage.MdHostMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.ems.EmsAddHost;
import com.asiainfo.lcims.omc.model.ems.EmsAddHostRequest;
import com.asiainfo.lcims.omc.model.ems.EmsAddHostResponse;
import com.asiainfo.lcims.omc.model.ems.EmsAddhostResult;
import com.asiainfo.lcims.omc.model.ems.EmsDelHost;
import com.asiainfo.lcims.omc.model.ems.EmsDelHostRequest;
import com.asiainfo.lcims.omc.model.ems.EmsDelHostResponse;
import com.asiainfo.lcims.omc.model.ems.EmsMetric;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.HostMetricDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdAlarmRuleDetailDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdBusinessHostDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MonHostDAO;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.AlarmRuleManageService;
import com.asiainfo.lcims.omc.socket.SimpleChatClient;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.EmsOmcUtil;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

/**
 * ems to omc 处理接口
 * @author zhul
 *
 */
@Service(value = "emsOmcService")
public class EmsOmcService {
    private static final Logger log = LoggerFactory.getLogger(EmsOmcService.class);
    @Inject
    private MonHostDAO monHostDAO;
    
    @Inject
    private HostMetricDAO hostMetricDAO;
    
    @Inject
    private MdBusinessHostDAO businessHostDAO;
    
    @Inject
    private MdAlarmRuleDetailDAO mdAlarmRuleDetailDAO;
    
    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;
    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;
    
    @Resource(name = "commoninit")
    CommonInit commoninit;
    
    /**
     * 新增主机处理
     * @param addHostRequest
     * @return
     */
    public EmsAddHostResponse emsAddHost(EmsAddHostRequest addHostRequest) {
        EmsAddHostResponse response = new EmsAddHostResponse();
        //数据校验
        if(ToolsUtils.StringIsNull(addHostRequest.getUuid())) {
            log.info("emsAddHost uuid:"+addHostRequest.getUuid()+" emsAddHost uuid is null");
            return response;
        }
        response.setUuid(addHostRequest.getUuid());
        if(ToolsUtils.ListIsNull(addHostRequest.getHostinfos())) {
            log.info("emsAddHost uuid:"+addHostRequest.getUuid()+" hostinfos is null");
            return response;
        }
        // 主机表、主机指标配置、页面展示、告警规则
        List<EmsAddHost> hostinfos = addHostRequest.getHostinfos();
        List<String> ipList = new ArrayList<String>();
        for (EmsAddHost emsAddHost : hostinfos) {
            String ipReturn = processEmsHost(emsAddHost, response);
            if(!ToolsUtils.StringIsNull(ipReturn))
                ipList.add(ipReturn);
            else
                log.info("vmid:["+emsAddHost.getVmid()+" not find host.]");
        }
        log.info("emsAddHost:绑定调用下发接口----start publish interface");
        SimpleChatClient.sendMsg(ipList);
        return response;
    }
    
    private String processEmsHost(EmsAddHost emsAddHost, EmsAddHostResponse response) {
        EmsAddhostResult hostResult = new EmsAddhostResult(emsAddHost.getVmid(), EmsOmcUtil.SUCCESS_STATE,EmsOmcUtil.SUCCESS_DESC);
        MonHost host = null;
        String ipReturn = null;
        String hostId = null;
        try {
            // 主机表操作
            host = addHost(emsAddHost);
            hostId = host.getHostid();
            ipReturn = host.getAddr();
            commoninit.refreshMdHost();
            //主机指标配置
            String desc= addHostMetric(emsAddHost, hostId);
            if(!ToolsUtils.StringIsNull(desc)) {
                hostResult.setDesc(desc);
            }
            //页面展示
            addBusinessHost(emsAddHost, hostId);
        } catch (Exception e) {
            hostResult.setResult(EmsOmcUtil.FAIL_STATE);
            hostResult.setDesc(EmsOmcUtil.FAIL_DESC);
            //删除
            if(!ToolsUtils.StringIsNull(hostId)) {
                hostMetricDAO.deleteHostMetricByHostId(hostId);
                businessHostDAO.deleteByHostId(hostId);
                mdAlarmRuleDetailDAO.deleteByDimension(hostId);
            }
            log.error("processEmsHost:"+emsAddHost.getVmid(), e);
        }
        response.getResults().add(hostResult);
        return ipReturn;
    }
    
    private MonHost addHost(EmsAddHost emsAddHost) {
        MonHost host = CommonInit.getHostByIps(emsAddHost.getHostips());
        if(null == host) {
            // insert
            host = new MonHost();
            String hostId = IDGenerateUtil.getUuid();
            host.setHostid(hostId);
            host.setHostname(emsAddHost.getHostname());
            host.setHosttype(emsAddHost.getHosttype()+"");
            host.setNodeid("0");
            host.setStatus(1);
            host.setAddr(emsAddHost.getHostips().split(Constant.IP_SPIT)[0].trim());
            host.setIps(emsAddHost.getHostips().trim());
            host.setVmid(emsAddHost.getVmid());
            monHostDAO.insertEmsHost(host);
        } else {
            // update
            host.setHostname(emsAddHost.getHostname());
            host.setHosttype(emsAddHost.getHosttype()+"");
            host.setIps(emsAddHost.getHostips());
            host.setVmid(emsAddHost.getVmid());
            monHostDAO.updateEmsHost(host);
        }
        JSONObject hostJson = JSONObject.parseObject(JSON.toJSONString(host));
        log.info("addHost: insert or update hostInfo:"+hostJson.toString());
        return host;
    }
    
    private String addHostMetric(EmsAddHost emsAddHost, String hostId) {
        List<EmsMetric> metricData = emsAddHost.getMetricData();
        String desc = "";
        if(ToolsUtils.ListIsNull(metricData)) {
            log.info("vmid=" + emsAddHost.getVmid() + "{metricData is null}");
            desc = "metricData is null";
            return desc;
        }
        commoninit.loadMetricInfos();
        List<MdHostMetric> hostMetrics = new ArrayList<MdHostMetric>();
        // 先删除
        hostMetricDAO.deleteHostMetricByHostId(hostId);
        String noMetric = "";
        for (EmsMetric emsMetric : metricData) {
            MdMetric metric = CommonInit.getMetricByIdentity(emsMetric.getMetricIdnntity());
            if(null!=metric) {
                MdHostMetric hostMetric = new MdHostMetric();
                hostMetric.setId(IDGenerateUtil.getUuid());
                hostMetric.setHost_id(hostId);
                hostMetric.setMetric_id(metric.getId());
                hostMetric.setCycle_id(metric.getCycle_id());
                hostMetric.setScript(emsMetric.getShell());
                hostMetric.setScript_param(emsMetric.getParams());
                hostMetric.setScript_return_type(emsMetric.getReturnType());
                hostMetric.setState(0);
                hostMetrics.add(hostMetric);
            } else {
                noMetric = noMetric + emsMetric.getMetricIdnntity() + ",";
            }
        }
        if(!ToolsUtils.StringIsNull(noMetric)) {
            noMetric = noMetric.substring(0, noMetric.length()-1);
            desc = "metric:[" + noMetric+ "] is not find";
            log.info(desc);
        }
        
        if(!ToolsUtils.ListIsNull(hostMetrics))
            hostMetricDAO.insertHostMetricList(hostMetrics);
        return desc;
    }
    
    private void addBusinessHost(EmsAddHost emsAddHost, String hostId) {
        String hostPages = emsAddHost.getHostPages();
        if(ToolsUtils.StringIsNull(hostPages)) {
            log.info("vmid=" + emsAddHost.getVmid() + "{hostPages is null}");
            return;
        }
        //先删除
        businessHostDAO.deleteByHostId(hostId);
        // 删除对应告警规则
        mdAlarmRuleDetailDAO.deleteByDimension(hostId);
        
        String[] pages = hostPages.split(Constant.IP_SPIT);
        List<MdBusinessHost> businessHosts = new ArrayList<MdBusinessHost>();
        for (String page : pages) {
            MdBusinessHost bh = new MdBusinessHost();
            bh.setId(IDGenerateUtil.getUuid());
            bh.setName(page);
            bh.setHostid(hostId);
            businessHosts.add(bh);
        }
        if(DbSqlUtil.isOracle()) {
            businessHostDAO.addBattchBusinessHostForOracle(businessHosts, DbSqlUtil.getSqlQuery());
        } else {
            businessHostDAO.addBattchBusinessHostForMysql(businessHosts, DbSqlUtil.getSqlQuery());
        }
        
        /** 重构方法变更 */
        commoninit.loadMdBusinessHostList();
        
        //添加告警规则
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("operationType", Constant.DYNAMICTYPE_BUSINESSHOST);
        map.put("dynamicType", Constant.OPERATIONTYPE_ADD);
        map.put("operationId", hostId);
        alarmRuleManageService.dimensionModify(map);
    }
    
    /**
     * 删除主机
     * @param delHostRequest
     * @return
     */
    public EmsDelHostResponse emsDelHost(EmsDelHostRequest delHostRequest) {
        EmsDelHostResponse response = 
                new EmsDelHostResponse(delHostRequest.getUuid(), EmsOmcUtil.SUCCESS_STATE, EmsOmcUtil.SUCCESS_DESC);
        // 删除主机服务器、主机指标配置信息、页面配置、告警规则
        StringBuffer strb = new StringBuffer("");
        try {
            List<EmsDelHost> vmidinfos = delHostRequest.getVmidinfos();
            for (EmsDelHost emsDelHost : vmidinfos) {
                //根据 vmid 获取 hostId
                MonHost host = CommonInit.getHostByVmid(emsDelHost.getVmid());
                if(null!=host) {
                    strb.append("'").append(host.getHostid()).append("'").append(",");
                } else {
                    log.info("vimd:["+emsDelHost.getVmid()+"] find hostinfo is null!");
                }
            }
            String hostIds = strb.toString();
            log.info("hostIds is ["+hostIds+"]");
            if(!ToolsUtils.StringIsNull(hostIds)) {
                hostIds = hostIds.substring(0, hostIds.length()-1);
                //删除操作
                monHostDAO.deleteByHostIds(hostIds);
                hostMetricDAO.deleteHostMetricByHostIds(hostIds);
                businessHostDAO.deleteByHostIds(hostIds);
                mdAlarmRuleDetailDAO.deleteByDimensions(hostIds);
            }
            commoninit.refreshMdHost();
            commoninit.loadMdBusinessHostList();
        } catch (Exception e) {
            response.setResult(EmsOmcUtil.FAIL_STATE);
            response.setDesc(EmsOmcUtil.FAIL_DESC);
            log.error("emsDelHost error:",e);
        }
        return response;
    }
    
}
