package com.asiainfo.lcims.omc.persistence.configmanage;

import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.model.system.AreaHostMeau;
import com.asiainfo.lcims.omc.persistence.SqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AreaDAO {

    @Select("SELECT AREANO,NAME FROM MD_AREA")
    List<MdArea> getAll();
    
    @Select("SELECT AREANO,NAME FROM MD_AREA where AREANO in  "
    		+ "(select PERMISSIONID from  MD_ROLE_PERMISSIONS where  type='area' and \n" + 
    		"roleid=#{roleid})")
    List<MdArea> getByRoleMdAreaList(@Param("roleid") String roleid);

    @Select("SELECT AREANO,NAME FROM MD_AREA ORDER BY AREANO")
    List<MdArea> getAllOrder();
    
    @Select("SELECT AREANO,NAME FROM MD_AREA WHERE AREANO = #{areano}")
    MdArea getAreabyAreano(@Param("areano") String areano);

    @Select("SELECT AREANO,NAME FROM MD_AREA ORDER BY AREANO")
    List<AreaHostMeau> getAreaHostAllOrder();

    @Select("SELECT AREANO,NAME FROM MD_AREA WHERE AREANO<>'00' ORDER BY AREANO")
    List<MdArea> getAreaExceptTotal();

    @Select("SELECT AREANO,NAME FROM MD_AREA ORDER BY AREANO")
    List<MdArea> getAllArea();

    @Insert("INSERT INTO MD_AREA (AREANO,NAME,AREACODE) VALUES(#{area.areano},#{area.name},#{area.areacode})")
    int insert(@Param("area") MdArea area);

    @Update("UPDATE MD_AREA SET NAME=#{area.name},AREANO=#{area.areano} WHERE AREANO=#{area.areano}")
    int update(@Param("area") MdArea area);

    @Delete("DELETE FROM MD_AREA WHERE AREANO = #{areano}")
    int deleteById(@Param("areano") String areano);

    @Select("SELECT MAX(REPLACE(AREANO,'00','')+1) FROM MD_AREA WHERE AREANO LIKE'00%'")
    String getMaxId();

    @SelectProvider(method = "getArea", type = SqlProvider.class)
    List<MdArea> getArea(@Param("areano") String areano, @Param("name") String name);

}
