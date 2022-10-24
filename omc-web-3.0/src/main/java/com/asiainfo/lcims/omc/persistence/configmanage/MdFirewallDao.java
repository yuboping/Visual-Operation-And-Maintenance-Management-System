package com.asiainfo.lcims.omc.persistence.configmanage;

import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdFirewall;
import com.asiainfo.lcims.omc.persistence.configmanage.impl.BdNasDAOImpl;
import com.asiainfo.lcims.omc.persistence.configmanage.impl.MdFirewallDAOImpl;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MdFirewallDao {

    /**
     * 表格数据
     * @param mdFirewall
     * @return
     */
    @SelectProvider(method = "getFirewall", type = MdFirewallDAOImpl.class)
    List<MdFirewall> getFirewall(@Param("mdFirewall") MdFirewall mdFirewall);

//    @SelectProvider(method = "getBdNasByRoleList", type = BdNasDAOImpl.class)
//    public List<BdNas> getBdNasByRoleList(@Param("roleid") String roleid,@Param("bdNas") BdNas bdNas);

    /**
     * 新增
     * @param mdFirewall
     * @return
     */
    @Insert("INSERT INTO MD_FIREWALL(ID,FIREWALL_NAME,FIREWALL_IP,EQUIP_ID,AREA_NO,IP_TYPE,PORT,USER_NAME,PASSWORD,FILE_PATH,RESULT_PATH,DESCRIPTION) " +
            "VALUES(#{mdFirewall.id},#{mdFirewall.firewall_name},#{mdFirewall.firewall_ip},#{mdFirewall.equip_id,jdbcType=VARCHAR},#{mdFirewall.area_no}," +
            "#{mdFirewall.ip_type,jdbcType=VARCHAR},#{mdFirewall.port,jdbcType=VARCHAR},#{mdFirewall.user_name,jdbcType=VARCHAR},#{mdFirewall.password,jdbcType=VARCHAR}," +
            "#{mdFirewall.file_path,jdbcType=VARCHAR},#{mdFirewall.result_path,jdbcType=VARCHAR},#{mdFirewall.description,jdbcType=VARCHAR})")
    int insert(@Param("mdFirewall") MdFirewall mdFirewall);

    /**
     * 查询单条
     * @param id
     * @return
     */
    @Select("SELECT ID,FIREWALL_NAME,FIREWALL_IP,EQUIP_ID,AREA_NO,IP_TYPE,PORT,USER_NAME,PASSWORD,FILE_PATH,RESULT_PATH,DESCRIPTION FROM MD_FIREWALL WHERE ID = #{id}")
    MdFirewall getSingleFirewall(@Param("id") String id);

    /**
     * 修改
     * @param mdFirewall
     * @return
     */
    @UpdateProvider(method = "updateFirewall", type = MdFirewallDAOImpl.class)
    int update(@Param("mdFirewall") MdFirewall mdFirewall);

    /**
     * 删除
     * @param id
     * @return
     */
    @Delete("DELETE FROM MD_FIREWALL WHERE ID NOT IN (SELECT HOSTID FROM MON_HOST) AND ID IN (#{id})")
    public int delete(@Param("id") String id);

    /**
     * 设置属地
     * @param id
     * @param areano
     * @return
     */
    @Update("UPDATE MD_FIREWALL SET AREA_NO = #{areano} WHERE ID = #{id}")
    int updateAreaNo(@Param("id") String id, @Param("areano") String areano);

    @SelectProvider(method = "queryNotMatchIp", type = MdFirewallDAOImpl.class)
    List<MdFirewall> queryNotMatchIp(@Param("metricid") String metricid);
}
