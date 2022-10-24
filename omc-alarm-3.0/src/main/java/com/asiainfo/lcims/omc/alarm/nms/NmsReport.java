package com.asiainfo.lcims.omc.alarm.nms;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.business.AlarmService;
import com.asiainfo.lcims.omc.alarm.model.AlarmMode;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.model.MdMetric;
import com.asiainfo.lcims.omc.alarm.model.SendAddress;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.util.ByteUtil;
import com.asiainfo.lcims.util.DateTools;
import com.asiainfo.lcims.util.MetricUtil;
import com.asiainfo.lcims.util.ProviceUtill;
import com.asiainfo.lcims.util.ToolsUtils;

public class NmsReport {
    private static final Logger log = LoggerFactory.getLogger(NmsReport.class);
    private static DateTools dateTools = new DateTools("yyyy-MM-dd HH:mm:ss");
    /**
     * 上报操作
     * @param ruleDetail
     * @param alarmMode
     * @param currentTime
     * @param reportflag
     */
    public static void reportAlarm(MdAlarmRuleDetail ruleDetail, AlarmMode am,
            String currentTime, int alarmStatus, MdAlarmInfo alarmInfo){
        // 组装告警信息，返回json格式
        String jsonData = makeALarmInfoByProvince(ruleDetail, currentTime, alarmStatus, alarmInfo);
        log.info("nms report alarm json data : {}", jsonData);
        //连接socket 信息上报
        sendDataToSocket(jsonData,am);
    }
    
    private static String makeALarmInfoByProvince(MdAlarmRuleDetail ruleDetail, String currentTime,
            int alarmStatus, MdAlarmInfo alarmInfo){
        String jsonData = "";
        String province = AlarmService.conf.getProvince();
        switch (province) {
        case ProviceUtill.PROVINCE_GSCM:
            jsonData = makeAlarmInfoForGSCM(ruleDetail, currentTime, alarmStatus);
            break;
        case ProviceUtill.PROVINCE_HNCM:
            currentTime = currentTime + ":00";
            jsonData = makeAlarmInfoForHNCM(alarmInfo, currentTime, alarmStatus);
            break;
        case ProviceUtill.PROVINCE_QHCM:
            jsonData = makeAlarmInfoForQHCM(ruleDetail, currentTime, alarmStatus);
            break;
        default:
            jsonData = makeAlarmInfoForGSCM(ruleDetail, currentTime, alarmStatus);
            break;
        }
        return jsonData;
    }
    
    private static String makeAlarmInfoForHNCM(MdAlarmInfo alarmInfo, String currentTime,
            int alarmStatus){
        NmsALarmInfo nmsALarmInfo = new NmsALarmInfo();
        nmsALarmInfo.setAlarmSeq(alarmInfo.getAlarm_seq());
        nmsALarmInfo.setAlarmTitle(alarmInfo.getAlarmmsg());
        nmsALarmInfo.setAlarmStatus(alarmStatus);
        nmsALarmInfo.setAlarmType("认证系统");
        //填告警级数字
        nmsALarmInfo.setOrigSeverity(Integer.parseInt(alarmInfo.getAlarm_level()));
        nmsALarmInfo.setEventTime(currentTime);
        nmsALarmInfo.setAlarmId(alarmInfo.getAlarm_id());
        nmsALarmInfo.setSpecificProblemID(alarmInfo.getAttr());
        nmsALarmInfo.setSpecificProblem(alarmInfo.getMsg_desc());
        nmsALarmInfo.setNeUID(alarmInfo.getDimension1());
        nmsALarmInfo.setNeName(alarmInfo.getDimension1_name());
        /**
         * 根据指标来定义netType类型：olt告警就写OLT ，pon口告警就写PON 
         */
        nmsALarmInfo.setNeType(getNetypeHncm(alarmInfo.getMetric_id(),"ENB"));
        nmsALarmInfo.setObjectUID(alarmInfo.getDimension1());
        nmsALarmInfo.setObjectName(alarmInfo.getDimension1_name());
        nmsALarmInfo.setObjectType("EutranCellTdd");
        nmsALarmInfo.setLocationInfo("--");
        return nmsALarmInfo.toString();
    }
    
    private static String getNetypeHncm(String metricId, String defVal) {
        MdMetric metric = InitParam.getMetricById(metricId);
        if(null == metric)
            return defVal;
        if(MetricUtil.METRIC_OLT.equals(metric.getMetric_identity())) {
            return "OLT";
        } else if (MetricUtil.METRIC_PON.equals(metric.getMetric_identity())) {
            return "PON";
        }
        return defVal;
    }
    
    private static String makeAlarmInfoForGSCM(MdAlarmRuleDetail ruleDetail, String currentTime,
            int alarmStatus){
        NmsALarmInfo nmsALarmInfo = new NmsALarmInfo();
        nmsALarmInfo.setAlarmTitle(ruleDetail.getAlarmmsg());
        nmsALarmInfo.setAlarmStatus(alarmStatus);
        nmsALarmInfo.setAlarmType("性能告警");
        //填告警级别中文
        nmsALarmInfo.setOrigSeverity(Integer.parseInt(ruleDetail.getAlarm_level()));
        
        nmsALarmInfo.setEventTime(currentTime+":00");
        nmsALarmInfo.setAlarmId(ruleDetail.getAlarm_id());
        //nmsALarmInfo.setNeUID(getObjectUID(ruleDetail.getDimension1(), ruleDetail.getDimension2()));
        //nmsALarmInfo.setNeName(getNeName(ruleDetail.getDimension1_name(), ruleDetail.getDimension2_name()));
//        "neType ":"Radius",
        nmsALarmInfo.setNeType("Radius");
        nmsALarmInfo.setObjectUID(getObjectUID(ruleDetail.getDimension1(), ruleDetail.getDimension2()));
        nmsALarmInfo.setObjectName(getNeName(ruleDetail.getDimension1_name(), ruleDetail.getDimension2_name()));
        nmsALarmInfo.setObjectType("主机");
//        "locationInfo ":"主机位置",
        nmsALarmInfo.setLocationInfo(getObjectName(ruleDetail.getDimension1_name(), ruleDetail.getDimension2_name()));
        return nmsALarmInfo.toString();
    }
    
    private static String makeAlarmInfoForQHCM(MdAlarmRuleDetail ruleDetail, String currentTime,
            int alarmStatus) {
        NmsALarmInfo nmsALarmInfo = new NmsALarmInfo();
        nmsALarmInfo.setAlarmTitle(ruleDetail.getAlarmmsg());
        nmsALarmInfo.setAlarmStatus(alarmStatus);
        nmsALarmInfo.setAlarmType("性能告警");
        // 填告警级别中文
        nmsALarmInfo.setOrigSeverity(Integer.parseInt(ruleDetail.getAlarm_level()));
        nmsALarmInfo.setEventTime(currentTime + ":00");
        nmsALarmInfo.setAlarmId(ruleDetail.getAlarm_id());
        // 网元名称
        nmsALarmInfo.setNeName(
                getNeName(ruleDetail.getDimension1_name(), ruleDetail.getDimension2_name()));
        // "neType ":"Radius",
        nmsALarmInfo.setNeType("Radius");
        nmsALarmInfo
                .setObjectUID(getObjectUID(ruleDetail.getDimension1(), ruleDetail.getDimension2()));
        nmsALarmInfo.setObjectName(
                getNeName(ruleDetail.getDimension1_name(), ruleDetail.getDimension2_name()));
        nmsALarmInfo.setObjectType("主机");
        // "locationInfo ":"主机位置",
        nmsALarmInfo.setLocationInfo(
                getObjectName(ruleDetail.getDimension1_name(), ruleDetail.getDimension2_name()));
        return nmsALarmInfo.toString();
    }

    private static void sendDataToSocket(String jsonData, AlarmMode am){
        List<Object> addrs = am.getAddrs();
        byte msgType = 20;
        byte[] msg = ByteUtil.getStrToByteArr(jsonData, msgType);
        for (Object tmp : addrs) {
            SendAddress addr = (SendAddress) tmp;
            sendData(msg, addr);
            log.info("nms report to ["+addr.getIp()+":"+addr.getPort()+"]: " + jsonData);
        }
    }
    
    private static void sendData(byte[] msg, SendAddress addr){
        Socket socket = null;
        String ip = addr.getIp();
        int port = addr.getPort();
        DataOutputStream dataOut = null;
        InputStream in = null;
        try {
            socket = new Socket(ip, port);
            dataOut = new DataOutputStream(socket.getOutputStream());
            dataOut.write(msg);
            dataOut.flush();
            in = socket.getInputStream();
            byte[] topBytes = new byte[9];
            in.read(topBytes, 0, 9);
            //解析 topBytes 返回list
            List<Object> toplist = ByteUtil.getByteToTopMsgList(topBytes);
            short bodyLenParser = (short) toplist.get(3);
            byte[] buff = new byte[bodyLenParser];
            int count = in.read(buff);
            if(count>0) {
                String result = new String(buff);
                log.info("recevice from server data: " + result);
            } else {
                log.info("recevice from server data is null");
            }
        } catch (IOException e) {
            log.error("socket ["+ip+":"+port+"] :"+e.getMessage());
        } finally {
            try {
                if(socket != null){
                    socket.close();
                }
            } catch (IOException e) {
                log.error("socket ["+ip+":"+port+"] :"+e.getMessage());
            }
        }
    }
    
    
//    private static String getObjectTypeByUrl(String url){
//        String objectType = null;
//        String [] strings = url.split("--");
//        String url_1 = url;
//        if(strings.length==2){
//            url_1 = strings[0];
//        }
//        if (url_1.endsWith("node")) {
//            if(url_1.endsWith("area/node")){
//                objectType = "地市下节点";
//            }else{// 节点
//                objectType = "节点";
//            }
//        } else if (url_1.endsWith("host")) {
//            if (url_1.endsWith("node/host")) {// 节点下主机
//                objectType = "节点下主机";
//            } else {// 主机
//                objectType = "主机";
//            }
//        } else if (url_1.endsWith("area")) {
//            if (url_1.endsWith("node/area")) {// 节点下地市
//                objectType = "节点下地市";
//            } else {// 属地
//                objectType = "地市";
//            }
//        } else if (url_1.endsWith("area/bras")) {// 属地下bras
//            objectType = "地市下bras";
//        } else if (url_1.endsWith("node/summary")) {// 节点总览
//            objectType = "节点总览";
//        } else if (url_1.endsWith("area/summary")) {// 属地总览
//            objectType = "属地总览";
//        } else if (url_1.endsWith("host/summary")) {// 服务器总览
//            objectType = "服务器总览";
//        }
//        return objectType;
//    }
    
    private static String getObjectName(String dimension1_name, String dimension2_name){
        String objectName = dimension1_name;
        if(!ToolsUtils.StringIsNull(dimension2_name)){
            //objectName = objectName +"-->"+ dimension2_name;
        }
        return objectName;
    }
    
    private static String getObjectUID(String dimension1, String dimension2){
        String objectUID = dimension1;
        if(!ToolsUtils.StringIsNull(dimension2)){
            objectUID = dimension2;
        }
        return objectUID;
    }
    
    private static String getNeName(String dimension1_name, String dimension2_name) {
        String objectName = dimension1_name;
        if (StringUtils.isNotBlank(dimension2_name)) {
            objectName = dimension2_name;
        }
        return objectName;
    }
    
    /**
     * 清除告警
     * @param alarmInfo
     * @param am
     * @param alarmStatus
     */
    public static void cleanAlarm(MdAlarmInfo alarmInfo, AlarmMode am,
            int alarmStatus){
        String jsonData = "";
        String province = AlarmService.conf.getProvince();
        switch (province) {
        case ProviceUtill.PROVINCE_GSCM:
            jsonData = makeAlarmInfoByALarmInfo(alarmInfo, alarmStatus);
            break;
        case ProviceUtill.PROVINCE_HNCM:
            jsonData = makeAlarmInfoForHNCM(alarmInfo, dateTools.getCurrentDate(), alarmStatus);
            break;
        case ProviceUtill.PROVINCE_QHCM:
            jsonData = makeAlarmInfoByALarmInfo(alarmInfo, alarmStatus);
            break;
        default:
            break;
        }
        log.info("clean alarm json data : {}", jsonData);
        //连接socket 信息上报
        sendDataToSocket(jsonData,am);
    }
    
    private static String makeAlarmInfoByALarmInfo(MdAlarmInfo alarmInfo, int alarmStatus){
        NmsALarmInfo nmsALarmInfo = new NmsALarmInfo();
        nmsALarmInfo.setAlarmTitle(alarmInfo.getAlarmmsg());
        nmsALarmInfo.setAlarmStatus(alarmStatus);
        nmsALarmInfo.setAlarmType("性能告警");
        //转中文
        nmsALarmInfo.setOrigSeverity(Integer.parseInt(alarmInfo.getAlarm_level()));
        nmsALarmInfo.setEventTime(dateTools.getCurrentDate());
        nmsALarmInfo.setAlarmId(alarmInfo.getAlarm_id());
        
        nmsALarmInfo.setNeType("Radius");
        nmsALarmInfo.setObjectUID(getObjectUID(alarmInfo.getDimension1(), alarmInfo.getDimension2()));
        nmsALarmInfo.setObjectName(getNeName(alarmInfo.getDimension1_name(), alarmInfo.getDimension2_name()));
        nmsALarmInfo.setObjectType("主机");
        nmsALarmInfo.setLocationInfo(getObjectName(alarmInfo.getDimension1_name(), alarmInfo.getDimension2_name()));
        
//      "locationInfo ":"主机位置",
        return nmsALarmInfo.toString();
    }
    public static void main(String[] args) {
        System.out.println(dateTools.getCurrentDate());
    }
}
