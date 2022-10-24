package com.asiainfo.lcims.omc.persistence.configmanage.impl;

import com.asiainfo.lcims.omc.persistence.po.MonHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MdCollectManageDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(MonHostDAOImpl.class);

    public String updateMonHost(Map<String, Object> paras) {
        MonHost monHost = (MonHost) paras.get("host");
        StringBuffer buffer = new StringBuffer("UPDATE MON_HOST SET HOSTID=HOSTID");
        if (null != monHost.getAddr()) {
            buffer.append(",ADDR='").append(monHost.getAddr()).append("' ");
        }
        if (null != monHost.getHostname()) {
            buffer.append(",HOSTNAME='").append(monHost.getHostname()).append("' ");
        }
        if (null != monHost.getNodeid()) {
            buffer.append(",NODEID='").append(monHost.getNodeid()).append("' ");
        }
        if (null != monHost.getOs()) {
            buffer.append(",OS='").append(monHost.getOs()).append("' ");
        }
//        if (null != monHost.getProductname()) {
//            buffer.append(",PRODUCTNAME='").append(monHost.getProductname()).append("' ");
//        }
        if (null != monHost.getCpu()) {
            buffer.append(",CPU='").append(monHost.getCpu()).append("' ");
        }
//        if (null != monHost.getMemory()) {
//            buffer.append(",MEMORY='").append(monHost.getMemory()).append("' ");
//        }
//        if (null != monHost.getDiskspace()) {
//            buffer.append(",DISKSPACE='").append(monHost.getDiskspace()).append("' ");
//        }
//        if (null != monHost.getHost_room()) {
//            buffer.append(",HOST_ROOM='").append(monHost.getHost_room()).append("' ");
//        }
//        if (null != monHost.getRack_num()) {
//            buffer.append(",RACK_NUM='").append(monHost.getRack_num()).append("' ");
//        }
//        if (null != monHost.getSerialnumber()) {
//            buffer.append(",SERIALNUMBER='").append(monHost.getSerialnumber()).append("' ");
//        }
        if (null != monHost.getIpv4()) {
            buffer.append(",IPV4='").append(monHost.getIpv4()).append("' ");
        }
        if (null != monHost.getIpv6()) {
            buffer.append(",IPV6='").append(monHost.getIpv6()).append("' ");
        }
//        if (null != monHost.getLocation()) {
//            buffer.append(",LOCATION='").append(monHost.getLocation()).append("' ");
//        }
        if (null != monHost.getIs_collect()) {
            buffer.append(",IS_COLLECT='").append(monHost.getIs_collect()).append("' ");
        }
        buffer.append("WHERE HOSTID='").append(monHost.getHostid()).append("'");
        LOG.debug("updateMonHost sql = {}", buffer.toString());
        return buffer.toString();
    }

    // 新增采集机执行的语句
    public String insertMonHost(Map<String, Object> paras) {
        MonHost host = (MonHost) paras.get("host");
        StringBuffer buffer = new StringBuffer(
                "INSERT INTO MON_HOST(HOSTID,ADDR,HOSTNAME,NODEID,STATUS,OS,HOSTTYPE,IPV4,IPV6,IS_COLLECT) VALUES('");
        buffer.append(host.getHostid()).append("','");
        buffer.append(host.getAddr()).append("','");
        buffer.append(host.getHostname()).append("','");
        buffer.append(host.getNodeid()).append("',").append(host.getStatus()).append(",'")
                .append(host.getOs()).append("','")
                .append(15).append("','")
                .append(host.getIpv4()).append("','")
                .append(host.getIpv6()).append("','")
                .append(host.getIs_collect()).append("')");
        LOG.debug("insertMonHost sql = {}", buffer.toString());
        return buffer.toString();
    }
}
