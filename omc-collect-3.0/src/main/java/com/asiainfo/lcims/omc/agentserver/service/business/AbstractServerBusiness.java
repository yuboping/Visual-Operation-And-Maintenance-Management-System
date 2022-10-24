package com.asiainfo.lcims.omc.agentserver.service.business;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.dao.MdHostMetricDao;
import com.asiainfo.lcims.omc.agentserver.dao.MdHostProcessDao;
import com.asiainfo.lcims.omc.agentserver.enity.MdHostMetric;
import com.asiainfo.lcims.omc.agentserver.enity.MdHostProcess;
import com.asiainfo.lcims.omc.agentserver.model.BaseData;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.MetricServer2Client;
import com.asiainfo.lcims.omc.agentserver.netty.CollectServer;
import com.asiainfo.lcims.omc.agentserver.service.DbDataCache;
import com.asiainfo.lcims.omc.common.OpType;
import com.asiainfo.lcims.omc.conf.InitParam;
import com.asiainfo.lcims.omc.utils.IDGenerateUtil;

import io.netty.channel.Channel;

/**
 * AgentServer业务处理
 * 
 * @author XHT
 *
 */
public abstract class AbstractServerBusiness<T extends BaseData> {
    private static final Logger log = LoggerFactory.make();

    protected String mark = IDGenerateUtil.getUuid();

    /**
     * 方法入口,开始进行对应的业务处理
     * 
     * @param mark:当前流程的mark
     * @param jsonInfo:请求的json格式数据
     * @param channel:当前请求的通道信息.
     */
    public void doBusiness(String mark, String jsonInfo, Channel channel) {
        try {
            // 将请求中的mark计入缓存,打印日志的时候将mark打印在第一列,方便多线程的时候跟踪日志.
            this.mark = mark;

            // 解析JSON数据
            BaseValue<T> req = this.parseObject(jsonInfo);

            // 如果请求数据不为空,则进行对应的业务处理
            if (req != null) {
                this.doAction(req.getInfo(), channel);
            } else {
                log.error("mark:[" + mark + "].infoList is null.please check the request msg.");
            }
        } catch (Exception e) {
            log.error("mark:[" + mark + "].business error:", e);
        }
    }

    /**
     * 具体业务操作
     * 
     * @param infoList:请求中的具体信息.
     * @param channel:当前请求的通道信息.
     */
    protected abstract void doAction(List<T> infoList, Channel channel);

    /**
     * 解析JSON
     * 
     * @param jsonInfo
     * @return
     */
    protected BaseValue<T> parseObject(String jsonInfo) {
        BaseValue<T> req = null;
        try {
            Type sType = getClass().getGenericSuperclass();
            Type[] generics = ((ParameterizedType) sType).getActualTypeArguments();
            Type tt = generics[0];
            req = JSON.parseObject(jsonInfo, new TypeReference<BaseValue<T>>(tt) {
            });
        } catch (Exception e) {
            log.error("mark:[" + mark + "].JSON.parseObject error:" + e);
        }
        return req;
    }

    // 向指定的客户端IP下发所有的指标
    protected void sendMetric2Client(String clientIp) {
        // 获取对应IP的所有指标
        String tmpJson = this.getMetricInfoByIp(clientIp);
        if (tmpJson == null) {
            log.error("mark:[" + mark + "].json info is:" + tmpJson);
            return;
        }
        // 向对应客户端主机下发指标
        CollectServer.sendInfo2Client(clientIp, tmpJson);
    }

    /**
     * 根据客户主机IP的获取对应主机IP最新的指标信息
     * 
     * @param ip
     * @return
     */
    private String getMetricInfoByIp(String clientIp) {
        BaseValue<MetricServer2Client> metricSend2Client = new BaseValue<MetricServer2Client>();

        // 根据客户端IP获取主机信息
        String hostId = DbDataCache.getHostIdByIp(clientIp, mark);
        log.info("hostid is : {}", hostId);
        if (hostId == null) {
            log.error("mark:[" + mark + "].host ip:{" + clientIp + "} does not have metric..");
            return null;
        }

        // 读取对应主机的指标
        List<MdHostMetric> tmpMetricList = (new MdHostMetricDao()).getMdHostMetricByHostId(hostId);
        log.info("host metric list is : {}", tmpMetricList);
        // 根据采集周期、采集脚本、脚本参数、脚本返回数据类型过滤重复信息
        tmpMetricList = this.filterMetric(tmpMetricList);

        // 合并进程指标和主机进程管理中的进程参数
        tmpMetricList = this.mkMetricByMetricType(tmpMetricList);

        // 把指标信息转化为用于下发的格式
        List<MetricServer2Client> metricJobList = this.transferMetric(tmpMetricList);
        log.info("metric server to client list is : {}", metricJobList);
        metricSend2Client.setInfo(metricJobList);
        metricSend2Client.setOptype(OpType.METRIC_AGENTSERVER_CLIENT.getType());
        metricSend2Client.setMark(mark);
        return JSON.toJSONString(metricSend2Client);
    }

    // 根据采集周期、采集脚本、脚本参数、脚本返回数据类型过滤重复信息
    private List<MdHostMetric> filterMetric(List<MdHostMetric> metricList) {
        Map<String, MdHostMetric> map = new HashMap<String, MdHostMetric>();
        List<MdHostMetric> mdMetricList = new ArrayList<MdHostMetric>();
        for (int i = 0; i < metricList.size(); i++) {
            MdHostMetric mdMetric = metricList.get(i);
            String script = mdMetric.getScript();
            String param = mdMetric.getScriptParam();
            String key = mdMetric.getCycleId() + "_" + script + "_" + param + "_"
                    + mdMetric.getScriptReturnType();
            MdHostMetric metric = map.get(key);
            if (metric == null) {
                map.put(key, mdMetric);
            }
        }
        Collection<MdHostMetric> values = map.values();
        mdMetricList.addAll(values);
        return mdMetricList;
    }

    /**
     * 把指标信息转化为相对应的格式
     * 
     * @param metricList
     * @return
     */
    private List<MetricServer2Client> transferMetric(List<MdHostMetric> metricList) {
        List<MetricServer2Client> mdMetricList = new ArrayList<MetricServer2Client>();
        for (MdHostMetric mdMetric : metricList) {
            MetricServer2Client metricJson = new MetricServer2Client();
            metricJson.setMetricid(mdMetric.getMetricId());
            metricJson.setShell(mdMetric.getScript());
            metricJson.setParams(mdMetric.getScriptParam());
            metricJson.setExpr(DbDataCache.getCronByCycleId(mdMetric.getCycleId(), mark));
            metricJson.setResulttype(String.valueOf(mdMetric.getScriptReturnType()));
            mdMetricList.add(metricJson);
        }
        return mdMetricList;
    }

    /**
     * 遍历指标，根据指标类型进行判断.如果是进程类指标，则将主机进程管理表(MD_HOST_PROCESS)中的进程参数合并进来.
     * <p>
     * 只会在有进程指标的情况下会进行合并动作。
     * 
     * @param list
     * @return
     */
    private List<MdHostMetric> mkMetricByMetricType(List<MdHostMetric> list) {
        for (MdHostMetric info : list) {
            if (InitParam.getProcessMetricType().equals(info.getMetricType())) {
                List<MdHostProcess> processList = (new MdHostProcessDao())
                        .getMdHostProcessByHostId(info.getHostId());
                String tmp = this.mergeProcessParam(info.getScriptParam(), processList);
                info.setScriptParam(tmp);
            }
        }
        return list;
    }

    // 将指标中的参数和进程list中的进程关键字合并(去重).
    private String mergeProcessParam(String metricParam, List<MdHostProcess> processList) {
        Set<String> paramSet = new HashSet<String>();
        if (metricParam != null) {
            String[] params = metricParam.split(InitParam.getProcessMetricSplitStr());
            for (String param : params) {
                paramSet.add(param);
            }
        }
        if (processList != null && !processList.isEmpty()) {
            for (MdHostProcess info : processList) {
                paramSet.add(info.getProcessKey());
            }
        }
        StringBuffer strb = new StringBuffer();
        Iterator<String> it = paramSet.iterator();
        while (it.hasNext()) {
            strb.append(it.next()).append(InitParam.getProcessMetricSplitStr());
        }
        if (strb.length() == 0) {
            return null;
        }
        strb.setLength(strb.length() - InitParam.getProcessMetricSplitStr().length());
        return strb.toString();
    }
}
