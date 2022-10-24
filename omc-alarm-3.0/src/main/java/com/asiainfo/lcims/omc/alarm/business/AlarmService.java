package com.asiainfo.lcims.omc.alarm.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.conf.SystemConf;
import com.asiainfo.lcims.omc.alarm.dao.AlarmRuleDAO;
import com.asiainfo.lcims.omc.alarm.dao.HisAlarmDAO;
import com.asiainfo.lcims.omc.alarm.dao.MetricDataDAO;
import com.asiainfo.lcims.omc.alarm.dao.MonAlarmDAO;
import com.asiainfo.lcims.omc.alarm.http.HttpDailyLimit;
import com.asiainfo.lcims.omc.alarm.http.HttpDataPush;
import com.asiainfo.lcims.omc.alarm.http.HttpReport;
import com.asiainfo.lcims.omc.alarm.mail.SimpleMail;
import com.asiainfo.lcims.omc.alarm.mail.SimpleMailSender;
import com.asiainfo.lcims.omc.alarm.model.AlarmDetail;
import com.asiainfo.lcims.omc.alarm.model.AlarmMode;
import com.asiainfo.lcims.omc.alarm.model.AlarmSyslog;
import com.asiainfo.lcims.omc.alarm.model.ChartData;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmHisInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.model.MdChartDataSet;
import com.asiainfo.lcims.omc.alarm.model.SendAddress;
import com.asiainfo.lcims.omc.alarm.nms.NmsReport;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.omc.alarm.param.SendMode;
import com.asiainfo.lcims.omc.alarm.sms.GdcuSms;
import com.asiainfo.lcims.omc.alarm.sms.SmsSender;
import com.asiainfo.lcims.omc.alarm.sms.hncm.HncuSmsSender;
import com.asiainfo.lcims.omc.alarm.snmptrap.SnmpTrapSender;
import com.asiainfo.lcims.omc.alarm.syslog.SyslogSender;
import com.asiainfo.lcims.omc.alarm.syslog.ahct.SyslogDelivery;
import com.asiainfo.lcims.util.ConstantUtil;
import com.asiainfo.lcims.util.DateTools;
import com.asiainfo.lcims.util.EnumUtil;
import com.asiainfo.lcims.util.ProviceUtill;
import com.asiainfo.lcims.util.ToolsUtils;

public class AlarmService {

    private static final Logger log = LoggerFactory.getLogger(AlarmService.class);

    private static final DateTools CREATETIME = new DateTools("yyyy-MM-dd HH:mm:ss");
    public static final SystemConf conf = new SystemConf();
    public static final SimpleMailSender sender = new SimpleMailSender(conf.getSmtpHost(),
            conf.getSmtpUser(), conf.getSmtpPwd());

    static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

    /**
     * 根据周期时间取值
     * 
     * @param rule
     * @param currentTime
     * @return
     */
    public static List<ChartData> getByTime(MdAlarmRuleDetail rule, String currentTime) {
        String chartname = rule.getChart_name();
        MdChartDataSet chartDataSet = InitParam.getChartDateSet(chartname);
        if (chartDataSet == null) {
            log.error("chartDataSate is null: " + chartname);
            return new ArrayList<ChartData>();
        }
        String tablename = chartDataSet.getTable_name();
        tablename = tablename.toUpperCase();
        chartDataSet.setTable_name(tablename);
        List<ChartData> chartDatas = null;
        switch (tablename) {
        case "METRIC_DATA_SINGLE":
            chartDatas = getByTimeSingle(rule, chartDataSet, currentTime);
            break;
        case "METRIC_DATA_MULTI":
            // 多维度数据
            chartDatas = getByTimeMuti(rule, chartDataSet, currentTime);
            break;
        case "STATIS_DATA_DAY":
            String mm = DateTools.getCurrentDate("mm");
            String m = mm.substring(mm.length() - 1);
            if ("3".equals(m) || "4".equals(m) || "8".equals(m) || "9".equals(m)) {
                chartDatas = getByTimeStatisDay(rule, chartDataSet, currentTime);
            }
            break;
        default:
            break;
        }
        return chartDatas;
    }

    private static List<ChartData> getByTimeStatisDay(MdAlarmRuleDetail rule,
            MdChartDataSet chartDataSet, String currentTime) {
        List<ChartData> chartDatas = MetricDataDAO.getByTimeStatisDay(rule, chartDataSet,
                currentTime);
        return chartDatas;
    }

    private static List<ChartData> getByTimeSingle(MdAlarmRuleDetail rule,
            MdChartDataSet chartDataSet, String currentTime) {
        List<ChartData> chartDatas = MetricDataDAO.getByTimeSingle(rule, chartDataSet, currentTime);
        return chartDatas;
    }

    private static List<ChartData> getByTimeMuti(MdAlarmRuleDetail rule,
            MdChartDataSet chartDataSet, String time) {
        List<ChartData> chartDatas = null;
        if (EnumUtil.FILE_SYSTEM_USERATE.equals(chartDataSet.getChart_name())) {
            // 文件系统使用率 特殊处理
            chartDatas = MetricDataDAO.getByTimeMutiFileSystemUseRate(rule, chartDataSet, time);
        } else {
            if (EnumUtil.FILE_DATA_STYLE.equals(chartDataSet.getMarktype())) {
                chartDatas = MetricDataDAO.getByTimeMutiFileSystemUseRate(rule, chartDataSet, time);
            }
            if ("firewall_pdu".equals(chartDataSet.getChart_name())
                    || "firewall_flow".equals(chartDataSet.getChart_name())
                    || "nat_flow".equals(chartDataSet.getChart_name())
                    || "nat_session".equals(chartDataSet.getChart_name())) {
                chartDatas = MetricDataDAO.getByTimeMutigscm5G(rule, chartDataSet, time);
            } else {
                chartDatas = MetricDataDAO.getByTimeMuti(rule, chartDataSet, time);
            }
        }
        return chartDatas;
    }

    /**
     * 新增告警信息
     * 
     * @param rule
     * @param time
     * @param alarmInfo
     * @return
     */
    public static int addAlarm(MdAlarmRuleDetail rule, Timestamp time, MdAlarmInfo alarmInfo) {
        /**
         * 1、alarmInfo 信息存在：告警数量+1 更新所有数据(除了告警首次方式时间) 2、alarmInfo 信息不存在：新增告警信息
         */
        int rt = 0;
        if (null == alarmInfo) {
            rt = insertAlarm(rule, time);
        } else {
            rt = updateAlarm(rule, time, alarmInfo);
        }
        return rt;
    }

    private static int insertAlarm(MdAlarmRuleDetail rule, Timestamp time) {
        MdAlarmInfo alarmInfo = new MdAlarmInfo();
        alarmInfo.setAlarm_id(rule.getAlarm_id());
        alarmInfo.setName(rule.getName());
        alarmInfo.setUrl(rule.getUrl());
        alarmInfo.setDimension_type(rule.getDimension_type());
        alarmInfo.setChart_name(rule.getChart_name());
        alarmInfo.setMetric_id(rule.getMetric_id());
        alarmInfo.setAttr(rule.getAttr());
        alarmInfo.setAlarm_level(rule.getAlarm_level());
        alarmInfo.setAlarm_rule(rule.getEffectiveRule());
        alarmInfo.setModes(rule.getModes());
        alarmInfo.setAlarmmsg(rule.getAlarmmsg());
        alarmInfo.setDimension1(rule.getDimension1());
        alarmInfo.setDimension2(rule.getDimension2());
        alarmInfo.setDimension3(rule.getDimension3());
        alarmInfo.setAlarm_num(1);
        alarmInfo.setConfirm_state(0);
        alarmInfo.setDimension1_name(rule.getDimension1_name());
        alarmInfo.setDimension2_name(rule.getDimension2_name());
        alarmInfo.setDelete_state(0);
        alarmInfo.setAlarm_mvalue(rule.getAlarm_mvalue());
        alarmInfo.setAlarm_type(rule.getAlarm_type());
        alarmInfo.setMetric_original(rule.getMetric_original());
        alarmInfo.setMetric_threshold(rule.getMetric_threshold());
        alarmInfo.setAlarmText(rule.getAlarmText());
        alarmInfo.setReport_rule(rule.getReport_rule());
        return MonAlarmDAO.insertAlarm(alarmInfo, time);
    }

    private static int updateAlarm(MdAlarmRuleDetail rule, Timestamp time, MdAlarmInfo alarmInfo) {
        MdAlarmInfo alarmUpdateInfo = new MdAlarmInfo();
        alarmUpdateInfo.setAlarm_id(alarmInfo.getAlarm_id());
        alarmUpdateInfo.setName(rule.getName());
        alarmUpdateInfo.setUrl(rule.getUrl());
        alarmUpdateInfo.setDimension_type(rule.getDimension_type());
        alarmUpdateInfo.setChart_name(rule.getChart_name());
        alarmUpdateInfo.setMetric_id(rule.getMetric_id());
        alarmUpdateInfo.setAttr(rule.getAttr());
        alarmUpdateInfo.setAlarm_level(rule.getAlarm_level());
        alarmUpdateInfo.setAlarm_rule(rule.getEffectiveRule());
        alarmUpdateInfo.setModes(rule.getModes());
        alarmUpdateInfo.setAlarmmsg(rule.getAlarmmsg());
        alarmUpdateInfo.setDimension1(rule.getDimension1());
        alarmUpdateInfo.setDimension2(rule.getDimension2());
        alarmUpdateInfo.setDimension3(rule.getDimension3());
        alarmUpdateInfo.setDimension1_name(rule.getDimension1_name());
        alarmUpdateInfo.setDimension2_name(rule.getDimension2_name());
        alarmUpdateInfo.setAlarm_num(alarmInfo.getAlarm_num());
        alarmUpdateInfo.setAlarm_mvalue(rule.getAlarm_mvalue());
        alarmUpdateInfo.setAlarm_type(rule.getAlarm_type());
        alarmUpdateInfo.setMetric_original(rule.getMetric_original());
        alarmUpdateInfo.setMetric_threshold(rule.getMetric_threshold());
        alarmUpdateInfo.setAlarmText(rule.getAlarmText());
        alarmUpdateInfo.setReport_rule(rule.getReport_rule());
        alarmUpdateInfo.setClear_time(null);
        return MonAlarmDAO.updateAlarm(alarmUpdateInfo, time);
    }

    public static int insertHistoryAlarm(MdAlarmRuleDetail rule, Timestamp time,
            MdAlarmInfo alarmInfo) {
        int rt = 0;
        if (null == alarmInfo || alarmInfo.getAlarm_num() == 0) {
            // 首次新增告警历史
            MdAlarmHisInfo alarmHisInfo = new MdAlarmHisInfo();
            alarmHisInfo.setAlarm_id(rule.getAlarm_id());
            alarmHisInfo.setName(rule.getName());
            alarmHisInfo.setUrl(rule.getUrl());
            alarmHisInfo.setDimension_type(rule.getDimension_type());
            alarmHisInfo.setChart_name(rule.getChart_name());
            alarmHisInfo.setMetric_id(rule.getMetric_id());
            alarmHisInfo.setAttr(rule.getAttr());
            alarmHisInfo.setAlarm_level(rule.getAlarm_level());
            alarmHisInfo.setAlarm_rule(rule.getEffectiveRule());
            alarmHisInfo.setModes(rule.getModes());
            alarmHisInfo.setAlarmmsg(rule.getAlarmmsg());
            alarmHisInfo.setDimension1(rule.getDimension1());
            alarmHisInfo.setDimension2(rule.getDimension2());
            alarmHisInfo.setDimension3(rule.getDimension3());
            alarmHisInfo.setDimension1_name(rule.getDimension1_name());
            alarmHisInfo.setDimension2_name(rule.getDimension2_name());
            alarmHisInfo.setAlarm_type(rule.getAlarm_type());
            alarmHisInfo.setMetric_original(rule.getMetric_original());
            alarmHisInfo.setMetric_threshold(rule.getMetric_threshold());
            alarmHisInfo.setAlarmText(rule.getAlarmText());
            alarmHisInfo.setReport_rule(rule.getReport_rule());
            return HisAlarmDAO.insertHistoryAlarm(alarmHisInfo, time);
        }
        return rt;
    }

    public static int modifyAlarm(MdAlarmRuleDetail rule, Timestamp time) {
        return MonAlarmDAO.modifyAlarm(rule, time);
    }

    public static void syslogAlarm(MdAlarmRuleDetail rule, AlarmMode am, boolean isActive) {
        AlarmSyslog syslog = makeSyslogInfo(rule, isActive);
        List<Object> addrs = am.getAddrs();
        for (Object tmp : addrs) {
            SendAddress addr = (SendAddress) tmp;
            SyslogSender sender = new SyslogSender(addr.getIp(), addr.getPort());
            try {
                sender.send(syslog.toString().getBytes("UTF-8"));
                log.info("send syslog alarm==>" + syslog.toString());
            } catch (Exception e) {
                log.error("syslog alarm error, reason : {}", e);
            } finally {
                sender.close();
            }
        }
    }

    private static AlarmSyslog makeSyslogInfo(MdAlarmRuleDetail rule, boolean isActive) {
        AlarmSyslog syslog = new AlarmSyslog();
        AlarmDetail detail = new AlarmDetail();
        if (ToolsUtils.StringIsNull(rule.getDimension2())) {
            detail.setHostip(rule.getDimension1());
            detail.setHostname(rule.getDimension1_name());
        } else {
            detail.setHostip(rule.getDimension2());
            detail.setHostname(rule.getDimension1_name() + ":" + rule.getDimension2_name());
        }
        syslog.setDevice(detail.getHostip());
        detail.setAlarmdesc(rule.getAlarmmsg() + " 当前值{" + rule.getCollectvalue() + "}");
        detail.setAlarmlevel(InitParam.getAlarmLevelName(rule.getAlarm_level()));
        detail.setAlarmtime(CREATETIME.getCurrentDate());
        detail.setUniquemark(String.valueOf(rule.getAlarm_id()));
        detail.setFlag(isActive ? "active" : "isclear");
        syslog.setDetail(detail);
        return syslog;
    }

    private static SimpleMail makeEmailContent(MdAlarmRuleDetail rule, String currentTime) {
        SimpleMail mail = new SimpleMail();
        String level = InitParam.getAlarmLevelName(rule.getAlarm_level());
        String elementname = "";// 网元名称
        String elementno = "";// 网元编号
        String elementlink = null;// 网元链路
        if (ToolsUtils.StringIsNull(rule.getDimension2())) {
            elementno = rule.getDimension1();
            elementname = rule.getDimension1_name();
        } else {
            elementno = rule.getDimension2();
            elementname = rule.getDimension2_name();
            elementlink = rule.getDimension1_name() + " -> " + rule.getDimension2_name();
        }
        mail.setSubject("故障告警：{" + level + "}" + elementname + "(omc)[" + currentTime + "]");
        String pstyle = "<p style='text-align:justify;margin:0cm;margin-bottom:.0001pt;'>";
        StringBuffer sb = new StringBuffer();
        sb.append("<html><body style='font-family:微软雅黑'>");
        sb.append("<p>管理员，您好：</p>");
        sb.append("<p>网元[" + elementname + "]存在告警，需要解决。</p>");
        sb.append("=================================================");
        sb.append(pstyle).append("网元编号：" + elementno + "</p>");
        if (!ToolsUtils.StringIsNull(elementlink))
            sb.append(pstyle).append("网元链路：" + elementlink + "</p>");
        sb.append(pstyle).append("网元名称：" + elementname + "</p>");
        sb.append(pstyle).append("告警级别：" + level + "</p>");
        sb.append(pstyle).append("告警来源：omc</p>");
        sb.append(pstyle).append("最近发生时间：" + currentTime + "</p>");
        sb.append(pstyle).append("告警描述：" + rule.getAlarmmsg() + "</p>");
        sb.append(pstyle).append("采集值：[" + rule.getCollectvalue() + "]</p>");
        sb.append("=================================================");
        sb.append("<p>宽带接入可视化运维平台</p>");
        sb.append("-----------------------------------------------------------");
        sb.append(pstyle).append("这是您在亚信宽带接入可视化运维平台订阅的故障邮件，</p>");
        sb.append(pstyle).append("如果需要取消订阅，请登录系统取消订阅或联系管理员。</p>");
        sb.append("----------------------------------------------------------");
        sb.append("</body></html>");
        mail.setContent(sb.toString());
        return mail;
    }

    public static void alarm(MdAlarmRuleDetail rule, String currentTime, MdAlarmInfo alarmInfo) {
        if (rule.getModes() == null || "".equals(rule.getModes().trim())) {
            log.info("alarm mode is blank.");
            return;
        }
        String[] modes = rule.getModes().split(EnumUtil.ALARM_RULE_SPLIT);
        for (String modeid : modes) {
            AlarmMode am = InitParam.getAlarmMode(modeid);
            if (am != null) {
                sendByMode(rule, am, currentTime, alarmInfo);
            } else {
                log.info("alarm mode is : {} null");
            }
        }
    }

    private static void sendByMode(MdAlarmRuleDetail rule, AlarmMode am, String currentTime,
            MdAlarmInfo alarmInfo) {
        int alarmNum = 0;
        String reportRule = "alarmNum == 1";
        if (null != alarmInfo) {
            alarmNum = alarmInfo.getAlarm_num();
        }
        alarmNum += 1;
        if (null != rule.getReport_rule()) {
            reportRule = rule.getReport_rule();
            reportRule = reportRule.replace("alarmNum", String.valueOf(alarmNum));
        }

        Boolean reportBool = false;
        try {
            reportBool = Boolean.valueOf(jse.eval(reportRule).toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        String modename = am.getModename();
        log.info(rule.getAlarm_id() + ": alarm num = " + alarmNum + ",alarm mode is : " + modename
                + ",alarm modetype is : " + am.getModetype() + ",alarm reportBool is : "
                + reportBool);
        if (alarmNum <= conf.getAlarmMinNum()) {
            log.info(rule.getAlarm_id()
                    + ": System conf alarm_min_num is:{}, so this alarm don't need to send msg.",
                    conf.getAlarmMinNum());
            return;
        }

        if (am.getModetype() == SendMode.SYSLOG.getType() && reportBool) {// syslog方式上报网管
            if (ProviceUtill.PROVINCE_AHCT.equals(conf.getProvince())) {
                // 安徽电信的分支
                SyslogDelivery.ahctSyslogAlarm(rule, am, currentTime, "0");
            } else {
                syslogAlarm(rule, am, true);
            }
        } else if (am.getModetype() == SendMode.EMAIL.getType()
                && (reportBool || alarmLevelChanged(rule, alarmInfo))) {// 邮件告警
            emailAlarm(rule, am, currentTime);
        } else if (am.getModetype() == SendMode.SMS.getType()) {// 短信告警
            if (ProviceUtill.PROVINCE_JSCU.equalsIgnoreCase(conf.getProvince())
                    && !alarmLevelChanged(rule, alarmInfo)) {
                log.info("sms alarm is return!");
                return;
            }
            // 短信告警
            if (ProviceUtill.PROVINCE_GDCU.equals(conf.getProvince())) {
                GdcuSms.smsAlarm(rule, am);
            } else if (ProviceUtill.PROVINCE_HNCU.equals(conf.getProvince())) {
                hnSmsAlarm(rule, am, currentTime);
            } else {
                smsAlarm(rule, am);
            }
        } else if (am.getModetype() == SendMode.SNMPTRAP.getType()) {
            // snmptrap方式告警
            // 根据告警规则判断
            if (alarmRuleChanged(rule, alarmInfo)) {
                snmptrapAlarm(rule, am, currentTime, ConstantUtil.TRAP_ALARM_FLAG);
            } else {
                if (ProviceUtill.PROVINCE_SXCM.equals(conf.getProvince())) {
                    snmptrapAlarm(rule, am, currentTime, ConstantUtil.TRAP_ALARM_FLAG);
                }
                if (ProviceUtill.PROVINCE_GZCM.equals(conf.getProvince())) {
                    snmptrapAlarm(rule, am, currentTime, ConstantUtil.TRAP_ALARM_FLAG);
                }
            }
        } else if (am.getModetype() == SendMode.REPORTNMS.getType() && reportBool) {
            // 上报至alarm-report
            NmsReport.reportAlarm(rule, am, currentTime, ConstantUtil.NMS_ALARM_STATUS_REPORT,
                    alarmInfo);
        } else if (am.getModetype() == SendMode.REPORTHTTP.getType() && reportBool) {
            // HTTP告警上报
            if (ProviceUtill.PROVINCE_SHCM.equals(conf.getProvince())) {
                String currentDate = CREATETIME.getCurrentDate();
                HttpReport.reportAlarm(rule, am, currentDate, alarmInfo);
                String alarmId = rule.getAlarm_id();
                MonAlarmDAO.modifyReportFlag(alarmId, ConstantUtil.ALARM_REPORT_RULE);
            } else if (ProviceUtill.PROVINCE_ZYWLW.equals(conf.getProvince())) {
                if (StringUtils.equalsIgnoreCase(modename, "APN限流")) {
                    HttpDailyLimit.orderNotice(rule, am, currentTime, alarmInfo);
                } else if (StringUtils.equalsIgnoreCase(modename, "集中监控告警")) {
                    HttpDataPush.pushAlarm(rule, am, currentTime, alarmInfo);
                }
            }
        } else {
            log.debug(am.getModeattr());
        }

    }

    /**
     * 1、判断当前告警级别与历史告警级别是否一致，不一致则告警 2、或者告警数量为0 则告警
     * 
     * @param rule
     * @param alarmInfo
     * @return
     */
    private static boolean alarmLevelChanged(MdAlarmRuleDetail rule, MdAlarmInfo alarmInfo) {
        int alarmNum = 0;
        String alarm_level_his = null;
        if (null != alarmInfo) {
            alarmNum = alarmInfo.getAlarm_num();
            alarm_level_his = alarmInfo.getAlarm_level();
        }
        // 告警数量为 0
        if (alarmNum == 0) {
            log.info("alarm num is 0. alarmLevel init.");
            return true;
        }
        if (!ToolsUtils.StringIsNull(alarm_level_his)
                && !rule.getAlarm_level().equals(alarm_level_his)) {
            log.info("alarmLevelChanged: alarm_level_his=" + alarm_level_his + ",alarm_level_now="
                    + rule.getAlarm_level());
            return true;
        }
        return false;
    }

    /**
     * 1、判断当前生效告警规则与历史告警规则是否一致，不一致则告警 2、或者告警数量为0 则告警
     * 
     * @param rule
     * @param alarmInfo
     * @return
     */
    private static boolean alarmRuleChanged(MdAlarmRuleDetail rule, MdAlarmInfo alarmInfo) {
        int alarmNum = 0;
        String alarm_rule_his = null;
        if (null != alarmInfo) {
            alarmNum = alarmInfo.getAlarm_num();
            alarm_rule_his = alarmInfo.getAlarm_rule();
        }
        // 告警数量为 0
        if (alarmNum == 0) {
            log.info("alarmRuleChanged alarm num is 0. alarmLevel init.");
            return true;
        }
        if (!ToolsUtils.StringIsNull(alarm_rule_his)
                && !rule.getEffectiveRule().equals(alarm_rule_his)) {
            log.info("alarmLevelChanged: alarm_rule_his=" + alarm_rule_his + ",alarm_rule_now="
                    + rule.getEffectiveRule());
            return true;
        }
        return false;
    }

    private static void smsAlarm(MdAlarmRuleDetail rule, AlarmMode am) {
        log.debug("sms alarm start...");
        SmsSender smsSender = new SmsSender();
        smsSender.sendMsg(am.getAddrs(), rule);
        log.debug("sms alarm end...");
    }

    private static void hnSmsAlarm(MdAlarmRuleDetail rule, AlarmMode am, String currentTime) {
        log.info("hncn sms alarm start...");
        HncuSmsSender smsSender = new HncuSmsSender();
        smsSender.sendMsg(am.getAddrs(), rule, currentTime);
        log.info("hncn sms alarm end...");
    }

    private static void emailAlarm(MdAlarmRuleDetail rule, AlarmMode am, String currentTime) {
        SimpleMail mail = makeEmailContent(rule, currentTime);
        try {
            sender.send(am.getAddrs(), mail);
            log.info("send email alarm==>{}", mail.getSubject());
        } catch (Exception e) {
            log.error("邮件发送失败, error : {}", e);
        }
    }

    private static void snmptrapAlarm(MdAlarmRuleDetail rule, AlarmMode am, String currentTime,
            String alarmRestore) {
        log.info("snmptrap alarm start.");
        SnmpTrapSender snmpTrapSender = new SnmpTrapSender();
        List<Object> addrList = am.getAddrs();
        log.info("snmptrap addrList size : {}.", addrList.size());
        snmpTrapSender.sendMsg(addrList, rule, currentTime, alarmRestore);
        log.info("snmptrap alarm end.");
    }

    public static List<MdAlarmRuleDetail> getAlarmRuleDetail(String cycle) {
        return AlarmRuleDAO.getAlarmRuleDetail(cycle);
    }

    /**
     * 告警清除
     * 
     * @param rule
     * @param currentTime
     */
    public static void cleanAlarm(MdAlarmRuleDetail rule, String currentTime,
            MdAlarmInfo alarmInfo) {
        if (rule.getModes() == null || "".equals(rule.getModes().trim())) {
            log.info("alarm mode is blank.");
            return;
        }
        String[] modes = rule.getModes().split("#");
        for (String modeid : modes) {
            AlarmMode am = InitParam.getAlarmMode(modeid);
            if (am != null) {
                log.info("alarm mode is : {}", am.getModename());
                sendByModeClean(rule, am, currentTime, alarmInfo);
            }
        }
    }

    private static void sendByModeClean(MdAlarmRuleDetail rule, AlarmMode am, String currentTime,
            MdAlarmInfo alarmInfo) {
        int alarmNum = 0;
        String reportRule = "alarmNum == 1";
        if (null != alarmInfo) {
            alarmNum = alarmInfo.getAlarm_num();
        }
        if (null != rule.getReport_rule()) {
            reportRule = rule.getReport_rule();
            reportRule = reportRule.replace("alarmNum", String.valueOf(alarmNum));
        }
        Boolean reportBool = false;
        try {
            reportBool = Boolean.valueOf(jse.eval(reportRule).toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            reportBool = false;
        }
        log.info("alarm clean reportBool is : {}", reportBool);
        if (am.getModetype() == SendMode.SYSLOG.getType()) {// syslog方式上报网管
            if (ProviceUtill.PROVINCE_AHCT.equals(conf.getProvince())) {
                // 安徽电信的分支
                SyslogDelivery.ahctSyslogAlarm(rule, am, currentTime, ConstantUtil.CLEAN_FLAG);
            } else {
                syslogAlarm(rule, am, false);
            }
        } else if (am.getModetype() == SendMode.SNMPTRAP.getType()) {
            snmptrapAlarm(rule, am, currentTime, ConstantUtil.TRAP_CLEAN_FLAG);
        } else if (am.getModetype() == SendMode.REPORTNMS.getType()) {
            NmsReport.reportAlarm(rule, am, currentTime, ConstantUtil.NMS_ALARM_STATUS_CLEAR,
                    alarmInfo);
        } else if (am.getModetype() == SendMode.REPORTHTTP.getType()) {
            // HTTP告警清除
            String reportFlag = alarmInfo == null ? "0" : alarmInfo.getReport_flag();
            if (ProviceUtill.PROVINCE_SHCM.equals(conf.getProvince())
                    && ConstantUtil.ALARM_REPORT_RULE.equals(reportFlag)) {
                String currentDate = CREATETIME.getCurrentDate();
                HttpReport.cleanAlarm(rule, am, currentDate, alarmInfo);
            } else if (ProviceUtill.PROVINCE_ZYWLW.equals(conf.getProvince())) {
                String modeName = am.getModename();
                if (StringUtils.equalsIgnoreCase(modeName, "集中监控告警")) {
                    HttpDataPush.cleanAlarm(rule, am, currentTime, alarmInfo);
                } else if (StringUtils.equalsIgnoreCase(modeName, "APN限流")) {
                    HttpDailyLimit.unorderNotice(rule, am, currentTime, alarmInfo);
                }
            }
        } else {
            log.debug(am.getModeattr());
        }
    }

    public static int deleteAlarmInfo(String alamrId) {
        return MonAlarmDAO.deleteAlarmInfo(alamrId);
    }

    public static int deleteHisAlarmInfo(String alamrId) {
        return HisAlarmDAO.deleteHisAlarmInfo(alamrId);
    }

    public static List<MdAlarmInfo> getAlarmInfos(String cycleid) {
        return AlarmRuleDAO.getAlarmInfos(cycleid);
    }

    public static int updateAlarmHis(String alarm_id, Timestamp time) {
        // 查询历史表中最新一条数据获取历史信息id，更新清除时间 time
        String hisId = HisAlarmDAO.getAlarmHisLastId(alarm_id);
        if (!ToolsUtils.StringIsNull(hisId)) {
            return HisAlarmDAO.updateAlarmHisClearTime(hisId, time);
        }
        return 0;
    }

    /**
     * 告警规则不存在 告警清除
     *
     */
    public static void cleanAlarmByAlarmInfo(MdAlarmInfo alarmInfo) {
        if (alarmInfo.getModes() == null || "".equals(alarmInfo.getModes().trim())) {
            return;
        }
        String[] modes = alarmInfo.getModes().split("#");
        for (String modeid : modes) {
            AlarmMode am = InitParam.getAlarmMode(modeid);
            log.info("cleanAlarmByAlarmInfo alarm mode is : {}", am.getModename());
            if (am != null) {
                sendByModeCleanByAlarmInfo(alarmInfo, am);
            }
        }
    }

    private static void sendByModeCleanByAlarmInfo(MdAlarmInfo alarmInfo, AlarmMode am) {
        MdAlarmRuleDetail rule = makeRuleDetaiCopyAlarmInfo(alarmInfo);
        int alarmNum = alarmInfo.getAlarm_num();
        String reportFlag = alarmInfo.getReport_flag();
        String reportRule = "alarmNum == 1";
        if (null != alarmInfo.getReport_rule()) {
            reportRule = alarmInfo.getReport_rule();
            reportRule = reportRule.replace("alarmNum", String.valueOf(alarmNum));
        }
        Boolean reportBool = false;
        try {
            reportBool = Boolean.valueOf(jse.eval(reportRule).toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            reportBool = false;
        }
        log.info("mode clean by alarmInfo reportRule is {}, reportBool is {}", reportRule,
                reportBool);
        String currentTime = null;
        if (am.getModetype() == SendMode.SYSLOG.getType()) {// syslog方式上报网管
            if (ProviceUtill.PROVINCE_AHCT.equals(conf.getProvince())) {
                // 安徽电信的分支
                currentTime = CREATETIME.getCurrentDate("yyyy-MM-dd HH:mm");
                SyslogDelivery.ahctSyslogAlarm(rule, am, currentTime, "1");
            } else {
                syslogAlarm(rule, am, false);
            }
        } else if (am.getModetype() == SendMode.SNMPTRAP.getType()) {
            currentTime = CREATETIME.getCurrentDate();
            snmptrapAlarm(rule, am, currentTime, "1");
        } else if (am.getModetype() == SendMode.REPORTNMS.getType()) {
            NmsReport.cleanAlarm(alarmInfo, am, 0);
        } else if (am.getModetype() == SendMode.REPORTHTTP.getType()) {
            // HTTP告警清除
            log.info("report flag is : {}", reportFlag);
            if (ProviceUtill.PROVINCE_SHCM.equals(conf.getProvince())
                    && ConstantUtil.ALARM_REPORT_RULE.equals(reportFlag)) {
                currentTime = CREATETIME.getCurrentDate("yyyy-MM-dd HH:mm");
                HttpReport.cleanAlarm(rule, am, currentTime, alarmInfo);
            } else if (ProviceUtill.PROVINCE_ZYWLW.equals(conf.getProvince())) {
                HttpDataPush.cleanAlarm(rule, am, currentTime, alarmInfo);
            }
        } else {
            log.debug(am.getModeattr());
        }
    }

    private static MdAlarmRuleDetail makeRuleDetaiCopyAlarmInfo(MdAlarmInfo alarmInfo) {
        MdAlarmRuleDetail rule = new MdAlarmRuleDetail();
        rule.setAlarm_id(alarmInfo.getAlarm_id());
        rule.setModes(alarmInfo.getModes());
        rule.setAlarm_level(alarmInfo.getAlarm_level());
        rule.setAlarm_rule(alarmInfo.getAlarm_rule());
        rule.setAlarmmsg(alarmInfo.getAlarmmsg());
        rule.setDimension1(alarmInfo.getDimension1());
        rule.setDimension1_name(alarmInfo.getDimension1_name());
        rule.setDimension2(alarmInfo.getDimension2());
        rule.setDimension2_name(alarmInfo.getDimension2_name());
        rule.setDimension3(alarmInfo.getDimension3());
        rule.setDimension3_name(alarmInfo.getDimension3_name());
        rule.setEffectiveRule(alarmInfo.getAlarm_rule());
        rule.setChart_name(alarmInfo.getChart_name());
        rule.setEffectiveRule(alarmInfo.getAlarm_rule());
        rule.setAlarm_type(alarmInfo.getAlarm_type());
        rule.setNeName(alarmInfo.getNeName());
        rule.setNeIp(alarmInfo.getNeIp());
        return rule;
    }
}
