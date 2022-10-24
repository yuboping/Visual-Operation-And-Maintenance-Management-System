package com.asiainfo.lcims.omc.persistence.configmanage;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.sos.SosRoleBean;
import com.asiainfo.lcims.omc.model.system.*;
import com.asiainfo.lcims.omc.persistence.SqlRoleProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RoleManageDAO {

    @SelectProvider(method = "getRoleList", type = SqlRoleProvider.class)
    List<MdRole> getRoleList(@Param("name") String name);

    @Delete("DELETE FROM MD_ROLE WHERE ROLEID = #{roleid}")
    int deleteById(@Param("roleid") String roleid);

    @Update("UPDATE MD_ROLE SET NAME=#{mdRole.name},DESCRIPTION=#{mdRole.description} WHERE ROLEID=#{mdRole.roleid}")
    int updateRoleInfo(@Param("mdRole") MdRole mdRole);

    @Delete("DELETE FROM MD_ROLE_PERMISSIONS WHERE ROLEID = #{roleid}")
    int deleteRolePermissionsById(@Param("roleid") String roleid);

    @Select("SELECT ROLEID, NAME, DESCRIPTION FROM MD_ROLE WHERE ROLEID = #{roleid}")
    MdRole getRolebyRoleid(@Param("roleid") String roleid);

    @Select("SELECT ROLEID, TYPE, PERMISSIONID FROM MD_ROLE_PERMISSIONS " +
            "WHERE ROLEID = #{roleid} AND TYPE = #{type} ")
    List<MdRolePermissions> getRolePermissionByRoleid(@Param("roleid") String roleid, @Param("type") String type);

    @Select("SELECT ROLEID, TYPE, PERMISSIONID FROM MD_ROLE_PERMISSIONS " +
            "WHERE ROLEID = #{roleid} AND TYPE = #{type} ")
    List<MdRolePermissions> getMenuRolePermissionByRoleid(@Param("roleid") String roleid, @Param("type") String type);

    @Select("SELECT S.AREANO, S.NAME, F.CHECKED FROM MD_AREA S LEFT JOIN " +
            "(SELECT ROLEID, TYPE, PERMISSIONID, 1 AS CHECKED " +
            "FROM MD_ROLE_PERMISSIONS WHERE ROLEID = #{roleid} AND TYPE = #{type}) F ON PERMISSIONID = S.AREANO")
    List<MdArea> getModifyInfoWithArea(@Param("roleid") String roleid, @Param("type") String type);

    @Select("SELECT S.ID, S.NODE_NAME, F.CHECKED FROM MD_NODE S LEFT JOIN " +
            "(SELECT ROLEID, TYPE, PERMISSIONID, 1 AS CHECKED " +
            "FROM MD_ROLE_PERMISSIONS WHERE ROLEID = #{roleid} AND TYPE = #{type}) F ON PERMISSIONID = S.ID")
    List<MdNode> getModifyInfoWithNode(@Param("roleid") String roleid, @Param("type") String type);

    @Select("SELECT N.COUNT , F.* FROM " +
            "(SELECT M.CODE, M.DESCRIPTION FROM MD_PARAM M WHERE M.CODE = #{areatype}) F , " +
            "(SELECT COUNT(1) AS COUNT FROM MD_ROLE_PERMISSIONS WHERE ROLEID = #{roleid} AND TYPE = #{areatype}) N " +
            "UNION " +
            "SELECT N.COUNT , F.* FROM " +
            "(SELECT M.CODE, M.DESCRIPTION FROM MD_PARAM M WHERE M.CODE = #{nodetype}) F , " +
            "(SELECT COUNT(1) AS COUNT FROM MD_ROLE_PERMISSIONS WHERE ROLEID = #{roleid} AND TYPE = #{nodetype}) N ")
    List<MdParam> getModifyInfoWithParam(@Param("roleid") String roleid, @Param("areatype") String areatype, @Param("nodetype") String nodetype);

    @Select("SELECT ROLEID, NAME, DESCRIPTION FROM MD_ROLE")
    List<MdRole> getAllRoleList();

    @Select("SELECT ROLEID, NAME as ROLENAME  FROM MD_ROLE")
    List<SosRoleBean> getSosAllRoleList();
    
    
//    @Select("SELECT F.ID , F.SHOW_NAME AS TEXT , F.PARENT_ID AS PARENT, M.CHECKED " +
//            "FROM MD_MENU_TREE F " +
//            "LEFT JOIN (SELECT ROLEID, TYPE, PERMISSIONID, 1 AS CHECKED FROM MD_ROLE_PERMISSIONS WHERE ROLEID = #{roleid} AND TYPE = #{type} AND MENU_TYPE = '1') M " +
//            "ON M.PERMISSIONID = F.ID WHERE F.IS_GRANT = 1 ORDER BY F.ID ASC")
    @Select("SELECT F.ID , F.SHOW_NAME AS TEXT , F.PARENT_ID AS PARENT, M.CHECKED " +
            "FROM MD_MENU_TREE F " +
            "LEFT JOIN (SELECT ROLEID, TYPE, PERMISSIONID, 1 AS CHECKED FROM MD_ROLE_PERMISSIONS WHERE ROLEID = #{roleid} AND TYPE = #{type} AND MENU_TYPE = '1') M " +
            "ON M.PERMISSIONID = F.ID WHERE F.IS_GRANT = 1")
    List<MdMenuTree> getModifyInfoWithMenu(@Param("roleid") String roleid, @Param("type") String type);

    @SelectProvider(method = "getMenuTreeByRole", type = SqlRoleProvider.class)
    List<MdMenuTree> getMenuTreeByRole();

    @Select("SELECT F.ID , F.SHOW_NAME AS TEXT , F.PARENT_ID AS PARENT FROM MD_ROLE_PERMISSIONS M" +
            " LEFT JOIN MD_MENU_TREE F ON F.ID = M.PERMISSIONID WHERE M.ROLEID = #{roleid} AND M.TYPE = #{type}")
    List<MdMenuTree> getDetailMenuTree(@Param("roleid") String roleid, @Param("type") String type);

    @Insert("INSERT INTO MD_ROLE (ROLEID, NAME, DESCRIPTION) VALUES(#{mdRole.roleid}, #{mdRole.name},#{mdRole.description})")
    int addMdRoleInfo(@Param("mdRole") MdRole mdRole);

    @Insert("INSERT INTO MD_ROLE_PERMISSIONS (ROLEID, TYPE, PERMISSIONID) VALUES(#{mdRolePermissions.roleid}, #{mdRolePermissions.type},#{mdRolePermissions.permissionid})")
    int addMdRolePermissionInfo(@Param("mdRolePermissions") MdRolePermissions mdRolePermissions);

    @Insert("INSERT INTO MD_ROLE_PERMISSIONS (ROLEID, TYPE, PERMISSIONID, MENU_TYPE) VALUES(#{mdRolePermissions.roleid}, #{mdRolePermissions.type},#{mdRolePermissions.permissionid},#{mdRolePermissions.menu_type})")
    int addMenuMdRolePermissionInfo(@Param("mdRolePermissions") MdRolePermissions mdRolePermissions);

    @Select("SELECT COUNT(1) FROM M_ADMIN_ROLE WHERE ROLEID = #{roleid}")
    int queryIfExistInAdminRole(@Param("roleid") String roleid);

    @Select("SELECT COUNT(1) FROM MD_ROLE WHERE NAME = #{mdRole.name} AND ROLEID <> #{mdRole.roleid} ")
    int queryIfExistInRoleName(@Param("mdRole") MdRole mdRole);

    @Select("SELECT COUNT(1) FROM MD_ROLE WHERE NAME = #{mdRole.name} ")
    int queryIfExistInRoleNameWithAdd(@Param("mdRole") MdRole mdRole);

    @Select("SELECT * FROM MD_ROLE_PERMISSIONS F WHERE F.ROLEID = (SELECT K.ROLEID FROM M_ADMIN_ROLE K WHERE K.ADMIN = #{username}) AND F.TYPE = 'menu'")
    List<MdRolePermissions> queryMenuByUsername(@Param("username") String username);

    @Select("SELECT * FROM MD_ROLE_PERMISSIONS F WHERE F.ROLEID = (SELECT K.ROLEID FROM M_ADMIN_ROLE K WHERE K.ADMIN = #{username}) AND F.TYPE = #{type}")
    List<MdRolePermissions> queryPermissionTypeByUsername(@Param("username") String username, @Param("type") String type);

//    @Select("SELECT F.NAME, F.AREANO, 1 AS CHECKED FROM MD_ROLE_PERMISSIONS S LEFT JOIN (SELECT AREANO, NAME FROM MD_AREA ) F ON S.PERMISSIONID = F.AREANO WHERE S.ROLEID = #{roleid} AND S.TYPE = #{type}")
    @Select("SELECT F.NAME, F.AREANO, 1 AS CHECKED FROM MD_ROLE_PERMISSIONS S , MD_AREA F WHERE S.PERMISSIONID = F.AREANO AND S.ROLEID = #{roleid} AND S.TYPE = #{type}")
    List<MdArea> getDetailInfoWithArea(@Param("roleid") String roleid, @Param("type") String type);

    @Select("SELECT F.ID, F.NODE_NAME, 1 AS CHECKED FROM MD_ROLE_PERMISSIONS S , MD_NODE F WHERE S.PERMISSIONID = F.ID AND S.ROLEID = #{roleid} AND S.TYPE = #{type}")
    List<MdNode> getDetailInfoWithNode(@Param("roleid") String roleid, @Param("type") String type);

    @Select("SELECT F.ID , F.SHOW_NAME AS TEXT , F.PARENT_ID AS PARENT, 1 AS CHECKED " +
            "FROM MD_MENU_TREE F , MD_ROLE_PERMISSIONS M " +
            "WHERE M.ROLEID = #{roleid} AND M.TYPE = #{type} " +
            "AND M.PERMISSIONID = F.ID AND F.IS_GRANT = 1 ORDER BY F.ID ASC ")
    List<MdMenuTree> getDetailInfoWithMenu(@Param("roleid") String roleid, @Param("type") String type);

}
