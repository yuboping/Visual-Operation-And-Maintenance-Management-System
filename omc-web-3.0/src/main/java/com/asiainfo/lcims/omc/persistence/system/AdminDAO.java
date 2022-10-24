package com.asiainfo.lcims.omc.persistence.system;

import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdRole;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AdminDAO {

    @Select("SELECT ADMIN,PASSWORD,PASSWORDTYPE FROM M_ADMIN WHERE ADMIN = #{adminname}")
    MAdmin getAdminByAdmin(@Param("adminname") String adminname);
    
    @Select("SELECT R.* FROM M_ADMIN_ROLE M INNER JOIN MD_ROLE R ON M.ROLEID = R.ROLEID WHERE M.ADMIN = #{adminname}")
	List<MdRole> getRoleByAdmin(@Param("adminname") String admin);
    
    @Select("SELECT R.* FROM M_ADMIN_ROLE AR LEFT JOIN MD_ROLE R ON AR.ROLEID=R.ROLEID WHERE AR.ADMIN=#{username}")
    List<MdRole> getRolesByAdmin(@Param("username") String username);

    @Select("SELECT COUNT(*) FROM M_ADMIN_ROLE M INNER JOIN MD_ROLE R ON M.ROLEID = R.ROLEID WHERE M.ADMIN = #{adminname} AND M.ROLEID = '0' ")
    int getIfAdmin(@Param("adminname") String admin);

    @Update("UPDATE M_ADMIN SET PASSWORD = #{mAdmin.password} WHERE ADMIN = #{mAdmin.admin}")
    int updateOldPassword(@Param("mAdmin") MAdmin mAdmin);
    
    @Insert("insert into M_ADMIN(ADMIN,PASSWORD,PASSWORDTYPE,STATUS) values(#{mAdmin.admin},#{mAdmin.password},#{mAdmin.passwordtype},#{mAdmin.status})")
    int insertAdmin(@Param("mAdmin") MAdmin mAdmin);
    
}
