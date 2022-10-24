package com.asiainfo.lcims.omc.service.snmp;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.asiainfo.lcims.omc.model.configmanage.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.model.hncu.RealTimeAlarmInfo;
import com.asiainfo.lcims.omc.model.hncu.RealTimeAlarmResult;
import com.asiainfo.lcims.omc.model.hncu.SendAddress;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import com.asiainfo.lcims.omc.util.ais.TrapUtil;

public class SnmpTrapService {

    private static final Logger LOG = LoggerFactory.getLogger(SnmpTrapService.class);

    public SnmpTrapService() {
    }

    private static final String TARGET_COMMUNITY = "public";

    private Snmp snmp = null;

    private Address targetAddress = null;

    private TransportMapping<UdpAddress> transport = null;

    public RealTimeAlarmResult sendMsg(List<Object> addressList, RealTimeAlarmInfo info) {
        RealTimeAlarmResult result = new RealTimeAlarmResult();
        try {
            for (Object address : addressList) {
                SendAddress addr = (SendAddress) address;
                LOG.info("alarmId {} send to address ip : {}, port : {}", info.getAlarmid(),
                        addr.getIp(), addr.getPort());
                init(addr.getIp(), addr.getPort());
                sendV2Trap(info, TARGET_COMMUNITY);
            }
            result.setRet("0");
            result.setDesc("成功");
        } catch (Exception e) {
            LOG.error("snmptrap send msg error, reason : {}", e);
            result.setRet("1");
            result.setDesc("失败");
        }
        return result;
    }

    public void init(String ip, int port) throws IOException {
        // 目标主机的ip地址 和 端口号 udp:127.0.0.1/162
        String address = "udp:" + ip + "/" + port;
        targetAddress = GenericAddress.parse(address);
        transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public ResponseEvent sendV2Trap(RealTimeAlarmInfo info, String communityName)
            throws IOException {
        // 根据省份动态生成对应的PDU信息
        PDU pdu = assembPDUHncm(info);
        CommunityTarget target = assembTarget(communityName);
        // send pdu, return response
        ResponseEvent response = snmp.send(pdu, target);
        LOG.info("alarmId {} send pdu response : {}", info.getAlarmid(), response);
        return response;
    }

    public CommunityTarget assembTarget(String communityName) {
        // 设置 target
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(communityName));
        target.setAddress(targetAddress);
        // 通信不成功时的重试次数
        target.setRetries(0);
        // 超时时间
        target.setTimeout(1000 * 5);
        // snmp版本
        target.setVersion(SnmpConstants.version2c);
        return target;
    }

    private PDU assembPDUHncm(RealTimeAlarmInfo info) {
        PDU pdu = new PDU();
        // 获取本次上报距离首次上报天数时间 格式： 179days, 16:54:33
        String upTime = info.getUpdate();
        VariableBinding sysUpTime = new VariableBinding(new OID(TrapUtil.SYS_UP_TIME),
                new OctetString(upTime));
        pdu.add(sysUpTime);
        LOG.info("本次上报距离首次上报天数:{}", sysUpTime);

        VariableBinding snmpTrapOid = new VariableBinding(new OID(TrapUtil.SNMP_TRAP_OID),
                new OctetString(TrapUtil.SNMP_TRAP_OID_VAL));
        pdu.add(snmpTrapOid);
        LOG.info("trap oid : {}", snmpTrapOid);

        String uuid = info.getUuid();
        // 告警流水号
        VariableBinding alarmCSN = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_CSN),
                new OctetString(uuid));
        pdu.add(alarmCSN);
        LOG.info("告警流水号: {}", alarmCSN);
        // 告警标题
        VariableBinding alarmTitle = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_TITLE),
                new OctetString(info.getAlarmmsg()));
        pdu.add(alarmTitle);
        LOG.info("告警标题: {}", alarmTitle);
        // 设备厂家
        VariableBinding alarmSource = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_SOURCE),
                new OctetString(TrapUtil.HNCM_TRAP_ALARM_SOURCE_VAL));
        pdu.add(alarmSource);
        LOG.info("设备厂家: {}", alarmSource);
        // 设备类型
        VariableBinding deviceType = new VariableBinding(
                new OID(TrapUtil.HNCM_TRAP_ALARM_DEVICETYPE),
                new OctetString(TrapUtil.HNCM_TRAP_ALARM_DEVICETYPE_VAL));
        pdu.add(deviceType);
        LOG.info("设备类型: {}", deviceType);
        // 告警对象类型
        VariableBinding objectType = new VariableBinding(
                new OID(TrapUtil.HNCM_TRAP_ALARM_OBJECTTYPE),
                new OctetString(TrapUtil.HNCM_TRAP_ALARM_OBJECTTYPE_VAL));
        pdu.add(objectType);
        LOG.info("告警对象类型: {}", objectType);
        // 告警类别
        VariableBinding alarmCategory = new VariableBinding(
                new OID(TrapUtil.HNCM_TRAP_ALARM_CATEGORY), new OctetString(info.getAlarmtype()));
        pdu.add(alarmCategory);
        LOG.info("告警类别: {}", alarmCategory);
        // 产生告警时间
        VariableBinding occurTime = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_OCCURTIME),
                new OctetString(info.getCreatetime()));
        pdu.add(occurTime);
        LOG.info("产生告警时间: {}", occurTime);
        // 产生告警的网元名称
        VariableBinding alarmMoname = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_MONAME),
                new OctetString(info.getHostname()));
        pdu.add(alarmMoname);
        LOG.info("产生告警的网元名称: {}", alarmMoname);
        // TRAPOID，告警的静态唯一标识
        VariableBinding alarmId = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_ID),
                new OctetString(info.getAlarmid()));
        pdu.add(alarmId);
        LOG.info("告警ID: {}", alarmId);
        // 告警级别
        VariableBinding alarmLevel = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_LEVEL),
                new OctetString(info.getAlarmlevel()));
        pdu.add(alarmLevel);
        LOG.info("告警级别: {}", alarmLevel);
        // 告警清除状态
        VariableBinding alarmRestore = new VariableBinding(
                new OID(TrapUtil.HNCM_TRAP_ALARM_RESTORE), new OctetString(info.getClearstatus()));
        pdu.add(alarmRestore);
        LOG.info("告警清除状态: {}", alarmRestore);
        // 告警清除时间
        VariableBinding alarmCleanTime = new VariableBinding(
                new OID(TrapUtil.HNCM_TRAP_ALARM_RESTORE_TIME),
                new OctetString(info.getCleartime()));
        pdu.add(alarmCleanTime);
        LOG.info("告警清除时间: {}", alarmCleanTime);
        return pdu;
    }

    public String getSysUpTime(String alarmId, String currentTime) {
        String time = currentTime.substring(11, currentTime.length());
        if (time.length() < 8) {
            time = time + ":00";
        }
        String upTime = "1days, " + time;
        String firstTime = "";
        if (ToolsUtils.StringIsNull(firstTime)) {
            return upTime;
        }
        if (firstTime.contains(currentTime.substring(0, 10))) {
            return upTime;
        }
        upTime = DateTools.DEFAULT.differentDays(firstTime, "yyyy-MM-dd HH:mm:ss", currentTime,
                "yyyy-MM-dd HH:mm");
        return upTime;
    }

    // 获取网元名称
    public String getMoName(MdAlarmRuleDetail rule) {
        String name = "";
        return name;
    }

}
