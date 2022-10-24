package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.persistence.configmanage.impl.BdNasDAOImpl;

/**
 * BRAS设备的DAO层
 * 
 * @author zhujiansheng
 * @date 2018年8月8日 下午3:20:22
 * @version V1.0
 */
public interface BdNasDAO {
    @SelectProvider(method = "getBdNas", type = BdNasDAOImpl.class)
    List<BdNas> getBdNas(@Param("bdNas") BdNas bdNas);
    
    @SelectProvider(method = "getBdNasByRoleList", type = BdNasDAOImpl.class)
    public List<BdNas> getBdNasByRoleList(@Param("roleid") String roleid,@Param("bdNas") BdNas bdNas);
    
    @Insert("INSERT INTO BD_NAS(ID,NAS_NAME,NAS_IP,EQUIP_ID,AREA_NO,IP_TYPE,DESCRIPTION) VALUES(#{bdNas.id},#{bdNas.nas_name},#{bdNas.nas_ip},#{bdNas.equip_id,jdbcType=VARCHAR},#{bdNas.area_no},#{bdNas.ip_type,jdbcType=VARCHAR},#{bdNas.description,jdbcType=VARCHAR})")
    int insert(@Param("bdNas") BdNas bdNas);

    @Delete("DELETE FROM BD_NAS WHERE ID NOT IN (SELECT HOSTID FROM MON_HOST) AND ID IN (#{id})")
    public int delete(@Param("id") String id);

    @UpdateProvider(method = "updateBdNas", type = BdNasDAOImpl.class)
    int update(@Param("bdNas") BdNas bdNas);

    @Select("SELECT * FROM BD_NAS WHERE AREA_NO = #{areano}")
    List<BdNas> getBdNasWithAreaid(@Param("areano") String areano);

    @Select("SELECT ID,NAS_NAME,NAS_IP,EQUIP_ID,AREA_NO,IP_TYPE,DESCRIPTION FROM BD_NAS WHERE ID = #{id}")
    BdNas getSingleBras(@Param("id") String id);
    
    @SelectProvider(method = "queryNotMatchNasIp", type = BdNasDAOImpl.class)
    List<BdNas> queryNotMatchNasIp(@Param("metricid") String metricid);
    
    @Update("UPDATE BD_NAS SET AREA_NO = #{areano} WHERE ID = #{id}")
    int updateAreaNo(@Param("id") String id, @Param("areano") String areano);

    @Select("SELECT * FROM BD_NAS")
    List<BdNas> getAll();
    
    @SelectProvider(method = "queryNotMatchNasIpForShcm", type = BdNasDAOImpl.class)
    List<BdNas> queryNotMatchNasIpForShcm(@Param("metricid") String metricid);

    @Select("SELECT * FROM BD_NAS WHERE NAS_IP = #{nasIp}")
    BdNas getBdNasByIp(@Param("nasIp") String nasIp);
}
