package com.asiainfo.lcims.omc.param.ais;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.ais.AisGroupMetricModel;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.persistence.alarm.MdAlarmInfoDao;
import com.asiainfo.lcims.omc.persistence.configmanage.AreaDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.BdNasDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdNodeDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MonHostDAO;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.util.ToolsUtils;

@Service(value = "aisinit")
public class AisInit {
    @Inject
    private MonHostDAO monHostDAO;

    @Inject
    private AreaDAO areaDAO;

    @Inject
    private BdNasDAO bdNasDAO;

    @Inject
    private MdNodeDAO nodeDAO;

    @Inject
    private MdAlarmInfoDao alarminfoDAO;

    private static List<MdArea> mdAreaList = null;
    private static List<BdNas> bdNasList = null;
    private static List<MonHost> monHostList = null;
    private static List<MdNode> nodeList = null;
    private static List<MdAlarmInfo> alarmInfos = null;

    public static List<MdArea> getMdAreaList() {
        return mdAreaList;
    }

    private static void setMdAreaList(List<MdArea> mdAreaList) {
        AisInit.mdAreaList = mdAreaList;
    }

    public static List<BdNas> getBdNasList() {
        return bdNasList;
    }

    private static void setBdNasList(List<BdNas> bdNasList) {
        AisInit.bdNasList = bdNasList;
    }

    public static List<MonHost> getMonHostList() {
        return monHostList;
    }

    private static void setMonHostList(List<MonHost> monHostList) {
        AisInit.monHostList = monHostList;
    }

    public static List<MdNode> getNodeList() {
        return nodeList;
    }

    private static void setNodeList(List<MdNode> nodeList) {
        AisInit.nodeList = nodeList;
    }

    public static List<MdAlarmInfo> getAlarmInfos() {
        return alarmInfos;
    }

    private static void setAlarmInfos(List<MdAlarmInfo> alarmInfos) {
        AisInit.alarmInfos = alarmInfos;
    }

    public void init() {

        // monHostList = monHostDAO.getAllHost();
        // bdNasList = bdNasDAO.getAll();
        // mdAreaList = areaDAO.getAll();
        // nodeList = nodeDAO.getMdNodeList();
        // alarmInfos = alarminfoDAO.getAlarmInfosforAis();
        setMdAreaList(areaDAO.getAll());
        setBdNasList(bdNasDAO.getAll());
        setMonHostList(monHostDAO.getAllHost());
        setNodeList(nodeDAO.getMdNodeList());
        setAlarmInfos(alarminfoDAO.getAlarmInfosforAis());
    }

    public static String getAlarmMsg(AisGroupMetricModel groupMetric) {
        String msg = null;
        if (ToolsUtils.ListIsNull(alarmInfos)) {
            return msg;
        }
        if (!ToolsUtils.StringIsNull(groupMetric.getAttr())) {
            for (MdAlarmInfo mdAlarmInfo : alarmInfos) {
                if (groupMetric.getUrl().equals(mdAlarmInfo.getUrl())
                        && groupMetric.getMetric_id().equals(mdAlarmInfo.getMetric_id())
                        && groupMetric.getAttr().equals(mdAlarmInfo.getAttr())) {
                    msg = mdAlarmInfo.getAlarmmsg();
                    groupMetric.setAlarm_rule(mdAlarmInfo.getAlarm_rule());
                    groupMetric.setAlarm_mvalue(mdAlarmInfo.getAlarm_mvalue());
                    return msg;
                }
            }
            return msg;
        }

        for (MdAlarmInfo mdAlarmInfo : alarmInfos) {
            if (groupMetric.getUrl().equals(mdAlarmInfo.getUrl())
                    && groupMetric.getMetric_id().equals(mdAlarmInfo.getMetric_id())) {
                msg = mdAlarmInfo.getAlarmmsg();
                groupMetric.setAlarm_rule(mdAlarmInfo.getAlarm_rule());
                groupMetric.setAlarm_mvalue(mdAlarmInfo.getAlarm_mvalue());
                return msg;
            }
        }
        return msg;
    }

    public static MdNode getNodeById(String nodeId) {
        if (ToolsUtils.ListIsNull(nodeList)) {
            return null;
        }
        for (MdNode node : nodeList) {
            if (nodeId.equals(node.getId()))
                return node;
        }
        return null;
    }

    public static BdNas getBdNasById(String nasId) {
        if (ToolsUtils.ListIsNull(bdNasList)) {
            return null;
        }
        for (BdNas nas : bdNasList) {
            if (nasId.equals(nas.getId()))
                return nas;
        }
        return null;
    }

    public static MonHost getHostByHostId(String hostId) {
        if (ToolsUtils.ListIsNull(monHostList)) {
            return null;
        }
        for (MonHost host : monHostList) {
            if (hostId.equals(host.getHostid()))
                return host;
        }
        return null;
    }

    public static List<MonHost> getHostByHostType(String hosttype) {
        List<MonHost> monHostTypeList = new ArrayList<MonHost>();
        if (!ToolsUtils.ListIsNull(monHostList)) {
            for (MonHost host : monHostList) {
                if (hosttype.equals(host.getHosttype()))
                    monHostTypeList.add(host);
            }
        }
        return monHostTypeList;
    }

    public static MdArea getAreaByAreano(String areano) {
        if (ToolsUtils.ListIsNull(mdAreaList)) {
            return null;
        }
        for (MdArea area : mdAreaList) {
            if (areano.equals(area.getAreano()))
                return area;
        }
        return null;
    }

}
