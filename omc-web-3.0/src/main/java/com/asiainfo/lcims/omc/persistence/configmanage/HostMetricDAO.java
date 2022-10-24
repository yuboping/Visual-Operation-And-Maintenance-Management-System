package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.MdHostMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.persistence.SqlProvider;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.util.page.Page;

public interface HostMetricDAO {
    /**
     * 根据查询条件获取主机指标关系数据
     * 
     * @param hostid
     * @param metricid
     * @return
     */
    @SelectProvider(method = "getHostMetricInfo", type = SqlProvider.class)
    List<MdHostMetric> getHostMetricInfo(@Param("hostid") String hostid,
            @Param("metricid") String metricid, @Param("page") Page page);

    /**
     * 查询数据总数
     * 
     * @param hostid
     * @param metricid
     * @return
     */
    @SelectProvider(method = "getHostMetricInfoCount", type = SqlProvider.class)
    int getHostMetricInfoCount(@Param("hostid") String hostid, @Param("metricid") String metricid);

    /**
     * 查询详情
     * 
     * @param id
     * @return
     */
    @Select("SELECT HM.ID,HM.HOST_ID,H.HOSTNAME,H.ADDR,HM.METRIC_ID,"
            + "M.METRIC_NAME,HM.CYCLE_ID,C.CYCLENAME,C.CRON,"
            + "C.DESCRIPTION AS CYCLE_DESCRIPTION,HM.SCRIPT,"
            + "HM.SCRIPT_PARAM,HM.SCRIPT_RETURN_TYPE,P1.DESCRIPTION AS RETURNTYPENAME,"
            + "HM.STATE,P2.DESCRIPTION AS statename"
            + " FROM MD_HOST_METRIC HM"
            + " LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID LEFT JOIN MD_METRIC M ON HM.METRIC_ID = M.ID"
            + " LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID"
            + " LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7"
            + " LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8" + " WHERE HM.ID=#{id}")
    MdHostMetric querySingleinfo(@Param("id") String id);

    @SelectProvider(method = "getMdMetricListByMetric", type = SqlProvider.class)
    List<MdHostMetric> getMdMetricListByMetric(@Param("metric") MdMetric metric);

    @SelectProvider(method = "getMdMetricListByHost", type = SqlProvider.class)
    List<MdHostMetric> getMdMetricListByHost(@Param("host") MonHost host);

    @SelectProvider(method = "insertHostMetricList", type = SqlProvider.class)
    void insertHostMetricList(@Param("list") List<MdHostMetric> list);

    @UpdateProvider(method = "updateHostMetricState", type = SqlProvider.class)
    Integer updateHostMetricState(@Param("hostMetricidArray") String[] hostMetricidArray,
            @Param("state") Integer state);

    @SelectProvider(method = "getHostMetricIpList", type = SqlProvider.class)
    List<String> getHostMetricIpList(@Param("hostMetricidArray") String[] hostMetricidArray);

    @UpdateProvider(method = "modifyHostMetric", type = SqlProvider.class)
    Integer modifyHostMetric(@Param("hostMetric") MdHostMetric hostMetric);

    @Select("SELECT t1.ID AS ID,t1.HOST_ID AS HOST_ID,t1.METRIC_ID AS METRIC_ID,t1.CYCLE_ID AS CYCLE_ID,t1.SCRIPT AS SCRIPT,t1.SCRIPT_PARAM AS SCRIPT_PARAM,t1.SCRIPT_RETURN_TYPE AS SCRIPT_RETURN_TYPE,t1.STATE AS STATE,t1.UPDATE_TIME AS UPDATE_TIME,t2.METRIC_TYPE AS METRIC_TYPE FROM MD_HOST_METRIC t1 LEFT JOIN MD_METRIC t2 ON t1.METRIC_ID=t2.ID  WHERE t1.HOST_ID=#{hostId} AND t1.METRIC_ID = #{mdMetricId} AND t1.STATE != 3")
    MdHostMetric getMdHostMetricByhostmdMetric(@Param("mdMetricId") String mdMetricId,
            @Param("hostId") String hostId);
    
    @Select("SELECT t1.ID AS ID,t1.HOST_ID AS HOST_ID,t1.METRIC_ID AS METRIC_ID,t1.CYCLE_ID AS CYCLE_ID,t1.SCRIPT AS SCRIPT,t1.SCRIPT_PARAM AS SCRIPT_PARAM,t1.SCRIPT_RETURN_TYPE AS SCRIPT_RETURN_TYPE,t1.STATE AS STATE,t1.UPDATE_TIME AS UPDATE_TIME,t2.METRIC_TYPE AS METRIC_TYPE FROM MD_HOST_METRIC t1 LEFT JOIN MD_METRIC t2 ON t1.METRIC_ID=t2.ID  WHERE t1.HOST_ID=#{hostId} AND METRIC_TYPE = '1' AND t1.STATE != 3")
    List<MdHostMetric> getMdHostMetricBymdMetric(@Param("hostId") String hostId);

    @SelectProvider(method = "insertHostMetric", type = SqlProvider.class)
    void insertHostMetric(@Param("hostMetric") MdHostMetric mdHostMetric);
    
    /**
     * 根据id数组查询主机指标配置信息列表
     * @param hostMetricidArray
     * @return
     */
    @SelectProvider(method = "getHostMetricInfoByIds", type = SqlProvider.class)
    List<MdHostMetric> getHostMetricInfoByIds(@Param("hostMetricidArray") String[] hostMetricidArray);
    
    @Delete("DELETE FROM MD_HOST_METRIC WHERE ID = #{id}")
    void deleteHostMetric(@Param("id") String id);
    
    @Delete("DELETE FROM MD_HOST_METRIC WHERE HOST_ID = #{hostId}")
    void deleteHostMetricByHostId(@Param("hostId") String hostId);
    /**
     * 根据hostId批量删除EMS模块调用
     * @param hostIds
     */
    @Delete("DELETE FROM MD_HOST_METRIC WHERE HOST_ID in (${hostIds})")
    void deleteHostMetricByHostIds(@Param("hostIds") String hostIds);
}
