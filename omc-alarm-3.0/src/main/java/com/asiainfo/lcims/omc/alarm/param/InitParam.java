package com.asiainfo.lcims.omc.alarm.param;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.business.TimeControl;
import com.asiainfo.lcims.omc.alarm.conf.SystemConf;
import com.asiainfo.lcims.omc.alarm.dao.CommonDAO;
import com.asiainfo.lcims.omc.alarm.model.AlarmMode;
import com.asiainfo.lcims.omc.alarm.model.Area;
import com.asiainfo.lcims.omc.alarm.model.BdNas;
import com.asiainfo.lcims.omc.alarm.model.Host;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmLevel;
import com.asiainfo.lcims.omc.alarm.model.MdApnRecord;
import com.asiainfo.lcims.omc.alarm.model.MdChartDataSet;
import com.asiainfo.lcims.omc.alarm.model.MdChartDetail;
import com.asiainfo.lcims.omc.alarm.model.MdCollCycle;
import com.asiainfo.lcims.omc.alarm.model.MdCollCycleTime;
import com.asiainfo.lcims.omc.alarm.model.MdMetric;
import com.asiainfo.lcims.omc.alarm.model.MdParam;
import com.asiainfo.lcims.omc.alarm.model.Node;
import com.asiainfo.lcims.util.ProviceUtill;
import com.asiainfo.lcims.util.ToolsUtils;

public class InitParam {
    public static final SystemConf conf = new SystemConf();
    private static final Logger logger = LoggerFactory.make();
    private static final List<Host> HOSTS = new ArrayList<Host>();
    private static final List<BdNas> NASLIST = new ArrayList<BdNas>();
    private static final List<Area> AREAS = new ArrayList<Area>();
    private static final List<AlarmMode> MODES = new ArrayList<AlarmMode>();
    private static final List<Node> NODES = new ArrayList<Node>();
    private static final List<MdParam> PARAMS = new ArrayList<MdParam>();
    private static final List<MdAlarmLevel> ALARMLEVELS = new ArrayList<MdAlarmLevel>();
    private static final List<MdCollCycle> COLLCYCLES = new ArrayList<MdCollCycle>();
    private static final List<MdChartDataSet> CHART_DATA_SETS = new ArrayList<MdChartDataSet>();
    private static final List<MdChartDetail> CHART_DETAILS = new ArrayList<MdChartDetail>();
    private static final List<MdMetric> METRICS = new ArrayList<MdMetric>();

    private static final List<MdAlarmInfo> ALARMINFOS = new ArrayList<MdAlarmInfo>();
    private static final List<MdApnRecord> MDAPNRECORDS = new ArrayList<MdApnRecord>();

    // public static final List<DataConver> dataConverList;
    public static void init() {
        HOSTS.clear();
        HOSTS.addAll(CommonDAO.getHosts());
        NASLIST.clear();
        NASLIST.addAll(CommonDAO.getBdNas());
        AREAS.clear();
        AREAS.addAll(CommonDAO.getAreas());
        MODES.clear();
        MODES.addAll(CommonDAO.getModes());
        NODES.clear();
        NODES.addAll(CommonDAO.getNodes());
        PARAMS.clear();
        PARAMS.addAll(CommonDAO.getParams());
        ALARMLEVELS.clear();
        ALARMLEVELS.addAll(CommonDAO.getAlarmLevels());
        COLLCYCLES.clear();
        COLLCYCLES.addAll(CommonDAO.getCollCycles());
        CHART_DATA_SETS.clear();
        CHART_DATA_SETS.addAll(CommonDAO.getChartDataSets());
        CHART_DETAILS.clear();
        CHART_DETAILS.addAll(CommonDAO.getChartDetails());
        METRICS.clear();
        METRICS.addAll(CommonDAO.getMetrics());
        loadApnInfo();
        logger.debug("------ HOSTS length : {} ", HOSTS.size());
        logger.debug("------ NASLIST length : {} ", NASLIST.size());
        logger.debug("------ AREAS length : {} ", AREAS.size());
        logger.debug("------ MODES length : {} ", MODES.size());
        logger.debug("------ NODES length : {} ", NODES.size());
        logger.debug("------ PARAMS length : {} ", PARAMS.size());
        logger.debug("------ ALARMLEVELS length : {} ", ALARMLEVELS.size());
        logger.debug("------ COLLCYCLES length : {} ", COLLCYCLES.size());
        logger.debug("------ CHART_DATA_SETS length : {} ", CHART_DATA_SETS.size());
        logger.debug("------ METRICS length : {} ", METRICS.size());
        logger.info("params databases init ends");
    }

    public static MdMetric getMetricById(String metricId) {
        if (ToolsUtils.ListIsNull(METRICS)) {
            return null;
        }
        for (MdMetric metric : METRICS) {
            if (metricId.equals(metric.getId())) {
                return metric;
            }
        }
        return null;
    }

    public static MdMetric getMetricByIdentity(String metric_identity) {
        if (ToolsUtils.ListIsNull(METRICS)) {
            return null;
        }
        for (MdMetric metric : METRICS) {
            if (metric_identity.equals(metric.getMetric_identity())) {
                return metric;
            }
        }
        return null;
    }

    public static String getHostId(String ip) {
        if (ToolsUtils.ListIsNull(HOSTS)) {
            return null;
        }
        for (Host host : HOSTS) {
            if (ip.equals(host.getAddr())) {
                return host.getHostid();
            }
        }
        return null;
    }

    public static String getHostIp(String hostid) {
        if (ToolsUtils.ListIsNull(HOSTS)) {
            return null;
        }
        for (Host host : HOSTS) {
            if (hostid.equals(host.getHostid())) {
                return host.getAddr();
            }
        }
        return null;
    }

    public static Host getHost(String hostid) {
        if (ToolsUtils.ListIsNull(HOSTS)) {
            return null;
        }
        for (Host host : HOSTS) {
            if (hostid.equals(host.getHostid())) {
                return host;
            }
        }
        return null;
    }

    public static String getNodeId(String hostid) {
        if (ToolsUtils.ListIsNull(HOSTS)) {
            return null;
        }
        for (Host host : HOSTS) {
            if (hostid.equals(host.getHostid())) {
                return host.getNodeid();
            }
        }
        return null;
    }

    public static String getNasId(String ip) {
        if (ToolsUtils.ListIsNull(NASLIST)) {
            return null;
        }
        for (BdNas nas : NASLIST) {
            if (ip.equals(nas.getNas_ip())) {
                return nas.getId();
            }
        }
        return null;
    }

    public static String getNasIp(String nasid) {
        if (ToolsUtils.ListIsNull(NASLIST)) {
            return null;
        }
        for (BdNas nas : NASLIST) {
            if (nasid.equals(nas.getId())) {
                return nas.getNas_ip();
            }
        }
        return null;
    }

    public static BdNas getNas(String nasid) {
        if (ToolsUtils.ListIsNull(NASLIST)) {
            return null;
        }
        for (BdNas nas : NASLIST) {
            if (nasid.equals(nas.getId())) {
                return nas;
            }
        }
        return null;
    }

    public static Area getArea(String areano) {
        if (ToolsUtils.ListIsNull(AREAS)) {
            return null;
        }
        for (Area area : AREAS) {
            if (areano.equals(area.getAreano())) {
                return area;
            }
        }
        return null;
    }

    public static AlarmMode getAlarmMode(String modeid) {
        if (ToolsUtils.ListIsNull(NODES)) {
            return null;
        }
        for (AlarmMode mode : MODES) {
            if (modeid.equals(mode.getModeid())) {
                return mode;
            }
        }
        return null;
    }

    public static Node getNode(String nodeid) {
        if (ToolsUtils.ListIsNull(NODES)) {
            return null;
        }
        for (Node node : NODES) {
            if (nodeid.equals(node.getId())) {
                return node;
            }
        }
        return null;
    }

    public static String getParamType(int type, String description) {
        if (ToolsUtils.ListIsNull(PARAMS)) {
            return null;
        }
        for (MdParam param : PARAMS) {
            if (param.getType().intValue() == type && description.equals(param.getDescription())) {
                return param.getCode();
            }
        }
        return null;
    }

    public static String getParamDescByTypeAndCode(int type, String code) {
        String desc = "";
        if (ToolsUtils.ListIsNull(PARAMS)) {
            return desc;
        }
        for (MdParam param : PARAMS) {
            if (param.getType().intValue() == type && code.equals(param.getCode())) {
                desc = param.getDescription() == null ? "" : param.getDescription();
                return desc;
            }
        }
        return desc;
    }

    public static String getAlarmLevelName(String level) {
        String alarmname = "";
        if (ToolsUtils.ListIsNull(ALARMLEVELS)) {
            return level;
        }
        for (MdAlarmLevel alarmLevel : ALARMLEVELS) {
            if (level.equals(alarmLevel.getAlarmlevel())) {
                alarmname = alarmLevel.getAlarmname() == null ? "" : alarmLevel.getAlarmname();
                break;
            }
        }
        return alarmname;
    }

    public static String getCollCycleName(Integer cycleId) {
        String cycleName = null;
        if (ToolsUtils.ListIsNull(COLLCYCLES)) {
            return null;
        }
        for (MdCollCycle collCycle : COLLCYCLES) {
            if (cycleId != null && cycleId.intValue() == collCycle.getCycleid().intValue()) {
                cycleName = collCycle.getCyclename();
                return cycleName;
            }
        }
        return cycleName;
    }

    public static String getCollCycleId(String cyclename) {
        String cycleid = "";
        if (ToolsUtils.ListIsNull(COLLCYCLES)) {
            return cycleid;
        }
        for (MdCollCycle collCycle : COLLCYCLES) {
            if (cyclename.equals(collCycle.getCyclename())) {
                cycleid = collCycle.getCycleid().toString();
                return cycleid;
            }
        }
        return cycleid;
    }

    public static List<MdCollCycle> getCollCycleListByCyclename(String cyclename) {
        List<MdCollCycle> list = null;
        if (ToolsUtils.StringIsNull(cyclename) || ToolsUtils.ListIsNull(COLLCYCLES)) {
            return COLLCYCLES;
        } else {
            list = new ArrayList<MdCollCycle>();
            for (MdCollCycle collCycle : COLLCYCLES) {
                if (cyclename.equals(collCycle.getCyclename())) {
                    list.add(collCycle);
                }
            }
        }
        return list;
    }

    public static List<MdCollCycleTime> getAllCollCycleTimes(String cyclename, String time) {
        List<MdCollCycle> collCyclelist = getCollCycleListByCyclename(cyclename);
        if (ToolsUtils.ListIsNull(collCyclelist)) {
            return null;
        }
        List<MdCollCycleTime> list = new ArrayList<MdCollCycleTime>();
        for (MdCollCycle mdCollCycle : collCyclelist) {
            String currenttime = TimeControl.getCurrenttime(null, mdCollCycle.getCyclename(),
                    mdCollCycle.getCron());
            list.add(newMdCollCycleTime(mdCollCycle, currenttime));
        }

        return list;
    }

    public static List<MdCollCycleTime> getCollCycleTimes(String cyclename, String time) {
        List<MdCollCycle> collCyclelist = getCollCycleListByCyclename(cyclename);
        if (ToolsUtils.ListIsNull(collCyclelist)) {
            return null;
        }
        List<MdCollCycleTime> list = new ArrayList<MdCollCycleTime>();
        /**
         * time 不为空，则和周期时间比较，相等的执行
         */
        boolean isCureentCycle = false;
        if (!ToolsUtils.StringIsNull(time)) {
            for (MdCollCycle mdCollCycle : collCyclelist) {
                // 获取当前周期时间 getCurrenttime
                String currenttime = TimeControl.getCurrenttime(null, mdCollCycle.getCyclename(),
                        mdCollCycle.getCron());
                if (time.equals(currenttime)) {
                    isCureentCycle = true;
                } else {
                    // 往上推一个周期
                    String currenttime2 = TimeControl.lasttime(currenttime,
                            mdCollCycle.getCyclename(), -1);
                    if (time.equals(currenttime2)) {
                        isCureentCycle = true;
                    }
                }
                if (isCureentCycle) {
                    list.add(newMdCollCycleTime(mdCollCycle, time));
                }
                isCureentCycle = false;
            }
            return list;
        }

        for (MdCollCycle mdCollCycle : collCyclelist) {
            /**
             * 判断当前周期时间与当前时间差是否小于 5 分钟 小于 5 分钟的执行，大于 5 分钟过滤掉
             */
            String currenttime = TimeControl.isCycleTimeInFiveMin(mdCollCycle.getCron(),
                    mdCollCycle.getCyclename());
            if (!ToolsUtils.StringIsNull(currenttime)) {
                list.add(newMdCollCycleTime(mdCollCycle, currenttime));
            }
        }
        return list;
    }

    private static MdCollCycleTime newMdCollCycleTime(MdCollCycle mdCollCycle, String time) {
        MdCollCycleTime cycleTime = new MdCollCycleTime();
        cycleTime.setCycleid(mdCollCycle.getCycleid());
        cycleTime.setCyclename(mdCollCycle.getCyclename());
        cycleTime.setCurrenttime(time);
        return cycleTime;
    }

    public static String getChartDateSetTableName(String chartname) {
        String tablename = "";
        if (ToolsUtils.ListIsNull(CHART_DATA_SETS)) {
            return tablename;
        }
        for (MdChartDataSet chartDataSet : CHART_DATA_SETS) {
            if (chartname.equals(chartDataSet.getChart_name())) {
                tablename = chartDataSet.getTable_name();
                return tablename;
            }
        }
        return tablename;
    }

    public static MdChartDataSet getChartDateSet(String chartname) {
        MdChartDataSet dataSet = null;
        if (ToolsUtils.ListIsNull(CHART_DATA_SETS)) {
            return null;
        }
        for (MdChartDataSet chartDataSet : CHART_DATA_SETS) {
            if (chartname.equals(chartDataSet.getChart_name())) {
                dataSet = chartDataSet;
                return dataSet;
            }
        }
        return dataSet;
    }

    public static MdChartDetail getChartDetail(String chartname) {
        if (ToolsUtils.ListIsNull(CHART_DETAILS)) {
            return null;
        }
        for (MdChartDetail chartDetail : CHART_DETAILS) {
            if (chartname.equals(chartDetail.getChart_name())) {
                return chartDetail;
            }
        }
        return null;
    }

    public static void addAlarmInfos(List<MdAlarmInfo> alarmInfos) {
        ALARMINFOS.clear();
        ALARMINFOS.addAll(alarmInfos);
    }

    public static String getAlarmFirstTime(String alarmId) {
        for (MdAlarmInfo mdAlarmInfo : ALARMINFOS) {
            if (alarmId.equals(mdAlarmInfo.getAlarm_id())) {
                return mdAlarmInfo.getFirst_time();
            }
        }

        return null;
    }

    public static MdParam getParamByTypeAndCode(int type, String code) {
        for (MdParam mdParam : PARAMS) {
            if (mdParam.getType().intValue() == type && code.equals(mdParam.getCode())) {
                return mdParam;
            }
        }
        return null;
    }

    public static String getMetricValDesc(String metricId, String val) {
        String valDesc = val;
        if (!ToolsUtils.StringIsNull(val) && !ToolsUtils.ListIsNull(PARAMS)) {
            MdParam param = getParamByTypeAndCode(41, metricId);
            if (null != param) {
                MdParam param2 = getParamByTypeAndCode(Integer.parseInt(param.getDescription()),
                        val);
                if (null != param2) {
                    return param2.getDescription();
                }
            }
        }
        return valDesc;
    }

    public static void loadApnInfo() {
        if (ProviceUtill.PROVINCE_DEV.equals(conf.getProvince())
                || ProviceUtill.PROVINCE_DEV_REAL.equals(conf.getProvince())
                || ProviceUtill.PROVINCE_CQCM.equals(conf.getProvince())|| ProviceUtill.PROVINCE_ZYWLW.equals(conf.getProvince())) {
            MDAPNRECORDS.clear();
            MDAPNRECORDS.addAll(CommonDAO.getApn());
            logger.debug("------ MDAPNRECORDS length : {} ", MDAPNRECORDS.size());
        }
    }

    public static String getApnName(String apnid) {
        if (ToolsUtils.ListIsNull(MDAPNRECORDS)) {
            return null;
        }
        for (MdApnRecord mdApnRecord : MDAPNRECORDS) {
            if (apnid.equals(mdApnRecord.getApnid())) {
                return mdApnRecord.getApn();
            }
        }
        return null;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static List<Host> getHosts() {
        return HOSTS;
    }

    public static List<BdNas> getNaslist() {
        return NASLIST;
    }

    public static List<Area> getAreas() {
        return AREAS;
    }

    public static List<AlarmMode> getModes() {
        return MODES;
    }

    public static List<Node> getNodes() {
        return NODES;
    }

    public static List<MdParam> getParams() {
        return PARAMS;
    }

    public static List<MdAlarmLevel> getAlarmlevels() {
        return ALARMLEVELS;
    }

    public static List<MdCollCycle> getCollcycles() {
        return COLLCYCLES;
    }

    public static List<MdChartDataSet> getChartDataSets() {
        return CHART_DATA_SETS;
    }

    public static List<MdChartDetail> getChartDetails() {
        return CHART_DETAILS;
    }

    public static List<MdMetric> getMetrics() {
        return METRICS;
    }

    public static List<MdAlarmInfo> getAlarminfos() {
        return ALARMINFOS;
    }

    public static List<MdApnRecord> getApns() {
        return MDAPNRECORDS;
    }

    // public static String getDataConver(String converFrom) {
    // if (dataConverList == null || dataConverList.isEmpty() || converFrom ==
    // null) {
    // return converFrom;
    // }
    // for (DataConver info : dataConverList) {
    // if (converFrom.equals(info.getConverfrom())) {
    // return info.getConverto();
    // }
    // }
    // return converFrom;
    // }
}
