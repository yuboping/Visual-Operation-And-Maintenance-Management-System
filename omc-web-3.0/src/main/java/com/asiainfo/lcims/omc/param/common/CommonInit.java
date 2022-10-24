package com.asiainfo.lcims.omc.param.common;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.lcims.omc.boot.JettyServerConf;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.model.apn.MdApnRecord;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdAlarmMode;
import com.asiainfo.lcims.omc.model.configmanage.MdBusinessHost;
import com.asiainfo.lcims.omc.model.configmanage.MdEquipmentModel;
import com.asiainfo.lcims.omc.model.configmanage.MdFactory;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdModule;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.configmanage.MdProcess;
import com.asiainfo.lcims.omc.model.configmanage.MdSoundAlarm;
import com.asiainfo.lcims.omc.model.monitor.MdChartDataSet;
import com.asiainfo.lcims.omc.model.monitor.MdChartDetail;
import com.asiainfo.lcims.omc.model.monitor.MdPageCharts;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.model.system.MdCollCycle;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.model.system.MdMenuTree;
import com.asiainfo.lcims.omc.persistence.apn.MdApnRecordDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.AlarmmodeManageDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.AreaDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.BdNasDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MAdminDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdBusinessHostDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdEquipmentDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdFactoryDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdHostFileSystemDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdMetricDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdNodeDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdProcessDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MonHostDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.SoundAlarmManageDAO;
import com.asiainfo.lcims.omc.persistence.monitor.ChartDatsetDAO;
import com.asiainfo.lcims.omc.persistence.monitor.ChartDetailDAO;
import com.asiainfo.lcims.omc.persistence.monitor.PageChartDAO;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.persistence.system.MdCollCycleDAO;
import com.asiainfo.lcims.omc.persistence.system.MdMenuDAO;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.ProviceUtill;
import com.asiainfo.lcims.omc.util.ReadFile;
import com.asiainfo.lcims.omc.util.ToolsUtils;

@Service(value = "commoninit")
public class CommonInit {

    private static final Logger LOG = LoggerFactory.getLogger(CommonInit.class);

    public static final JettyServerConf conf = new JettyServerConf();
    public static final BusinessConf BUSCONF = new BusinessConf();

    private static final List<MdChartDataSet> chartDataSets = new ArrayList<MdChartDataSet>();
    private static final List<MdChartDetail> chartDetails = new ArrayList<MdChartDetail>();
    private static final List<MdPageCharts> pageCharts = new ArrayList<MdPageCharts>();

    private static final List<MdMetric> metriclist = new ArrayList<MdMetric>();

    private static final List<MdCollCycle> collcylelist = new ArrayList<MdCollCycle>();

    private static final List<MdModule> modulelist = new ArrayList<MdModule>();

    private static final List<MdMenu> menulist = new ArrayList<MdMenu>();

    private static final List<MdMenuTree> mdMenuTreeList = new ArrayList<MdMenuTree>();

    private static final List<MonHost> monHostList = new ArrayList<MonHost>();

    private static final List<MdArea> mdAreaList = new ArrayList<MdArea>();

    private static final List<MdProcess> mdProcessList = new ArrayList<MdProcess>();

    private static final List<MdEquipmentModel> mdEquipmentModelList = new ArrayList<MdEquipmentModel>();

    private static final List<MdFactory> mdFactoryList = new ArrayList<MdFactory>();

    private static final List<MAdmin> mAdminList = new ArrayList<MAdmin>();

    private static final List<BdNas> bdNasList = new ArrayList<BdNas>();

    private static final List<MdAlarmMode> mdAlarmModeList = new ArrayList<MdAlarmMode>();

    private static final List<MdNode> nodeList = new ArrayList<MdNode>();

    private static final List<MdSoundAlarm> soundAlarmList = new ArrayList<MdSoundAlarm>();

    private static final List<MdBusinessHost> mdBusinessHostList = new ArrayList<MdBusinessHost>();

    private static final List<MdApnRecord> mdApnRecordList = new ArrayList<MdApnRecord>();

    @Inject
    private ChartDatsetDAO chartDatsetDAO;

    @Inject
    private ChartDetailDAO chartDetailDAO;

    @Inject
    private PageChartDAO pageChartDAO;

    @Inject
    private MdMetricDAO mdMetricDAO;

    @Inject
    private MdCollCycleDAO cycleDAO;

    @Inject
    private MdMenuDAO mdMenuDAO;

    @Inject
    private MonHostDAO monHostDAO;

    @Inject
    private AreaDAO areaDAO;

    @Inject
    private MdProcessDAO mdProcessDAO;

    @Inject
    private MdHostFileSystemDAO mdHostFileSystemDAO;

    @Autowired
    private MdEquipmentDAO mdEquipmentDAO;

    @Autowired
    private MdFactoryDAO mdFactoryDAO;

    @Autowired
    private MAdminDAO mAdminDAO;

    @Autowired
    private BdNasDAO bdNasDAO;

    @Autowired
    private AlarmmodeManageDAO alarmmodeManageDAO;

    @Autowired
    private MdNodeDAO nodeDAO;

    @Autowired
    private SoundAlarmManageDAO soundAlarmManageDAO;

    @Autowired
    private MdBusinessHostDAO mdBusinessHostDAO;

    @Autowired
    private MdApnRecordDAO mdApnRecordDAO;

    public void init() {
        loadChartInfo();
        loadMetricInfos();
        loadCollcyleInfo();
        loadMenuInfos();
        loadMenuTreeInfos();
        loadAreaInfo();
        loadProcessInfo();
        loadMdEquipmentInfo();
        loadMdFactoryInfo();
        loadMAdminInfo();
        loadBdNasInfo();
        loadMdAlarmModeInfo();
        loadNodelist();
        loadMdBusinessHostList();
        loadApnInfo();
        // loadSoundAlarmList();
    }

    public void loadMenuInfos() {
        List<MdMenu> datalist = mdMenuDAO.getMenuList();
        menulist.clear();
        menulist.addAll(datalist);
    }

    /**
     * 主机增删改时调用刷新缓存信息
     */
    public void refreshMdHost() {
        List<MonHost> datalist = monHostDAO.getAllHost();
        monHostList.clear();
        monHostList.addAll(datalist);
    }

    public void loadMenuTreeInfos() {
        List<MdMenuTree> datalist1 = mdMenuDAO.getMenuTreeList();
        List<MonHost> datalist2 = monHostDAO.getAllHost();
        mdMenuTreeList.clear();
        monHostList.clear();
        mdMenuTreeList.addAll(datalist1);
        monHostList.addAll(datalist2);
    }

    public void loadCollcyleInfo() {
        List<MdCollCycle> datalist = cycleDAO.getMdCollCycle();
        collcylelist.clear();
        collcylelist.addAll(datalist);
    }

    public void refreshBdNas() {
        BdNas nas = new BdNas();
        List<BdNas> datas = bdNasDAO.getBdNas(nas);
        bdNasList.clear();
        bdNasList.addAll(datas);
    }

    public void refreshMetriclist() {
        loadMetricInfos();
    }

    public void loadChartInfo() {

        List<MdPageCharts> dataPageCharts = pageChartDAO.getPageChartsAll();
        List<MdChartDataSet> dataChartDataSets = chartDatsetDAO.getChartDataSetAll();
        List<MdChartDetail> dataChartDetails = chartDetailDAO.getChartDetailAll();
        pageCharts.clear();
        chartDataSets.clear();
        chartDetails.clear();
        pageCharts.addAll(dataPageCharts);
        chartDataSets.addAll(dataChartDataSets);
        chartDetails.addAll(dataChartDetails);
    }

    public void loadProcessInfo() {
        List<MdProcess> dataProcessList = mdProcessDAO.getAllMdProcess();
        mdProcessList.clear();
        mdProcessList.addAll(dataProcessList);
    }

    public void loadAreaInfo() {
        List<MdArea> dataAreaList = areaDAO.getAll();
        mdAreaList.clear();
        mdAreaList.addAll(dataAreaList);
    }

    public void loadMdEquipmentInfo() {
        List<MdEquipmentModel> datas = mdEquipmentDAO.getAll();
        mdEquipmentModelList.clear();
        mdEquipmentModelList.addAll(datas);
    }

    public void loadMdFactoryInfo() {
        List<MdFactory> datas = mdFactoryDAO.getAll();
        mdFactoryList.clear();
        mdFactoryList.addAll(datas);
    }

    public void loadMAdminInfo() {
        List<MAdmin> datas = mAdminDAO.getAll();
        mAdminList.clear();
        mAdminList.addAll(datas);
    }

    public void loadBdNasInfo() {
        BdNas nas = new BdNas();
        List<BdNas> datas = bdNasDAO.getBdNas(nas);
        bdNasList.clear();
        bdNasList.addAll(datas);
    }

    public void loadMdAlarmModeInfo() {
        List<MdAlarmMode> datas = alarmmodeManageDAO.getAll();
        mdAlarmModeList.clear();
        mdAlarmModeList.addAll(datas);
    }

    public void loadMetricInfos() {
        List<MdMetric> datas = mdMetricDAO.getMdMetric(new MdMetric());
        metriclist.clear();
        metriclist.addAll(datas);
    }

    public void loadNodelist() {
        List<MdNode> datas = nodeDAO.getMdNode(new MdNode());
        nodeList.clear();
        nodeList.addAll(datas);
    }

    public void loadSoundAlarmList() {
        List<MdSoundAlarm> datas = soundAlarmManageDAO.getMdSoundAlarmList();
        soundAlarmList.clear();
        soundAlarmList.addAll(datas);
    }

    public void loadMdBusinessHostList() {
        List<MdBusinessHost> datas = mdBusinessHostDAO.getMdBusinessHost(new MdBusinessHost());
        mdBusinessHostList.clear();
        mdBusinessHostList.addAll(datas);
    }

    public void loadApnInfo() {
        if (!StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_DEV, ReadFile.PROVINCE)
                && !StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_DEV_REAL, ReadFile.PROVINCE)
                && !StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_CQCMTEST, ReadFile.PROVINCE)
                && !StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_CQCM, ReadFile.PROVINCE)) {
            return;
        }
        List<MdApnRecord> apnList = mdApnRecordDAO.getAllApn();
        mdApnRecordList.clear();
        mdApnRecordList.addAll(apnList);
    }

    public static MdApnRecord getApnByName(String apn) {
        for (MdApnRecord apnRecord : mdApnRecordList) {
            String apnName = apnRecord.getApn();
            if (StringUtils.equals(apn, apnName)) {
                return apnRecord;
            }
        }
        return null;
    }

    public static List<MdNode> getNodeList() {
        List<MdNode> data = new ArrayList<MdNode>();
        if (ToolsUtils.ListIsNull(nodeList))
            return null;
        data.addAll(nodeList);
        return data;
    }

    public static MdNode getNodeById(String id) {
        MdNode node = null;
        if (!ToolsUtils.ListIsNull(nodeList)) {
            for (MdNode n : nodeList) {
                if (id.equals(n.getId())) {
                    node = n;
                    break;
                }
            }
        }
        return node;
    }

    public static MonHost getHost(String hostid) {
        MonHost host = null;
        for (MonHost h : monHostList) {
            if (hostid.equals(h.getHostid())) {
                host = h;
                break;
            }
        }
        return host;
    }

    public static MonHost getHostByIp(String ip) {
        MonHost host = null;
        for (MonHost h : monHostList) {
            if (ip.equals(h.getAddr())) {
                host = h;
                break;
            }
        }
        return host;
    }

    public static MonHost getHostByIps(String ips) {
        MonHost host = null;
        for (MonHost h : monHostList) {
            if (matchHost(ips, h)) {
                return h;
            }
        }
        return host;
    }

    /**
     * 根据vimd获取主机服务器
     * 
     * @param vmid
     * @return
     */
    public static MonHost getHostByVmid(String vmid) {
        MonHost host = null;
        if (ToolsUtils.StringIsNull(vmid))
            return host;
        for (MonHost h : monHostList) {
            if (vmid.equals(h.getVmid())) {
                return h;
            }
        }
        return host;
    }

    private static boolean matchHost(String ips, MonHost host) {
        boolean match = false;
        if (ips.equals(host.getAddr()))
            return true;
        if (!ToolsUtils.StringIsNull(ips)) {
            String[] ipsArr = ips.split(Constant.IP_SPIT);
            for (String ip : ipsArr) {
                ip = ip.trim();
                if (host.getAddr().equals(ip)) {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }

    public static String getMetricNameByMetricId(String metricId) {
        String metricName = "";
        for (MdMetric metric : metriclist) {
            if (metricId.equals(metric.getId())) {
                metricName = metric.getMetric_name();
                break;
            }
        }
        return metricName;
    }

    public static String getMetricIdByMetricIdentity(String metricIdentity) {
        String metricId = "";
        for (MdMetric metric : metriclist) {
            if (metricIdentity.equals(metric.getMetric_identity())) {
                metricId = metric.getId();
                break;
            }
        }
        return metricId;
    }

    /**
     * 根据pageUri 获取 MdChartDetail，并对MdChartDetail中的url加上参数 urlParam
     * 
     * @param pageUri
     * @param urlParam
     * @return
     */
    public static List<MdChartDetail> getChartDetailsByPageUri(String pageUri, String urlParam,
            List<MdAlarmInfo> infos) {
        List<MdChartDetail> list = new ArrayList<MdChartDetail>();
        List<MdPageCharts> pageChartslist = getPageChartsByURi(pageUri, true);
        for (MdPageCharts mdPageCharts : pageChartslist) {
            for (MdChartDetail mdChartDetail : chartDetails) {
                if (mdPageCharts.getChart_name().equals(mdChartDetail.getChart_name())
                        && mdPageCharts.getIs_show() == 1) {
                    for (MdChartDataSet mdChartDataSet : chartDataSets) {
                        if (mdPageCharts.getChart_name().equals(mdChartDataSet.getChart_name())) {
                            mdChartDetail.setChart_interval(mdChartDataSet.getChart_interval());
                            mdChartDetail.setScope(mdChartDataSet.getScope());
                        }
                    }
                    cloneChartDetail(list, mdChartDetail, urlParam, pageUri, infos);
                }
            }
        }
        return list;
    }

    public static MdChartDetail getChartDetailByChartName(String chartname) {
        for (MdChartDetail mdChartDetail : chartDetails) {
            if (chartname.equals(mdChartDetail.getChart_name()))
                return mdChartDetail;
        }
        return null;
    }

    public static MdChartDataSet getChartDataSetByChartName(String chartname) {
        for (MdChartDataSet mdChartDataSet : chartDataSets) {
            if (chartname.equals(mdChartDataSet.getChart_name()))
                return new MdChartDataSet(mdChartDataSet);
        }
        return null;
    }

    public static List<MdPageCharts> getPageChartsByURi(String uri, boolean flag) {
        List<MdPageCharts> list = new ArrayList<MdPageCharts>();
        for (MdPageCharts pageChart : pageCharts) {
            if (uri.equals(pageChart.getUrl()))
                list.add(pageChart);
        }
        if (list.size() == 0) {
            uri = uri.split("" + ConstantUtill.URL_SPLIT)[0];
            if (flag)
                list = getPageChartsByURi(uri, false);
        }
        return list;
    }

    public static void cloneChartDetail(List<MdChartDetail> list, MdChartDetail mdChartDetail,
            String urlParam, String pageUri, List<MdAlarmInfo> infos) {
        MdChartDetail chartDetail = new MdChartDetail(mdChartDetail);
        if (ToolsUtils.StringIsNull(urlParam)) {
            urlParam = "chart_name=" + chartDetail.getChart_name();
        } else {
            urlParam = urlParam + "&chart_name=" + chartDetail.getChart_name();
        }
        String data_url = chartDetail.getData_url();
        if (data_url.contains("?")) {
            data_url = data_url + urlParam;
        } else {
            data_url = data_url + "?" + urlParam;
        }
        chartDetail.setData_url(data_url);
        // 显示具体图表的告警信息
        List<MdAlarmInfo> alarmInfoList = getByUrlAndName(pageUri, mdChartDetail.getChart_name(),
                infos);
        if (alarmInfoList != null && !alarmInfoList.isEmpty()) {
            List<String> alarmMsgList = getMsgByAlarmNum(alarmInfoList);
            if (!alarmMsgList.isEmpty()) {
                // 多条告警信息以分号拼接
                String alarmMsgStr = StringUtils.join(alarmMsgList, ";");
                chartDetail.setAlarm(true);
                chartDetail.setAlarmmsg(alarmMsgStr);
            }
        }
        list.add(chartDetail);
    }

    // 从同一图表中选取多个告警信息
    public static List<String> getMsgByAlarmNum(List<MdAlarmInfo> alarmInfoList) {
        List<String> alarmMsgList = new ArrayList<>();
        for (MdAlarmInfo alarmInfo : alarmInfoList) {
            Integer alarmNum = alarmInfo.getAlarm_num();
            if (alarmNum > 0) {
                String alarmmsg = alarmInfo.getAlarmmsg();
                alarmMsgList.add(alarmmsg);
            }
        }
        return alarmMsgList;
    }

    public static List<MdAlarmInfo> getByUrlAndName(String pageUrl, String chartNameStr,
            List<MdAlarmInfo> infos) {
        List<MdAlarmInfo> alarmList = new ArrayList<>();
        if (infos == null) {
            LOG.debug("alarm info list is null, please check...");
            return null;
        }
        for (MdAlarmInfo mdAlarmInfo : infos) {
            String url = mdAlarmInfo.getUrl();
            String chartName = mdAlarmInfo.getChart_name();
            if (url.equals(pageUrl) && chartName.equals(chartNameStr)) {
                alarmList.add(mdAlarmInfo);
            }
        }
        return alarmList;
    }

    public static MdMetric getMetricByIdentity(String metricIdnntity) {
        if (ToolsUtils.ListIsNull(metriclist))
            return null;
        for (MdMetric metric : metriclist) {
            if (metricIdnntity.equals(metric.getMetric_identity())) {
                return metric;
            }
        }
        return null;
    }

    public static Integer getCycleIdFromMetric(String mecric_id) {
        if (ToolsUtils.ListIsNull(metriclist))
            return null;
        Integer cycleId = null;
        for (MdMetric metric : metriclist) {
            if (mecric_id.equals(metric.getId())) {
                cycleId = metric.getCycle_id();
                break;
            }
        }
        return cycleId;
    }

    public static String getCollCycleCronById(Integer cycleId) {
        if (ToolsUtils.ListIsNull(collcylelist) || null == cycleId) {
            return null;
        }
        String cron = null;
        for (MdCollCycle ccyle : collcylelist) {
            if (ccyle.getCycleid().intValue() == cycleId.intValue()) {
                cron = ccyle.getCron();
                break;
            }
        }
        return cron;
    }

    public static List<MdModule> getModulelist() {

        return modulelist;
    }

    public static void setModulelist(List<MdModule> list) {
        modulelist.clear();
        modulelist.addAll(list);
    }

    public static List<MdMenu> getMenuList() {
        return menulist;
    }

    public static List<MdMenuTree> getMdMenuTreeList() {
        return mdMenuTreeList;
    }

    public static MdMenuTree getMdMenuTreeById(String id) {
        if (ToolsUtils.ListIsNull(mdMenuTreeList))
            return null;
        for (MdMenuTree menu : mdMenuTreeList) {
            if (id.equals(menu.getId())) {
                return menu;
            }
        }
        return null;
    }

    public static List<MonHost> getMonHostList() {
        return monHostList;
    }

    public static List<MdArea> getMdAreaList() {
        return mdAreaList;
    }

    public static List<MdMetric> getMetricList() {
        return metriclist;
    }

    public static MdArea getAreaByAreano(String areano) {
        if (ToolsUtils.ListIsNull(mdAreaList)) {
            return null;
        }
        for (MdArea area : mdAreaList) {
            if (area.getAreano().equals(areano)) {
                return area;
            }
        }
        return null;
    }

    public static String getAreaNoByAreaname(String areaname) {
        if (ToolsUtils.ListIsNull(mdAreaList)) {
            return null;
        }
        for (MdArea area : mdAreaList) {
            if (area.getName().contains(areaname)) {
                return area.getAreano();
            }
        }
        return null;
    }

    public static List<MdProcess> getMdProcessList() {
        return mdProcessList;
    }

    public static List<MdSoundAlarm> getMdSoundAlarmList() {
        return soundAlarmList;
    }

    public static List<MAdmin> getMAdminList() {
        return mAdminList;
    }

    public static List<MdFactory> getMdFactoryList() {
        return mdFactoryList;
    }

    public static List<BdNas> getBdNasList() {
        return bdNasList;
    }

    public static List<MdBusinessHost> getMdbusinesshostlist() {
        return mdBusinessHostList;
    }

    public static BdNas getBdNasById(String id) {
        BdNas nas = null;
        if (!ToolsUtils.ListIsNull(bdNasList)) {
            for (BdNas bdNas : bdNasList) {
                if (id.equals(bdNas.getId())) {
                    nas = bdNas;
                    break;
                }
            }
        }
        return nas;
    }

    public static List<MdAlarmMode> getMdAlarmModeList() {
        return mdAlarmModeList;
    }

    public static MdAlarmMode getAlarmModeByModetype(String modeType) {
        if (CollectionUtils.isEmpty(mdAlarmModeList)) {
            return null;
        }
        for (MdAlarmMode mdAlarmMode : mdAlarmModeList) {
            String type = mdAlarmMode.getModetype();
            if (StringUtils.equals(modeType, type)) {
                return mdAlarmMode;
            }
        }
        return null;
    }

    public static List<MdEquipmentModel> getMdEquipmentModelList() {
        return mdEquipmentModelList;
    }

    public static List<MdApnRecord> getMdApnRecordList() {
        return mdApnRecordList;
    }

    /**
     * 
     * @Title: refreshfilesystem @Description: (刷新文件系统表) @param @param date
     *         参数 @return void 返回类型 @throws
     */
    @Transactional
    public void refreshfilesystem(String date) {
        String metricId = BUSCONF.getFileSystemMetricId();
        mdHostFileSystemDAO.truncate();
        mdHostFileSystemDAO.insertFileSystem(date, metricId);
    }
}
