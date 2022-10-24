package com.asiainfo.lcims.omc.persistence.gscm5G;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.MdHostMetric;
import com.asiainfo.lcims.omc.persistence.gscm5G.impl.CollectMetricDAOImpl;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.util.page.Page;

public interface CollectMetricDAO {
    @SelectProvider(method = "getMdFirewallListByHost", type = CollectMetricDAOImpl.class)
    List<MdHostMetric> getMdFirewallListByHost(@Param("host") MonHost host);

    @SelectProvider(method = "getMdThirdpartyListByHost", type = CollectMetricDAOImpl.class)
    List<MdHostMetric> getMdThirdpartyListByHost(@Param("host") MonHost host);

    @SelectProvider(method = "getMdProtocolListByHost", type = CollectMetricDAOImpl.class)
    List<MdHostMetric> getMdProtocolListByHost(@Param("host") MonHost host);

    @SelectProvider(method = "insertHostFirewall", type = CollectMetricDAOImpl.class)
    void insertHostFirewall(@Param("hostMetric") MdHostMetric mdHostMetric);

    @SelectProvider(method = "insertHostThirdparty", type = CollectMetricDAOImpl.class)
    void insertHostThirdparty(@Param("hostMetric") MdHostMetric mdHostMetric);

    @SelectProvider(method = "insertHostProtocol", type = CollectMetricDAOImpl.class)
    void insertHostProtocol(@Param("hostMetric") MdHostMetric mdHostMetric);

    @SelectProvider(method = "getHostFirewallInfoCount", type = CollectMetricDAOImpl.class)
    int getHostFirewallInfoCount(@Param("hostid") String hostid);

    @SelectProvider(method = "getHostFirewallInfo", type = CollectMetricDAOImpl.class)
    List<MdHostMetric> getHostFirewallInfo(@Param("hostid") String hostid,
            @Param("page") Page page);

    @SelectProvider(method = "getHostThirdpartyInfoCount", type = CollectMetricDAOImpl.class)
    int getHostThirdpartyInfoCount(@Param("hostid") String hostid);

    @SelectProvider(method = "getHostThirdpartyInfo", type = CollectMetricDAOImpl.class)
    List<MdHostMetric> getHostThirdpartyInfo(@Param("hostid") String hostid,
            @Param("page") Page page);

    @SelectProvider(method = "getHostProtocolInfoCount", type = CollectMetricDAOImpl.class)
    int getHostProtocolInfoCount(@Param("hostid") String hostid);

    @SelectProvider(method = "getHostProtocolInfo", type = CollectMetricDAOImpl.class)
    List<MdHostMetric> getHostProtocolInfo(@Param("hostid") String hostid,
            @Param("page") Page page);

    @Select("SELECT HM.ID,HM.HOST_ID,H.HOSTNAME,H.ADDR,HM.FIREWALL_ID AS METRIC_ID,"
            + "M.FIREWALL_NAME AS METRIC_NAME,HM.CYCLE_ID,C.CYCLENAME,C.CRON,"
            + "C.DESCRIPTION AS CYCLE_DESCRIPTION,HM.SCRIPT,"
            + "HM.SCRIPT_PARAM,HM.SCRIPT_RETURN_TYPE,P1.DESCRIPTION AS RETURNTYPENAME,"
            + "HM.STATE,P2.DESCRIPTION AS statename" + " FROM MD_HOST_FIREWALL HM"
            + " LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID LEFT JOIN MD_FIREWALL M ON HM.FIREWALL_ID = M.ID"
            + " LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID"
            + " LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7"
            + " LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8" + " WHERE HM.ID=#{id}")
    MdHostMetric queryFirewallSingleinfo(@Param("id") String id);

    @Select("SELECT HM.ID,HM.HOST_ID,H.HOSTNAME,H.ADDR,HM.THIRDPARTY_ID AS METRIC_ID,"
            + "M.FIREWALL_NAME AS METRIC_NAME,HM.CYCLE_ID,C.CYCLENAME,C.CRON,"
            + "C.DESCRIPTION AS CYCLE_DESCRIPTION,HM.SCRIPT,"
            + "HM.SCRIPT_PARAM,HM.SCRIPT_RETURN_TYPE,P1.DESCRIPTION AS RETURNTYPENAME,"
            + "HM.STATE,P2.DESCRIPTION AS statename" + " FROM MD_HOST_THIRDPARTY HM"
            + " LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID LEFT JOIN MD_FIREWALL M ON HM.THIRDPARTY_ID = M.ID"
            + " LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID"
            + " LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7"
            + " LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8" + " WHERE HM.ID=#{id}")
    MdHostMetric queryThirdpartySingleinfo(@Param("id") String id);

    @Select("SELECT HM.ID,HM.HOST_ID,H.HOSTNAME,H.ADDR,HM.PROTOCOL_ID AS METRIC_ID,"
            + "M.PROTOCOL_NAME AS METRIC_NAME,HM.CYCLE_ID,C.CYCLENAME,C.CRON,"
            + "C.DESCRIPTION AS CYCLE_DESCRIPTION,HM.SCRIPT,"
            + "HM.SCRIPT_PARAM,HM.SCRIPT_RETURN_TYPE,P1.DESCRIPTION AS RETURNTYPENAME,"
            + "HM.STATE,P2.DESCRIPTION AS statename" + " FROM MD_HOST_PROTOCOL HM"
            + " LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID LEFT JOIN MD_COLLECT_PROTOCOL M ON HM.PROTOCOL_ID = M.ID"
            + " LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID"
            + " LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7"
            + " LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8" + " WHERE HM.ID=#{id}")
    MdHostMetric queryProtocolSingleinfo(@Param("id") String id);

    @SelectProvider(method = "getHostFirewallInfoByIds", type = CollectMetricDAOImpl.class)
    List<MdHostMetric> getHostFirewallInfoByIds(
            @Param("hostMetricidArray") String[] hostMetricidArray);

    @SelectProvider(method = "getHostThirdpartyInfoByIds", type = CollectMetricDAOImpl.class)
    List<MdHostMetric> getHostThirdpartyInfoByIds(
            @Param("hostMetricidArray") String[] hostMetricidArray);

    @SelectProvider(method = "getHostProtocolInfoByIds", type = CollectMetricDAOImpl.class)
    List<MdHostMetric> getHostProtocolInfoByIds(
            @Param("hostMetricidArray") String[] hostMetricidArray);

    @Delete("DELETE FROM MD_HOST_FIREWALL WHERE ID = #{id}")
    void deleteHostFirewall(@Param("id") String id);

    @Delete("DELETE FROM MD_HOST_THIRDPARTY WHERE ID = #{id}")
    void deleteHostThirdparty(@Param("id") String id);

    @Delete("DELETE FROM MD_HOST_PROTOCOL WHERE ID = #{id}")
    void deleteHostProtocol(@Param("id") String id);

    @UpdateProvider(method = "modifyHostFirewall", type = CollectMetricDAOImpl.class)
    Integer modifyHostFirewall(@Param("hostMetric") MdHostMetric hostMetric);

    @UpdateProvider(method = "modifyHostThirdparty", type = CollectMetricDAOImpl.class)
    Integer modifyHostThirdparty(@Param("hostMetric") MdHostMetric hostMetric);

    @UpdateProvider(method = "modifyHostProtocol", type = CollectMetricDAOImpl.class)
    Integer modifyHostProtocol(@Param("hostMetric") MdHostMetric hostMetric);

    @UpdateProvider(method = "updateHostFirewallState", type = CollectMetricDAOImpl.class)
    Integer updateHostFirewallState(@Param("idArray") String[] idArray,
            @Param("state") Integer state);

    @UpdateProvider(method = "updateHostThirdpartyState", type = CollectMetricDAOImpl.class)
    Integer updateHostThirdpartyState(@Param("idArray") String[] idArray,
            @Param("state") Integer state);

    @UpdateProvider(method = "updateHostProtocolState", type = CollectMetricDAOImpl.class)
    Integer updateHostProtocolState(@Param("idArray") String[] idArray,
            @Param("state") Integer state);
}
