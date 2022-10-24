package com.asiainfo.lcims.omc.service.configmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdHostMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.HostMetricDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdMetricDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MonHostDAO;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.socket.SimpleChatClient;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import com.asiainfo.lcims.omc.util.page.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service(value = "hostMetricService")
public class HostMetricService {
    private static final Logger LOG = LoggerFactory.getLogger(HostMetricService.class);
    @Inject
    private HostMetricDAO hostMetricDAO;
    
    @Inject
    private MdMetricDAO metricDAO;
    
    @Inject
    private MonHostDAO hostDAO;
    
    @Resource(name = "operateHisService")
    OperateHisService operateHisService;
    /**
     * 根据查询条件获取主机指标关系数据
     * @param hostid
     * @param metricid
     * @return
     */
    public Page getMdMetricList(String hostid ,String metricid, int pageNumber) {
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        //查询总数
        int totalCount = hostMetricDAO.getHostMetricInfoCount(hostid, metricid);
        page.setTotalCount(totalCount);
        //查询分页数据
        if(totalCount>0){
            List<MdHostMetric> data = hostMetricDAO.getHostMetricInfo(hostid, metricid ,page);
            page.setPageList(data);
        }
        return page;
    }

    public MdHostMetric querySingleinfo(String id) {
        return hostMetricDAO.querySingleinfo(id);
    }

    public List<MdHostMetric> getMdMetricListByMetric(String metricid) {
        //获取指标信息
        MdMetric metric = new MdMetric();
        metric.setId(metricid);
        List<MdMetric> metriclist = metricDAO.getMdMetric(metric);
        return hostMetricDAO.getMdMetricListByMetric(metriclist.get(0));
    }

    public List<MdHostMetric> getMdMetricListByHost(String operateid) {
        //获取主机信息
        MonHost host = hostDAO.getHostById(operateid);
        return hostMetricDAO.getMdMetricListByHost(host);
    }

    public WebResult bindOperate(String datas) {
        //json转换实体类
        ObjectMapper mapper = new ObjectMapper();
        List<MdHostMetric> list = null;
        WebResult result = new  WebResult(true,"绑定操作成功");
        try {
            list = mapper.readValue(datas, new TypeReference<List<MdHostMetric>>() {});
            StringBuffer loginfo = new StringBuffer("");
            MonHost host = null;
            String metricName = null;
            Map<String, String> msgMap = new HashMap<String, String>();
            for (MdHostMetric mdHostMetric : list) {
                mdHostMetric.setId(IDGenerateUtil.getUuid());
                host = CommonInit.getHost(mdHostMetric.getHost_id());
                metricName = CommonInit.getMetricNameByMetricId(mdHostMetric.getMetric_id());
                addMsgMap(msgMap,host.getAddr(),metricName);
                hostMetricDAO.insertHostMetric(mdHostMetric);
            }
            loginfo = makeLogMsg(loginfo, msgMap);
            String logstr = loginfo.toString();
            //调用下发接口
            LOG.info("绑定调用下发接口----start publish interface");
            SimpleChatClient.sendMsg(filerHostIp(list));
            if(!ToolsUtils.StringIsNull(logstr)){
                logstr = logstr.substring(0, logstr.length()-1);
                logstr = "新增 "+logstr;
                LOG.info(logstr);
                operateHisService.insertOperateHistory(Constant.OPERATE_HIS_HOSTMETRICMANAGE_QUERY, logstr);
            }
            LOG.info("绑定调用下发接口----end: publish interface");
        } catch (Exception e) {
            LOG.error("绑定操作失败:" + e.getMessage());
            result.setOpSucc(false);
            result.setMessage("绑定操作失败");
        }
        return result;
    }
    
    public List<String> filerHostIp(List<MdHostMetric> list){
        List<String> iplist = new ArrayList<String>();
        for (MdHostMetric hostMetric : list) {
            String hostip = hostMetric.getAddr();
            if(!iplist.contains(hostip))
                iplist.add(hostip);
        }
        return iplist;
    }
    
    /**
     * 绑定操作
     * @param hostMetricids
     * @return
     */
    public WebResult updateHostMetricState(String hostMetricids, Integer state) {
        StringBuffer msg = new StringBuffer("");
        if(state.equals(ConstantUtill.HOST_METRIC_STATE_UNPUBLISH)){
            LOG.info("下发操作开始----unbindHostMetric start");
            msg.append("下发数据：");
        }else{
            LOG.info("解绑操作开始----unbindHostMetric start");
            msg.append("解绑数据：");
        }
        WebResult result = new  WebResult(true,"操作成功");
        //将hostMetricids 的数据状态更新为3待删除
        String [] hostMetricidArray = hostMetricids.split(",");
        try {
            hostMetricDAO.updateHostMetricState(hostMetricidArray, state);
            List<MdHostMetric> list = hostMetricDAO.getHostMetricInfoByIds(hostMetricidArray);
            //查询hostMetricids 的iplist信息，做过滤ip操作
            List<String> ipList = new ArrayList<String>();
            Map<String, String> msgMap = new HashMap<String, String>();
            for(int i =0; i<list.size();i++) {
                MdHostMetric hm = list.get(i);
                MonHost host = CommonInit.getHost(hm.getHost_id());
                //主机ip过滤操作
                if(!ipList.contains(host.getAddr())){
                    ipList.add(host.getAddr());
                }
                String metricName = CommonInit.getMetricNameByMetricId(hm.getMetric_id());
                addMsgMap(msgMap,host.getAddr(),metricName);
            }
            msg = makeLogMsg(msg, msgMap);
//            List<String> ipList = hostMetricDAO.getHostMetricIpList(hostMetricidArray);
            //调用下发接口
            LOG.info(msg.toString()+"调用下发接口----start");
            SimpleChatClient.sendMsg(ipList);
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_HOSTMETRICMANAGE_QUERY, msg.toString());
            LOG.info(msg.toString()+"调用下发接口----end:");
        } catch (Exception e) {
        	LOG.error(e.getMessage(), e);
        	result.setOpSucc(false);
        	result.setMessage("操作失败");
        }
        return result;
    }

    public WebResult modifyHostMetric(MdHostMetric newData) {
        LOG.info("修改操作----modifyHostMetric start");
        StringBuffer msg = new StringBuffer("修改数据");
        WebResult result = new  WebResult(true,"操作成功");
        //修改
        String [] hostMetricidArray = new String[1];
        hostMetricidArray[0] = newData.getId();
        List<MdHostMetric> list = hostMetricDAO.getHostMetricInfoByIds(hostMetricidArray);
        hostMetricDAO.modifyHostMetric(newData);
        //查询hostMetricids 的iplist信息，做过滤ip操作
        try {
        	List<String> ipList = new ArrayList<String>();
        	MdHostMetric oldData = list.get(0);
        	String addr = CommonInit.getHost(oldData.getHost_id()).getAddr();
        	ipList.add(addr);
        	String metricname = CommonInit.getMetricNameByMetricId(oldData.getMetric_id());
        	msg.append(" [主机: "+addr+",指标: "+metricname+"]");
        	if(!newData.getScript().equals(oldData.getScript())) {
        	    msg.append(" 脚本 [ "+oldData.getScript()+"-->"+newData.getScript()+" ]");
        	}
        	if(!newData.getScript_param().equals(oldData.getScript_param())) {
        	    msg.append(" 参数 [ "+oldData.getScript_param()+"-->"+newData.getScript_param()+" ]");
        	}
            //调用下发接口
            LOG.info("修改调用下发接口----modifyHostMetric start");
            SimpleChatClient.sendMsg(ipList);
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_HOSTMETRICMANAGE_QUERY, msg.toString());
            LOG.info(msg.toString());
            LOG.info("修改调用下发接口----modifyHostMetric end:");
        } catch (Exception e) {
        	LOG.error(e.getMessage(), e);
        	result.setOpSucc(false);
        	result.setMessage("操作失败");
        }
        return result;
    }
    
    public WebResult deleteHostMetric(String hostMetricids) {
        StringBuffer msg = new StringBuffer("删除数据：");
        WebResult result = new  WebResult(true,"操作成功");
        String [] hostMetricidArray = hostMetricids.split(",");
        List<MdHostMetric> list = hostMetricDAO.getHostMetricInfoByIds(hostMetricidArray);
        String undeletemsg = "";
        String deletemsg = "";
        Map<String, String> msgMap = new HashMap<String, String>();
        for (MdHostMetric mdHostMetric : list) {
            /**
             * 已下发状态不可删
             */
            if(mdHostMetric.getState().equals(ConstantUtill.HOST_METRIC_STATE_PUBLISH)){
                undeletemsg = "状态为已下发的数据不可删除,需先解绑.";
            }else{
                //未下发、待删除状态可删除
                deletemsg = "状态为未下发或待删除的数据删除成功.";
                String addr = CommonInit.getHost(mdHostMetric.getHost_id()).getAddr();
                String metricName = CommonInit.getMetricNameByMetricId(mdHostMetric.getMetric_id());
                hostMetricDAO.deleteHostMetric(mdHostMetric.getId());
                addMsgMap(msgMap,addr,metricName);
            }
        }
        msg = makeLogMsg(msg, msgMap);
        if(!ToolsUtils.StringIsNull(deletemsg)){
            String insertMsg = msg.toString();
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_HOSTMETRICMANAGE_QUERY, insertMsg);
        }
        result.setMessage(deletemsg + undeletemsg);
        return result;
    }
    
    private StringBuffer makeLogMsg(StringBuffer msg, Map<String, String> msgMap){
        if(msgMap.isEmpty())
            return msg;
        Iterator<Map.Entry<String, String>> it = msgMap.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if(i==0){
                msg.append("[主机: " + entry.getKey() +",指标: "+entry.getValue()+"]");
            }else{
                msg.append(",[主机: " + entry.getKey() +",指标: "+entry.getValue()+"]");
            }
            i++;
        }
        return msg;
    }
    
    private void addMsgMap(Map<String, String> msgMap,String hostip,String metricname){
        if(msgMap.containsKey(hostip)){
            String value = msgMap.get(hostip);
            value = value +";"+metricname;
            msgMap.put(hostip, value);
        }else{
            msgMap.put(hostip, metricname);
        }
    }
}
