package com.asiainfo.lcims.omc.alarm.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.conf.SystemConf;
import com.asiainfo.lcims.omc.alarm.model.Area;
import com.asiainfo.lcims.omc.alarm.model.ChartData;
import com.asiainfo.lcims.omc.alarm.model.Host;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.model.MdChartDataSet;
import com.asiainfo.lcims.omc.alarm.model.MdChartDetail;
import com.asiainfo.lcims.omc.alarm.model.Node;
import com.asiainfo.lcims.omc.alarm.model.TimeCell;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.omc.alarm.param.RuleValue;
import com.asiainfo.lcims.util.ConstantUtil;
import com.asiainfo.lcims.util.DateTools;
import com.asiainfo.lcims.util.EnumUtil;
import com.asiainfo.lcims.util.ProviceUtill;
import com.asiainfo.lcims.util.ThresholdUtil;
import com.asiainfo.lcims.util.ToolsUtils;

public class AlarmProcess implements Runnable {

    static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

    private static final Logger logger = LoggerFactory.make();

    public static final SystemConf conf = new SystemConf();

    private MdAlarmRuleDetail rule;
    private String currentTime; // 当前周期
    private String cyclename;
    private String thred_id;
    private List<ChartData> newest = null;
    private List<ChartData> lastest = null;
    private List<ChartData> yescurrent = null;
    private List<MdAlarmInfo> alarmInfos = null;

    public AlarmProcess(MdAlarmRuleDetail rule, String currentTime, String cyclename,
            List<MdAlarmInfo> alarmInfos, String thred_id) {
        this.rule = rule;
        this.currentTime = currentTime;
        this.cyclename = cyclename;
        this.alarmInfos = alarmInfos;
        this.thred_id = thred_id;
    }

    @Override
    public void run() {
        try {
            // Thread.sleep(30000); // 延时30秒
            boolean isAlarm = isAlarm();
            makeMonitorTargetInfo();
            // 上周期告警信息
            MdAlarmInfo alarmInfo = getAlarmInfoByAlarmId();
            long millSeconds = TimeControl.getMillSeconds(currentTime);
            Timestamp time = new Timestamp(millSeconds);
            rule.makeCollectValue();
            boolean needReport = AlarmService.conf.needReport();
            if (isAlarm) {
                if (null == newest) {
                    logger.info(thred_id + "--isAlarm-newest-size:NULL");
                } else {
                    logger.info(thred_id + "--isAlarm-newest-size:" + newest.size());
                }

                String tmp = rule.getAlarmmsg();
                if (tmp != null) {
                    tmp = rule.getAlarmmsg().replace("{newest}", rule.getCollectvalue());
                    rule.setAlarmmsg(tmp);
                }
                logger.info("thread id : {}, final express : currentTime:[{}], rule is : {}",
                        thred_id, currentTime, rule);
                rule.setMetric_original(rule.getCollectvalue());
                // 指标原始值和指标阀值的转换
                ThresholdUtil.getMetricThresholdByRule(rule);
                // 生成告警正文内容
                makeAlarmText();

                AlarmService.addAlarm(rule, time, alarmInfo);
                // 记录告警历史信息
                AlarmService.insertHistoryAlarm(rule, time, alarmInfo);
                if (needReport) {
                    // 当前告警级别与上个周期告警级别比较，级别不同则发短信、邮件
                    AlarmService.alarm(rule, currentTime, alarmInfo);
                }
            } else {
                // 判断是否需求做清除操作
                if (null != alarmInfo && alarmInfo.getAlarm_num() != 0 && isRunStatis()) {
                    logger.info("alarm_id:" + rule.getAlarm_id() + " is clear.");
                    /**
                     * 清除操作： 1、告警数量清零、清除时间更新 2、告警确认清空 状态为未确定 删除状态为 0
                     * 3、syslog清除操作 4、对应的告警历史信息 清除时间更新
                     */
                    AlarmService.modifyAlarm(rule, time);
                    // 对应的告警历史信息 清除时间更新
                    AlarmService.updateAlarmHis(rule.getAlarm_id(), time);
                    if (needReport) {
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
                        AlarmService.cleanAlarm(rule, currentTime, alarmInfo);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error(thred_id + e.getMessage());
        }
    }

    private void makeAlarmText() {
        rule.setAlarmText("系统：宽带AAA；告警内容：" + rule.getAlarmmsg() + "；设备名称：" + rule.getNeName()
                + "；设备IP：" + rule.getNeIp() + "；指标原始值：" + rule.getMetric_original() + "；指标阀值："
                + rule.getMetric_threshold());
    }

    /**
     * 根据url 赋值 维度1 维度2 值:
     * 
     * node/host 节点 --> 主机 ： 维度1：节点名称， 维度2：主机IP.
     * 
     * node/area 节点 --> 地市 ： 维度1：节点名称， 维度2：地市名称.
     * 
     * area/bras 地市 --> bras: 维度1：地市 ， 维度2：brasip.
     * 
     * node 节点 ： 维度1：节点名称， 维度2：空.
     * 
     * area 地市 ： 维度1：地市名称， 维度2：空.
     * 
     * node/summary： 维度1 节点总览.
     * 
     * area/summary: 维度1 地市总览.
     * 
     * area/node: 地市 --> 节点 ：维度1：地市名称， 维度2：节点名称.
     * 
     * bras --> bras: 维度1：bras.
     */
    private void makeMonitorTargetInfo() {
        String url = rule.getUrl();
        int dimensionType = rule.getDimension_type();
        String[] strings = url.split("--");
        String url_1 = url;
        if (strings.length == 2) {
            url_1 = strings[0];
        }
        switch (dimensionType) {
        case 23:
            /** 维度类型23--> bras(all) bras ： 维度1：空， 维度2：空 */
            rule.setDimension1_name(InitParam.getNasIp(rule.getDimension1()));
            rule.setNeIp(InitParam.getNasIp(rule.getDimension1()));
            break;
        case 24:
            /** 维度类型24--> bras(single) bras ： 维度1：brasip， 维度2：空 */
            rule.setDimension1_name(InitParam.getNasIp(rule.getDimension1()));
            rule.setNeIp(InitParam.getNasIp(rule.getDimension1()));
            break;
        case 26:
            /** 维度类型26--> apn(all) apn ： 维度1：空， 维度2：空 */
            rule.setDimension1_name(InitParam.getApnName(rule.getDimension1()));
            break;
        case 27:
            /** 维度类型27--> apn(single) apn ： 维度1：apn名称， 维度2：空 */
            rule.setDimension1_name(InitParam.getApnName(rule.getDimension1()));
            break;
        }
        if (url_1.endsWith("node")) {
            if (url_1.endsWith("area/node")) {
                Area area = InitParam.getArea(rule.getDimension1());
                if (area != null)
                    rule.setDimension1_name(area.getName());
                Node node = InitParam.getNode(rule.getDimension2());
                if (node != null)
                    rule.setDimension2_name(node.getNode_name());
            } else {// 节点
                Node node = InitParam.getNode(rule.getDimension1());
                if (node != null)
                    rule.setDimension1_name(node.getNode_name());
            }
        } else if (url_1.endsWith("host")) {
            if (url_1.endsWith("node/host")) {// 节点下主机
                Node node = InitParam.getNode(rule.getDimension1());
                if (node != null)
                    rule.setDimension1_name(node.getNode_name());
                Host host = InitParam.getHost(rule.getDimension2());
                rule.setDimension2_name(host.getHostname() + "_" + host.getAddr());
                rule.setNeIp(host.getAddr());
                rule.setNeName(host.getHostname() + "/" + host.getHosttypename());
            } else {// 主机
                Host host = InitParam.getHost(rule.getDimension1());
                if (host == null) {
                    logger.info("rule dimension1 is [{}]", rule.getDimension1());
                } else {
                    rule.setDimension1_name(host.getHostname() + "_" + host.getAddr());
                    rule.setNeIp(host.getAddr());
                    rule.setNeName(host.getHostname() + "/" + host.getHosttypename());
                }
            }
        } else if (url_1.endsWith("area")) {
            if (url_1.endsWith("node/area")) {// 节点下地市
                Node node = InitParam.getNode(rule.getDimension1());
                if (node != null)
                    rule.setDimension1_name(node.getNode_name());
                Area area = InitParam.getArea(rule.getDimension2());
                if (area != null)
                    rule.setDimension2_name(area.getName());
            } else {// 属地
                Area area = InitParam.getArea(rule.getDimension1());
                if (area != null)
                    rule.setDimension1_name(area.getName());
            }
        } else if (url_1.endsWith("bras")) {// 属地下bras
            if (url_1.endsWith("area/bras")) {
                Area area = InitParam.getArea(rule.getDimension1());
                if (area != null)
                    rule.setDimension1_name(area.getName());
                String nasip = InitParam.getNasIp(rule.getDimension2());
                rule.setDimension2_name(nasip);
                rule.setNeIp(nasip);
            } else {
                String nasip = InitParam.getNasIp(rule.getDimension1());
                rule.setDimension1_name(nasip);
                rule.setNeIp(nasip);
            }
        } else if (url_1.endsWith("node/summary")) {// 节点总览
            rule.setDimension1_name("节点总览");
        } else if (url_1.endsWith("area/summary")) {// 属地总览
            rule.setDimension1_name("属地总览");
        } else if (url_1.endsWith("host/summary")) {// 服务器总览
            rule.setDimension1_name("服务器总览");
        } else if (url_1.endsWith("apn/summary")) {// apn总览
            rule.setDimension1_name("apn总览");
        }
    }

    private boolean isAlarm() {
        String rules = rule.getAlarm_rule();
        // 查询最新周期数据
        newest = AlarmService.getByTime(rule, currentTime);
        // 上个周期数据
        if (rules.indexOf(RuleValue.LASTEST.getType()) >= 0) {
            String lastTime = TimeControl.lasttime(currentTime, cyclename, -1);
            lastest = AlarmService.getByTime(rule, lastTime);
        }
        // 昨天同一时间数据
        if (rules.indexOf(RuleValue.YESCURRENT.getType()) >= 0) {
            TimeCell cell = TimeControl.getYesterday(currentTime, cyclename);
            yescurrent = AlarmService.getByTime(rule, cell.getStarttime());
        }

        String[] arr_levels = rule.getAlarm_level().split(EnumUtil.ALARM_RULE_SPLIT);
        String[] arr_rules = rules.split(EnumUtil.ALARM_RULE_SPLIT);
        String[] arr_msg = rule.getAlarmmsg().split(EnumUtil.ALARM_RULE_SPLIT);
        int[] array_leves = new int[arr_levels.length];
        // 字符转int
        for (int i = 0; i < arr_levels.length; i++) {
            array_leves[i] = Integer.parseInt(arr_levels[i]);
        }
        // 对 告警级别 排序 从高到低排序 告警子规则、告警信息对应排序
        sortAlarmLevel(array_leves, arr_rules, arr_msg);
        boolean isAlarm = false;
        List<ChartData> itemList = new ArrayList<ChartData>();
        for (int i = 0; i < arr_rules.length; i++) {
            isAlarm = isAlarmRule(arr_rules[i], itemList);
            if (isAlarm) {
                // 级别
                rule.setAlarm_level(array_leves[i] + "");
                // 生效规则
                rule.setEffectiveRule(arr_rules[i]);
                // 生效告警信息
                rule.setAlarmmsg(replaceValue(arr_msg[i], itemList));
                // 生效告警值
                rule.setAlarm_mvalue(makeAlarmMvalue(itemList));

                break;
            }
        }

        return isAlarm;
    }

    private String makeAlarmMvalue(List<ChartData> itemList) {
        String value = "";
        if (ToolsUtils.ListIsNull(itemList))
            return value;
        for (ChartData chartData : itemList) {
            if (ToolsUtils.StringIsNull(chartData.getMark())) {
                value = value + "[" + chartData.getValue() + "],";
            } else {
                value = value + "[" + chartData.getMark() + ": " + chartData.getValue() + "],";
            }
        }
        value = value.substring(0, value.length() - 1);
        return value;
    }

    private String replaceValue(String message, List<ChartData> itemList) {
        if (ToolsUtils.ListIsNull(itemList)) {
            return message;
        }
        String chartName = rule.getChart_name();
        MdChartDetail chartDetail = InitParam.getChartDetail(chartName);
        MdChartDataSet chartDataSet = InitParam.getChartDateSet(chartName);
        if (chartDetail == null) {
            return message;
        }
        if (chartDataSet == null) {
            return message;
        }
        String extend = "";
        if (null != chartDetail.getExtend()) {
            extend = chartDetail.getExtend();
        }
        String val = "[";
        String item = "[";
        for (ChartData chartData : itemList) {
            val = val + chartData.getValue() + extend + ",";
            if (!ToolsUtils.StringIsNull(item)) {
                item = item + chartData.getMark() + ",";
            }
        }
        val = val.substring(0, val.length() - 1) + "]";
        item = item.substring(0, item.length() - 1) + "]";
        message = message.replace(ConstantUtil.REPLACE_VAL, val);
        message = message.replace(ConstantUtil.REPLACE_ITEM, item);
        String conditions = chartDataSet.getConditions();
        if (ProviceUtill.PROVINCE_ZYWLW.equals(conf.getProvince())) {
            if (StringUtils.isBlank(conditions)) {
                return message;
            }
            String itemData = null;
            logger.info("sql condition is : {}", conditions);
            int index = StringUtils.indexOf(conditions, "DT.ITEM=");
            if (index > 0) {
                String dtItem = StringUtils.substring(conditions, index + "DT.ITEM=".length());
                String dtItemName = StringUtils.replace(dtItem, "'", "");
                for (ChartData chartData : itemList) {
                    String mark = chartData.getMark();
                    if (StringUtils.equals(mark, dtItemName)) {
                        itemData = mark;
                        break;
                    }
                }
            }
            int likeIndex = StringUtils.indexOf(conditions, "DT.ITEM like ");
            if (likeIndex > 0) {
                String dtItem = StringUtils.substring(conditions,
                        likeIndex + "DT.ITEM like ".length());
                dtItem = StringUtils.replace(dtItem, "'", "");
                String dtItemName = StringUtils.replace(dtItem, "%", "");
                for (ChartData chartData : itemList) {
                    String mark = chartData.getMark();
                    if (StringUtils.contains(mark, dtItemName)) {
                        itemData = mark;
                        break;
                    }
                }
            }
            logger.info("item name is : {}, alarm message is : {}", itemData, message);
            message = converAlarmMsg(message, itemData);
        }
        return message;
    }

    // 解决告警信息出现${0},${1},${2}等等的问题，其中item也只有对应的一条
    // ${0}::${1}_在线重启 对应 ${hostname}_${servernum}_AB_ONLINE_restart
    public String converAlarmMsg(String alarmMsg, String item) {
        if (StringUtils.isBlank(item)) {
            return alarmMsg;
        }
        Pattern pattern = Pattern.compile("\\$\\{\\d+\\}");
        List<String> list = new ArrayList<>();
        Matcher matcher = pattern.matcher(alarmMsg);
        while (matcher.find()) {
            String matchData = matcher.group();
            list.add(matchData);
        }
        if (list.isEmpty()) {
            return alarmMsg;
        }
        for (String alarmData : list) {
            String indexStr = StringUtils.substring(alarmData, 2, alarmData.length() - 1);
            String[] itemArr = StringUtils.split(item, "_");
            int index = Integer.parseInt(indexStr);
            String itemName = itemArr[index];
            alarmMsg = alarmMsg.replace(alarmData, itemName);
        }
        logger.info("after convert alarm msg is : {}", alarmMsg);
        return alarmMsg;
    }

    /**
     * 对告警子规则进行判断是否存在告警 告警子规则必须包含 newest
     * 
     * @param alarmrule
     * @return boolean
     */
    private boolean isAlarmRule(String alarmrule, List<ChartData> itemList) {
        boolean isAlarm = false;
        // 解决有一分钟维度下二次统计反复告警问题
        if (isRunStatis()) {
            if (alarmrule.indexOf(RuleValue.COLLECT.getType()) >= 0
                    && ToolsUtils.ListIsNull(newest)) {
                // 未采集到数据告警
                return true;
            } else if (alarmrule.indexOf(RuleValue.COLLECT.getType()) >= 0
                    && !ToolsUtils.ListIsNull(newest)) {
                return false;
            }

            if (ToolsUtils.ListIsNull(newest)) {
                return false;
            }

            for (ChartData chartData : newest) {
                if (changeExpress(alarmrule, chartData)) {
                    // 存在告警
                    isAlarm = true;
                    itemList.add(chartData);
                }
            }
        }
        return isAlarm;
    }

    private boolean isRunStatis() {
        boolean isRun = true;
        // 解决有一分钟维度下二次统计反复告警问题
        String chartname = rule.getChart_name();
        MdChartDataSet chartDataSet = InitParam.getChartDateSet(chartname);
        if (chartDataSet == null) {
            return false;
        }
        String tablename = chartDataSet.getTable_name();
        tablename = tablename.toUpperCase();
        switch (tablename) {
        case "STATIS_DATA_DAY":
            String mm = DateTools.getCurrentDate("mm");
            String m = mm.substring(mm.length() - 1);
            if ("0".equals(m) || "1".equals(m) || "2".equals(m) || "5".equals(m) || "6".equals(m)
                    || "7".equals(m)) {
                return false;
            }
            break;
        default:
            break;
        }
        return isRun;
    }

    /**
     * 对告警级别、告警规则排序
     * 
     * @param array_leves
     * @param arr_rules
     */
    private void sortAlarmLevel(int[] array_leves, String[] arr_rules, String[] arr_msg) {
        int temp;
        String str = null;
        String msg = null;
        for (int i = 0; i < array_leves.length - 1; i++) {
            for (int j = 0; j < array_leves.length - i - 1; j++) {
                if (array_leves[j + 1] > array_leves[j]) {
                    temp = array_leves[j];
                    array_leves[j] = array_leves[j + 1];
                    array_leves[j + 1] = temp;
                    str = arr_rules[j];
                    arr_rules[j] = arr_rules[j + 1];
                    arr_rules[j + 1] = str;
                    msg = arr_msg[j];
                    arr_msg[j] = arr_msg[j + 1];
                    arr_msg[j + 1] = msg;
                }
            }
        }
    }

    private boolean changeExpress(String alarmrule, ChartData chartData) {
        String expr = new String(alarmrule);
        if (expr.indexOf(RuleValue.NEWEST.getType()) >= 0) {
            expr = replaceAllValue(expr, RuleValue.NEWEST, chartData);
        }
        ChartData lastChartData = null;
        if (expr.indexOf(RuleValue.LASTEST.getType()) >= 0) {
            lastChartData = getChartDataByList(lastest, chartData);
            expr = null == lastChartData ? expr.replaceAll(RuleValue.LASTEST.getType(), "0")
                    : replaceAllValue(expr, RuleValue.LASTEST, lastChartData);
        }
        ChartData yesChartData = null;
        if (expr.indexOf(RuleValue.YESCURRENT.getType()) >= 0) {
            yesChartData = getChartDataByList(yescurrent, chartData);
            expr = null == yesChartData ? expr.replaceAll(RuleValue.YESCURRENT.getType(), "0")
                    : replaceAllValue(expr, RuleValue.YESCURRENT, yesChartData);
        }
        boolean isAlarm = false;
        try {
            isAlarm = Boolean.valueOf(jse.eval(expr).toString());
            // 组装告警信息
            if (isAlarm) {
                rule.addMarks(chartData.getMark());
                rule.addNewestvalues(chartData.getValue());
            }
        } catch (ScriptException e) {
            logger.error(expr);
            logger.error(e.getMessage(), e);
        }

        return isAlarm;
    }

    private ChartData getChartDataByList(List<ChartData> list, ChartData chartData) {
        if (ToolsUtils.ListIsNull(list))
            return null;
        ChartData returnData = null;
        for (ChartData chartData2 : list) {
            if (null == chartData.getMark()) {
                if (null == chartData2.getMark()) {
                    returnData = chartData2;
                    break;
                }
            } else {
                if (null != chartData.getMark()
                        && chartData.getMark().equals(chartData2.getMark())) {
                    returnData = chartData2;
                    break;
                }
            }
        }
        return returnData;
    }

    private String replaceAllValue(String expr, RuleValue rv, ChartData chartData) {
        String express = expr.replaceAll(rv.getType(), chartData.getValue());
        return express;
    }

    private MdAlarmInfo getAlarmInfoByAlarmId() {
        String alarmId = rule.getAlarm_id();
        MdAlarmInfo alarmInfo = null;
        if (ToolsUtils.ListIsNull(alarmInfos)) {
            return null;
        }
        for (MdAlarmInfo info : alarmInfos) {
            if (alarmId.equals(info.getAlarm_id())) {
                alarmInfo = info;
                return alarmInfo;
            }
        }
        return alarmInfo;
    }
}
