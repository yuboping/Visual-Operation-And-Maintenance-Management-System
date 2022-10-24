package com.asiainfo.lcims.omc.service.configmanage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmLevel;
import com.asiainfo.lcims.omc.model.apn.MdApnRecord;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdAlarmMode;
import com.asiainfo.lcims.omc.model.configmanage.MdAlarmRule;
import com.asiainfo.lcims.omc.model.configmanage.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.model.configmanage.MdBusinessHost;
import com.asiainfo.lcims.omc.model.configmanage.MdHostFileSystem;
import com.asiainfo.lcims.omc.model.configmanage.MdHostMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdHostProcess;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.monitor.MdPageCharts;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.alarm.MdAlarmInfoDao;
import com.asiainfo.lcims.omc.persistence.configmanage.AlarmmodeManageDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.HostMetricDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdAlarmRuleDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdAlarmRuleDetailDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdHostFileSystemDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdHostProcessDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdMetricDAO;
import com.asiainfo.lcims.omc.persistence.monitor.PageChartDAO;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.persistence.system.MdParamDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.service.system.MenuService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.StringUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

@Service(value = "alarmRuleManageService")
public class AlarmRuleManageService {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmRuleManageService.class);

    private static String fileSystemMetricId = CommonInit.BUSCONF.getFileSystemMetricId();
    private static String authenFailMetricId = CommonInit.BUSCONF.getAuthenFailMetricId();

    @Resource(name = "menuService")
    MenuService menuService;

    @Inject
    private MdAlarmRuleDAO mdAlarmRuleDAO;

    @Inject
    private PageChartDAO pageChartDAO;

    @Inject
    private MdHostProcessDAO mdHostProcessDAO;

    @Inject
    private MdMetricDAO mdMetricDAO;

    @Inject
    private HostMetricDAO hostMetricDAO;

    @Inject
    private MdParamDAO mdParamDAO;

    @Inject
    private MdHostFileSystemDAO mdHostFileSystemDAO;

    @Inject
    private MdAlarmRuleDetailDAO mdAlarmRuleDetailDAO;

    @Inject
    private MdAlarmInfoDao mdAlarmInfoDao;

    @Inject
    private AlarmmodeManageDAO alarmmodeManageDAO;

    @Autowired
    private OperateHisService operateHisService;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    /**
     * 
     * @Title: getModuleList @Description: (获取模块列表) @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    public List<MdMenu> getModuleList() {
        List<MdMenu> result = new ArrayList<MdMenu>();
        List<String> urlList = new ArrayList<String>();
        List<String> mdMenuIdList = new ArrayList<String>();
        List<Map<String, Object>> pageChartList = pageChartDAO.getGroupURL();
        for (Map<String, Object> map : pageChartList) {
            String URL = map.get("URL").toString();
            String sURL = URL.split(ConstantUtill.URL_SPLIT)[0];
            if (!urlList.contains(sURL)) {
                urlList.add(sURL);
                MdMenu mdMenu = menuService.getTwoLevelMdMenu(sURL);
                if (null != mdMenu) {
                    if (!mdMenuIdList.contains(mdMenu.getId())) {
                        mdMenuIdList.add(mdMenu.getId());
                        result.add(mdMenu);
                    }
                } else {
                    LOG.error(
                            "AlarmRuleManageService getModuleList getTwoLevelMdMenu return mdMenu is null , URL:{} , sURL:{}",
                            URL, sURL);
                }
            }
        }
        return result;
    }

    /**
     * 
     * @Title: getMonitorTargetOneLevelList @Description:
     *         (获取一级监控目标) @param @return 参数 @return List<Map<String,Object>>
     *         返回类型 @throws
     */
    public List<Map<String, Object>> getMonitorTargetOneLevelList(String id) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<MdMenu> mdMenuList = menuService.getParentMdMenuList(id);
        for (MdMenu mdMenu : mdMenuList) {
            String url = mdMenu.getUrl();
            if (judgeIsContainsPageChartByURL(url)) {
                String dynamic = mdMenu.getDynamic();
                switch (dynamic) {
                case Constant.DYNAMICTYPE_NODE:
                    Map<String, Object> nodeallmap = new HashMap<String, Object>();
                    nodeallmap.put("mdMenu", mdMenu);
                    nodeallmap.put("type", Constant.MONITORTARGET_ALL);
                    nodeallmap.put("dimensiontype", Constant.DYNAMICTYPE_NODE);
                    nodeallmap.put("name", Constant.ALL_NODE);
                    nodeallmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_ALL);
                    result.add(nodeallmap);
                    Map<String, Object> nodesinglemap = new HashMap<String, Object>();
                    nodesinglemap.put("mdMenu", mdMenu);
                    nodesinglemap.put("type", Constant.MONITORTARGET_SINGLE);
                    nodesinglemap.put("dimensiontype", Constant.DYNAMICTYPE_NODE);
                    nodesinglemap.put("name", Constant.SINGLE_NODE);
                    nodesinglemap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SINGLE);
                    result.add(nodesinglemap);
                    break;
                case Constant.DYNAMICTYPE_AREA:
                    Map<String, Object> areaallmap = new HashMap<String, Object>();
                    areaallmap.put("mdMenu", mdMenu);
                    areaallmap.put("type", Constant.MONITORTARGET_ALL);
                    areaallmap.put("dimensiontype", Constant.DYNAMICTYPE_AREA);
                    areaallmap.put("name", Constant.ALL_AREA);
                    areaallmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_ALL);
                    result.add(areaallmap);
                    Map<String, Object> areasinglemap = new HashMap<String, Object>();
                    areasinglemap.put("mdMenu", mdMenu);
                    areasinglemap.put("type", Constant.MONITORTARGET_SINGLE);
                    areasinglemap.put("dimensiontype", Constant.DYNAMICTYPE_AREA);
                    areasinglemap.put("name", Constant.SINGLE_AREA);
                    areasinglemap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SINGLE);
                    result.add(areasinglemap);
                    break;
                case Constant.DYNAMICTYPE_HOST:
                    Map<String, Object> hostallmap = new HashMap<String, Object>();
                    hostallmap.put("mdMenu", mdMenu);
                    hostallmap.put("type", Constant.MONITORTARGET_ALL);
                    hostallmap.put("dimensiontype", Constant.DYNAMICTYPE_HOST);
                    hostallmap.put("name", Constant.ALL_HOST);
                    hostallmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_ALL);
                    result.add(hostallmap);
                    Map<String, Object> hostsinglemap = new HashMap<String, Object>();
                    hostsinglemap.put("mdMenu", mdMenu);
                    hostsinglemap.put("type", Constant.MONITORTARGET_SINGLE);
                    hostsinglemap.put("dimensiontype", Constant.DYNAMICTYPE_HOST);
                    hostsinglemap.put("name", Constant.SINGLE_HOST);
                    hostsinglemap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SINGLE);
                    result.add(hostsinglemap);
                    break;
                case Constant.DYNAMICTYPE_BRAS:
                    Map<String, Object> brasallmap = new HashMap<String, Object>();
                    brasallmap.put("mdMenu", mdMenu);
                    brasallmap.put("type", Constant.MONITORTARGET_ALL);
                    brasallmap.put("dimensiontype", Constant.DYNAMICTYPE_BRAS);
                    brasallmap.put("name", Constant.ALL_BRAS);
                    brasallmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_ALL);
                    result.add(brasallmap);
                    Map<String, Object> brassinglemap = new HashMap<String, Object>();
                    brassinglemap.put("mdMenu", mdMenu);
                    brassinglemap.put("type", Constant.MONITORTARGET_SINGLE);
                    brassinglemap.put("dimensiontype", Constant.DYNAMICTYPE_BRAS);
                    brassinglemap.put("name", Constant.SINGLE_BRAS);
                    brassinglemap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SINGLE);
                    result.add(brassinglemap);
                    break;
                case Constant.DYNAMICTYPE_APN:
                    Map<String, Object> apnallmap = new HashMap<String, Object>();
                    apnallmap.put("mdMenu", mdMenu);
                    apnallmap.put("type", Constant.MONITORTARGET_ALL);
                    apnallmap.put("dimensiontype", Constant.DYNAMICTYPE_APN);
                    apnallmap.put("name", Constant.ALL_APN);
                    apnallmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_ALL);
                    result.add(apnallmap);
                    Map<String, Object> apnsinglemap = new HashMap<String, Object>();
                    apnsinglemap.put("mdMenu", mdMenu);
                    apnsinglemap.put("type", Constant.MONITORTARGET_SINGLE);
                    apnsinglemap.put("dimensiontype", Constant.DYNAMICTYPE_APN);
                    apnsinglemap.put("name", Constant.SINGLE_APN);
                    apnsinglemap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SINGLE);
                    result.add(apnsinglemap);
                    break;
                case Constant.DYNAMICTYPE_STATIC:
                    Map<String, Object> staticmap = new HashMap<String, Object>();
                    staticmap.put("mdMenu", mdMenu);
                    staticmap.put("type", Constant.MONITORTARGET_SUMMARY);
                    staticmap.put("dimensiontype", Constant.DYNAMICTYPE_STATIC);
                    staticmap.put("name", mdMenu.getShow_name());
                    staticmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SUMMARY);
                    result.add(staticmap);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 
     * @Title: getMonitorTargetTwoLevelList @Description:
     *         (获取二级监控目标) @param @param id @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    public List<Map<String, Object>> getMonitorTargetTwoLevelList(
            Map<String, Object> monitorTargetOneMap) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String name = monitorTargetOneMap.get("name").toString();
        String dynamic = monitorTargetOneMap.get("dynamic").toString();
        String type = monitorTargetOneMap.get("type").toString();
        if (type.equals(Constant.MONITORTARGET_SINGLE)) {
            switch (dynamic) {
            case Constant.DYNAMICTYPE_NODE:
                for (MdNode mdNode : MdMenuDataListener.getMdNodeList()) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", mdNode.getId());
                    m.put("name", mdNode.getNode_name());
                    result.add(m);
                }
                break;
            case Constant.DYNAMICTYPE_AREA:
                for (MdArea mdArea : MdMenuDataListener.getMdAreaList()) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", mdArea.getAreano());
                    m.put("name", mdArea.getName());
                    result.add(m);
                }
                break;
            case Constant.DYNAMICTYPE_HOST:
                List<MonHost> monHostList = new ArrayList<MonHost>();
                // 筛选业务下的主机
                List<MdBusinessHost> mdBusinessHostList = mdMenuDataListener
                        .getMdBusinessHostListByName(name);
                for (MdBusinessHost mdBusinessHost : mdBusinessHostList) {
                    monHostList.add(mdMenuDataListener.getMonHostById(mdBusinessHost.getHostid()));
                }
                for (MonHost monHost : monHostList) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", monHost.getHostid());
                    m.put("name", monHost.getHostname());
                    result.add(m);
                }
                break;
            case Constant.DYNAMICTYPE_BRAS:
                for (BdNas bdNas : MdMenuDataListener.getBdNasList()) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", bdNas.getId());
                    m.put("name", bdNas.getNas_name());
                    result.add(m);
                }
                break;
            case Constant.DYNAMICTYPE_APN:
                for (MdApnRecord mdApnRecord : CommonInit.getMdApnRecordList()) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", mdApnRecord.getApnid());
                    m.put("name", mdApnRecord.getApn());
                    result.add(m);
                }
                break;
            }
        }
        return result;
    }

    /**
     * 
     * @Title: getMonitorTargetThreeLevelList @Description:
     *         (获取三级监控目标) @param @param id @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    public List<Map<String, Object>> getMonitorTargetThreeLevelList(String id) {
        String type = id.substring(id.length() - 1);
        id = id.substring(0, id.length() - 1);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<MdMenu> mdMenuList = menuService.getParentMdMenuList(id);
        for (MdMenu mdMenu : mdMenuList) {
            String url = mdMenu.getUrl();
            if (judgeIsContainsPageChartByURL(url)) {
                String dynamic = mdMenu.getDynamic();
                switch (dynamic) {
                case Constant.DYNAMICTYPE_NODE:
                    Map<String, Object> nodeallmap = new HashMap<String, Object>();
                    nodeallmap.put("mdMenu", mdMenu);
                    nodeallmap.put("type", Constant.MONITORTARGET_ALL);
                    nodeallmap.put("dimensiontype", Constant.DYNAMICTYPE_NODE);
                    nodeallmap.put("name", Constant.ALL_NODE);
                    nodeallmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_ALL);
                    result.add(nodeallmap);
                    if (type.equals(Constant.MONITORTARGET_SINGLE)) {
                        Map<String, Object> nodesinglemap = new HashMap<String, Object>();
                        nodesinglemap.put("mdMenu", mdMenu);
                        nodesinglemap.put("type", Constant.MONITORTARGET_SINGLE);
                        nodesinglemap.put("dimensiontype", Constant.DYNAMICTYPE_NODE);
                        nodesinglemap.put("name", Constant.SINGLE_NODE);
                        nodesinglemap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SINGLE);
                        result.add(nodesinglemap);
                    }
                    break;
                case Constant.DYNAMICTYPE_AREA:
                    Map<String, Object> areaallmap = new HashMap<String, Object>();
                    areaallmap.put("mdMenu", mdMenu);
                    areaallmap.put("type", Constant.MONITORTARGET_ALL);
                    areaallmap.put("dimensiontype", Constant.DYNAMICTYPE_AREA);
                    areaallmap.put("name", Constant.ALL_AREA);
                    areaallmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_ALL);
                    result.add(areaallmap);
                    if (type.equals(Constant.MONITORTARGET_SINGLE)) {
                        Map<String, Object> areasinglemap = new HashMap<String, Object>();
                        areasinglemap.put("mdMenu", mdMenu);
                        areasinglemap.put("type", Constant.MONITORTARGET_SINGLE);
                        areasinglemap.put("dimensiontype", Constant.DYNAMICTYPE_AREA);
                        areasinglemap.put("name", Constant.SINGLE_AREA);
                        areasinglemap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SINGLE);
                        result.add(areasinglemap);
                    }
                    break;
                case Constant.DYNAMICTYPE_HOST:
                    Map<String, Object> hostallmap = new HashMap<String, Object>();
                    hostallmap.put("mdMenu", mdMenu);
                    hostallmap.put("type", Constant.MONITORTARGET_ALL);
                    hostallmap.put("dimensiontype", Constant.DYNAMICTYPE_HOST);
                    hostallmap.put("name", Constant.ALL_HOST);
                    hostallmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_ALL);
                    result.add(hostallmap);
                    if (type.equals(Constant.MONITORTARGET_SINGLE)) {
                        Map<String, Object> hostsinglemap = new HashMap<String, Object>();
                        hostsinglemap.put("mdMenu", mdMenu);
                        hostsinglemap.put("type", Constant.MONITORTARGET_SINGLE);
                        hostsinglemap.put("dimensiontype", Constant.DYNAMICTYPE_HOST);
                        hostsinglemap.put("name", Constant.SINGLE_HOST);
                        hostsinglemap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SINGLE);
                        result.add(hostsinglemap);
                    }
                    break;
                case Constant.DYNAMICTYPE_BRAS:
                    Map<String, Object> brasallmap = new HashMap<String, Object>();
                    brasallmap.put("mdMenu", mdMenu);
                    brasallmap.put("type", Constant.MONITORTARGET_ALL);
                    brasallmap.put("dimensiontype", Constant.DYNAMICTYPE_BRAS);
                    brasallmap.put("name", Constant.ALL_BRAS);
                    brasallmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_ALL);
                    result.add(brasallmap);
                    if (type.equals(Constant.MONITORTARGET_SINGLE)) {
                        Map<String, Object> brassinglemap = new HashMap<String, Object>();
                        brassinglemap.put("mdMenu", mdMenu);
                        brassinglemap.put("type", Constant.MONITORTARGET_SINGLE);
                        brassinglemap.put("dimensiontype", Constant.DYNAMICTYPE_BRAS);
                        brassinglemap.put("name", Constant.SINGLE_BRAS);
                        brassinglemap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SINGLE);
                        result.add(brassinglemap);
                    }
                    break;
                case Constant.DYNAMICTYPE_APN:
                    Map<String, Object> apnallmap = new HashMap<String, Object>();
                    apnallmap.put("mdMenu", mdMenu);
                    apnallmap.put("type", Constant.MONITORTARGET_ALL);
                    apnallmap.put("dimensiontype", Constant.DYNAMICTYPE_APN);
                    apnallmap.put("name", Constant.ALL_APN);
                    apnallmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_ALL);
                    result.add(apnallmap);
                    if (type.equals(Constant.MONITORTARGET_SINGLE)) {
                        Map<String, Object> apnsinglemap = new HashMap<String, Object>();
                        apnsinglemap.put("mdMenu", mdMenu);
                        apnsinglemap.put("type", Constant.MONITORTARGET_SINGLE);
                        apnsinglemap.put("dimensiontype", Constant.DYNAMICTYPE_APN);
                        apnsinglemap.put("name", Constant.SINGLE_APN);
                        apnsinglemap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SINGLE);
                        result.add(apnsinglemap);
                    }
                    break;
                case Constant.DYNAMICTYPE_STATIC:
                    Map<String, Object> staticmap = new HashMap<String, Object>();
                    staticmap.put("mdMenu", mdMenu);
                    staticmap.put("type", Constant.MONITORTARGET_SUMMARY);
                    staticmap.put("dimensiontype", Constant.DYNAMICTYPE_STATIC);
                    staticmap.put("name", mdMenu.getShow_name());
                    staticmap.put("id", mdMenu.getId() + Constant.MONITORTARGET_SUMMARY);
                    result.add(staticmap);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 
     * @Title: getMonitorTargetFourLevelList @Description:
     *         (获取四级监控目标) @param @param map @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    public List<Map<String, Object>> getMonitorTargetFourLevelList(
            Map<String, Object> monitorTargetMap) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String monitorTargetOneType = monitorTargetMap.get("monitorTargetOneType").toString();
        String monitorTargetOneDynamic = monitorTargetMap.get("monitorTargetOneDynamic").toString();
        String monitorTargetThreeDynamic = monitorTargetMap.get("monitorTargetThreeDynamic")
                .toString();
        String monitorTargetThreeName = monitorTargetMap.get("monitorTargetThreeName").toString();
        String monitorTargetThreeType = monitorTargetMap.get("monitorTargetThreeType").toString();
        if (monitorTargetThreeType.equals(Constant.MONITORTARGET_SINGLE)) {
            switch (monitorTargetThreeDynamic) {
            case Constant.DYNAMICTYPE_NODE:
                for (MdNode mdNode : MdMenuDataListener.getMdNodeList()) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", mdNode.getId());
                    m.put("name", mdNode.getNode_name());
                    result.add(m);
                }
                break;
            case Constant.DYNAMICTYPE_AREA:
                for (MdArea mdArea : MdMenuDataListener.getMdAreaList()) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", mdArea.getAreano());
                    m.put("name", mdArea.getName());
                    result.add(m);
                }
                break;
            case Constant.DYNAMICTYPE_HOST:
                List<MonHost> monHostList = new ArrayList<MonHost>();
                if (Constant.DYNAMICTYPE_NODE.equals(monitorTargetOneDynamic)
                        && Constant.MONITORTARGET_SINGLE.equals(monitorTargetOneType)) {
                    String monitorTargetTwoMapId = monitorTargetMap.get("monitorTargetTwoId")
                            .toString();
                    // 筛选节点下主机
                    List<MonHost> monHostListAll = MdMenuDataListener.getMonHostList();
                    for (MonHost monHost : monHostListAll) {
                        if (monHost.getNodeid().equals(monitorTargetTwoMapId)) {
                            // 筛选业务下的主机
                            MdBusinessHost mdBusinessHost = mdMenuDataListener
                                    .getMdBusinessHostByNameAndHostid(monitorTargetThreeName,
                                            monHost.getHostid());
                            if (mdBusinessHost != null) {
                                monHostList.add(monHost);
                            }
                        }
                    }
                } else {
                    // 筛选业务下的主机
                    List<MdBusinessHost> mdBusinessHostList = mdMenuDataListener
                            .getMdBusinessHostListByName(monitorTargetThreeName);
                    for (MdBusinessHost mdBusinessHost : mdBusinessHostList) {
                        monHostList
                                .add(mdMenuDataListener.getMonHostById(mdBusinessHost.getHostid()));
                    }
                }
                for (MonHost monHost : monHostList) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", monHost.getHostid());
                    m.put("name", monHost.getHostname());
                    result.add(m);
                }
                break;
            case Constant.DYNAMICTYPE_BRAS:
                List<BdNas> bdNasList = new ArrayList<BdNas>();
                if (Constant.DYNAMICTYPE_AREA.equals(monitorTargetOneDynamic)
                        && Constant.MONITORTARGET_SINGLE.equals(monitorTargetOneType)) {
                    String monitorTargetTwoMapId = monitorTargetMap.get("monitorTargetTwoId")
                            .toString();
                    // 筛选属地下bas
                    List<BdNas> bdNasListAll = MdMenuDataListener.getBdNasList();
                    for (BdNas BdNas : bdNasListAll) {
                        if (BdNas.getArea_no().equals(monitorTargetTwoMapId)) {
                            bdNasList.add(BdNas);
                        }
                    }
                } else {
                    bdNasList = MdMenuDataListener.getBdNasList();
                }
                for (BdNas bdNas : bdNasList) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", bdNas.getId());
                    m.put("name", bdNas.getNas_name());
                    result.add(m);
                }
                break;
            case Constant.DYNAMICTYPE_APN:
                for (MdApnRecord mdApnRecord : CommonInit.getMdApnRecordList()) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", mdApnRecord.getApnid());
                    m.put("name", mdApnRecord.getApn());
                    result.add(m);
                }
                break;
            }
        }
        return result;
    }

    /**
     * 
     * @Title: judgeIsContainsPageChartByURL @Description:
     *         (判断URL是否包含在PageChart) @param @param url @param @return 参数 @return
     *         boolean 返回类型 @throws
     */
    public boolean judgeIsContainsPageChartByURL(String url) {
        boolean result = false;
        List<String> urlList = new ArrayList<String>();
        List<Map<String, Object>> pageChartList = pageChartDAO.getGroupURL();
        for (Map<String, Object> map : pageChartList) {
            String URL = map.get("URL").toString();
            String sURL = URL.split(ConstantUtill.URL_SPLIT)[0];
            if (!urlList.contains(sURL)) {
                urlList.add(sURL);
                if (sURL.contains(url)) {
                    return true;
                }
            }
        }
        return result;
    }

    /**
     * 
     * @Title: getAllMetricManage @Description: (获取告警规则信息) @param @return
     *         参数 @return List<MdAlarmRule> 返回类型 @throws
     */
    public List<MdAlarmRule> getMdAlarmRuleList(Map<String, Object> map) {
        MdAlarmRule mdAlarmRule = new MdAlarmRule();
        String wholeUrl = getWholeUrl(map);
        if (wholeUrl != null && !wholeUrl.equals("")) {
            mdAlarmRule.setUrl(wholeUrl);
        }
        String mdMenuId = "";
        String chart_name = "";
        String metric_id = "";
        String rule_id = "";
        String attr = "";
        if (map.get("mdMenuId") != null) {
            mdMenuId = map.get("mdMenuId").toString();
            if (!mdMenuId.equals("")) {
                MdMenu mdMenu = menuService.getMdMenuById(mdMenuId);
                mdAlarmRule.setName(mdMenu.getName());
            }
        }
        if (map.get("rule_id") != null) {
            rule_id = map.get("rule_id").toString();
            if (!rule_id.equals("")) {
                mdAlarmRule.setRule_id(rule_id);
            }
        }
        if (map.get("metric_id") != null) {
            metric_id = map.get("metric_id").toString();
            if (!metric_id.equals("")) {
                mdAlarmRule.setMetric_id(metric_id);
            }
        }
        if (map.get("attr") != null) {
            attr = map.get("attr").toString();
            if (!attr.equals("")) {
                mdAlarmRule.setAttr(attr);
            }
        }
        if (map.get("chart_name") != null) {
            chart_name = map.get("chart_name").toString();
            if (!chart_name.equals("")) {
                mdAlarmRule.setChart_name(chart_name);
            }
        }
        List<MdAlarmRule> mdAlarmRuleList = mdAlarmRuleDAO.getMdAlarmRule(mdAlarmRule);
        for (MdAlarmRule alarmRule : mdAlarmRuleList) {
            String URL = alarmRule.getUrl();
            String businesslinkurl = URL.split(ConstantUtill.URL_SPLIT)[0];
            alarmRule.setBusinesslinkurl(businesslinkurl);
            MdMenu mdMenu = menuService.getTwoLevelMdMenu(businesslinkurl);
            String module = mdMenu.getId();
            alarmRule.setModule(module);
            // 获取模块名称
            alarmRule.setModule_name(mdMenu.getShow_name());
            int dimension_type = alarmRule.getDimension_type();
            String dimension_type_name = getDimensionTypeName(dimension_type);
            alarmRule.setDimension_type_name(dimension_type_name);
            List<Map<String, Object>> monitorTargetOneLevelList = getMonitorTargetOneLevelList(
                    module);
            String businesslinkoneurl = businesslinkurl;
            if (dimension_type == Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE
                    || dimension_type == Constant.DIMENSIONTYPE_NODE_ALL_AREA_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_SINGLE
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_ALL_BRAS_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_SINGLE
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_ALL_NODE_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_ALL
                    || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_SINGLE) {
                String s[] = businesslinkurl.split("/");
                int length = s.length;
                int lastlength = 0;
                if (length > 0) {
                    lastlength = s[length - 1].length() + 1;
                }
                businesslinkoneurl = businesslinkurl.substring(0,
                        businesslinkurl.length() - lastlength);
            }
            String monitortarget1 = getMonitorTargetOne(businesslinkoneurl, dimension_type,
                    monitorTargetOneLevelList);
            alarmRule.setMonitortarget1(monitortarget1);
            if (!monitortarget1.equals("")) {
                List<Map<String, Object>> monitorTargetThreeLevelList = getMonitorTargetThreeLevelList(
                        monitortarget1);
                String monitortarget3 = getMonitorTargetThree(businesslinkurl, dimension_type,
                        monitorTargetThreeLevelList);
                alarmRule.setMonitortarget3(monitortarget3);
            }
            // 获取告警等级名称
            String alarm_level_name = "";
            List<MdAlarmLevel> alarm_levelList = getMdAlarmLevelList();
            String alarm_level = alarmRule.getAlarm_level();
            String alarm_levelS[] = alarm_level.split(ConstantUtill.PROCESSMETRICSPLITSTR);
            for (String alarm_levels : alarm_levelS) {
                for (MdAlarmLevel mdAlarmLevel : alarm_levelList) {
                    if (mdAlarmLevel.getAlarmlevel().equals(alarm_levels)) {
                        alarm_level_name = alarm_level_name + ConstantUtill.PROCESSMETRICSPLITSTR
                                + mdAlarmLevel.getAlarmname();
                    }
                }
            }
            if (alarm_level_name.length() > 0) {
                alarm_level_name = alarm_level_name.substring(1, alarm_level_name.length());
            }
            alarmRule.setAlarm_level_name(alarm_level_name);
            // 获取告警方式名称
            String modes_name = "";
            List<Map<String, Object>> alarmModesMapList = getAlarmModesList();
            String modes = alarmRule.getModes();
            if (modes != null && !modes.equals("")) {
                String modesS[] = modes.split(ConstantUtill.PROCESSMETRICSPLITSTR);
                for (String modess : modesS) {
                    for (Map<String, Object> alarmModesMap : alarmModesMapList) {
                        @SuppressWarnings("unchecked")
                        List<MdAlarmMode> mdAlarmModeList = (List<MdAlarmMode>) alarmModesMap
                                .get("mdAlarmModeList");
                        for (MdAlarmMode mdAlarmMode : mdAlarmModeList) {
                            if (mdAlarmMode.getModeid().equals(modess)) {
                                modes_name = modes_name + ConstantUtill.PROCESSMETRICSPLITSTR
                                        + mdAlarmMode.getModename();
                            }
                        }
                    }
                }
                if (modes_name.length() > 0) {
                    modes_name = modes_name.substring(1, modes_name.length());
                }
            }
            alarmRule.setModes_name(modes_name);
            // 获取维度1名称
            String dimension1 = alarmRule.getDimension1();
            String dimension1_name = getDynamicNameById(dimension1);
            alarmRule.setDimension1_name(dimension1_name);
            // 获取维度2名称
            String dimension2 = alarmRule.getDimension2();
            String dimension2_name = getDynamicNameById(dimension2);
            alarmRule.setDimension2_name(dimension2_name);
        }
        return mdAlarmRuleList;
    }

    public String getDimensionTypeName(int dimension_type) {
        String dimension_type_name = "";
        switch (dimension_type) {
        case Constant.DIMENSIONTYPE_NODE_SUMMARY:
            /** 维度类型 1--> node/summary * node/summary： 维度1 节点总览 */
            dimension_type_name = Constant.DIMENSIONTYPE_NODE_SUMMARY_NAME;
            break;
        case Constant.DIMENSIONTYPE_AREA_SUMMARY:
            /** 维度类型2--> area/summary * area/summary: 维度1 地市总览 */
            dimension_type_name = Constant.DIMENSIONTYPE_AREA_SUMMARY_NAME;
            break;
        case Constant.DIMENSIONTYPE_HOST_SUMMARY:
            /** 维度类型3--> host/summary * host/summary 服务器总览 */
            dimension_type_name = Constant.DIMENSIONTYPE_HOST_SUMMARY_NAME;
            break;
        case Constant.DIMENSIONTYPE_NODE_ALL:
            /** 维度类型4--> node(all) * node(全部) 节点 ： 维度1：节点名称， 维度2：空 */
            dimension_type_name = Constant.DIMENSIONTYPE_NODE_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_NODE_SINGLE:
            /** 维度类型5--> node(single) * node(单个) 节点 ： 维度1：节点名称， 维度2：空 */
            dimension_type_name = Constant.DIMENSIONTYPE_NODE_SINGLE_NAME;
            break;
        case Constant.DIMENSIONTYPE_AREA_ALL:
            /** 维度类型6--> area(all) * area(全部) 地市 ： 维度1：地市名称， 维度2：空 */
            dimension_type_name = Constant.DIMENSIONTYPE_AREA_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_AREA_SINGLE:
            /** 维度类型7--> area(single) * area(单个) 地市 ： 维度1：地市名称， 维度2：空 */
            dimension_type_name = Constant.DIMENSIONTYPE_AREA_SINGLE_NAME;
            break;
        case Constant.DIMENSIONTYPE_HOST_ALL:
            /** 维度类型8--> host(all) * host(全部) 服务器： 维度1：主机名称， 维度2：空 */
            dimension_type_name = Constant.DIMENSIONTYPE_HOST_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_HOST_SINGLE:
            /** 维度类型9--> host(single) * host(单个) 服务器： 维度1：主机名称， 维度2：空 */
            dimension_type_name = Constant.DIMENSIONTYPE_HOST_SINGLE_NAME;
            break;
        case Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL:
            /**
             * 维度类型10--> node(all)/host(all) * node/host 节点(全部) --> 主机(全部) ：
             * 维度1：节点名称， 维度2：主机IP
             */
            dimension_type_name = Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL:
            /**
             * 维度类型11--> node(single)/host(all) * node/host 节点(单个) --> 主机(全部) ：
             * 维度1：节点名称， 维度2：主机IP
             */
            dimension_type_name = Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE:
            /**
             * 维度类型12--> node(single)/host(single) * node/host 节点(单个) --> 主机(单个)
             * ： 维度1：节点名称， 维度2：主机IP
             */
            dimension_type_name = Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE_NAME;
            break;
        case Constant.DIMENSIONTYPE_NODE_ALL_AREA_ALL:
            /**
             * 维度类型13--> node(all)/area(all) * node/area 节点(全部) --> 地市(全部) ：
             * 维度1：节点名称， 维度2：地市名称
             */
            dimension_type_name = Constant.DIMENSIONTYPE_NODE_ALL_AREA_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_ALL:
            /**
             * 维度类型14--> node(single)/area(all) * node/area 节点(单个) --> 地市(全部) ：
             * 维度1：节点名称， 维度2：地市名称
             */
            dimension_type_name = Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_SINGLE:
            /**
             * 维度类型15--> node(single)/area(single) * node/area 节点(单个) --> 地市(单个)
             * ： 维度1：节点名称， 维度2：地市名称
             */
            dimension_type_name = Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_SINGLE_NAME;
            break;
        case Constant.DIMENSIONTYPE_AREA_ALL_BRAS_ALL:
            /**
             * 维度类型16--> area(all)/bras(all) * area/bras 地市(全部) --> bras(全部) :
             * 维度1：地市 ， 维度2：brasip
             */
            dimension_type_name = Constant.DIMENSIONTYPE_AREA_ALL_BRAS_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL:
            /**
             * 维度类型17--> area(single)/bras(all) * area/bras 地市(单个) --> bras(全部)
             * : 维度1：地市 ， 维度2：brasip
             */
            dimension_type_name = Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_SINGLE:
            /**
             * 维度类型18--> area(single)/bras(single) * area/bras 地市(单个) -->
             * bras(单个) : 维度1：地市 ， 维度2：brasip
             */
            dimension_type_name = Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_SINGLE_NAME;
            break;
        case Constant.DIMENSIONTYPE_AREA_ALL_NODE_ALL:
            /**
             * 维度类型19--> area(all)/node(all) * area/node 地市(全部) --> 节点(全部) ：
             * 维度1：地市名称， 维度2：节点名称
             */
            dimension_type_name = Constant.DIMENSIONTYPE_AREA_ALL_NODE_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_ALL:
            /**
             * 维度类型20--> area(single)/node(all) * area/node 地市(单个) --> 节点(全部) ：
             * 维度1：地市名称， 维度2：节点名称
             */
            dimension_type_name = Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_SINGLE:
            /**
             * 维度类型21--> area(single)/node(single) * area/node 地市(单个) --> 节点(单个)
             * ： 维度1：地市名称， 维度2：节点名称
             */
            dimension_type_name = Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_SINGLE_NAME;
            break;
        case Constant.DIMENSIONTYPE_BRAS_SUMMARY:
            /** 维度类型22--> bras/summary * bras/summary bras总览 */
            dimension_type_name = Constant.DIMENSIONTYPE_BRAS_SUMMARY_NAME;
            break;
        case Constant.DIMENSIONTYPE_BRAS_ALL:
            /** 维度类型23--> bras(all) bras ： 维度1：空， 维度2：空 */
            dimension_type_name = Constant.DIMENSIONTYPE_BRAS_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_BRAS_SINGLE:
            /** 维度类型24--> bras(single) bras ： 维度1：brasip， 维度2：空 */
            dimension_type_name = Constant.DIMENSIONTYPE_BRAS_SINGLE_NAME;
            break;
        case Constant.DIMENSIONTYPE_APN_SUMMARY:
            /** 维度类型25--> apn/summary * apn/summary apn总览 */
            dimension_type_name = Constant.DIMENSIONTYPE_APN_SUMMARY_NAME;
            break;
        case Constant.DIMENSIONTYPE_APN_ALL:
            /** 维度类型26--> apn(all) apn ： 维度1：空， 维度2：空 */
            dimension_type_name = Constant.DIMENSIONTYPE_APN_ALL_NAME;
            break;
        case Constant.DIMENSIONTYPE_APN_SINGLE:
            /** 维度类型27--> apn(single) apn ： 维度1：apn名称， 维度2：空 */
            dimension_type_name = Constant.DIMENSIONTYPE_APN_SINGLE_NAME;
            break;
        }
        return dimension_type_name;
    }

    public String getMonitorTargetOne(String businesslinkurl, int dimension_type,
            List<Map<String, Object>> monitorTargetLevelList) {
        String monitortarget = "";
        for (Map<String, Object> monitorTargetLevel : monitorTargetLevelList) {
            MdMenu menu = (MdMenu) monitorTargetLevel.get("mdMenu");
            String menuUrl = menu.getUrl();
            if (businesslinkurl.equals(menuUrl)) {
                if (dimension_type == Constant.DIMENSIONTYPE_NODE_SUMMARY
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SUMMARY
                        || dimension_type == Constant.DIMENSIONTYPE_HOST_SUMMARY
                        || dimension_type == Constant.DIMENSIONTYPE_BRAS_SUMMARY
                        || dimension_type == Constant.DIMENSIONTYPE_APN_SUMMARY) {
                    monitortarget = menu.getId() + Constant.MONITORTARGET_SUMMARY;
                }
                if (dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_HOST_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_BRAS_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_APN_SINGLE) {
                    monitortarget = menu.getId() + Constant.MONITORTARGET_SINGLE;
                }
                if (dimension_type == Constant.DIMENSIONTYPE_NODE_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_HOST_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_ALL_AREA_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_ALL_BRAS_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_ALL_NODE_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_BRAS_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_APN_ALL) {
                    monitortarget = menu.getId() + Constant.MONITORTARGET_ALL;
                }
            }
        }
        return monitortarget;
    }

    public String getMonitorTargetThree(String businesslinkurl, int dimension_type,
            List<Map<String, Object>> monitorTargetLevelList) {
        String monitortarget = "";
        for (Map<String, Object> monitorTargetLevel : monitorTargetLevelList) {
            MdMenu menu = (MdMenu) monitorTargetLevel.get("mdMenu");
            String menuUrl = menu.getUrl();
            if (businesslinkurl.equals(menuUrl)) {
                if (dimension_type == Constant.DIMENSIONTYPE_NODE_SUMMARY
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SUMMARY
                        || dimension_type == Constant.DIMENSIONTYPE_HOST_SUMMARY
                        || dimension_type == Constant.DIMENSIONTYPE_BRAS_SUMMARY
                        || dimension_type == Constant.DIMENSIONTYPE_APN_SUMMARY) {
                    monitortarget = menu.getId() + Constant.MONITORTARGET_SUMMARY;
                }
                if (dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_HOST_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_BRAS_SINGLE
                        || dimension_type == Constant.DIMENSIONTYPE_APN_SINGLE) {
                    monitortarget = menu.getId() + Constant.MONITORTARGET_SINGLE;
                }
                if (dimension_type == Constant.DIMENSIONTYPE_NODE_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_HOST_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_ALL_AREA_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_ALL_BRAS_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_ALL_NODE_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_BRAS_ALL
                        || dimension_type == Constant.DIMENSIONTYPE_APN_ALL) {
                    monitortarget = menu.getId() + Constant.MONITORTARGET_ALL;
                }
            }
        }
        return monitortarget;
    }

    /**
     * 
     * @Title: getMdAlarmLevelList @Description: (获取告警等级) @param @return
     *         参数 @return List<MdAlarmLevel> 返回类型 @throws
     */
    public List<MdAlarmLevel> getMdAlarmLevelList() {
        return mdAlarmInfoDao.getAllAlarmLevelList();
    }

    /**
     * 
     * @Title: getAlarmModesList @Description: (获取告警方式) @param @return
     *         参数 @return List<Map<String,Object>> 返回类型 @throws
     */
    public List<Map<String, Object>> getAlarmModesList() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        result = alarmmodeManageDAO.getModesByType(Constant.ALARM_MODE_TYPE);
        if (result != null && result.size() > 0) {
            for (Map<String, Object> m : result) {
                String modetype = m.get("MODETYPE").toString();
                MdAlarmMode mdAlarmMode = new MdAlarmMode();
                mdAlarmMode.setModetype(modetype);
                List<MdAlarmMode> mdAlarmModeList = alarmmodeManageDAO.getMdAlarmMode(mdAlarmMode);
                m.put("mdAlarmModeList", mdAlarmModeList);
            }
        }
        return result;
    }

    /**
     * 
     * @Title: getAlarmTypeList @Description: (获取告警类型) @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    public List<Map<String, Object>> getAlarmTypeList() {
        return mdAlarmInfoDao.getAlarmTypeList();
    }

    /**
     * 
     * @Title: addMdAlarmRule @Description: (新增告警规则) @param @param
     *         mdAlarmRule @param @return 参数 @return Map<String,Object>
     *         返回类型 @throws
     */
    @Transactional
    public WebResult addMdAlarmRule(Map<String, Object> map) {
        String URL = getWholeUrl(map);
        String uuid = IDGenerateUtil.getUuid();
        MdAlarmRule mdAlarmRule = new MdAlarmRule();
        String metric_id = "";
        String chart_name = "";
        String attr = "";
        String alarm_rule = "";
        String alarm_level = "";
        String modes = "";
        String alarmmsg = "";
        String dimension1 = "";
        String dimension2 = "";
        String report_rule = "";
        String alarm_type = "";
        if (map.get("metric_id") != null) {
            metric_id = map.get("metric_id").toString();
        }
        if (map.get("attr") != null) {
            attr = map.get("attr").toString();
        }
        if (map.get("alarm_rule") != null) {
            alarm_rule = map.get("alarm_rule").toString();
        }
        if (map.get("alarm_level") != null) {
            alarm_level = map.get("alarm_level").toString();
        }
        if (map.get("modes") != null) {
            modes = map.get("modes").toString();
        }
        if (map.get("alarmmsg") != null) {
            alarmmsg = map.get("alarmmsg").toString();
        }
        if (map.get("attr1") != null) {
            dimension1 = map.get("attr1").toString();
        }
        if (map.get("attr2") != null) {
            dimension2 = map.get("attr2").toString();
        }
        if (map.get("report_rule") != null) {
            report_rule = map.get("report_rule").toString();
        }
        if (map.get("alarm_type") != null) {
            alarm_type = map.get("alarm_type").toString();
        }

        if (!("").equals(alarm_rule) && !("").equals(alarmmsg)) {
            mdAlarmRule.setUrl(URL);
            mdAlarmRule.setMetric_id(metric_id);
            mdAlarmRule.setAttr(attr);
            mdAlarmRule.setAlarm_rule(alarm_rule);
            mdAlarmRule.setAlarm_level(alarm_level);
            mdAlarmRule.setModes(modes);
            mdAlarmRule.setAlarmmsg(alarmmsg);
            mdAlarmRule.setDimension1(dimension1);
            mdAlarmRule.setDimension2(dimension2);
            mdAlarmRule.setDimension3("");
            mdAlarmRule.setReport_rule(report_rule);
            mdAlarmRule.setAlarm_type(alarm_type);
            mdAlarmRule.setRule_id(uuid);
            String businesslinkurl = URL.split(ConstantUtill.URL_SPLIT)[0];
            MdMenu mdMenu = menuService.getTwoLevelMdMenu(businesslinkurl);
            mdAlarmRule.setName(mdMenu.getName());
            int dimension_type = judgeDimensionType(URL);
            mdAlarmRule.setDimension_type(dimension_type);
            if (map.get("chart_name") != null) {
                chart_name = map.get("chart_name").toString();
            } else {
                List<Map<String, Object>> ml = pageChartDAO.getChartNamePageChart(URL, metric_id);
                if (ml == null || ml.size() == 0) {
                    ml = pageChartDAO.getChartNamePageChart(businesslinkurl, metric_id);
                }
                for (Map<String, Object> m : ml) {
                    chart_name = chart_name + "," + m.get("CHART_NAME").toString();
                }
                if (chart_name.length() > 0) {
                    chart_name = chart_name.substring(1);
                }
            }
            mdAlarmRule.setChart_name(chart_name);
            mdAlarmRule.setCreate_time(new Date());
            mdAlarmRule.setUpdate_time(new Date());
            int addResult = mdAlarmRuleDAO.insertMdAlarmRule(mdAlarmRule);
            if (1 == addResult) {
                // 用户日志记录
                operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ALARMRULE_MANAGE,
                        "新增数据[告警规则:" + mdAlarmRule.getAlarmmsg() + "]");
                MdAlarmRuleDetail mdAlarmRuleDetail = new MdAlarmRuleDetail();
                mdAlarmRuleDetail.setRule_id(mdAlarmRule.getRule_id());
                mdAlarmRuleDetail.setName(mdAlarmRule.getName());
                mdAlarmRuleDetail.setUrl(mdAlarmRule.getUrl());
                mdAlarmRuleDetail.setDimension_type(mdAlarmRule.getDimension_type());
                mdAlarmRuleDetail.setChart_name(mdAlarmRule.getChart_name());
                mdAlarmRuleDetail.setMetric_id(mdAlarmRule.getMetric_id());
                mdAlarmRuleDetail.setAttr(mdAlarmRule.getAttr());
                mdAlarmRuleDetail.setAlarm_level(mdAlarmRule.getAlarm_level());
                mdAlarmRuleDetail.setAlarm_rule(mdAlarmRule.getAlarm_rule());
                mdAlarmRuleDetail.setModes(mdAlarmRule.getModes());
                mdAlarmRuleDetail.setAlarmmsg(mdAlarmRule.getAlarmmsg());
                mdAlarmRuleDetail.setDimension3(mdAlarmRule.getDimension3());
                mdAlarmRuleDetail.setCreate_time(new Date());
                mdAlarmRuleDetail.setUpdate_time(new Date());
                mdAlarmRuleDetail.setReport_rule(mdAlarmRule.getReport_rule());
                mdAlarmRuleDetail.setAlarm_type(mdAlarmRule.getAlarm_type());
                List<Map<String, Object>> dimensionList = getDimensionList(map);
                if (dimensionList != null && dimensionList.size() > 0) {
                    for (Map<String, Object> m : dimensionList) {
                        String attr1 = m.get("dimension1").toString();
                        mdAlarmRuleDetail.setDimension1(attr1);
                        String mdAlarmRuleDetailDimension1URL = businesslinkurl
                                + ConstantUtill.URL_SPLIT + "/" + attr1;
                        mdAlarmRuleDetail.setUrl(mdAlarmRuleDetailDimension1URL);
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> list = (List<Map<String, Object>>) m.get("list");
                        if (list != null && list.size() > 0) {
                            for (Map<String, Object> listmap : list) {
                                String attr2 = listmap.get("dimension2").toString();
                                mdAlarmRuleDetail.setDimension2(attr2);
                                String mdAlarmRuleDetailDimension2URL = businesslinkurl
                                        + ConstantUtill.URL_SPLIT + "/" + attr1 + "/" + attr2;
                                mdAlarmRuleDetail.setUrl(mdAlarmRuleDetailDimension2URL);
                                mdAlarmRuleDetail.setAlarm_id(IDGenerateUtil.getUuid());
                                boolean judgeMetricIsExist = judgeMetricIsExist(
                                        mdAlarmRuleDetailDimension2URL, metric_id);
                                if (judgeMetricIsExist) {
                                    mdAlarmRuleDetailDAO.insertMdAlarmRuleDetail(mdAlarmRuleDetail);
                                }
                            }
                        } else {
                            mdAlarmRuleDetail.setDimension2("");
                            mdAlarmRuleDetail.setAlarm_id(IDGenerateUtil.getUuid());
                            boolean judgeMetricIsExist = judgeMetricIsExist(
                                    mdAlarmRuleDetailDimension1URL, metric_id);
                            if (judgeMetricIsExist) {
                                mdAlarmRuleDetailDAO.insertMdAlarmRuleDetail(mdAlarmRuleDetail);
                            }
                        }
                    }
                } else {
                    mdAlarmRuleDetail.setDimension1("");
                    mdAlarmRuleDetail.setDimension2("");
                    mdAlarmRuleDetail.setAlarm_id(IDGenerateUtil.getUuid());
                    mdAlarmRuleDetailDAO.insertMdAlarmRuleDetail(mdAlarmRuleDetail);
                }
                return new WebResult(true, "成功", uuid);
            } else {
                return new WebResult(false, "失败");
            }
        } else {
            return new WebResult(false, "失败");
        }
    }

    /**
     * 
     * @Title: judgeMetricIsExist @Description: (根据URL判断指标是否特殊化) @param @param
     *         URL @param @param metric_id @param @return 参数 @return boolean
     *         返回类型 @throws
     */
    public boolean judgeMetricIsExist(String URL, String metric_id) {
        boolean judgeMetricIsExist = false;
        List<MdPageCharts> mdPageChartsList = pageChartDAO.getMdPageChartsByUrl(URL);
        if (mdPageChartsList != null && mdPageChartsList.size() > 0) {
            List<Map<String, Object>> metricList = pageChartDAO.getMetricIdByUrl(URL);
            String metricIds = "";
            for (Map<String, Object> metricmap : metricList) {
                try {
                    metricIds = metricIds + "," + metricmap.get("METRIC_IDS").toString();
                } catch (Exception e) {
                    LOG.error("AlarmRuleManageService judgeMetricIsExist , URL:{}", URL);
                }
            }
            if (metricIds.length() > 0) {
                metricIds = metricIds.replace(",", "','").substring(2) + "'";
            } else {
                metricIds = "''";
            }
            List<MdMetric> mdMetricList = mdMetricDAO.getMdMetricListByIds(metricIds);
            for (MdMetric mdMetric : mdMetricList) {
                if (mdMetric.getId().equals(metric_id)) {
                    judgeMetricIsExist = true;
                }
            }
        } else {
            judgeMetricIsExist = true;
        }
        return judgeMetricIsExist;
    }

    /**
     * 
     * @Title: getDimensionList @Description: (获取维度1) @param @param
     *         mdAlarmRule @param @return 参数 @return List<Map<String,Object>>
     *         返回类型 @throws
     */
    public List<Map<String, Object>> getDimensionList(Map<String, Object> map) {
        List<Map<String, Object>> dimensionList = new ArrayList<Map<String, Object>>();
        String attr1 = "";
        String attr2 = "";
        String monitorTargetOneDimensionType = map.get("monitorTargetOneDimensionType").toString();
        if (map.get("monitorTargetThreeType") != null) {
            String monitorTargetThreeType = map.get("monitorTargetThreeType").toString();
            switch (monitorTargetThreeType) {
            case Constant.MONITORTARGET_SUMMARY:
                return dimensionList;
            case Constant.MONITORTARGET_ALL:
                if (map.get("attr1") != null && !map.get("attr1").toString().equals("")) {
                    attr1 = map.get("attr1").toString();
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("dimension1", attr1);
                    List<Map<String, Object>> list = getDimension2List(map, attr1,
                            monitorTargetOneDimensionType);
                    m.put("list", list);
                    if (list != null && list.size() > 0) {
                        dimensionList.add(m);
                    }
                } else {
                    String monitorTargetOneMdMenuName = map.get("monitorTargetOneMdMenuName")
                            .toString();
                    String monitorTargetOneName = map.get("monitorTargetOneName").toString();
                    switch (monitorTargetOneName) {
                    case Constant.ALL_NODE:
                        for (MdNode mdNode : MdMenuDataListener.getMdNodeList()) {
                            Map<String, Object> m = new HashMap<String, Object>();
                            m.put("dimension1", mdNode.getId());
                            List<Map<String, Object>> list = getDimension2List(map, mdNode.getId(),
                                    monitorTargetOneDimensionType);
                            m.put("list", list);
                            if (list != null && list.size() > 0) {
                                dimensionList.add(m);
                            }
                        }
                        break;
                    case Constant.ALL_AREA:
                        for (MdArea mdArea : MdMenuDataListener.getMdAreaList()) {
                            Map<String, Object> m = new HashMap<String, Object>();
                            m.put("dimension1", mdArea.getAreano());
                            List<Map<String, Object>> list = getDimension2List(map,
                                    mdArea.getAreano(), monitorTargetOneDimensionType);
                            m.put("list", list);
                            if (list != null && list.size() > 0) {
                                dimensionList.add(m);
                            }
                        }
                        break;
                    case Constant.ALL_HOST:
                        List<MonHost> monHostList = new ArrayList<MonHost>();
                        // 筛选业务下的主机
                        List<MdBusinessHost> mdBusinessHostList = mdMenuDataListener
                                .getMdBusinessHostListByName(monitorTargetOneMdMenuName);
                        for (MdBusinessHost mdBusinessHost : mdBusinessHostList) {
                            monHostList.add(
                                    mdMenuDataListener.getMonHostById(mdBusinessHost.getHostid()));
                        }
                        for (MonHost monHost : monHostList) {
                            Map<String, Object> m = new HashMap<String, Object>();
                            m.put("dimension1", monHost.getHostid());
                            List<Map<String, Object>> list = getDimension2List(map,
                                    monHost.getHostid(), monitorTargetOneDimensionType);
                            m.put("list", list);
                            if (list != null && list.size() > 0) {
                                dimensionList.add(m);
                            }
                        }
                        break;
                    case Constant.ALL_BRAS:
                        for (BdNas bdNas : MdMenuDataListener.getBdNasList()) {
                            Map<String, Object> m = new HashMap<String, Object>();
                            m.put("dimension1", bdNas.getId());
                            List<Map<String, Object>> list = getDimension2List(map, bdNas.getId(),
                                    monitorTargetOneDimensionType);
                            m.put("list", list);
                            if (list != null && list.size() > 0) {
                                dimensionList.add(m);
                            }
                        }
                        break;
                    case Constant.ALL_APN:
                        for (MdApnRecord mdApnRecord : CommonInit.getMdApnRecordList()) {
                            Map<String, Object> m = new HashMap<String, Object>();
                            m.put("dimension1", mdApnRecord.getApnid());
                            List<Map<String, Object>> list = getDimension2List(map,
                                    mdApnRecord.getApnid(), monitorTargetOneDimensionType);
                            m.put("list", list);
                            if (list != null && list.size() > 0) {
                                dimensionList.add(m);
                            }
                        }
                        break;
                    }
                }
                break;
            case Constant.MONITORTARGET_SINGLE:
                if (map.get("attr1") != null && map.get("attr2") != null
                        && !map.get("attr1").toString().equals("")
                        && !map.get("attr2").toString().equals("")) {
                    attr1 = map.get("attr1").toString();
                    attr2 = map.get("attr2").toString();
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("dimension1", attr1);
                    Map<String, Object> attr2m = new HashMap<String, Object>();
                    attr2m.put("dimension2", attr2);
                    list.add(attr2m);
                    m.put("list", list);
                    dimensionList.add(m);
                }
                break;
            }
        } else {
            String monitorTargetOneType = map.get("monitorTargetOneType").toString();
            String monitorTargetOneMdMenuName = map.get("monitorTargetOneMdMenuName").toString();
            switch (monitorTargetOneType) {
            case Constant.MONITORTARGET_SUMMARY:
                return dimensionList;
            case Constant.MONITORTARGET_ALL:
                String monitorTargetOneName = map.get("monitorTargetOneName").toString();
                switch (monitorTargetOneName) {
                case Constant.ALL_NODE:
                    for (MdNode mdNode : MdMenuDataListener.getMdNodeList()) {
                        Map<String, Object> m = new HashMap<String, Object>();
                        m.put("dimension1", mdNode.getId());
                        dimensionList.add(m);
                    }
                    break;
                case Constant.ALL_AREA:
                    for (MdArea mdArea : MdMenuDataListener.getMdAreaList()) {
                        Map<String, Object> m = new HashMap<String, Object>();
                        m.put("dimension1", mdArea.getAreano());
                        dimensionList.add(m);
                    }
                    break;
                case Constant.ALL_HOST:
                    List<MonHost> monHostList = new ArrayList<MonHost>();
                    // 筛选业务下的主机
                    List<MdBusinessHost> mdBusinessHostList = mdMenuDataListener
                            .getMdBusinessHostListByName(monitorTargetOneMdMenuName);
                    for (MdBusinessHost mdBusinessHost : mdBusinessHostList) {
                        monHostList
                                .add(mdMenuDataListener.getMonHostById(mdBusinessHost.getHostid()));
                    }
                    for (MonHost monHost : monHostList) {
                        Map<String, Object> m = new HashMap<String, Object>();
                        m.put("dimension1", monHost.getHostid());
                        dimensionList.add(m);
                    }
                    break;
                case Constant.ALL_BRAS:
                    for (BdNas bdNas : MdMenuDataListener.getBdNasList()) {
                        Map<String, Object> m = new HashMap<String, Object>();
                        m.put("dimension1", bdNas.getId());
                        dimensionList.add(m);
                    }
                    break;
                case Constant.ALL_APN:
                    for (MdApnRecord mdApnRecord : CommonInit.getMdApnRecordList()) {
                        Map<String, Object> m = new HashMap<String, Object>();
                        m.put("dimension1", mdApnRecord.getApnid());
                        dimensionList.add(m);
                    }
                    break;
                }
                break;
            case Constant.MONITORTARGET_SINGLE:
                if (map.get("attr1") != null) {
                    attr1 = map.get("attr1").toString();
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("dimension1", attr1);
                    dimensionList.add(m);
                }
                break;
            }
        }
        return dimensionList;
    }

    /**
     * 
     * @Title: getDimension2List @Description: (获取维度2) @param @param
     *         mdAlarmRule @param @param map @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    public List<Map<String, Object>> getDimension2List(Map<String, Object> map, String Dimension1Id,
            String Dimension1Type) {
        List<Map<String, Object>> dimensionList = new ArrayList<Map<String, Object>>();
        String monitorTargetThreeName = map.get("monitorTargetThreeName").toString();
        String monitorTargetThreeMdMenuName = map.get("monitorTargetThreeMdMenuName").toString();
        switch (monitorTargetThreeName) {
        case Constant.ALL_NODE:
            for (MdNode mdNode : MdMenuDataListener.getMdNodeList()) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("dimension2", mdNode.getId());
                dimensionList.add(m);
            }
            break;
        case Constant.ALL_AREA:
            for (MdArea mdArea : MdMenuDataListener.getMdAreaList()) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("dimension2", mdArea.getAreano());
                dimensionList.add(m);
            }
            break;
        case Constant.ALL_HOST:
            List<MonHost> monHostList = new ArrayList<MonHost>();
            if (Constant.DYNAMICTYPE_NODE.equals(Dimension1Type)) {
                // 筛选节点下主机
                List<MonHost> monHostListAll = MdMenuDataListener.getMonHostList();
                for (MonHost monHost : monHostListAll) {
                    if (monHost.getNodeid().equals(Dimension1Id)) {
                        // 筛选业务下的主机
                        MdBusinessHost mdBusinessHost = mdMenuDataListener
                                .getMdBusinessHostByNameAndHostid(monitorTargetThreeMdMenuName,
                                        monHost.getHostid());
                        if (mdBusinessHost != null) {
                            monHostList.add(monHost);
                        }
                    }
                }
            } else {
                // 筛选业务下的主机
                List<MdBusinessHost> mdBusinessHostList = mdMenuDataListener
                        .getMdBusinessHostListByName(monitorTargetThreeMdMenuName);
                for (MdBusinessHost mdBusinessHost : mdBusinessHostList) {
                    monHostList.add(mdMenuDataListener.getMonHostById(mdBusinessHost.getHostid()));
                }
            }
            for (MonHost monHost : monHostList) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("dimension2", monHost.getHostid());
                dimensionList.add(m);
            }
            break;
        case Constant.ALL_BRAS:
            List<BdNas> bdNasList = new ArrayList<BdNas>();
            if (Constant.DYNAMICTYPE_AREA.equals(Dimension1Type)) {
                // 筛选属地下bas
                List<BdNas> bdNasListAll = MdMenuDataListener.getBdNasList();
                for (BdNas BdNas : bdNasListAll) {
                    if (BdNas.getArea_no().equals(Dimension1Id)) {
                        bdNasList.add(BdNas);
                    }
                }
            } else {
                bdNasList = MdMenuDataListener.getBdNasList();
            }
            for (BdNas bdNas : bdNasList) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("dimension2", bdNas.getId());
                dimensionList.add(m);
            }
            break;
        case Constant.ALL_APN:
            for (MdApnRecord mdApnRecord : CommonInit.getMdApnRecordList()) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("dimension2", mdApnRecord.getApnid());
                dimensionList.add(m);
            }
            break;
        }
        return dimensionList;
    }

    /**
     * 
     * @Title: judgeDimensionType
     * @Description: (获取维度类型)
     * @param @param
     *            URL
     * @param @return
     *            参数
     * @return int 返回类型
     * @throws @1-->
     *             node/summary * node/summary： 维度1 节点总览 @2--> area/summary *
     *             area/summary: 维度1 地市总览 @3--> host/summary * host/summary
     *             服务器总览 @4--> node(all) * node(全部) 节点 ： 维度1：节点名称， 维度2：空 @5-->
     *             node(single) * node(单个) 节点 ： 维度1：节点名称， 维度2：空 @6--> area(all)
     *             * area(全部) 地市 ： 维度1：地市名称， 维度2：空 @7--> area(single) * area(单个)
     *             地市 ： 维度1：地市名称， 维度2：空 @8--> host(all) * host(全部) 服务器：
     *             维度1：主机名称， 维度2：空 @9--> host(single) * host(单个) 服务器： 维度1：主机名称，
     *             维度2：空 @10--> node(all)/host(all) * node/host 节点(全部) -->
     *             主机(全部) ： 维度1：节点名称， 维度2：主机IP @11--> node(single)/host(all) *
     *             node/host 节点(单个) --> 主机(全部) ： 维度1：节点名称， 维度2：主机IP @12-->
     *             node(single)/host(single) * node/host 节点(单个) --> 主机(单个) ：
     *             维度1：节点名称， 维度2：主机IP @13--> node(all)/area(all) * node/area
     *             节点(全部) --> 地市(全部) ： 维度1：节点名称， 维度2：地市名称 @14-->
     *             node(single)/area(all) * node/area 节点(单个) --> 地市(全部) ：
     *             维度1：节点名称， 维度2：地市名称 @15--> node(single)/area(single) *
     *             node/area 节点(单个) --> 地市(单个) ： 维度1：节点名称， 维度2：地市名称 @16-->
     *             area(all)/bras(all) * area/bras 地市(全部) --> bras(全部) : 维度1：地市
     *             ， 维度2：brasip @17--> area(single)/bras(all) * area/bras 地市(单个)
     *             --> bras(全部) : 维度1：地市 ， 维度2：brasip @18-->
     *             area(single)/bras(single) * area/bras 地市(单个) --> bras(单个) :
     *             维度1：地市 ， 维度2：brasip
     */
    public int judgeDimensionType(String URL) {
        int dimensionType = 0;
        String businesslinkurl = URL.split(ConstantUtill.URL_SPLIT)[0];
        String s[] = businesslinkurl.split(ConstantUtill.URL_SPLIT)[0].split("/");
        String dynamicDimensionOne = "";
        String dynamicDimensionTwo = "";
        int length = s.length;
        if (length > 0) {
            dynamicDimensionOne = s[length - 1];
        }
        if (length > 1) {
            dynamicDimensionTwo = s[length - 2] + "/" + s[length - 1];
        }
        if (URL.equals(businesslinkurl)) {
            switch (dynamicDimensionOne) {
            case Constant.DYNAMICTYPE_NODE:
                dimensionType = Constant.DIMENSIONTYPE_NODE_ALL;
                break;
            case Constant.DYNAMICTYPE_AREA:
                dimensionType = Constant.DIMENSIONTYPE_AREA_ALL;
                break;
            case Constant.DYNAMICTYPE_HOST:
                dimensionType = Constant.DIMENSIONTYPE_HOST_ALL;
                break;
            case Constant.DYNAMICTYPE_BRAS:
                dimensionType = Constant.DIMENSIONTYPE_BRAS_ALL;
                break;
            case Constant.DYNAMICTYPE_APN:
                dimensionType = Constant.DIMENSIONTYPE_APN_ALL;
                break;
            }
            switch (dynamicDimensionTwo) {
            case Constant.DYNAMICTYPE_NODE + "/" + Constant.DYNAMICTYPE_SUMMARY:
                dimensionType = Constant.DIMENSIONTYPE_NODE_SUMMARY;
                break;
            case Constant.DYNAMICTYPE_AREA + "/" + Constant.DYNAMICTYPE_SUMMARY:
                dimensionType = Constant.DIMENSIONTYPE_AREA_SUMMARY;
                break;
            case Constant.DYNAMICTYPE_HOST + "/" + Constant.DYNAMICTYPE_SUMMARY:
                dimensionType = Constant.DIMENSIONTYPE_HOST_SUMMARY;
                break;
            case Constant.DYNAMICTYPE_BRAS + "/" + Constant.DYNAMICTYPE_SUMMARY:
                dimensionType = Constant.DIMENSIONTYPE_BRAS_SUMMARY;
                break;
            case Constant.DYNAMICTYPE_APN + "/" + Constant.DYNAMICTYPE_SUMMARY:
                dimensionType = Constant.DIMENSIONTYPE_APN_SUMMARY;
                break;
            case Constant.DYNAMICTYPE_NODE + "/" + Constant.DYNAMICTYPE_HOST:
                dimensionType = Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL;
                break;
            case Constant.DYNAMICTYPE_NODE + "/" + Constant.DYNAMICTYPE_AREA:
                dimensionType = Constant.DIMENSIONTYPE_NODE_ALL_AREA_ALL;
                break;
            case Constant.DYNAMICTYPE_AREA + "/" + Constant.DYNAMICTYPE_BRAS:
                dimensionType = Constant.DIMENSIONTYPE_AREA_ALL_BRAS_ALL;
                break;
            case Constant.DYNAMICTYPE_AREA + "/" + Constant.DYNAMICTYPE_NODE:
                dimensionType = Constant.DIMENSIONTYPE_AREA_ALL_NODE_ALL;
                break;
            }
        } else {
            String attr = URL.split(ConstantUtill.URL_SPLIT)[1];
            String attrs[] = attr.split("/");
            int attrlength = attrs.length;
            switch (dynamicDimensionOne) {
            case Constant.DYNAMICTYPE_NODE:
                dimensionType = Constant.DIMENSIONTYPE_NODE_SINGLE;
                break;
            case Constant.DYNAMICTYPE_AREA:
                dimensionType = Constant.DIMENSIONTYPE_AREA_SINGLE;
                break;
            case Constant.DYNAMICTYPE_HOST:
                dimensionType = Constant.DIMENSIONTYPE_HOST_SINGLE;
                break;
            case Constant.DYNAMICTYPE_BRAS:
                dimensionType = Constant.DIMENSIONTYPE_BRAS_SINGLE;
                break;
            case Constant.DYNAMICTYPE_APN:
                dimensionType = Constant.DIMENSIONTYPE_APN_SINGLE;
                break;
            }
            switch (dynamicDimensionTwo) {
            case Constant.DYNAMICTYPE_NODE + "/" + Constant.DYNAMICTYPE_HOST:
                if (attrlength > 2) {
                    dimensionType = Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE;
                } else {
                    dimensionType = Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL;
                }
                break;
            case Constant.DYNAMICTYPE_NODE + "/" + Constant.DYNAMICTYPE_AREA:
                if (attrlength > 2) {
                    dimensionType = Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_SINGLE;
                } else {
                    dimensionType = Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_ALL;
                }
                break;
            case Constant.DYNAMICTYPE_AREA + "/" + Constant.DYNAMICTYPE_BRAS:
                if (attrlength > 2) {
                    dimensionType = Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_SINGLE;
                } else {
                    dimensionType = Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL;
                }
                break;
            case Constant.DYNAMICTYPE_AREA + "/" + Constant.DYNAMICTYPE_NODE:
                if (attrlength > 2) {
                    dimensionType = Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_SINGLE;
                } else {
                    dimensionType = Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_ALL;
                }
                break;
            }
        }
        return dimensionType;
    }

    /**
     * 
     * @Title: addMdAlarmRule @Description: (修改告警规则) @param @param
     *         mdAlarmRule @param @return 参数 @return Map<String,Object>
     *         返回类型 @throws
     */
    @Transactional
    public WebResult modifyMdAlarmRule(Map<String, Object> map) {
        MdAlarmRule mdAlarmRule = new MdAlarmRule();
        String URL = getWholeUrl(map);
        String rule_id = "";
        String alarm_rule = "";
        String alarm_level = "";
        String modes = "";
        String alarmmsg = "";
        String report_rule = "";
        String alarm_type = "";
        if (map.get("rule_id") != null) {
            rule_id = map.get("rule_id").toString();
        }
        if (map.get("alarm_rule") != null) {
            alarm_rule = map.get("alarm_rule").toString();
        }
        if (map.get("alarm_level") != null) {
            alarm_level = map.get("alarm_level").toString();
        }
        if (map.get("modes") != null) {
            modes = map.get("modes").toString();
        }
        if (map.get("alarmmsg") != null) {
            alarmmsg = map.get("alarmmsg").toString();
        }
        if (map.get("report_rule") != null) {
            report_rule = map.get("report_rule").toString();
        }
        if (map.get("alarm_type") != null) {
            alarm_type = map.get("alarm_type").toString();
        }
        if (!("").equals(alarm_rule) && !("").equals(alarmmsg)) {
            MdAlarmRule hisMdAlarmRule = new MdAlarmRule();
            hisMdAlarmRule.setRule_id(rule_id);
            hisMdAlarmRule = mdAlarmRuleDAO.getMdAlarmRule(hisMdAlarmRule).get(0);
            mdAlarmRule.setRule_id(rule_id);
            mdAlarmRule.setAlarm_rule(alarm_rule);
            mdAlarmRule.setAlarm_level(alarm_level);
            mdAlarmRule.setModes(modes);
            mdAlarmRule.setAlarmmsg(alarmmsg);
            mdAlarmRule.setReport_rule(report_rule);
            mdAlarmRule.setAlarm_type(alarm_type);
            String businesslinkurl = URL.split(ConstantUtill.URL_SPLIT)[0];
            MdMenu mdMenu = menuService.getTwoLevelMdMenu(businesslinkurl);
            mdAlarmRule.setName(mdMenu.getName());
            int dimension_type = judgeDimensionType(URL);
            mdAlarmRule.setDimension_type(dimension_type);
            mdAlarmRule.setUpdate_time(new Date());
            int updateResult = mdAlarmRuleDAO.update(mdAlarmRule);
            if (1 == updateResult) {
                // 用户日志记录
                operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ALARMRULE_MANAGE,
                        "修改数据[告警规则:" + hisMdAlarmRule.getAlarmmsg() + "-->"
                                + mdAlarmRule.getAlarmmsg() + "]");
                MdAlarmRuleDetail mdAlarmRuleDetail = new MdAlarmRuleDetail();
                mdAlarmRuleDetail.setRule_id(mdAlarmRule.getRule_id());
                mdAlarmRuleDetail.setAlarm_level(mdAlarmRule.getAlarm_level());
                mdAlarmRuleDetail.setAlarm_rule(mdAlarmRule.getAlarm_rule());
                mdAlarmRuleDetail.setModes(mdAlarmRule.getModes());
                mdAlarmRuleDetail.setAlarmmsg(mdAlarmRule.getAlarmmsg());
                mdAlarmRuleDetail.setReport_rule(report_rule);
                mdAlarmRuleDetail.setAlarm_type(alarm_type);
                mdAlarmRuleDetail.setUpdate_time(new Date());
                mdAlarmRuleDetailDAO.update(mdAlarmRuleDetail);
                return new WebResult(true, "成功");
            } else {
                return new WebResult(false, "失败");
            }
        } else {
            return new WebResult(false, "失败");
        }
    }

    /**
     * 
     * @Title: deleteMdAlarmRule @Description: (批量删除告警规则) @param @param
     *         metricidArray @param @return 参数 @return Map<String,Object>
     *         返回类型 @throws
     */
    @Transactional
    public WebResult deleteMdAlarmRule(String[] ruleidArray) {
        int deleteSuccess = 0;
        int deleteFail = 0;
        List<String> delSuccessList = new ArrayList<String>();
        String message = "";
        if (ruleidArray != null && ruleidArray.length != 0) {
            for (String id : ruleidArray) {
                MdAlarmRule mdAlarmRule = new MdAlarmRule();
                mdAlarmRule.setRule_id(id);
                mdAlarmRule = mdAlarmRuleDAO.getMdAlarmRule(mdAlarmRule).get(0);
                int deleteResult = mdAlarmRuleDAO.delete(id);
                if (1 == deleteResult) {
                    mdAlarmRuleDetailDAO.delete(id);
                    delSuccessList.add(mdAlarmRule.getAlarmmsg());
                    deleteSuccess++;
                } else {
                    deleteFail++;
                }
            }
        }
        if (deleteSuccess > 0) {
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ALARMRULE_MANAGE,
                    "删除数据[告警规则:" + delSuccessList);
            message = message + deleteSuccess + "条告警规则删除成功。";
        }
        if (deleteFail > 0) {
            message = message + deleteFail + "条告警规则删除失败。";
        }
        return new WebResult(true, message);
    }

    /**
     * 
     * @Title: getInitializationList @Description: (获取告警规则初始化参数) @param @return
     *         参数 @return List<Map<String,Object>> 返回类型 @throws
     */
    public List<Map<String, Object>> getInitializationList() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<String> stringList = new ArrayList<String>();
        List<Map<String, Object>> pageChartList = pageChartDAO.getGroupURL();
        for (Map<String, Object> map : pageChartList) {
            String URL = map.get("URL").toString();
            String sURL = URL.split(ConstantUtill.URL_SPLIT)[0];
            if (!stringList.contains(sURL)) {
                stringList.add(sURL);
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("URL", sURL);
                MdMenu mdMenu = menuService.getMdMenuBusinessLink(sURL);
                if (mdMenu != null) {
                    m.put("business_link", mdMenu.getBusiness_link());
                }
                List<Map<String, Object>> dynamicList = new ArrayList<Map<String, Object>>();
                String s[] = sURL.split("/");
                List<String> sl = new ArrayList<String>();
                List<String> attrList = new ArrayList<String>();
                int i = 0;
                for (String string : s) {
                    switch (string) {
                    case Constant.DYNAMICTYPE_NODE:
                        attrList.add(Constant.DIMENSION_NODE);
                        m.put("attrList", attrList);
                        m.put("dynamicList", dynamicList);
                        sl.add(Constant.ALL_NODE);
                        addDynamicByURL(m, pageChartList, i, sURL, sl, attrList);
                        i++;
                        break;
                    case Constant.DYNAMICTYPE_AREA:
                        attrList.add(Constant.DIMENSION_AREA);
                        m.put("attrList", attrList);
                        m.put("dynamicList", dynamicList);
                        sl.add(Constant.ALL_AREA);
                        addDynamicByURL(m, pageChartList, i, sURL, sl, attrList);
                        i++;
                        break;
                    case Constant.DYNAMICTYPE_HOST:
                        attrList.add(Constant.DIMENSION_HOST);
                        m.put("attrList", attrList);
                        m.put("dynamicList", dynamicList);
                        sl.add(Constant.ALL_HOST);
                        addDynamicByURL(m, pageChartList, i, sURL, sl, attrList);
                        i++;
                        break;
                    case Constant.DYNAMICTYPE_BRAS:
                        attrList.add(Constant.DIMENSION_BRAS);
                        m.put("attrList", attrList);
                        m.put("dynamicList", dynamicList);
                        sl.add(Constant.ALL_BRAS);
                        addDynamicByURL(m, pageChartList, i, sURL, sl, attrList);
                        i++;
                        break;
                    case Constant.DYNAMICTYPE_APN:
                        attrList.add(Constant.DIMENSION_APN);
                        m.put("attrList", attrList);
                        m.put("dynamicList", dynamicList);
                        sl.add(Constant.ALL_APN);
                        addDynamicByURL(m, pageChartList, i, sURL, sl, attrList);
                        i++;
                        break;
                    }
                }
                result.add(m);
            }
        }
        return result;
    }

    /**
     * 
     * @Title: addDynamicByURL @Description: (根据URL动态添加维度) @param @param
     *         map @param @param pageChartList @param @param i 参数 @return void
     *         返回类型 @throws
     */
    public void addDynamicByURL(Map<String, Object> map, List<Map<String, Object>> pageChartList,
            int i, String sURL, List<String> sl, List<String> attrList) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> dynamicList = (List<Map<String, Object>>) map.get("dynamicList");
        String URL = map.get("URL").toString();
        // 添加全部维度
        if (URL.equals(sURL) && !URL.contains("summary")) {
            if (i == 0) {
                Map<String, Object> dynamicMap = new HashMap<String, Object>();
                dynamicMap.put("name", sl.get(i));
                dynamicMap.put("id", sl.get(i));
                dynamicMap.put("isDynamic", false);
                dynamicMap.put("list", new ArrayList<Map<String, Object>>());
                dynamicList.add(dynamicMap);
            } else {
                for (Map<String, Object> dm : dynamicList) {
                    if (dm.containsValue(sl.get(i - 1))) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> dl = (List<Map<String, Object>>) dm.get("list");
                        Map<String, Object> rm = new HashMap<String, Object>();
                        rm.put("name", sl.get(i));
                        rm.put("id", sl.get(i));
                        rm.put("isDynamic", false);
                        rm.put("list", new ArrayList<Map<String, Object>>());
                        dl.add(rm);
                    }
                }
            }
        }
        // 添加单个维度
        for (Map<String, Object> m : pageChartList) {
            String mURL = m.get("URL").toString();
            if (mURL.contains(ConstantUtill.URL_SPLIT)
                    && URL.equals(mURL.split(ConstantUtill.URL_SPLIT)[0])) {
                String s[] = mURL.split(ConstantUtill.URL_SPLIT + "/")[1].split("/");
                if (i == 0) {
                    boolean f = true;
                    for (Map<String, Object> dm : dynamicList) {
                        if (dm.containsValue(s[i])) {
                            f = false;
                            break;
                        }
                    }
                    if (f) {
                        Map<String, Object> rm = new HashMap<String, Object>();
                        String name = getDynamicName(s[i], attrList.get(i));
                        rm.put("name", name);
                        rm.put("id", s[i]);
                        rm.put("isDynamic", true);
                        rm.put("list", new ArrayList<Map<String, Object>>());
                        dynamicList.add(rm);
                    }
                } else {
                    for (Map<String, Object> dm : dynamicList) {
                        if (dm.containsValue(s[i - 1])) {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> dl = (List<Map<String, Object>>) dm
                                    .get("list");
                            Map<String, Object> rm = new HashMap<String, Object>();
                            String name = getDynamicName(s[i], attrList.get(i));
                            rm.put("name", name);
                            rm.put("id", s[i]);
                            rm.put("isDynamic", true);
                            rm.put("list", new ArrayList<Map<String, Object>>());
                            dl.add(rm);
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * @Title: getMetriclist @Description: (获取指标列表) @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    public List<MdMetric> getMetriclist(Map<String, Object> map) {
        String URL = getWholeUrl(map);
        String hostId = "";
        if (map.get("monitorTargetOneName") != null) {
            String monitorTargetOneName = map.get("monitorTargetOneName").toString();
            if (monitorTargetOneName.equals(Constant.SINGLE_HOST)) {
                if (map.get("attr1") != null) {
                    hostId = map.get("attr1").toString();
                }
            }
        }
        if (map.get("monitorTargetThreeName") != null) {
            String monitorTargetThreeName = map.get("monitorTargetThreeName").toString();
            if (monitorTargetThreeName.equals(Constant.SINGLE_HOST)) {
                if (map.get("attr2") != null) {
                    hostId = map.get("attr2").toString();
                }
            }
        }
        List<Map<String, Object>> metricList = pageChartDAO.getMetricIdByUrl(URL);
        if (null == metricList || metricList.size() == 0) {
            String businesslinkurl = URL.split(ConstantUtill.URL_SPLIT)[0];
            metricList = pageChartDAO.getMetricIdByUrl(businesslinkurl);
        }
        String metricIds = "";
        for (Map<String, Object> metricmap : metricList) {
            metricIds = metricIds + "," + metricmap.get("METRIC_IDS").toString();
        }
        if (metricIds.length() > 0) {
            metricIds = metricIds.replace(",", "','").substring(2) + "'";
        } else {
            metricIds = "''";
        }
        List<MdMetric> mdMetricList = mdMetricDAO.getMdMetricListByIds(metricIds);
        for (MdMetric mdMetric : mdMetricList) {
            List<String> metric_attr = new ArrayList<String>();
            if (!ToolsUtils.StringIsNull(hostId)
                    && mdMetric.getMetric_type().equals(ConstantUtill.PROCESSMETRICTYPE)
                    && !hostId.equals(Constant.ALL_HOST)) {
                MdHostMetric mdHostMetric = hostMetricDAO
                        .getMdHostMetricByhostmdMetric(mdMetric.getId(), hostId);
                if (mdHostMetric != null) {
                    mkMetricByMetricType(mdHostMetric);
                    String[] arr = mdHostMetric.getScript_param()
                            .split(ConstantUtill.PROCESSMETRICSPLITSTR);
                    metric_attr = Arrays.asList(arr);
                }
            }
            if (!ToolsUtils.StringIsNull(hostId)
                    && StringUtil.isContains(",", fileSystemMetricId, mdMetric.getId())
                    && !hostId.equals(Constant.ALL_HOST)) {
                List<MdHostFileSystem> mdHostFileSystemList = mdHostFileSystemDAO
                        .getMdHostFileSystemListByHostId(hostId);
                for (MdHostFileSystem mdHostFileSystem : mdHostFileSystemList) {
                    metric_attr.add(mdHostFileSystem.getFilesystem());
                }
            }
            if (StringUtil.isContains(",", authenFailMetricId, mdMetric.getId())) {
                List<MdParam> mdParamList = mdParamDAO
                        .getParamByType(ConstantUtill.AUTHENFAILMDPARAMTYPE);
                for (MdParam mdParam : mdParamList) {
                    metric_attr.add(mdParam.getDescription());
                }
            }
            mdMetric.setMetric_attr(metric_attr);
        }
        return mdMetricList;
    }

    /**
     * 
     * @Title: getChartsList @Description: (获取图表列表) @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    public List<Map<String, Object>> getChartsList(Map<String, Object> map) {
        String URL = getWholeUrl(map);
        String hostId = "";
        if (map.get("monitorTargetOneName") != null) {
            String monitorTargetOneName = map.get("monitorTargetOneName").toString();
            if (monitorTargetOneName.equals(Constant.SINGLE_HOST)) {
                if (map.get("attr1") != null) {
                    hostId = map.get("attr1").toString();
                }
            }
        }
        if (map.get("monitorTargetThreeName") != null) {
            String monitorTargetThreeName = map.get("monitorTargetThreeName").toString();
            if (monitorTargetThreeName.equals(Constant.SINGLE_HOST)) {
                if (map.get("attr2") != null) {
                    hostId = map.get("attr2").toString();
                }
            }
        }
        String businesslinkurl = "";
        List<Map<String, Object>> chartsList = pageChartDAO.getChartsByUrl(URL);
        if (null == chartsList || chartsList.size() == 0) {
            businesslinkurl = URL.split(ConstantUtill.URL_SPLIT)[0];
            chartsList = pageChartDAO.getChartsByUrl(businesslinkurl);
        }
        for (Map<String, Object> charts : chartsList) {
            String chart_name = charts.get("CHART_NAME").toString();
            List<Map<String, Object>> metricList = pageChartDAO.getMetricIdByUrlAndChartName(URL,
                    chart_name);
            if (null == metricList || metricList.size() == 0) {
                businesslinkurl = URL.split(ConstantUtill.URL_SPLIT)[0];
                metricList = pageChartDAO.getMetricIdByUrlAndChartName(businesslinkurl, chart_name);
            }
            String metricIds = "";
            for (Map<String, Object> metricmap : metricList) {
                metricIds = metricIds + "," + metricmap.get("METRIC_IDS").toString();
            }
            if (metricIds.length() > 0) {
                metricIds = metricIds.replace(",", "','").substring(2) + "'";
            } else {
                metricIds = "''";
            }
            List<MdMetric> mdMetricList = mdMetricDAO.getMdMetricListByIds(metricIds);
            for (MdMetric mdMetric : mdMetricList) {
                List<String> metric_attr = new ArrayList<String>();
                if (!ToolsUtils.StringIsNull(hostId)
                        && mdMetric.getMetric_type().equals(ConstantUtill.PROCESSMETRICTYPE)
                        && !hostId.equals(Constant.ALL_HOST)) {
                    MdHostMetric mdHostMetric = hostMetricDAO
                            .getMdHostMetricByhostmdMetric(mdMetric.getId(), hostId);
                    if (mdHostMetric != null) {
                        mkMetricByMetricType(mdHostMetric);
                        String[] arr = mdHostMetric.getScript_param()
                                .split(ConstantUtill.PROCESSMETRICSPLITSTR);
                        metric_attr = Arrays.asList(arr);
                    }
                }
                if (!ToolsUtils.StringIsNull(hostId)
                        && StringUtil.isContains(",", fileSystemMetricId, mdMetric.getId())
                        && !hostId.equals(Constant.ALL_HOST)) {
                    List<MdHostFileSystem> mdHostFileSystemList = mdHostFileSystemDAO
                            .getMdHostFileSystemListByHostId(hostId);
                    for (MdHostFileSystem mdHostFileSystem : mdHostFileSystemList) {
                        metric_attr.add(mdHostFileSystem.getFilesystem());
                    }
                }
                if (StringUtil.isContains(",", authenFailMetricId, mdMetric.getId())) {
                    List<MdParam> mdParamList = mdParamDAO
                            .getParamByType(ConstantUtill.AUTHENFAILMDPARAMTYPE);
                    for (MdParam mdParam : mdParamList) {
                        metric_attr.add(mdParam.getDescription());
                    }
                }
                mdMetric.setMetric_attr(metric_attr);
            }
            charts.put("mdMetricList", mdMetricList);
        }
        return chartsList;
    }

    /**
     * 
     * @Title: getWholeUrl @Description: (获取完整URL) @param @param
     *         map @param @return 参数 @return Map<String,Object> 返回类型 @throws
     */
    public String getWholeUrl(Map<String, Object> map) {
        String url = "";
        String attr1 = "";
        String attr2 = "";
        if (map.get("monitorTargetThreeType") != null) {
            String monitorTargetThreeType = map.get("monitorTargetThreeType").toString();
            switch (monitorTargetThreeType) {
            case Constant.MONITORTARGET_SUMMARY:
                url = map.get("monitorTargetThreeUrl").toString();
                break;
            case Constant.MONITORTARGET_ALL:
                url = map.get("monitorTargetThreeUrl").toString();
                if (map.get("attr1") != null && !map.get("attr1").toString().equals("")) {
                    attr1 = map.get("attr1").toString();
                    url = url + ConstantUtill.URL_SPLIT + "/" + attr1;
                }
                break;
            case Constant.MONITORTARGET_SINGLE:
                url = map.get("monitorTargetThreeUrl").toString();
                if (map.get("attr1") != null && !map.get("attr1").toString().equals("")) {
                    if (map.get("attr2") != null && !map.get("attr2").toString().equals("")) {
                        attr1 = map.get("attr1").toString();
                        attr2 = map.get("attr2").toString();
                        url = url + ConstantUtill.URL_SPLIT + "/" + attr1 + "/" + attr2;
                    } else {
                        attr1 = map.get("attr1").toString();
                        url = url + ConstantUtill.URL_SPLIT + "/" + attr1;
                    }
                }
                break;
            }
        } else {
            if (map.get("monitorTargetOneType") != null) {
                String monitorTargetOneType = map.get("monitorTargetOneType").toString();
                switch (monitorTargetOneType) {
                case Constant.MONITORTARGET_SUMMARY:
                    url = map.get("monitorTargetOneUrl").toString();
                    break;
                case Constant.MONITORTARGET_ALL:
                    url = map.get("monitorTargetOneUrl").toString();
                    break;
                case Constant.MONITORTARGET_SINGLE:
                    url = map.get("monitorTargetOneUrl").toString();
                    if (map.get("attr1") != null && !map.get("attr1").toString().equals("")) {
                        attr1 = map.get("attr1").toString();
                        url = url + ConstantUtill.URL_SPLIT + "/" + attr1;
                    }
                    break;
                }
            } else {
                return url;
            }
        }
        return url;
    }

    /**
     * 遍历指标，根据指标类型进行判断.如果是进程类指标，则将主机进程管理表(MD_HOST_PROCESS)中的进程参数合并进来.
     * <p>
     * 只会在有进程指标的情况下会进行合并动作。
     * 
     * @param
     * @return
     */
    private MdHostMetric mkMetricByMetricType(MdHostMetric info) {
        if (ConstantUtill.PROCESSMETRICTYPE.equals(info.getMetric_type())) {
            List<MdHostMetric> mdHostMetricList = hostMetricDAO
                    .getMdHostMetricBymdMetric(info.getHost_id());
            List<MdHostProcess> processList = mdHostProcessDAO
                    .getMdHostProcessByHostId(info.getHost_id());
            String tmp = this.mergeProcessParam(mdHostMetricList, processList);
            info.setScript_param(tmp);
        }
        return info;
    }

    // 将指标中的参数和进程list中的进程关键字合并(去重).
    private String mergeProcessParam(List<MdHostMetric> mdHostMetricList,
            List<MdHostProcess> processList) {
        Set<String> paramSet = new HashSet<>();
        for (MdHostMetric mdHostMetric : mdHostMetricList) {
            if (mdHostMetric.getScript_param() != null) {
                String[] params = mdHostMetric.getScript_param()
                        .split(ConstantUtill.PROCESSMETRICSPLITSTR);
                for (String param : params) {
                    paramSet.add(param);
                }
            }
        }
        if (processList != null && !processList.isEmpty()) {
            for (MdHostProcess info : processList) {
                paramSet.add(info.getProcess_key());
            }
        }
        StringBuffer strb = new StringBuffer();
        Iterator<String> it = paramSet.iterator();
        while (it.hasNext()) {
            strb.append(it.next()).append(ConstantUtill.PROCESSMETRICSPLITSTR);
        }
        if (strb.length() == 0) {
            return null;
        }
        strb.setLength(strb.length() - ConstantUtill.PROCESSMETRICSPLITSTR.length());
        return strb.toString();
    }

    public String getDynamicName(String id, String attr) {
        String name = "";
        switch (attr) {
        case Constant.DIMENSION_NODE:
            for (MdNode mdNode : MdMenuDataListener.getMdNodeList()) {
                if (mdNode.getId().equals(id)) {
                    name = mdNode.getNode_name();
                }
            }
            break;
        case Constant.DIMENSION_AREA:
            for (MdArea mdArea : MdMenuDataListener.getMdAreaList()) {
                if (mdArea.getAreano().equals(id)) {
                    name = mdArea.getName();
                }
            }
            break;
        case Constant.DIMENSION_HOST:
            for (MonHost monHost : MdMenuDataListener.getMonHostList()) {
                if (monHost.getHostid().equals(id)) {
                    name = monHost.getHostname();
                }
            }
            break;
        case Constant.DIMENSION_BRAS:
            for (BdNas bdNas : MdMenuDataListener.getBdNasList()) {
                if (bdNas.getId().equals(id)) {
                    name = bdNas.getNas_name();
                }
            }
            break;
        case Constant.DIMENSION_APN:
            for (MdApnRecord mdApnRecord : CommonInit.getMdApnRecordList()) {
                if (mdApnRecord.getApnid().equals(id)) {
                    name = mdApnRecord.getApn();
                }
            }
            break;
        }
        return name;
    }

    public String getDynamicNameById(String id) {
        String name = "";
        for (MdNode mdNode : MdMenuDataListener.getMdNodeList()) {
            if (mdNode.getId().equals(id)) {
                name = mdNode.getNode_name();
                return name;
            }
        }
        for (MdArea mdArea : MdMenuDataListener.getMdAreaList()) {
            if (mdArea.getAreano().equals(id)) {
                name = mdArea.getName();
                return name;
            }
        }
        for (MonHost monHost : MdMenuDataListener.getMonHostList()) {
            if (monHost.getHostid().equals(id)) {
                name = monHost.getHostname();
                return name;
            }
        }
        for (BdNas bdNas : MdMenuDataListener.getBdNasList()) {
            if (bdNas.getId().equals(id)) {
                name = bdNas.getNas_name();
                return name;
            }
        }
        for (MdApnRecord mdApnRecord : CommonInit.getMdApnRecordList()) {
            if (mdApnRecord.getApnid().equals(id)) {
                name = mdApnRecord.getApn();
                return name;
            }
        }
        return name;
    }

    /**
     * 
     * @Title: refreshMdAlarmRule @Description: (刷新告警规则) @param @param
     *         ruleidArray @param @return 参数 @return WebResult 返回类型 @throws
     */
    public WebResult refreshMdAlarmRule(String[] ruleidArray) {
        int refreshSuccess = 0;
        int refreshFail = 0;
        String message = "";
        List<String> refreshSuccessList = new ArrayList<String>();
        if (ruleidArray != null && ruleidArray.length != 0) {
            for (String id : ruleidArray) {
                MdAlarmRule alarmRule = new MdAlarmRule();
                alarmRule.setRule_id(id);
                alarmRule = mdAlarmRuleDAO.getMdAlarmRule(alarmRule).get(0);
                List<MdAlarmRule> mdAlarmRuleList = mdAlarmRuleDAO.getMdAlarmRule(alarmRule);
                if (mdAlarmRuleList != null && mdAlarmRuleList.size() != 0) {
                    refreshAddMdAlarmRule(mdAlarmRuleList);
                    refreshSuccessList.add(alarmRule.getAlarmmsg());
                    refreshSuccess++;
                } else {
                    refreshFail++;
                }
            }
        }
        if (refreshSuccess > 0) {
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ALARMRULE_MANAGE,
                    "刷新数据[告警规则:" + refreshSuccessList);
            message = message + refreshSuccess + "条告警规则刷新成功。";
        }
        if (refreshFail > 0) {
            message = message + refreshFail + "条告警规则刷新失败。";
        }
        return new WebResult(true, message);
    }

    /**
     * 
     * @Title: refreshAddMdAlarmRule @Description: (刷新添加告警规则) @param @param
     *         mdAlarmRuleList 参数 @return void 返回类型 @throws
     */
    public void refreshAddMdAlarmRule(List<MdAlarmRule> mdAlarmRuleList) {
        for (MdAlarmRule mdAlarmRule : mdAlarmRuleList) {
            String url = mdAlarmRule.getUrl();
            String businesslinkurl = url.split(ConstantUtill.URL_SPLIT)[0];
            // 获取监控目标map
            Map<String, Object> map = getMonitorTargetMap(businesslinkurl,
                    mdAlarmRule.getDimension_type());
            map.put("attr1", mdAlarmRule.getDimension1());
            map.put("attr2", mdAlarmRule.getDimension2());
            String metric_id = mdAlarmRule.getMetric_id();
            MdAlarmRuleDetail mdAlarmRuleDetail = new MdAlarmRuleDetail();
            mdAlarmRuleDetail.setRule_id(mdAlarmRule.getRule_id());
            mdAlarmRuleDetail.setName(mdAlarmRule.getName());
            mdAlarmRuleDetail.setUrl(mdAlarmRule.getUrl());
            mdAlarmRuleDetail.setDimension_type(mdAlarmRule.getDimension_type());
            mdAlarmRuleDetail.setChart_name(mdAlarmRule.getChart_name());
            mdAlarmRuleDetail.setMetric_id(mdAlarmRule.getMetric_id());
            mdAlarmRuleDetail.setAttr(mdAlarmRule.getAttr());
            mdAlarmRuleDetail.setAlarm_level(mdAlarmRule.getAlarm_level());
            mdAlarmRuleDetail.setAlarm_rule(mdAlarmRule.getAlarm_rule());
            mdAlarmRuleDetail.setModes(mdAlarmRule.getModes());
            mdAlarmRuleDetail.setAlarmmsg(mdAlarmRule.getAlarmmsg());
            mdAlarmRuleDetail.setDimension3(mdAlarmRule.getDimension3());
            mdAlarmRuleDetail.setReport_rule(mdAlarmRule.getReport_rule());
            mdAlarmRuleDetail.setAlarm_type(mdAlarmRule.getAlarm_type());
            mdAlarmRuleDetail.setCreate_time(new Date());
            mdAlarmRuleDetail.setUpdate_time(new Date());
            List<Map<String, Object>> dimensionList = getDimensionList(map);
            if (dimensionList != null && dimensionList.size() > 0) {
                for (Map<String, Object> m : dimensionList) {
                    String attr1 = m.get("dimension1").toString();
                    mdAlarmRuleDetail.setDimension1(attr1);
                    String mdAlarmRuleDetailDimension1URL = businesslinkurl
                            + ConstantUtill.URL_SPLIT + "/" + attr1;
                    mdAlarmRuleDetail.setUrl(mdAlarmRuleDetailDimension1URL);
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> list = (List<Map<String, Object>>) m.get("list");
                    if (list != null && list.size() > 0) {
                        for (Map<String, Object> listmap : list) {
                            String attr2 = listmap.get("dimension2").toString();
                            mdAlarmRuleDetail.setDimension2(attr2);
                            String mdAlarmRuleDetailDimension2URL = businesslinkurl
                                    + ConstantUtill.URL_SPLIT + "/" + attr1 + "/" + attr2;
                            mdAlarmRuleDetail.setUrl(mdAlarmRuleDetailDimension2URL);
                            mdAlarmRuleDetail.setAlarm_id(IDGenerateUtil.getUuid());
                            boolean judgeMetricIsExist = judgeMetricIsExist(
                                    mdAlarmRuleDetailDimension2URL, metric_id);
                            if (judgeMetricIsExist) {
                                List<MdAlarmRuleDetail> mdAlarmRuleDetailList = mdAlarmRuleDetailDAO
                                        .getMdAlarmRuleDetailByUrlAndRuleId(
                                                mdAlarmRuleDetail.getUrl(),
                                                mdAlarmRuleDetail.getRule_id());
                                if (mdAlarmRuleDetailList == null
                                        || mdAlarmRuleDetailList.size() == 0) {
                                    mdAlarmRuleDetailDAO.insertMdAlarmRuleDetail(mdAlarmRuleDetail);
                                }
                            }
                        }
                    } else {
                        mdAlarmRuleDetail.setDimension2("");
                        mdAlarmRuleDetail.setAlarm_id(IDGenerateUtil.getUuid());
                        boolean judgeMetricIsExist = judgeMetricIsExist(
                                mdAlarmRuleDetailDimension1URL, metric_id);
                        if (judgeMetricIsExist) {
                            List<MdAlarmRuleDetail> mdAlarmRuleDetailList = mdAlarmRuleDetailDAO
                                    .getMdAlarmRuleDetailByUrlAndRuleId(mdAlarmRuleDetail.getUrl(),
                                            mdAlarmRuleDetail.getRule_id());
                            if (mdAlarmRuleDetailList == null
                                    || mdAlarmRuleDetailList.size() == 0) {
                                mdAlarmRuleDetailDAO.insertMdAlarmRuleDetail(mdAlarmRuleDetail);
                            }
                        }
                    }
                }
            } else {
                mdAlarmRuleDetail.setDimension1("");
                mdAlarmRuleDetail.setDimension2("");
                mdAlarmRuleDetail.setAlarm_id(IDGenerateUtil.getUuid());
                List<MdAlarmRuleDetail> mdAlarmRuleDetailList = mdAlarmRuleDetailDAO
                        .getMdAlarmRuleDetailByUrlAndRuleId(mdAlarmRuleDetail.getUrl(),
                                mdAlarmRuleDetail.getRule_id());
                if (mdAlarmRuleDetailList == null || mdAlarmRuleDetailList.size() == 0) {
                    mdAlarmRuleDetailDAO.insertMdAlarmRuleDetail(mdAlarmRuleDetail);
                }
            }
        }
    }

    /**
     * 
     * @Title: getMonitorTargetMap @Description: (获取监控目标map) @param @param
     *         url @param @param dimension_type @param @return 参数 @return
     *         Map<String,Object> 返回类型 @throws
     */
    public Map<String, Object> getMonitorTargetMap(String url, int dimension_type) {
        Map<String, Object> monitorTargetMap = new HashMap<String, Object>();
        MdMenu mdMenu = menuService.getMdMenuByUrl(url);
        MdMenu mdMenuOne = new MdMenu();
        switch (dimension_type) {
        case Constant.DIMENSIONTYPE_NODE_SUMMARY:
            /** 维度类型 1--> node/summary * node/summary： 维度1 节点总览 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SUMMARY);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", mdMenu.getShow_name());
            break;
        case Constant.DIMENSIONTYPE_AREA_SUMMARY:
            /** 维度类型2--> area/summary * area/summary: 维度1 地市总览 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SUMMARY);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", mdMenu.getShow_name());
            break;
        case Constant.DIMENSIONTYPE_HOST_SUMMARY:
            /** 维度类型3--> host/summary * host/summary 服务器总览 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SUMMARY);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_HOST);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", mdMenu.getShow_name());
            break;
        case Constant.DIMENSIONTYPE_NODE_ALL:
            /** 维度类型4--> node(all) * node(全部) 节点 ： 维度1：节点名称， 维度2：空 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.ALL_NODE);
            break;
        case Constant.DIMENSIONTYPE_NODE_SINGLE:
            /** 维度类型5--> node(single) * node(单个) 节点 ： 维度1：节点名称， 维度2：空 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_NODE);
            break;
        case Constant.DIMENSIONTYPE_AREA_ALL:
            /** 维度类型6--> area(all) * area(全部) 地市 ： 维度1：地市名称， 维度2：空 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.ALL_AREA);
            break;
        case Constant.DIMENSIONTYPE_AREA_SINGLE:
            /** 维度类型7--> area(single) * area(单个) 地市 ： 维度1：地市名称， 维度2：空 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_AREA);
            break;
        case Constant.DIMENSIONTYPE_HOST_ALL:
            /** 维度类型8--> host(all) * host(全部) 服务器： 维度1：主机名称， 维度2：空 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_HOST);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.ALL_HOST);
            break;
        case Constant.DIMENSIONTYPE_HOST_SINGLE:
            /** 维度类型9--> host(single) * host(单个) 服务器： 维度1：主机名称， 维度2：空 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_HOST);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_HOST);
            break;
        case Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL:
            /**
             * 维度类型10--> node(all)/host(all) * node/host 节点(全部) --> 主机(全部) ：
             * 维度1：节点名称， 维度2：主机IP
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.ALL_NODE);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_HOST);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.ALL_HOST);
            break;
        case Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL:
            /**
             * 维度类型11--> node(single)/host(all) * node/host 节点(单个) --> 主机(全部) ：
             * 维度1：节点名称， 维度2：主机IP
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_NODE);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_HOST);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.ALL_HOST);
            break;
        case Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE:
            /**
             * 维度类型12--> node(single)/host(single) * node/host 节点(单个) --> 主机(单个)
             * ： 维度1：节点名称， 维度2：主机IP
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_NODE);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_HOST);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.SINGLE_HOST);
            break;
        case Constant.DIMENSIONTYPE_NODE_ALL_AREA_ALL:
            /**
             * 维度类型13--> node(all)/area(all) * node/area 节点(全部) --> 地市(全部) ：
             * 维度1：节点名称， 维度2：地市名称
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.ALL_NODE);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.ALL_AREA);
            break;
        case Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_ALL:
            /**
             * 维度类型14--> node(single)/area(all) * node/area 节点(单个) --> 地市(全部) ：
             * 维度1：节点名称， 维度2：地市名称
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_NODE);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.ALL_AREA);
            break;
        case Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_SINGLE:
            /**
             * 维度类型15--> node(single)/area(single) * node/area 节点(单个) --> 地市(单个)
             * ： 维度1：节点名称， 维度2：地市名称
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_NODE);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.SINGLE_AREA);
            break;
        case Constant.DIMENSIONTYPE_AREA_ALL_BRAS_ALL:
            /**
             * 维度类型16--> area(all)/bras(all) * area/bras 地市(全部) --> bras(全部) :
             * 维度1：地市 ， 维度2：brasip
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.ALL_AREA);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_BRAS);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.ALL_BRAS);
            break;
        case Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL:
            /**
             * 维度类型17--> area(single)/bras(all) * area/bras 地市(单个) --> bras(全部)
             * : 维度1：地市 ， 维度2：brasip
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_AREA);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_BRAS);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.ALL_BRAS);
            break;
        case Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_SINGLE:
            /**
             * 维度类型18--> area(single)/bras(single) * area/bras 地市(单个) -->
             * bras(单个) : 维度1：地市 ， 维度2：brasip
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_AREA);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_BRAS);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.SINGLE_BRAS);
            break;
        case Constant.DIMENSIONTYPE_AREA_ALL_NODE_ALL:
            /**
             * 维度类型19--> area(all)/node(all) * area/node 地市(全部) --> 节点(全部) ：
             * 维度1：地市名称， 维度2：节点名称
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.ALL_AREA);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.ALL_NODE);
            break;
        case Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_ALL:
            /**
             * 维度类型20--> area(single)/node(all) * area/node 地市(单个) --> 节点(全部) ：
             * 维度1：地市名称， 维度2：节点名称
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_AREA);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.ALL_NODE);
            break;
        case Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_SINGLE:
            /**
             * 维度类型21--> area(single)/node(single) * area/node 地市(单个) --> 节点(单个)
             * ： 维度1：地市名称， 维度2：节点名称
             */
            mdMenuOne = menuService.getMdMenuByUrl(StringUtil.removeLastSplit("/", url));
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_AREA);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenuOne.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_AREA);
            monitorTargetMap.put("monitorTargetThreeType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetThreeDimensionType", Constant.DYNAMICTYPE_NODE);
            monitorTargetMap.put("monitorTargetThreeMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetThreeName", Constant.SINGLE_NODE);
            break;
        case Constant.DIMENSIONTYPE_BRAS_SUMMARY:
            /** 维度类型22--> bras/summary * bras/summary bras总览 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SUMMARY);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_BRAS);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", mdMenu.getShow_name());
            break;
        case Constant.DIMENSIONTYPE_BRAS_ALL:
            /** 维度类型23--> bras(all) bras ： 维度1：空， 维度2：空 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_BRAS);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.ALL_BRAS);
            break;
        case Constant.DIMENSIONTYPE_BRAS_SINGLE:
            /** 维度类型24--> bras(single) bras ： 维度1：brasip， 维度2：空 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_BRAS);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_BRAS);
            break;
        case Constant.DIMENSIONTYPE_APN_SUMMARY:
            /** 维度类型25--> apn/summary * apn/summary apn总览 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SUMMARY);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_APN);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", mdMenu.getShow_name());
            break;
        case Constant.DIMENSIONTYPE_APN_ALL:
            /** 维度类型26--> apn(all) apn ： 维度1：空， 维度2：空 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_ALL);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_APN);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.ALL_APN);
            break;
        case Constant.DIMENSIONTYPE_APN_SINGLE:
            /** 维度类型27--> apn(single) apn ： 维度1：apn名称， 维度2：空 */
            monitorTargetMap.put("monitorTargetOneType", Constant.MONITORTARGET_SINGLE);
            monitorTargetMap.put("monitorTargetOneDimensionType", Constant.DYNAMICTYPE_APN);
            monitorTargetMap.put("monitorTargetOneMdMenuName", mdMenu.getName());
            monitorTargetMap.put("monitorTargetOneName", Constant.SINGLE_APN);
            break;
        }
        return monitorTargetMap;
    }

    /**
     * 
     * @Title: dimensionModify @Description: (维度更新告警规则) @param @param map
     *         维度类型：operationType { Constant.DYNAMICTYPE_NODE
     *         Constant.DYNAMICTYPE_AREA Constant.DYNAMICTYPE_HOST
     *         Constant.DYNAMICTYPE_BRAS Constant.DYNAMICTYPE_METRIC }
     * 
     *         操作类型： dynamicType { Constant.OPERATIONTYPE_ADD
     *         Constant.OPERATIONTYPE_MODIFY Constant.OPERATIONTYPE_DELETE }
     * 
     *         操作ID： operationId { }
     * 
     *         原先的维度ID： preDimensionId { }
     * 
     *         现在的维度ID： curDimensionId { }
     * 
     * @param @return
     *            参数 @return String 返回类型 @throws
     */
    public String dimensionModify(Map<String, Object> map) {
        String result = "success";
        if (map.get("operationType") != null) {
            String operationType = map.get("operationType").toString();
            switch (operationType) {
            case Constant.DYNAMICTYPE_NODE:
                result = dimensionModifyNode(map);
                break;
            case Constant.DYNAMICTYPE_AREA:
                result = dimensionModifyArea(map);
                break;
            case Constant.DYNAMICTYPE_HOST:
                result = dimensionModifyHost(map);
                break;
            case Constant.DYNAMICTYPE_BRAS:
                result = dimensionModifyBras(map);
                break;
            case Constant.DYNAMICTYPE_APN:
                result = dimensionModifyApn(map);
                break;
            case Constant.DYNAMICTYPE_METRIC:
                result = dimensionModifyMetric(map);
                break;
            case Constant.DYNAMICTYPE_BUSINESSHOST:
                result = dimensionModifyBusinesshost(map);
                break;
            }
        } else {
            result = "fail";
        }
        return result;
    }

    /**
     * 
     * @Title: operationAddByDimensionType @Description:
     *         (根据维度类型添加告警规则) @param @param dimension_type 参数 @return void
     *         返回类型 @throws
     */
    public void operationAddByDimensionType(Integer dimension_type) {
        MdAlarmRule alarmRule = new MdAlarmRule();
        alarmRule.setDimension_type(dimension_type);
        List<MdAlarmRule> mdAlarmRuleList = mdAlarmRuleDAO.getMdAlarmRule(alarmRule);
        refreshAddMdAlarmRule(mdAlarmRuleList);
    }

    /**
     * 
     * @Title: operationModifyByDimensionTypeByAllAll @Description:
     *         (修改多个多个维度) @param @param dimension_type @param @param map
     *         参数 @return void 返回类型 @throws
     */
    @Transactional
    public void operationModifyByDimensionTypeByAllAll(Integer dimension_type,
            Map<String, Object> map) {
        if (map.get("operationId") != null && map.get("preDimensionId") != null
                && map.get("curDimensionId") != null) {
            String operationId = map.get("operationId").toString();
            String preDimensionId = map.get("preDimensionId").toString();
            String curDimensionId = map.get("curDimensionId").toString();
            MdAlarmRule alarmRule = new MdAlarmRule();
            List<MdAlarmRule> mdAlarmRuleList = new ArrayList<MdAlarmRule>();
            alarmRule.setDimension_type(dimension_type);
            mdAlarmRuleList = mdAlarmRuleDAO.getMdAlarmRule(alarmRule);
            for (MdAlarmRule mar : mdAlarmRuleList) {
                String url = mar.getUrl();
                String preurl = url + ConstantUtill.URL_SPLIT + "/" + preDimensionId + "/"
                        + operationId;
                String cururl = url + ConstantUtill.URL_SPLIT + "/" + curDimensionId + "/"
                        + operationId;
                List<MdAlarmRuleDetail> mdAlarmRuleDetailList = mdAlarmRuleDetailDAO
                        .getMdAlarmRuleDetailByUrlAndRuleId(preurl, mar.getRule_id());
                for (MdAlarmRuleDetail mdAlarmRuleDetail : mdAlarmRuleDetailList) {
                    mdAlarmRuleDetail.setUrl(cururl);
                    mdAlarmRuleDetail.setDimension1(curDimensionId);
                    mdAlarmRuleDetailDAO.updateByAlarmId(mdAlarmRuleDetail);
                }
            }
        }
    }

    /**
     * 
     * @Title: operationModifyByDimensionTypeBySingleAll @Description:
     *         (修改单个多个维度) @param @param dimension_type @param @param map
     *         参数 @return void 返回类型 @throws
     */
    @Transactional
    public void operationModifyByDimensionTypeBySingleAll(Integer dimension_type,
            Map<String, Object> map) {
        if (map.get("operationId") != null && map.get("preDimensionId") != null
                && map.get("curDimensionId") != null) {
            String operationId = map.get("operationId").toString();
            String preDimensionId = map.get("preDimensionId").toString();
            MdAlarmRule alarmRule = new MdAlarmRule();
            List<MdAlarmRule> mdAlarmRuleList = new ArrayList<MdAlarmRule>();
            alarmRule.setDimension_type(dimension_type);
            alarmRule.setDimension1(preDimensionId);
            mdAlarmRuleList = mdAlarmRuleDAO.getMdAlarmRule(alarmRule);
            for (MdAlarmRule mar : mdAlarmRuleList) {
                String url = mar.getUrl();
                String businesslinkurl = url.split(ConstantUtill.URL_SPLIT)[0];
                String preurl = businesslinkurl + ConstantUtill.URL_SPLIT + "/" + preDimensionId
                        + "/" + operationId;
                List<MdAlarmRuleDetail> preMdAlarmRuleDetailList = mdAlarmRuleDetailDAO
                        .getMdAlarmRuleDetailByUrlAndRuleId(preurl, mar.getRule_id());
                for (MdAlarmRuleDetail mdAlarmRuleDetail : preMdAlarmRuleDetailList) {
                    mdAlarmRuleDetailDAO.deleteByAlarmId(mdAlarmRuleDetail.getAlarm_id());
                }
            }
        }
        operationAddByDimensionType(dimension_type);
    }

    /**
     * 
     * @Title: operationModifyByDimensionTypeBySingleSingle @Description:
     *         (修改单个单个维度) @param @param dimension_type @param @param map
     *         参数 @return void 返回类型 @throws
     */
    @Transactional
    public void operationModifyByDimensionTypeBySingleSingle(Integer dimension_type,
            Map<String, Object> map) {
        if (map.get("operationId") != null && map.get("preDimensionId") != null
                && map.get("curDimensionId") != null) {
            String operationId = map.get("operationId").toString();
            String preDimensionId = map.get("preDimensionId").toString();
            String curDimensionId = map.get("curDimensionId").toString();
            MdAlarmRule alarmRule = new MdAlarmRule();
            List<MdAlarmRule> mdAlarmRuleList = new ArrayList<MdAlarmRule>();
            alarmRule.setDimension_type(dimension_type);
            alarmRule.setDimension1(preDimensionId);
            alarmRule.setDimension2(operationId);
            mdAlarmRuleList = mdAlarmRuleDAO.getMdAlarmRule(alarmRule);
            for (MdAlarmRule mar : mdAlarmRuleList) {
                String url = mar.getUrl();
                String businesslinkurl = url.split(ConstantUtill.URL_SPLIT)[0];
                String preurl = businesslinkurl + ConstantUtill.URL_SPLIT + "/" + preDimensionId
                        + "/" + operationId;
                String cururl = businesslinkurl + ConstantUtill.URL_SPLIT + "/" + curDimensionId
                        + "/" + operationId;
                List<MdAlarmRuleDetail> mdAlarmRuleDetailList = mdAlarmRuleDetailDAO
                        .getMdAlarmRuleDetailByUrlAndRuleId(preurl, mar.getRule_id());
                for (MdAlarmRuleDetail mdAlarmRuleDetail : mdAlarmRuleDetailList) {
                    mdAlarmRuleDetail.setUrl(cururl);
                    mdAlarmRuleDetail.setDimension1(curDimensionId);
                    mdAlarmRuleDetailDAO.updateByAlarmId(mdAlarmRuleDetail);
                }
                mar.setUrl(cururl);
                mar.setDimension1(curDimensionId);
                mdAlarmRuleDAO.update(mar);
            }
        }
    }

    /**
     * 
     * @Title: operationDelete @Description: (删除维度) @param @param operationId
     *         参数 @return void 返回类型 @throws
     */
    @Transactional
    public void operationDeleteByDimension(String operationId) {
        mdAlarmRuleDAO.deleteByDimension(operationId);
        mdAlarmRuleDetailDAO.deleteByDimension(operationId);
    }

    /**
     * 
     * @Title: dimensionModifyNode @Description: (节点维度更新告警规则) @param @param
     *         map @param @return 参数 @return String 返回类型 @throws
     */
    public String dimensionModifyNode(Map<String, Object> map) {
        String result = "success";
        if (map.get("dynamicType") != null) {
            String dynamicType = map.get("dynamicType").toString();
            switch (dynamicType) {
            case Constant.OPERATIONTYPE_ADD:
                operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_ALL_AREA_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_AREA_ALL_NODE_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_AREA_SINGLE_NODE_ALL);
                break;
            case Constant.OPERATIONTYPE_DELETE:
                if (map.get("operationId") != null) {
                    String operationId = map.get("operationId").toString();
                    operationDeleteByDimension(operationId);
                } else {
                    result = "fail";
                }
                break;
            }
        } else {
            result = "fail";
        }
        return result;
    }

    /**
     * 
     * @Title: dimensionModifyArea @Description: (属地维度更新告警规则) @param @param
     *         map @param @return 参数 @return String 返回类型 @throws
     */
    public String dimensionModifyArea(Map<String, Object> map) {
        String result = "success";
        if (map.get("dynamicType") != null) {
            String dynamicType = map.get("dynamicType").toString();
            switch (dynamicType) {
            case Constant.OPERATIONTYPE_ADD:
                operationAddByDimensionType(Constant.DIMENSIONTYPE_AREA_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_ALL_AREA_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_SINGLE_AREA_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_AREA_ALL_BRAS_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_AREA_ALL_NODE_ALL);
                break;
            case Constant.OPERATIONTYPE_DELETE:
                if (map.get("operationId") != null) {
                    String operationId = map.get("operationId").toString();
                    operationDeleteByDimension(operationId);
                } else {
                    result = "fail";
                }
                break;
            }
        } else {
            result = "fail";
        }
        return result;
    }

    /**
     * 
     * @Title: dimensionModifyHost @Description: (主机维度更新告警规则) @param @param
     *         map @param @return 参数 @return String 返回类型 @throws
     */
    public String dimensionModifyHost(Map<String, Object> map) {
        String result = "success";
        if (map.get("dynamicType") != null) {
            String dynamicType = map.get("dynamicType").toString();
            switch (dynamicType) {
            case Constant.OPERATIONTYPE_ADD:
                operationAddByDimensionType(Constant.DIMENSIONTYPE_HOST_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL);
                break;
            case Constant.OPERATIONTYPE_MODIFY:
                if (map.get("operationId") != null && map.get("preDimensionId") != null
                        && map.get("curDimensionId") != null) {
                    operationModifyByDimensionTypeByAllAll(Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL,
                            map);
                    operationModifyByDimensionTypeBySingleAll(
                            Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL, map);
                    operationModifyByDimensionTypeBySingleSingle(
                            Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE, map);
                } else {
                    result = "fail";
                }
                break;
            case Constant.OPERATIONTYPE_DELETE:
                if (map.get("operationId") != null) {
                    String operationId = map.get("operationId").toString();
                    operationDeleteByDimension(operationId);
                } else {
                    result = "fail";
                }
                break;
            }
        } else {
            result = "fail";
        }
        return result;
    }

    /**
     * 
     * @Title: dimensionModifyBras @Description: (Bras维度更新告警规则) @param @param
     *         map @param @return 参数 @return String 返回类型 @throws
     */
    public String dimensionModifyBras(Map<String, Object> map) {
        String result = "success";
        if (map.get("dynamicType") != null) {
            String dynamicType = map.get("dynamicType").toString();
            switch (dynamicType) {
            case Constant.OPERATIONTYPE_ADD:
                operationAddByDimensionType(Constant.DIMENSIONTYPE_AREA_ALL_BRAS_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_BRAS_ALL);
                break;
            case Constant.OPERATIONTYPE_MODIFY:
                if (map.get("operationId") != null && map.get("preDimensionId") != null
                        && map.get("curDimensionId") != null) {
                    operationModifyByDimensionTypeByAllAll(Constant.DIMENSIONTYPE_AREA_ALL_BRAS_ALL,
                            map);
                    operationModifyByDimensionTypeBySingleAll(
                            Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL, map);
                    operationModifyByDimensionTypeBySingleSingle(
                            Constant.DIMENSIONTYPE_AREA_SINGLE_BRAS_SINGLE, map);
                } else {
                    result = "fail";
                }
                break;
            case Constant.OPERATIONTYPE_DELETE:
                if (map.get("operationId") != null) {
                    String operationId = map.get("operationId").toString();
                    operationDeleteByDimension(operationId);
                } else {
                    result = "fail";
                }
                break;
            }
        } else {
            result = "fail";
        }
        return result;
    }

    /**
     * 
     * @Title: dimensionModifyApn @Description: (Apn维度更新告警规则) @param @param
     *         map @param @return 参数 @return String 返回类型 @throws
     */
    public String dimensionModifyApn(Map<String, Object> map) {
        String result = "success";
        if (map.get("dynamicType") != null) {
            String dynamicType = map.get("dynamicType").toString();
            switch (dynamicType) {
            case Constant.OPERATIONTYPE_ADD:
                operationAddByDimensionType(Constant.DIMENSIONTYPE_APN_ALL);
                break;
            case Constant.OPERATIONTYPE_DELETE:
                if (map.get("operationId") != null) {
                    String operationId = map.get("operationId").toString();
                    operationDeleteByDimension(operationId);
                } else {
                    result = "fail";
                }
                break;
            }
        } else {
            result = "fail";
        }
        return result;
    }

    /**
     * 
     * @Title: dimensionModifyMetric @Description: (指标维度更新告警规则) @param @param
     *         map @param @return 参数 @return String 返回类型 @throws
     */
    @Transactional
    public String dimensionModifyMetric(Map<String, Object> map) {
        String result = "success";
        if (map.get("dynamicType") != null && map.get("operationId") != null) {
            String dynamicType = map.get("dynamicType").toString();
            String operationId = map.get("operationId").toString();
            switch (dynamicType) {
            case Constant.OPERATIONTYPE_DELETE:
                mdAlarmRuleDAO.deleteByMetricId(operationId);
                mdAlarmRuleDetailDAO.deleteByMetricId(operationId);
                break;
            }
        } else {
            result = "fail";
        }
        return result;
    }

    /**
     * 
     * @Title: dimensionModifyBusinesshost @Description:
     *         (业务关联主机维度更新告警规则) @param @param map @param @return 参数 @return
     *         String 返回类型 @throws
     */
    public String dimensionModifyBusinesshost(Map<String, Object> map) {
        String result = "success";
        if (map.get("dynamicType") != null) {
            String dynamicType = map.get("dynamicType").toString();
            switch (dynamicType) {
            case Constant.OPERATIONTYPE_ADD:
                operationAddByDimensionType(Constant.DIMENSIONTYPE_HOST_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL);
                operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE);
                break;
            case Constant.OPERATIONTYPE_MODIFY:
                if (map.get("operationId") != null && map.get("preDimensionId") != null) {
                    operationDeleteBusinesshost(map);
                    operationAddByDimensionType(Constant.DIMENSIONTYPE_HOST_ALL);
                    operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL);
                    operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL);
                    operationAddByDimensionType(Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE);
                } else {
                    result = "fail";
                }
                break;
            case Constant.OPERATIONTYPE_DELETE:
                if (map.get("operationId") != null && map.get("preDimensionId") != null) {
                    operationDeleteBusinesshost(map);
                } else {
                    result = "fail";
                }
                break;
            }
        } else {
            result = "fail";
        }
        return result;
    }

    /**
     * 
     * @Title: operationDeleteBusinesshost @Description:
     *         (删除业务关联主机维度) @param @param map 参数 @return void 返回类型 @throws
     */
    @Transactional
    public void operationDeleteBusinesshost(Map<String, Object> map) {
        if (map.get("operationId") != null && map.get("preDimensionId") != null) {
            String operationId = map.get("operationId").toString();
            String preDimensionId = map.get("preDimensionId").toString();
            String name = "";
            List<MdMenu> mdMenuList = menuService.getMdMenuHostList();
            for (MdMenu mdMenu : mdMenuList) {
                if (preDimensionId.equals(mdMenu.getName())) {
                    MdMenu twoLevelMdMenu = menuService.getTwoLevelMdMenu(mdMenu.getUrl());
                    name = twoLevelMdMenu.getName();
                }
            }
            MdAlarmRule alarmRule = new MdAlarmRule();
            List<MdAlarmRule> mdAlarmRuleList = new ArrayList<MdAlarmRule>();
            alarmRule.setDimension_type(Constant.DIMENSIONTYPE_HOST_ALL);
            mdAlarmRuleList = mdAlarmRuleDAO.getMdAlarmRule(alarmRule);
            for (MdAlarmRule mdAlarmRule : mdAlarmRuleList) {
                if (name.equals(mdAlarmRule.getName())) {
                    String rule_id = mdAlarmRule.getRule_id();
                    List<MdAlarmRuleDetail> mdAlarmRuleDetailList = mdAlarmRuleDetailDAO
                            .getMdAlarmRuleDetailByRuleId(rule_id);
                    for (MdAlarmRuleDetail mdAlarmRuleDetail : mdAlarmRuleDetailList) {
                        if (operationId.equals(mdAlarmRuleDetail.getDimension1())) {
                            mdAlarmRuleDetailDAO.deleteByAlarmId(mdAlarmRuleDetail.getAlarm_id());
                        }
                    }
                }
            }
            alarmRule.setDimension_type(Constant.DIMENSIONTYPE_NODE_ALL_HOST_ALL);
            mdAlarmRuleList = mdAlarmRuleDAO.getMdAlarmRule(alarmRule);
            for (MdAlarmRule mdAlarmRule : mdAlarmRuleList) {
                if (name.equals(mdAlarmRule.getName())) {
                    String rule_id = mdAlarmRule.getRule_id();
                    List<MdAlarmRuleDetail> mdAlarmRuleDetailList = mdAlarmRuleDetailDAO
                            .getMdAlarmRuleDetailByRuleId(rule_id);
                    for (MdAlarmRuleDetail mdAlarmRuleDetail : mdAlarmRuleDetailList) {
                        if (operationId.equals(mdAlarmRuleDetail.getDimension2())) {
                            mdAlarmRuleDetailDAO.deleteByAlarmId(mdAlarmRuleDetail.getAlarm_id());
                        }
                    }
                }
            }
            alarmRule.setDimension_type(Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_ALL);
            mdAlarmRuleList = mdAlarmRuleDAO.getMdAlarmRule(alarmRule);
            for (MdAlarmRule mdAlarmRule : mdAlarmRuleList) {
                if (name.equals(mdAlarmRule.getName())) {
                    String rule_id = mdAlarmRule.getRule_id();
                    List<MdAlarmRuleDetail> mdAlarmRuleDetailList = mdAlarmRuleDetailDAO
                            .getMdAlarmRuleDetailByRuleId(rule_id);
                    for (MdAlarmRuleDetail mdAlarmRuleDetail : mdAlarmRuleDetailList) {
                        if (operationId.equals(mdAlarmRuleDetail.getDimension2())) {
                            mdAlarmRuleDetailDAO.deleteByAlarmId(mdAlarmRuleDetail.getAlarm_id());
                        }
                    }
                }
            }
            alarmRule.setDimension_type(Constant.DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE);
            mdAlarmRuleList = mdAlarmRuleDAO.getMdAlarmRule(alarmRule);
            for (MdAlarmRule mdAlarmRule : mdAlarmRuleList) {
                if (name.equals(mdAlarmRule.getName())) {
                    String rule_id = mdAlarmRule.getRule_id();
                    List<MdAlarmRuleDetail> mdAlarmRuleDetailList = mdAlarmRuleDetailDAO
                            .getMdAlarmRuleDetailByRuleId(rule_id);
                    for (MdAlarmRuleDetail mdAlarmRuleDetail : mdAlarmRuleDetailList) {
                        if (operationId.equals(mdAlarmRuleDetail.getDimension2())) {
                            mdAlarmRuleDetailDAO.deleteByAlarmId(mdAlarmRuleDetail.getAlarm_id());
                            mdAlarmRuleDAO.delete(rule_id);
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * @Title: updateApnAlarmRuleDetail @Description:
     *         (更新APN每日限流阀值告警规则明细) @param @param apnId @param @param
     *         dayValue @param @return 参数 @return int 返回类型 @throws
     */
    public int updateApnAlarmRuleDetail(String apnId, String dayValue) {
        MdAlarmRuleDetail mdAlarmRuleDetail = new MdAlarmRuleDetail();
        String apnLimitThresholdAlarmRule = "newest<0";
        if (!"0".equals(dayValue)) {
            apnLimitThresholdAlarmRule = CommonInit.BUSCONF.getApnLimitThresholdAlarmRule()
                    .replace("#{dayvalue}", dayValue);
        }
        mdAlarmRuleDetail.setAlarm_rule(apnLimitThresholdAlarmRule);
        mdAlarmRuleDetail.setMetric_id(CommonInit.BUSCONF.getApnLimitThresholdMetricId());
        mdAlarmRuleDetail.setUrl(
                CommonInit.BUSCONF.getApnLimitThresholdAlarmRuleUrl().replace("#{apn_id}", apnId));
        return mdAlarmRuleDetailDAO.updateApnAlarmRuleDetail(mdAlarmRuleDetail);
    }
}
