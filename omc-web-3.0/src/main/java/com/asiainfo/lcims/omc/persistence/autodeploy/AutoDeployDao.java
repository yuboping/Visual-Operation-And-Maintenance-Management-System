package com.asiainfo.lcims.omc.persistence.autodeploy;

import com.asiainfo.lcims.omc.model.autodeploy.MdDeployLog;
import com.asiainfo.lcims.omc.model.autodeploy.MdFindBusiness;
import com.asiainfo.lcims.omc.model.autodeploy.MdHostHardwareInfoLog;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.system.MdMenuTree;
import com.asiainfo.lcims.omc.persistence.SqlAutoDeployProvider;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: YuChao
 * @Date: 2019/3/27 15:16
 */
public interface AutoDeployDao {

    @SelectProvider(method = "getMonHostInfoWithIp", type = SqlAutoDeployProvider.class)
    MonHost getMonHostInfoWithIp(@Param("monHost") MonHost monHost);

    @InsertProvider(method = "addMonHostInfo", type = SqlAutoDeployProvider.class)
    int addMonHostInfo(@Param("monHost") MonHost monHost);

    // 删除主机（根据ip删除）
    @Delete("DELETE FROM MON_HOST WHERE ADDR = #{ip}")
    int deleteMonhostByIp(@Param("ip") String ip);

    @UpdateProvider(method = "updateMonHostInfo", type = SqlAutoDeployProvider.class)
    int updateMonHostInfo(@Param("monHost") MonHost monHost);

    @InsertProvider(method = "addDeployLogInfo", type = SqlAutoDeployProvider.class)
    int addDeployLogInfo(@Param("deployLog") MdDeployLog deployLog);

    @InsertProvider(method = "addHostHardwareInfoLogInfo", type = SqlAutoDeployProvider.class)
    int addHostHardwareInfoLogInfo(
            @Param("hostHardwareInfoLog") MdHostHardwareInfoLog hostHardwareInfoLog);

    @Delete("DELETE FROM MD_HOST_METRIC WHERE HOST_ID = #{hostid}")
    int deleteHostMetricByHostId(@Param("hostid") String hostid);

    @SelectProvider(method = "queryMetricList", type = SqlAutoDeployProvider.class)
    List<MdMetric> queryMetricList(@Param("businesslist") String businesslist);

    @SelectProvider(method = "getFindBusinessList", type = SqlAutoDeployProvider.class)
    List<MdFindBusiness> getFindBusinessList(@Param("businesslist") String businesslist);

    @SelectProvider(method = "queryMenuList", type = SqlAutoDeployProvider.class)
    List<MdMenuTree> queryMenuList(@Param("businesslist") String businesslist);

    @Select("SELECT * FROM MD_FIND_BUSINESS WHERE BUSINESS_TAG != #{business_host}")
    List<MdFindBusiness> getFindBusinessListExcludeHost(@Param("business_host") String business_host);

    @Select("SELECT * FROM MD_DEPLOY_LOG WHERE DEPLOY_BATCH = #{deploy_batch}")
    List<MdDeployLog> getFindMdDeployLogByDeployBatch(@Param("deploy_batch") String deploy_batch);

    @Delete("DELETE FROM MD_HOST_METRIC WHERE HOST_ID = #{hostid} ")
    int deleteMdHostMetricByHostId(@Param("hostid") String hostid);

}
