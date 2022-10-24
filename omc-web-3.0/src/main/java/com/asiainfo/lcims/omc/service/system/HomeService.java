package com.asiainfo.lcims.omc.service.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.asiainfo.lcims.omc.service.report.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.model.monitor.ChartInfo;
import com.asiainfo.lcims.omc.model.serverlist.HomeAlarmBean;
import com.asiainfo.lcims.omc.model.serverlist.ServerlistBean;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.alarm.MdAlarmInfoDao;
import com.asiainfo.lcims.omc.persistence.configmanage.MonHostDAO;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.persistence.system.HomeDao;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.NumCount;

@Service(value = "homeService")
public class HomeService {
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private static String radiusProcessKeys = CommonInit.BUSCONF.getRadiusProcessKeys();
    private static String authenSuccessRateMetricId = CommonInit.BUSCONF
            .getAuthenSuccessRateMetricId();
    private static String authenMetricId = CommonInit.BUSCONF.getAuthenMetricId();
    private static String authenFailMetricId = CommonInit.BUSCONF.getAuthenFailMetricId();
    private static String authenSuccessMetricId = CommonInit.BUSCONF.getAuthenSuccessMetricId();
    private static String chargingRequestsMetricId = CommonInit.BUSCONF
            .getChargingRequestsMetricId();
    private static String authenRequestsMetricId = CommonInit.BUSCONF.getAuthenRequestsMetricId();
    private static String diskBusyRateMetricId = CommonInit.BUSCONF.getDiskBusyRateMetricId();
    private static String cpuOccupancyrateMetricId = CommonInit.BUSCONF
            .getCpuOccupancyrateMetricId();
    private static String memoryOccupancyrateMetricId = CommonInit.BUSCONF
            .getMemoryOccupancyrateMetricId();
    private static String sysNetConnectableMetricId = CommonInit.BUSCONF
            .getSysNetConnectableMetricId();

    @Inject
    private HomeDao homeDao;

    @Inject
    private MdAlarmInfoDao mdAlarmInfoDao;

    @Inject
    private MonHostDAO monHostDAO;

    /**
     * 
     * @Title: getRadiusProcess
     * @Description: TODO(radius进程状态)
     * @param @return 参数
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    public Map<String, Object> getRadiusProcess() {
        Map<String, Object> result = new HashMap<String, Object>();
        int processNormalNum = 0;
        int processAbnormalNum = 0;
        List<Map<String, Object>> radiusProcessData = new ArrayList<Map<String, Object>>();
        String tableName = Constant.HOST_CAP_TABLE_PREFIX + DateUtil.getFormatTime(0, "MM_dd");
        String stime = DateUtil.getFormatTimeIntegration();
        List<Map<String, Object>> metricDataSingleList = homeDao.getRadiusProcess(
                radiusProcessKeys, tableName, stime);
        for (Map<String, Object> metricDataSingle : metricDataSingleList) {
            String processMessage = "";
            String processState = "";
            String hostId = metricDataSingle.get("HOST_ID").toString();
            String metricId = metricDataSingle.get("METRIC_ID").toString();
            String radiusProcessKey = metricDataSingle.get("ITEM").toString();
            List<MdAlarmInfo> mdAlarmInfoList = mdAlarmInfoDao.getAlarmListByRadius(metricId,
                    radiusProcessKey, hostId);
            if (mdAlarmInfoList != null && mdAlarmInfoList.size() > 0) {
                processMessage = radiusProcessKey + " : 异常。 ";
                processState = "false";
            } else {
                processMessage = radiusProcessKey + " : 正常。 ";
                processState = "true";
            }
            boolean isNotExis = true;
            for (Map<String, Object> map : radiusProcessData) {
                if (map.get("HOST_ID").toString().equals(hostId)) {
                    isNotExis = false;
                    String preProcessMessage = map.get("processMessage").toString();
                    String preProcessState = map.get("processState").toString();
                    processMessage = processMessage + preProcessMessage;
                    map.put("processMessage", processMessage);
                    if (preProcessState.equals("true") && processState.equals("true")) {
                        map.put("processState", "true");
                    } else {
                        map.put("processState", "false");
                        if (preProcessState.equals("true")) {
                            processNormalNum--;
                            processAbnormalNum++;
                        }
                    }
                }
            }
            if (isNotExis) {
                metricDataSingle.put("processMessage", processMessage);
                metricDataSingle.put("processState", processState);
                radiusProcessData.add(metricDataSingle);
                if (processState.equals("true")) {
                    processNormalNum++;
                } else {
                    processAbnormalNum++;
                }
            }
        }
        result.put("radiusProcessData", radiusProcessData);
        List<ChartInfo> chartInfoList = new ArrayList<ChartInfo>();
        ChartInfo chartInfo = new ChartInfo();
        chartInfo.setTitle("radius进程状态");
        chartInfo.setLegend("radius进程状态");
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> normalDataMap = new HashMap<String, Object>();
        normalDataMap.put("mark", "radius进程状态正常");
        normalDataMap.put("value", processNormalNum);
        data.add(normalDataMap);
        Map<String, Object> abnormalDataMap = new HashMap<String, Object>();
        abnormalDataMap.put("mark", "radius进程状态异常");
        abnormalDataMap.put("value", processAbnormalNum);
        data.add(abnormalDataMap);
        chartInfo.setData(data);
        chartInfo.setGroup(String.valueOf(processNormalNum + processAbnormalNum));
        chartInfoList.add(chartInfo);
        result.put("chartInfoList", chartInfoList);
        return result;
    }

    /**
     * 
     * @Title: getRadiusAnalytic
     * @Description: TODO(radius解析量)
     * @param @return 参数
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    public Map<String, Object> getRadiusAnalytic() {
        Map<String, Object> result = new HashMap<String, Object>();
        String tableName = Constant.METRIC_DATA_MULTI + DateUtil.getFormatTime(0, "MM_dd");
        String hostField = Constant.METRIC_DATA_MULTI_HOST_ID;
        String attr1Field = Constant.STATIS_DATA_DAY_ATTR1;
        List<MonHost> monHostList = monHostDAO.getHostByHostType(Constant.HOST_HOST_TYPE_RADIUS);
        String hostIds = "";
        for (MonHost monHost : monHostList) {
            hostIds = hostIds + "," + monHost.getHostid();
        }
        if (hostIds.length() > 0) {
            hostIds = hostIds.replace(",", "','").substring(2) + "'";
        } else {
            hostIds = "''";
        }
        // 认证总量
        List<Map<String, Object>> authenList = homeDao.getRadiusAnalytic(authenMetricId, tableName,
                hostIds, hostField);
        List<Map<String, Object>> authenFailList = homeDao.getRadiusAnalytic(authenFailMetricId,
                tableName, hostIds, hostField);
        List<Map<String, Object>> authenSuccessList = homeDao.getRadiusAnalytic(
                authenSuccessMetricId, tableName, hostIds, hostField);
        List<ChartInfo> authenChartInfoList = new ArrayList<ChartInfo>();
        ChartInfo authenChartInfo = new ChartInfo();
        authenChartInfo.setTitle("认证总量");
        authenChartInfo.setLegend("认证总量");
        authenChartInfo.setData(authenList);
        authenChartInfoList.add(authenChartInfo);
        ChartInfo authenSuccessChartInfo = new ChartInfo();
        authenSuccessChartInfo.setLegend("认证成功量");
        authenSuccessChartInfo.setData(authenSuccessList);
        authenChartInfoList.add(authenSuccessChartInfo);
        ChartInfo authenFailChartInfo = new ChartInfo();
        authenFailChartInfo.setLegend("认证失败量");
        authenFailChartInfo.setData(authenFailList);
        authenChartInfoList.add(authenFailChartInfo);
        result.put("authenChartInfoList", authenChartInfoList);
        // 请求包数
        List<Map<String, Object>> chargingRequestsList = homeDao.getRadiusAnalytic(
                chargingRequestsMetricId, tableName, hostIds, hostField);
        List<Map<String, Object>> authenRequestsList = homeDao.getRadiusAnalytic(
                authenRequestsMetricId, tableName, hostIds, hostField);
        List<ChartInfo> requestsChartInfoList = new ArrayList<ChartInfo>();
        ChartInfo authenRequestsChartInfo = new ChartInfo();
        authenRequestsChartInfo.setTitle("请求包数");
        authenRequestsChartInfo.setLegend("认证请求数");
        authenRequestsChartInfo.setData(authenRequestsList);
        requestsChartInfoList.add(authenRequestsChartInfo);
        ChartInfo chargingRequestsChartInfo = new ChartInfo();
        chargingRequestsChartInfo.setLegend("计费请求总量");
        chargingRequestsChartInfo.setData(chargingRequestsList);
        requestsChartInfoList.add(chargingRequestsChartInfo);
        result.put("requestsChartInfoList", requestsChartInfoList);
        // 认证成功率
        tableName = Constant.STATIS_DATA_DAY + DateUtil.getFormatTime(0, "MM_dd");
        List<Map<String, Object>> authenSuccessRateList = homeDao.getRadiusAnalytic(
                authenSuccessRateMetricId, tableName, hostIds, attr1Field);
        List<ChartInfo> authenSuccessRateChartInfoList = new ArrayList<ChartInfo>();
        ChartInfo authenSuccessRateChartInfo = new ChartInfo();
        authenSuccessRateChartInfo.setTitle("认证成功率");
        authenSuccessRateChartInfo.setLegend("认证成功率");
        authenSuccessRateChartInfo.setData(authenSuccessRateList);
        authenSuccessRateChartInfoList.add(authenSuccessRateChartInfo);
        result.put("authenSuccessRateChartInfoList", authenSuccessRateChartInfoList);
        return result;
    }

    /**
     * 
     * @Title: getHostNetConnectable
     * @Description: TODO(获取主机连通性数据)
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public Map<String, Object> getHostNetConnectable() {
        Map<String, Object> result = new HashMap<String, Object>();
        int connectableNormalNum = 0;
        int connectableAbnormalNum = 0;
        String tableName = Constant.METRIC_DATA_MULTI + DateUtil.getFormatTime(0, "MM_dd");
        String stime = DateUtil.getFormatTimeIntegration();
        List<Map<String, Object>> hostNetConnectableList = homeDao.getHostNetConnectable(
                sysNetConnectableMetricId, tableName, stime);
        // 如果没数据则获取下一个周期的值
        if (hostNetConnectableList == null || hostNetConnectableList.size() == 0) {
            Map<String, Object> nextCycleMap = getNextCycle(stime, Constant.METRIC_DATA_MULTI);
            String nextcycletableName = nextCycleMap.get("tableName").toString();
            String nextcyclestime = nextCycleMap.get("stime").toString();
            hostNetConnectableList = homeDao.getHostNetConnectable(sysNetConnectableMetricId,
                    nextcycletableName, nextcyclestime);
        }
        for (Map<String, Object> hostNetConnectable : hostNetConnectableList) {
            String message = "";
            String mvalue = hostNetConnectable.get("MVALUE").toString();
            String addr = hostNetConnectable.get("ATTR1").toString();
            if (mvalue.equals(Constant.HOST_NET_CONNECTABLE_1)) {
                message = addr + " : 联通。 ";
                connectableNormalNum++;
            } else {
                message = addr + " : 不通。 ";
                connectableAbnormalNum++;
            }
            hostNetConnectable.put("message", message);
        }
        result.put("hostNetConnectableList", hostNetConnectableList);
        List<ChartInfo> chartInfoList = new ArrayList<ChartInfo>();
        ChartInfo chartInfo = new ChartInfo();
        chartInfo.setTitle("主机连通性");
        chartInfo.setLegend("主机连通性");
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> normalDataMap = new HashMap<String, Object>();
        normalDataMap.put("mark", "联通主机");
        normalDataMap.put("value", connectableNormalNum);
        data.add(normalDataMap);
        Map<String, Object> abnormalDataMap = new HashMap<String, Object>();
        abnormalDataMap.put("mark", "不通主机");
        abnormalDataMap.put("value", connectableAbnormalNum);
        data.add(abnormalDataMap);
        chartInfo.setData(data);
        chartInfoList.add(chartInfo);
        result.put("chartInfoList", chartInfoList);
        return result;
    }

    /**
     * 
     * @Title: getHostState
     * @Description: TODO(主机状态)
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public Map<String, Object> getHostState() {
        Map<String, Object> result = new HashMap<String, Object>();
        int cpuOccupancyrateAbnormalNum = 0;
        int memoryOccupancyrateAbnormalNum = 0;
        int diskBusyRateChartAbnormalNum = 0;
        int cpuOccupancyrateNormalNum = 0;
        int memoryOccupancyrateNormalNum = 0;
        int diskBusyRateChartNormalNum = 0;
        String tableName = Constant.HOST_CAP_TABLE_PREFIX + DateUtil.getFormatTime(0, "MM_dd");
        String stime = DateUtil.getFormatTimeIntegration();
        Map<String, Object> nextCycleMap = getNextCycle(stime, Constant.HOST_CAP_TABLE_PREFIX);
        String nextcycletableName = nextCycleMap.get("tableName").toString();
        String nextcyclestime = nextCycleMap.get("stime").toString();
        // cpu占用率
        List<Map<String, Object>> cpuOccupancyrateList = homeDao.getHostState(
                cpuOccupancyrateMetricId, tableName, stime);
        // 如果没数据则获取下一个周期的值
        if (cpuOccupancyrateList == null || cpuOccupancyrateList.size() == 0) {
            cpuOccupancyrateList = homeDao.getHostState(cpuOccupancyrateMetricId,
                    nextcycletableName, nextcyclestime);
        }
        // 内存占用率
        List<Map<String, Object>> memoryOccupancyrateList = homeDao.getHostState(
                memoryOccupancyrateMetricId, tableName, stime);
        // 如果没数据则获取下一个周期的值
        if (memoryOccupancyrateList == null || memoryOccupancyrateList.size() == 0) {
            memoryOccupancyrateList = homeDao.getHostState(memoryOccupancyrateMetricId,
                    nextcycletableName, nextcyclestime);
        }
        // 磁盘利用率
        List<Map<String, Object>> diskBusyRateList = homeDao.getHostState(diskBusyRateMetricId,
                tableName, stime);
        // 如果没数据则获取下一个周期的值
        if (diskBusyRateList == null || diskBusyRateList.size() == 0) {
            diskBusyRateList = homeDao.getHostState(diskBusyRateMetricId, nextcycletableName,
                    nextcyclestime);
        }
        List<Map<String, Object>> hostStateAllHostList = new ArrayList<Map<String, Object>>();
        setHostStateAllHostList(hostStateAllHostList, cpuOccupancyrateList, "cpuOccupancyrateValue");
        setHostStateAllHostList(hostStateAllHostList, memoryOccupancyrateList,
                "memoryOccupancyrateValue");
        setHostStateAllHostList(hostStateAllHostList, diskBusyRateList, "diskBusyRateValue");
        for (Map<String, Object> map : hostStateAllHostList) {
            String hostId = map.get("HOST_ID").toString();
            List<MdAlarmInfo> cpuOccupancyrateAlarmInfoList = mdAlarmInfoDao
                    .getAlarmListByHostState(cpuOccupancyrateMetricId, hostId);
            List<MdAlarmInfo> memoryOccupancyrateAlarmInfoList = mdAlarmInfoDao
                    .getAlarmListByHostState(memoryOccupancyrateMetricId, hostId);
            List<MdAlarmInfo> diskBusyRateAlarmInfoList = mdAlarmInfoDao.getAlarmListByHostState(
                    diskBusyRateMetricId, hostId);
            String message = "";
            if (cpuOccupancyrateAlarmInfoList != null && cpuOccupancyrateAlarmInfoList.size() > 0) {
                message = message + "cpu占用率 : 异常。 ";
                cpuOccupancyrateAbnormalNum++;
            } else {
                message = message + "cpu占用率 : 正常。 ";
                cpuOccupancyrateNormalNum++;
            }
            if (memoryOccupancyrateAlarmInfoList != null
                    && memoryOccupancyrateAlarmInfoList.size() > 0) {
                message = message + "内存占用率 : 异常。 ";
                memoryOccupancyrateAbnormalNum++;
            } else {
                message = message + "内存占用率 : 正常。 ";
                memoryOccupancyrateNormalNum++;
            }
            if (diskBusyRateAlarmInfoList != null && diskBusyRateAlarmInfoList.size() > 0) {
                message = message + "磁盘利用率 : 异常。 ";
                diskBusyRateChartAbnormalNum++;
            } else {
                message = message + "磁盘利用率 : 正常。 ";
                diskBusyRateChartNormalNum++;
            }
            map.put("message", message);
        }
        result.put("hostStateAllHostList", hostStateAllHostList);
        List<ChartInfo> chartInfoList = new ArrayList<ChartInfo>();
        ChartInfo normalchartInfo = new ChartInfo();
        normalchartInfo.setTitle("主机状态");
        normalchartInfo.setLegend("正常");
        List<Map<String, Object>> normaldata = new ArrayList<Map<String, Object>>();
        Map<String, Object> cpuOccupancyrateNormalDataMap = new HashMap<String, Object>();
        cpuOccupancyrateNormalDataMap.put("mark", "cpu");
        cpuOccupancyrateNormalDataMap.put(
                "value",
                NumCount.compared(cpuOccupancyrateNormalNum, cpuOccupancyrateAbnormalNum
                        + cpuOccupancyrateNormalNum));
        normaldata.add(cpuOccupancyrateNormalDataMap);
        Map<String, Object> memoryOccupancyrateNormalDataMap = new HashMap<String, Object>();
        memoryOccupancyrateNormalDataMap.put("mark", "内存");
        memoryOccupancyrateNormalDataMap.put(
                "value",
                NumCount.compared(memoryOccupancyrateNormalNum, memoryOccupancyrateAbnormalNum
                        + memoryOccupancyrateNormalNum));
        normaldata.add(memoryOccupancyrateNormalDataMap);
        Map<String, Object> diskBusyRateChartNormalDataMap = new HashMap<String, Object>();
        diskBusyRateChartNormalDataMap.put("mark", "磁盘");
        diskBusyRateChartNormalDataMap.put(
                "value",
                NumCount.compared(diskBusyRateChartNormalNum, diskBusyRateChartAbnormalNum
                        + diskBusyRateChartNormalNum));
        normaldata.add(diskBusyRateChartNormalDataMap);
        normalchartInfo.setData(normaldata);
        chartInfoList.add(normalchartInfo);
        ChartInfo abnormalchartInfo = new ChartInfo();
        abnormalchartInfo.setTitle("主机状态");
        abnormalchartInfo.setLegend("过载");
        List<Map<String, Object>> abnormaldata = new ArrayList<Map<String, Object>>();
        Map<String, Object> cpuOccupancyrateAbnormalDataMap = new HashMap<String, Object>();
        cpuOccupancyrateAbnormalDataMap.put("mark", "cpu");
        cpuOccupancyrateAbnormalDataMap.put(
                "value",
                NumCount.compared(cpuOccupancyrateAbnormalNum, cpuOccupancyrateAbnormalNum
                        + cpuOccupancyrateNormalNum));
        abnormaldata.add(cpuOccupancyrateAbnormalDataMap);
        Map<String, Object> memoryOccupancyrateAbnormalDataMap = new HashMap<String, Object>();
        memoryOccupancyrateAbnormalDataMap.put("mark", "内存");
        memoryOccupancyrateAbnormalDataMap.put(
                "value",
                NumCount.compared(memoryOccupancyrateAbnormalNum, memoryOccupancyrateAbnormalNum
                        + memoryOccupancyrateNormalNum));
        abnormaldata.add(memoryOccupancyrateAbnormalDataMap);
        Map<String, Object> diskBusyRateChartAbnormalDataMap = new HashMap<String, Object>();
        diskBusyRateChartAbnormalDataMap.put("mark", "磁盘");
        diskBusyRateChartAbnormalDataMap.put(
                "value",
                NumCount.compared(diskBusyRateChartAbnormalNum, diskBusyRateChartAbnormalNum
                        + diskBusyRateChartNormalNum));
        abnormaldata.add(diskBusyRateChartAbnormalDataMap);
        abnormalchartInfo.setData(abnormaldata);
        chartInfoList.add(abnormalchartInfo);
        result.put("chartInfoList", chartInfoList);
        return result;
    }

    /**
     * 
     * @Title: setHostStateAllHostList
     * @Description: TODO(赋值所有主机状态)
     * @param @param hostStateAllHostList
     * @param @param list
     * @param @param valueName 参数
     * @return void 返回类型
     * @throws
     */
    public void setHostStateAllHostList(List<Map<String, Object>> hostStateAllHostList,
            List<Map<String, Object>> list, String valueName) {
        for (Map<String, Object> map : list) {
            String hostId = map.get("HOST_ID").toString();
            boolean isNotExis = true;
            for (Map<String, Object> hostMap : hostStateAllHostList) {
                if (hostMap.get("HOST_ID").toString().equals(hostId)) {
                    isNotExis = false;
                    hostMap.put(valueName, map.get("MVALUE").toString());
                }
            }
            if (isNotExis) {
                Map<String, Object> hostMap = new HashMap<String, Object>();
                hostMap.put("HOST_ID", map.get("HOST_ID").toString());
                hostMap.put("HOSTNAME", map.get("HOSTNAME").toString());
                hostMap.put("ADDR", map.get("ADDR").toString());
                hostMap.put(valueName, map.get("MVALUE").toString());
                hostStateAllHostList.add(hostMap);
            }
        }
    }

    /**
     * 
     * @Title: getNextCycle
     * @Description: TODO(获取下个周期)
     * @param @param stimepre
     * @param @param tableprefix
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public Map<String, Object> getNextCycle(String stimepre, String tableprefix) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfd = new SimpleDateFormat("MM_dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(stimepre));
        } catch (ParseException e) {
            logger.error("ParseException:", e);
        }
        calendar.add(Calendar.MINUTE, -5);
        Date date = calendar.getTime();
        String formatDate = sdfd.format(date);
        String tableName = tableprefix + formatDate;
        String stime = sdf.format(calendar.getTime());
        returnMap.put("tableName", tableName);
        returnMap.put("stime", stime);
        return returnMap;
    }
    
    public String queryHomeAuthentication() {
    	 String map = null;
    	 SimpleDateFormat sdfd = new SimpleDateFormat("MM_dd");
         try {
             String formatDate = sdfd.format(new Date());
             map = homeDao.queryHomeAuthentication(formatDate);
         } catch (Exception e) {
             logger.error("Exception:", e);
         }
         return map;
    }
    
    public List<ServerlistBean>  queryServerList(){
    	List<ServerlistBean> serverlist=null;
    	try {
    		serverlist=homeDao.queryServerList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception:", e);
		}
    	return serverlist;
    }
    
    public List<ServerlistBean>  queryServerHostList(){
    	List<ServerlistBean> serverlist=null;
    	try {
    		serverlist=homeDao.queryServerHostList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception:", e);
		}
    	return serverlist;
    }
    
    
    public List<HomeAlarmBean> queryHomeAlarm(List<String> urlList){
    	List<HomeAlarmBean> alarmlist=null;
    	try {
    		alarmlist=homeDao.queryHomeAlarm(urlList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception:", e);
		}
    	return alarmlist;
    }
    
    public List<HomeAlarmBean> homeAlarmQueryById(String alarmId,List<String> urlList){
    	List<HomeAlarmBean> alarmlist=null;
    	try {
    		alarmlist=homeDao.homeAlarmQueryById(alarmId,urlList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception:", e);
		}
    	return alarmlist;
    }
    
    public List<HomeAlarmBean> queryAlarmByAlarmlevel(List<String> urlList){
    	List<HomeAlarmBean> alarmlist=null;
    	try {
    		alarmlist=homeDao.queryAlarmByAlarmlevel(urlList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception:", e);
		}
    	return alarmlist;
    }
    
}
