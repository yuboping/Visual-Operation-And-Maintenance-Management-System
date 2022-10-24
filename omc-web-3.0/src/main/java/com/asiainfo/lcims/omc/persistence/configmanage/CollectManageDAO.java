package com.asiainfo.lcims.omc.persistence.configmanage;

import com.asiainfo.lcims.omc.persistence.SqlProvider;
import com.asiainfo.lcims.omc.persistence.configmanage.impl.MdCollectManageDAOImpl;
import com.asiainfo.lcims.omc.persistence.configmanage.impl.MonHostDAOImpl;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface CollectManageDAO {

    @Select("SELECT A.*,B.NODE_NAME AS NODEIDNAME,C.DESCRIPTION AS HOSTTYPENAME FROM MON_HOST A LEFT JOIN MD_NODE B ON A.NODEID= B.ID  LEFT JOIN MD_PARAM C ON A.HOSTTYPE = C.CODE AND C.TYPE = 3 WHERE HOSTID=#{hostid}")
    MonHost getHostById(@Param("hostid") String hostid);

    @Select("SELECT A.*,B.NODE_NAME AS NODEIDNAME,C.DESCRIPTION AS HOSTTYPENAME ,D.DESCRIPTION AS DEPLOY_STATUS_NAME FROM MON_HOST A LEFT JOIN MD_NODE B ON A.NODEID= B.ID  LEFT JOIN MD_PARAM C ON A.HOSTTYPE = C.CODE AND C.TYPE = 3 LEFT JOIN MD_PARAM D ON A.DEPLOY_STATUS = D.CODE AND D.TYPE = 30 WHERE HOSTID=#{hostid}")
    MonHost getHostByIdAutodeploy(@Param("hostid") String hostid);

    @Select("SELECT * FROM MON_HOST ORDER BY NODEID,HOSTTYPE,HOSTNAME")
    List<MonHost> getAllHost();

    @SelectProvider(method = "getCollectorHost", type = SqlProvider.class)
    List<MonHost> getHost(@Param("hostname") String hostname, @Param("addr") String addr, @Param("nodeid") String nodeid);

    // 新增主机
    @InsertProvider(method = "insertMonHost", type = MdCollectManageDAOImpl.class)
    int insert(@Param("host") MonHost host);

    // 修改主机
    @UpdateProvider(method = "updateMonHost", type = MdCollectManageDAOImpl.class)
    int update(@Param("host") MonHost host);

    // 删除主机（主机已经配置指标信息，就不删除）
    @Delete("DELETE FROM MON_HOST WHERE HOSTID NOT IN (SELECT HOST_ID FROM MD_HOST_METRIC) AND HOSTID IN (#{hostid})")
    int deleteByHostId(@Param("hostid") String hostid);

    @Select("SELECT * FROM MON_HOST WHERE ADDR=#{addr} OR HOSTNAME=#{hostname}")
    List<MonHost> getRepeatHost(@Param("addr") String addr, @Param("hostname") String hostname);

    // 根据菜单name查询主机信息
    @Select("SELECT MH.* FROM MD_BUSINESS_HOST MBH LEFT JOIN MON_HOST MH ON (MBH.HOSTID = MH.HOSTID) WHERE MBH.NAME = #{menuname}")
    List<MonHost> getHostByMenuName(@Param("menuname") String menuname);

    /**
     *
     * @Title: getDayReport
     * @Description: TODO(查询日报表数据)
     * @param @param cpuRateMetricId
     * @param @param memoryMetricId
     * @param @param tableName
     * @param @param addr
     * @param @return 参数
     * @return List<MonHost> 返回类型
     * @throws
     */
    @SelectProvider(method = "getDayReport", type = SqlProvider.class)
    List<Map<String, Object>> getDayReport(@Param("cpuRateMetricId") String cpuRateMetricId,
                                           @Param("memoryMetricId") String memoryMetricId, @Param("tableName") String tableName,
                                           @Param("addr") String addr);

    /**
     *
     * @Title: getDayReport
     * @Description: TODO(查询周、月报表数据)
     * @param @param cpuRateMetricId
     * @param @param memoryMetricId
     * @param @param tableName
     * @param @param addr
     * @param @return 参数
     * @return List<MonHost> 返回类型
     * @throws
     */
    @SelectProvider(method = "getWMReport", type = SqlProvider.class)
    List<Map<String, Object>> getWMReport(@Param("cpuRateMetricId") String cpuRateMetricId,
                                          @Param("memoryMetricId") String memoryMetricId,
                                          @Param("tableNameList") List<String> tableNameList,
                                          @Param("addr") String addr);

    /**
     *
     * @Title: getHostByHostType
     * @Description: TODO(根据HostType查询)
     * @param @param hostType
     * @param @return 参数
     * @return List<MonHost> 返回类型
     * @throws
     */
    @Select("SELECT * FROM MON_HOST WHERE HOSTTYPE=#{hostType}")
    List<MonHost> getHostByHostType(@Param("hostType") int hostType);

    @Select("SELECT h.HOSTID,h.ADDR,h.HOSTNAME,n.NODE_NAME AS NODEIDNAME FROM MON_HOST h LEFT JOIN MD_NODE n ON h.NODEID = n.ID WHERE h.NODEID=#{nodeid} AND h.HOSTTYPE=#{hostType}")
    List<MonHost> getHostByNodeHostType(@Param("nodeid") String nodeid,@Param("hostType") int hostType);

    @Insert("INSERT INTO MON_HOST(HOSTID,ADDR,HOSTNAME,STATUS,OS,HOSTTYPE,VMID,IPS) VALUES(#{host.hostid},#{host.addr},#{host.hostname},#{host.status},'',${host.hosttype},#{host.vmid},#{host.ips})")
    int insertEmsHost(@Param("host") MonHost host);

    @Update("UPDATE MON_HOST SET HOSTNAME=#{host.hostname},HOSTTYPE=${host.hosttype},VMID=#{host.vmid},IPS=#{host.ips} WHERE HOSTID=#{host.hostid}")
    int updateEmsHost(@Param("host") MonHost host);


    /**
     * 批量删除主机（EMS模块调用）
     * @param hostids
     * @return
     */
    @Delete("DELETE FROM MON_HOST WHERE HOSTID IN (${hostids})")
    int deleteByHostIds(@Param("hostids") String hostids);
}
