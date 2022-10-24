package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.MdHostProcess;
import com.asiainfo.lcims.omc.model.configmanage.MdProcess;
import com.asiainfo.lcims.omc.persistence.SqlSpecialProvider;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.util.page.Page;

public interface MdHostProcessDAO {

    /**
     * 根据查询条件获取主机指标关系数据
     * @param mdHostProcess
     * @return
     */
    @SelectProvider(method = "getHostProcessList", type = SqlSpecialProvider.class)
    List<MdHostProcess> getHostProcessList(@Param("hostProcessParam") MdHostProcess mdHostProcess, @Param("page") Page page);

    /**
     * 查询数据总数
     * @param mdHostProcess
     * @return
     */
    @SelectProvider(method = "getHostProcessInfoCount", type = SqlSpecialProvider.class)
    int getHostProcessInfoCount(@Param("hostProcessParam") MdHostProcess mdHostProcess);

    /**
     * 根据ID获取单条主机指标关系数据
     * @param id
     * @return
     */
    @Select("SELECT M.ID, M.HOST_ID, M.PROCESS_ID, M.PROCESS_KEY, M.START_SCRIPT, M.STOP_SCRIPT, M.DESCRIPTION, H.ADDR as HOST_IP, HM.PROCESS_NAME FROM MD_HOST_PROCESS M LEFT JOIN MON_HOST H ON M.HOST_ID = H.HOSTID LEFT JOIN MD_PROCESS HM ON M.PROCESS_ID = HM.PROCESS_ID WHERE M.ID = #{id}")
    MdHostProcess getHostProcessInfo(@Param("id") String id);

    @Delete("DELETE FROM MD_HOST_PROCESS WHERE ID = #{id}")
    int deleteById(@Param("id") String id);

    @UpdateProvider(method = "modifyHostProcess", type = SqlSpecialProvider.class)
    Integer modifyHostProcess(@Param("hostProcess") MdHostProcess hostProcess);

    @SelectProvider(method = "getMdProcessListByProcess", type = SqlSpecialProvider.class)
    List<MdHostProcess> getMdProcessListByProcess(@Param("mdProcess") MdProcess mdProcess);

    @SelectProvider(method = "getMdProcessListByHost", type = SqlSpecialProvider.class)
    List<MdHostProcess> getMdProcessListByHost(@Param("host") MonHost host);

    @InsertProvider(method = "insertHostProcessList", type = SqlSpecialProvider.class)
    void insertHostProcessList(@Param("list") List<MdHostProcess> list);

    @Delete("DELETE FROM MD_HOST_PROCESS WHERE HOST_ID = #{hostid}")
    int deleteByHostId(@Param("hostid") String hostid);

    @SelectProvider(method = "getHostProcessIpList", type = SqlSpecialProvider.class)
    List<String> getHostProcessIpList(@Param("hostProcessArray") String[] hostProcessArray);

    @Select("SELECT ID,HOST_ID,PROCESS_ID,PROCESS_KEY,START_SCRIPT,STOP_SCRIPT,DESCRIPTION,CREATE_TIME,UPDATE_TIME FROM MD_HOST_PROCESS WHERE HOST_ID= #{hostId}")
    List<MdHostProcess> getMdHostProcessByHostId(@Param("hostId") String hostId);
}
