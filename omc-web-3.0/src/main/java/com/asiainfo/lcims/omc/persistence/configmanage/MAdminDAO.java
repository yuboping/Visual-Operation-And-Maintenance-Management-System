package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdRole;
import com.asiainfo.lcims.omc.persistence.configmanage.impl.MAdminDAOImpl;

/**
 * 管理员管理DAO接口
 * 
 * @author zhujiansheng
 * @date 2018年7月31日 下午4:21:03
 * @version V1.0
 */
public interface MAdminDAO {

    @SelectProvider(method = "getMAdminList", type = MAdminDAOImpl.class)
    List<MAdmin> getMAdminList(@Param("mAdmin") MAdmin mAdmin);

    @Delete("DELETE FROM M_ADMIN WHERE ADMIN = #{mAdmin} AND ADMIN NOT IN ('admin')")
    int delete(@Param("mAdmin") String mAdmin);

    @InsertProvider(method = "insertMAdmin", type = MAdminDAOImpl.class)
    int insert(@Param("mAdmin") MAdmin mAdmin);

    @UpdateProvider(method = "updateMAdmin", type = MAdminDAOImpl.class)
    int update(@Param("mAdmin") MAdmin mAdmin);

    @UpdateProvider(method = "modifyPasswd", type = MAdminDAOImpl.class)
    int modifyPasswd(@Param("mAdmin") MAdmin mAdmin);

    @Select("SELECT T1.*,T2.ROLEID AS ROLEID FROM M_ADMIN T1 LEFT JOIN M_ADMIN_ROLE T2 ON (T1.ADMIN=T2.ADMIN) WHERE T1.ADMIN = #{adminname}")
    MAdmin getAdminByAdmin(@Param("adminname") String adminname);

    @SelectProvider(method = "getAdmin", type = MAdminDAOImpl.class)
    List<MAdmin> getAdmin(@Param("admin") String admin, @Param("roleid") String roleid);

    @Select("SELECT R.* FROM M_ADMIN_ROLE AR LEFT JOIN MD_ROLE R ON AR.ROLEID=R.ROLEID WHERE AR.ADMIN=#{username}")
    List<MdRole> getRolesByAdmin(@Param("username") String username);

    @Select("SELECT ROLEID,NAME,DESCRIPTION FROM MD_ROLE WHERE ROLEID NOT IN ('0')")
    List<MdRole> getNotAdminRoles();

    @Select("SELECT ROLEID,NAME,DESCRIPTION FROM MD_ROLE WHERE ROLEID NOT IN (SELECT ROLEID FROM M_ADMIN_ROLE) OR ROLEID=#{roleid}")
    List<MdRole> getNotUsedRoles(@Param("roleid") String roleid);

    @Select("SELECT * FROM M_ADMIN")
    List<MAdmin> getAll();
    
    @Select("SELECT count(*) FROM M_ADMIN m where m.ADMIN = #{admin}")
    public int queryMAdminByAdmin(String admin);
    
    @SelectProvider(method = "checkType", type = MAdminDAOImpl.class)
    public int queryCheckType(@Param("admin") String admin,@Param("checkType") int checkType,@Param("checkValue") String checkValue);
}
