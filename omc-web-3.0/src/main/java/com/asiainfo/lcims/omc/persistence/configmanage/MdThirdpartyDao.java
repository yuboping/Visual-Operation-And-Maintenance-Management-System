package com.asiainfo.lcims.omc.persistence.configmanage;

import com.asiainfo.lcims.omc.model.configmanage.MdFirewall;
import com.asiainfo.lcims.omc.model.configmanage.MdThirdparty;
import com.asiainfo.lcims.omc.persistence.configmanage.impl.MdFirewallDAOImpl;
import com.asiainfo.lcims.omc.persistence.configmanage.impl.MdThirdpartyDaoImpl;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MdThirdpartyDao {

    /**
     * 表格数据
     * @param mdThirdparty
     * @return
     */
    @SelectProvider(method = "getThirdparty", type = MdThirdpartyDaoImpl.class)
    List<MdThirdparty> getThirdparty(@Param("mdThirdparty") MdThirdparty mdThirdparty);

//    @SelectProvider(method = "getBdNasByRoleList", type = BdNasDAOImpl.class)
//    public List<BdNas> getBdNasByRoleList(@Param("roleid") String roleid,@Param("bdNas") BdNas bdNas);

    /**
     * 新增
     * @param mdThirdparty
     * @return
     */
    @Insert("INSERT INTO MD_THIRDPARTY(ID,THIRDPARTY_NAME,THIRDPARTY_IP,EQUIP_ID,AREA_NO,IP_TYPE,PORT,USER_NAME,PASSWORD,FILE_PATH,DESCRIPTION) " +
            "VALUES(#{mdThirdparty.id},#{mdThirdparty.thirdparty_name},#{mdThirdparty.thirdparty_ip},#{mdThirdparty.equip_id,jdbcType=VARCHAR},#{mdThirdparty.area_no}," +
            "#{mdThirdparty.ip_type,jdbcType=VARCHAR},#{mdThirdparty.port,jdbcType=VARCHAR},#{mdThirdparty.user_name,jdbcType=VARCHAR},#{mdThirdparty.password,jdbcType=VARCHAR}," +
            "#{mdThirdparty.file_path,jdbcType=VARCHAR},#{mdThirdparty.description,jdbcType=VARCHAR})")
    int insert(@Param("mdThirdparty") MdThirdparty mdThirdparty);

    /**
     * 查询单条
     * @param id
     * @return
     */
    @Select("SELECT ID,THIRDPARTY_NAME,THIRDPARTY_IP,EQUIP_ID,AREA_NO,IP_TYPE,PORT,USER_NAME,PASSWORD,FILE_PATH,DESCRIPTION FROM MD_THIRDPARTY WHERE ID = #{id}")
    MdThirdparty getSingleThirdparty(@Param("id") String id);

    /**
     * 修改
     * @param mdThirdparty
     * @return
     */
    @UpdateProvider(method = "updateThirdparty", type = MdThirdpartyDaoImpl.class)
    int update(@Param("mdThirdparty") MdThirdparty mdThirdparty);

    /**
     * 删除
     * @param id
     * @return
     */
    @Delete("DELETE FROM MD_THIRDPARTY WHERE ID NOT IN (SELECT HOSTID FROM MON_HOST) AND ID IN (#{id})")
    public int delete(@Param("id") String id);

    /**
     * 设置属地
     * @param id
     * @param areano
     * @return
     */
    @Update("UPDATE MD_THIRDPARTY SET AREA_NO = #{areano} WHERE ID = #{id}")
    int updateAreaNo(@Param("id") String id, @Param("areano") String areano);

    @SelectProvider(method = "queryNotMatchIp", type = MdThirdpartyDaoImpl.class)
    List<MdThirdparty> queryNotMatchIp(@Param("metricid") String metricid);
}
