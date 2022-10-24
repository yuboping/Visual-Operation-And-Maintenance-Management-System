package com.asiainfo.lcims.omc.alarm.snmptrap;

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
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.asiainfo.lcims.omc.alarm.conf.SystemConf;
import com.asiainfo.lcims.omc.alarm.model.Host;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.model.SendAddress;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.util.ConstantUtil;
import com.asiainfo.lcims.util.DateTools;
import com.asiainfo.lcims.util.IDGenerateUtil;
import com.asiainfo.lcims.util.ProviceUtill;
import com.asiainfo.lcims.util.ToolsUtils;
import com.asiainfo.lcims.util.TrapUtil;

public class SnmpTrapSender {

    private static final Logger LOG = LoggerFactory.getLogger(SnmpTrapSender.class);
    public static final SystemConf sysConf = new SystemConf();

    private static final String TARGET_COMMUNITY = "public";

    private Snmp snmp = null;

    private Address targetAddress = null;

    private TransportMapping<UdpAddress> transport = null;

    private static final String TARGET_COMMUNITY_GZCM = "gz.radius.nms";

    // private static final String SECURITYNAME_GZCM =
    // "GZGY-PS-RADIUS-SV02-GA-691Aiproxy";

    public void sendMsg(List<Object> addressList, MdAlarmRuleDetail rule, String currentTime,
            String alarmRestore) {
        try {
            for (Object address : addressList) {
                SendAddress addr = (SendAddress) address;
                LOG.info(rule.getAlarm_id() + " send to address ip : {}, port : {}", addr.getIp(),
                        addr.getPort());
                init(addr.getIp(), addr.getPort());
                sendV2Trap(rule, TARGET_COMMUNITY, currentTime, alarmRestore);
            }
        } catch (IOException e) {
            LOG.error("snmptrap send msg error, reason : {}", e);
        }
    }

    public void init(String ip, int port) throws IOException {
        // 目标主机的ip地址 和 端口号 udp:127.0.0.1/162
        String address = "udp:" + ip + "/" + port;
        targetAddress = GenericAddress.parse(address);
        transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public ResponseEvent sendV2Trap(MdAlarmRuleDetail rule, String communityName,
            String currentTime, String alarmRestore) throws IOException {
        // 根据省分动态生成对应的PDU信息
        PDU pdu = makePDU(rule, currentTime, alarmRestore);
        CommunityTarget target = assembTarget(communityName);
        // send pdu, return response
        ResponseEvent response = snmp.send(pdu, target);
        LOG.info(rule.getAlarm_id() + " send pdu response : {}", response);
        return response;
    }

    private PDU makePDU(MdAlarmRuleDetail rule, String currentTime, String alarmFlag) {
        PDU pdu = null;
        switch (sysConf.getProvince()) {
        case ProviceUtill.PROVINCE_HNCM:
            pdu = assembPDUHncm(rule, currentTime, alarmFlag);
            break;
        case ProviceUtill.PROVINCE_SXCM:
            pdu = assembPDUSxcm(rule, currentTime, alarmFlag);
            break;
        case ProviceUtill.PROVINCE_GZCM:
            pdu = assembPDUGzcm(rule, currentTime, alarmFlag);
            break;
        default:
            pdu = assembPDU(rule, currentTime, alarmFlag);
            break;
        }
        return pdu;
    }

    private PDU assembPDUHncm(MdAlarmRuleDetail rule, String currentTime, String alarmFlag) {
        PDU pdu = new PDU();
        StringBuffer logInfo = new StringBuffer("pdu message{}: ");

        // 获取本次上报距离首次上报天数时间 格式： 179days, 16:54:33

        String upTime = getSysUpTime(rule.getAlarm_id(), currentTime);
        VariableBinding sysUpTime = new VariableBinding(new OID(TrapUtil.SYS_UP_TIME),
                new OctetString(upTime));
        pdu.add(sysUpTime);

        VariableBinding snmpTrapOid = new VariableBinding(new OID(TrapUtil.SNMP_TRAP_OID),
                new OctetString(TrapUtil.SNMP_TRAP_OID_VAL));
        pdu.add(snmpTrapOid);

        String uuid = IDGenerateUtil.getUuid();
        // 告警流水号
        VariableBinding alarmCSN = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_CSN),
                new OctetString(uuid));
        pdu.add(alarmCSN);
        logInfo.append("{告警流水号:" + uuid);
        // 告警标题
        VariableBinding alarmTitle = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_TITLE),
                new OctetString(rule.getAlarmmsg()));
        pdu.add(alarmTitle);
        logInfo.append(",告警标题:" + rule.getAlarmmsg());
        // 设备厂家
        VariableBinding alarmSource = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_SOURCE),
                new OctetString(TrapUtil.HNCM_TRAP_ALARM_SOURCE_VAL));
        pdu.add(alarmSource);
        logInfo.append(",设备厂家:" + TrapUtil.HNCM_TRAP_ALARM_SOURCE_VAL);
        // 设备类型
        VariableBinding deviceType = new VariableBinding(
                new OID(TrapUtil.HNCM_TRAP_ALARM_DEVICETYPE),
                new OctetString(TrapUtil.HNCM_TRAP_ALARM_DEVICETYPE_VAL));
        pdu.add(deviceType);
        logInfo.append(",设备类型:" + TrapUtil.HNCM_TRAP_ALARM_DEVICETYPE_VAL);
        // 告警对象类型
        VariableBinding objectType = new VariableBinding(
                new OID(TrapUtil.HNCM_TRAP_ALARM_OBJECTTYPE),
                new OctetString(TrapUtil.HNCM_TRAP_ALARM_OBJECTTYPE_VAL));
        pdu.add(objectType);
        logInfo.append(",告警对象类型:" + TrapUtil.HNCM_TRAP_ALARM_OBJECTTYPE_VAL);
        // 告警类别
        String alarmCategory_val = "1";
        String cleanTime = "";
        if (ConstantUtil.TRAP_CLEAN_FLAG.equals(alarmFlag)) {
            alarmCategory_val = "2";
            cleanTime = currentTime;
        }
        VariableBinding alarmCategory = new VariableBinding(
                new OID(TrapUtil.HNCM_TRAP_ALARM_CATEGORY), new OctetString(alarmCategory_val));
        pdu.add(alarmCategory);
        logInfo.append(",告警类别:" + alarmCategory_val);
        // 产生告警时间
        VariableBinding occurTime = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_OCCURTIME),
                new OctetString(currentTime));
        pdu.add(occurTime);
        logInfo.append(",产生告警时间:" + occurTime);
        // 产生告警的网元名称
        VariableBinding alarmMOName = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_MONAME),
                new OctetString(getMoNmae(rule)));
        pdu.add(alarmMOName);
        logInfo.append(",产生告警的网元名称:" + getMoNmae(rule));
        // TRAPOID，告警的静态唯一标识
        VariableBinding alarmId = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_ID),
                new OctetString(rule.getAlarm_id()));
        pdu.add(alarmId);
        logInfo.append(",告警ID:" + rule.getAlarm_id());
        // 告警级别
        VariableBinding alarmLevel = new VariableBinding(new OID(TrapUtil.HNCM_TRAP_ALARM_LEVEL),
                new OctetString(rule.getAlarm_level()));
        pdu.add(alarmLevel);
        logInfo.append(",告警级别:" + rule.getAlarm_level());
        // 告警清除状态
        VariableBinding alarmRestore = new VariableBinding(
                new OID(TrapUtil.HNCM_TRAP_ALARM_RESTORE), new OctetString(alarmFlag));
        pdu.add(alarmRestore);
        logInfo.append(",告警清除状态:" + alarmFlag);
        // 告警清除时间
        VariableBinding alarmCleanTime = new VariableBinding(
                new OID(TrapUtil.HNCM_TRAP_ALARM_RESTORE_TIME), new OctetString(cleanTime));
        pdu.add(alarmCleanTime);
        logInfo.append(",告警清除时间:" + cleanTime + "}");
        LOG.info(logInfo.toString());
        return pdu;
    }

    private String getSysUpTime(String alarmId, String currentTime) {
        String time = currentTime.substring(11, currentTime.length());
        if (time.length() < 8) {
            time = time + ":00";
        }
        String upTime = "1days, " + time;
        String firstTime = InitParam.getAlarmFirstTime(alarmId);
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

    /**
     * 组装一个PDU
     * 
     * @param rule
     * @param currentTime
     * @return
     */
    private PDU assembPDU(MdAlarmRuleDetail rule, String currentTime, String alarmFlag) {
        // 创建 PDU
        PDU pdu = new PDU();
        String uuid = IDGenerateUtil.getUuid();
        VariableBinding alarmCSN = new VariableBinding(new OID(ConstantUtil.TRAP_ALARM_CSN),
                new OctetString(uuid));
        pdu.add(alarmCSN);
        LOG.info("pdu message alarmCSN(告警流水号，动态唯一标识一条告警): {}", uuid);
        String hostname = ToolsUtils.StringIsNull(rule.getDimension2_name())
                ? rule.getDimension1_name()
                : rule.getDimension2_name();
        VariableBinding alarmName = new VariableBinding(new OID(ConstantUtil.TRAP_ALARM_MONAME),
                new OctetString(hostname));
        pdu.add(alarmName);
        LOG.info("pdu message alarmName(产生告警的网元名称): {}", hostname);
        VariableBinding alarmId = new VariableBinding(new OID(ConstantUtil.TRAP_ALARM_ID),
                new OctetString(rule.getAlarm_id()));
        pdu.add(alarmId);
        LOG.info("pdu message alarmId(告警ID，告警的静态唯一标识): {}", rule.getAlarm_id());
        VariableBinding alarmLevel = new VariableBinding(new OID(ConstantUtil.TRAP_ALARM_LEVEL),
                new OctetString(rule.getAlarm_level()));
        pdu.add(alarmLevel);
        LOG.info("pdu message alarmLevel(告警类别：1：故障告警2：清除告警): {}", rule.getAlarm_level());
        VariableBinding alarmReason = new VariableBinding(
                new OID(ConstantUtil.TRAP_ALARM_SPECIFIC_PROBLEMS),
                new OctetString(rule.getAlarmmsg()));
        pdu.add(alarmReason);
        LOG.info("pdu message alarmReason(告警详细原因): {}", rule.getAlarmmsg());
        VariableBinding alarmType = new VariableBinding(new OID(ConstantUtil.TRAP_ALARM_TYPE),
                new OctetString("1"));
        pdu.add(alarmType);
        LOG.info("pdu message alarmType(告警分类:1：Radius告警): {}", "1");
        if (ConstantUtil.TRAP_ALARM_FLAG.equals(alarmFlag)) {
            VariableBinding alarmCategory = new VariableBinding(
                    new OID(ConstantUtil.TRAP_ALARM_CATEGORY),
                    new OctetString(ConstantUtil.TRAP_ALARM_CATEGORY_FAULT));
            pdu.add(alarmCategory);
            LOG.info("pdu message alarmCategory(告警类别：1：故障告警2：清除告警): {}",
                    ConstantUtil.TRAP_ALARM_CATEGORY_FAULT);
            VariableBinding alarmTime = new VariableBinding(
                    new OID(ConstantUtil.TRAP_ALARM_RESTORETIME), new OctetString(currentTime));
            pdu.add(alarmTime);
            LOG.info("pdu message alarmTime(告警产生时间): {}", currentTime);
            VariableBinding alarmRestore = new VariableBinding(
                    new OID(ConstantUtil.TRAP_ALARM_RESTORE), new OctetString(alarmFlag));
            pdu.add(alarmRestore);
            LOG.info("pdu message alarmRestore(告警清除状态1：已清除告警2：未清除告警): {}", alarmFlag);
        } else if (ConstantUtil.TRAP_CLEAN_FLAG.equals(alarmFlag)) {
            VariableBinding alarmCategory = new VariableBinding(
                    new OID(ConstantUtil.TRAP_ALARM_CATEGORY),
                    new OctetString(ConstantUtil.TRAP_ALARM_CATEGORY_CLEAN));
            pdu.add(alarmCategory);
            LOG.info("pdu message alarmCategory(告警类别：1：故障告警2：清除告警): {}",
                    ConstantUtil.TRAP_ALARM_CATEGORY_CLEAN);
            VariableBinding alarmTime = new VariableBinding(
                    new OID(ConstantUtil.TRAP_ALARM_OCCURTIME), new OctetString(currentTime));
            pdu.add(alarmTime);
            LOG.info("pdu message alarmTime(告警产生时间): {}", currentTime);
            VariableBinding alarmRestore = new VariableBinding(
                    new OID(ConstantUtil.TRAP_ALARM_RESTORE), new OctetString(alarmFlag));
            pdu.add(alarmRestore);
            LOG.info("pdu message alarmRestore(告警清除状态1：已清除告警2：未清除告警): {}", alarmFlag);
        }
        LOG.info("pdu message information: {}", pdu);
        pdu.setType(PDU.TRAP);
        return pdu;
    }

    public CommunityTarget assembTarget(String communityName) {
        // 设置 target
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(communityName));
        target.setAddress(targetAddress);
        switch (sysConf.getProvince()) {
        case ProviceUtill.PROVINCE_HNCM:
            target.setRetries(0);
            target.setTimeout(1000 * 5);
            break;
        case ProviceUtill.PROVINCE_GZCM:
            // target.setSecurityName(new OctetString(SECURITYNAME_GZCM));
            target.setCommunity(new OctetString(TARGET_COMMUNITY_GZCM));
            break;
        default:
            // 通信不成功时的重试次数
            target.setRetries(2);
            // 超时时间
            target.setTimeout(1000 * 5);
            break;
        }
        // snmp版本
        target.setVersion(SnmpConstants.version2c);
        return target;
    }

    /**
     * 获取网元名称
     * 
     * @param rule
     * @return
     */
    private String getMoNmae(MdAlarmRuleDetail rule) {
        String name = ToolsUtils.StringIsNull(rule.getDimension2_name()) ? rule.getDimension1_name()
                : rule.getDimension2_name();
        return name;
    }

    /**
     * 组装一个PDU
     * 
     * @param rule
     * @param currentTime
     * @return
     */
    private PDU assembPDUSxcm(MdAlarmRuleDetail rule, String currentTime, String alarmFlag) {
        // 创建 PDU
        PDU pdu = new PDU();
        String uuid = IDGenerateUtil.getUuid();
        String currentTimeSecond = currentTime + ":00";
        VariableBinding alarmCSN = new VariableBinding(new OID(ConstantUtil.TRAP_ALARM_CSN),
                new OctetString(uuid));
        pdu.add(alarmCSN);
        LOG.info("pdu message alarmCSN(告警流水号，动态唯一标识一条告警): {}", uuid);
        String hostname = ToolsUtils.StringIsNull(rule.getDimension2_name())
                ? rule.getDimension1_name()
                : rule.getDimension2_name();
        VariableBinding alarmName = new VariableBinding(new OID(ConstantUtil.TRAP_ALARM_MONAME),
                new OctetString(hostname));
        pdu.add(alarmName);
        LOG.info("pdu message alarmName(产生告警的网元名称): {}", hostname);
        VariableBinding alarmId = new VariableBinding(new OID(ConstantUtil.TRAP_ALARM_ID),
                new OctetString(
                        rule.getChart_name() + rule.getMetric_id() + rule.getEffectiveRule()));
        pdu.add(alarmId);
        LOG.info("pdu message alarmId(告警ID，告警的静态唯一标识): {}",
                rule.getChart_name() + rule.getMetric_id() + rule.getEffectiveRule());
        VariableBinding alarmLevel = new VariableBinding(new OID(ConstantUtil.TRAP_ALARM_LEVEL),
                new OctetString(rule.getAlarm_level()));
        pdu.add(alarmLevel);
        LOG.info("pdu message alarmLevel(告警级别): {}", rule.getAlarm_level());
        VariableBinding alarmReason = new VariableBinding(
                new OID(ConstantUtil.TRAP_ALARM_SPECIFIC_PROBLEMS),
                new OctetString(rule.getAlarmmsg()));
        pdu.add(alarmReason);
        LOG.info("pdu message alarmReason(告警详细原因): {}", rule.getAlarmmsg());
        VariableBinding alarmType = new VariableBinding(new OID(ConstantUtil.TRAP_ALARM_TYPE),
                new OctetString("1"));
        pdu.add(alarmType);
        LOG.info("pdu message alarmType(告警分类:1：Radius告警): {}", "1");
        VariableBinding alarmTime = new VariableBinding(new OID(ConstantUtil.TRAP_ALARM_OCCURTIME),
                new OctetString(currentTimeSecond));
        pdu.add(alarmTime);
        LOG.info("pdu message alarmTime(告警产生时间): {}", currentTimeSecond);
        if (ConstantUtil.TRAP_ALARM_FLAG.equals(alarmFlag)) {
            VariableBinding alarmCategory = new VariableBinding(
                    new OID(ConstantUtil.TRAP_ALARM_CATEGORY),
                    new OctetString(ConstantUtil.TRAP_ALARM_CATEGORY_FAULT));
            pdu.add(alarmCategory);
            LOG.info("pdu message alarmCategory(告警类别：1：故障告警2：清除告警): {}",
                    ConstantUtil.TRAP_ALARM_CATEGORY_FAULT);
            VariableBinding alarmRestore = new VariableBinding(
                    new OID(ConstantUtil.TRAP_ALARM_RESTORE), new OctetString(alarmFlag));
            pdu.add(alarmRestore);
            LOG.info("pdu message alarmRestore(告警清除状态1：已清除告警2：未清除告警): {}", alarmFlag);
        } else if (ConstantUtil.TRAP_CLEAN_FLAG.equals(alarmFlag)) {
            VariableBinding alarmCategory = new VariableBinding(
                    new OID(ConstantUtil.TRAP_ALARM_CATEGORY),
                    new OctetString(ConstantUtil.TRAP_ALARM_CATEGORY_CLEAN));
            pdu.add(alarmCategory);
            LOG.info("pdu message alarmCategory(告警类别：1：故障告警2：清除告警): {}",
                    ConstantUtil.TRAP_ALARM_CATEGORY_CLEAN);
            VariableBinding restoeTime = new VariableBinding(
                    new OID(ConstantUtil.TRAP_ALARM_RESTORETIME),
                    new OctetString(currentTimeSecond));
            pdu.add(restoeTime);
            LOG.info("pdu message restoeTime(告警清除时间): {}", currentTimeSecond);
            VariableBinding alarmRestore = new VariableBinding(
                    new OID(ConstantUtil.TRAP_ALARM_RESTORE), new OctetString(alarmFlag));
            pdu.add(alarmRestore);
            LOG.info("pdu message alarmRestore(告警清除状态1：已清除告警2：未清除告警): {}", alarmFlag);
        }
        LOG.info("pdu message information: {}", pdu);
        pdu.setType(PDU.TRAP);
        return pdu;
    }

    private PDU assembPDUGzcm(MdAlarmRuleDetail rule, String currentTime, String alarmFlag) {
        // 创建 PDU
        PDU pdu = new PDU();
        StringBuffer logInfo = new StringBuffer("pdu message{}: ");

        Host host = InitParam.getHost(rule.getDimension1());
        VariableBinding alarmHostName = new VariableBinding(new OID(""),
                new OctetString(host.getHostname()));
        pdu.add(alarmHostName);
        logInfo.append("{产生告警的主机名:" + host.getHostname());

        VariableBinding alarmEmpty = new VariableBinding(new OID(ConstantUtil.GZCM_TRAP_EMPTY),
                new OctetString(""));
        pdu.add(alarmEmpty);

        // 产生告警的主机IP
        VariableBinding alarmIp = new VariableBinding(new OID(ConstantUtil.GZCM_TRAP_ALARM_IP),
                new OctetString(rule.getNeIp()));
        pdu.add(alarmIp);
        logInfo.append("产生告警的主机IP:" + rule.getNeIp());

        // 告警ID
        // Integer alarmIdInt = Integer.parseInt(rule.getAlarm_id());
        VariableBinding alarmId = new VariableBinding(new OID(ConstantUtil.GZCM_TRAP_ALARM_ID),
                new OctetString(rule.getAlarm_id()));
        pdu.add(alarmId);
        logInfo.append(",告警ID:" + rule.getAlarm_id());

        // 告警内容
        VariableBinding alarmContent = new VariableBinding(
                new OID(ConstantUtil.GZCM_TRAP_ALARM_CONTENT), new OctetString(rule.getAlarmmsg()));
        pdu.add(alarmContent);
        logInfo.append(",告警内容:" + rule.getAlarmmsg());

        // 告警类型：1 则为告警 0 则为消除告警
        int alarmStatus = 1;
        if (ConstantUtil.TRAP_ALARM_FLAG.equals(alarmFlag)) {
            alarmStatus = 1;
        } else if (ConstantUtil.TRAP_CLEAN_FLAG.equals(alarmFlag)) {
            alarmStatus = 0;
        }
        VariableBinding alarmType = new VariableBinding(new OID(ConstantUtil.GZCM_TRAP_ALARM_TYPE),
                new Integer32(alarmStatus));
        pdu.add(alarmType);
        logInfo.append(",告警类型:" + alarmStatus + "}");
        LOG.info(logInfo.toString());
        pdu.setType(PDU.TRAP);
        return pdu;
    }
}
