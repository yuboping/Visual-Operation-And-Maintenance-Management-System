package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.system.AdminRole;
import com.asiainfo.lcims.omc.model.system.MAdmin;

public interface MAdminRoleDAO {

    @Insert("INSERT INTO M_ADMIN_ROLE(ADMIN,ROLEID) VALUES(#{admin.admin},#{admin.roleid})")
    int insert(@Param("admin") MAdmin admin);

    @Delete("DELETE FROM M_ADMIN_ROLE WHERE ADMIN = #{admin} AND ADMIN NOT IN ('admin')")
    int deleteByAdmin(@Param("admin") String admin);

    @Delete("DELETE FROM M_ADMIN_ROLE WHERE ADMIN = #{admin} AND ROLEID=#{admin}")
    int deleteByAdminAndRole(@Param("admin") String admin, @Param("roleid") String roleid);

    @Select("SELECT * FROM M_ADMIN_ROLE WHERE roleid=#{roleid}")
    List<AdminRole> getAdminRoleByRoleid(@Param("roleid") String roleid);
}
