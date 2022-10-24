package com.asiainfo.lcims.omc.persistence.system;

import java.util.List;

import com.asiainfo.lcims.omc.model.system.MdMenuTree;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.system.MdMenu;

public interface MdMenuDAO {

    @Select("SELECT ID,NAME,SHOW_NAME,IS_MENU,IS_GRANT,IS_SHOW,URL,PARENT_ID,SEQUENCE,MENU_LEVEL,DYNAMIC,LARGEICON,ICON  FROM MD_MENU_TREE ORDER BY SEQUENCE ")
    List<MdMenu> getMenuList();

    @Select("SELECT ID,NAME,SHOW_NAME,IS_MENU,IS_GRANT,IS_SHOW,URL,PARENT_ID,SEQUENCE,MENU_LEVEL,DYNAMIC,LARGEICON,ICON  FROM MD_MENU_TREE WHERE PARENT_ID = #{parent_id} ORDER BY SEQUENCE ")
    List<MdMenu> getParentMdMenuList(@Param("parent_id") String parent_id);

    @Select("SELECT ID,NAME,SHOW_NAME,IS_MENU,IS_GRANT,IS_SHOW,URL,PARENT_ID,SEQUENCE,MENU_LEVEL,DYNAMIC,LARGEICON,ICON  FROM MD_MENU_TREE WHERE ID = #{Id}")
    MdMenu getMdMenuById(@Param("Id") String Id);

    @Select("SELECT ID,NAME,SHOW_NAME,IS_MENU,IS_GRANT,IS_SHOW,URL,PARENT_ID,SEQUENCE,MENU_LEVEL,DYNAMIC,LARGEICON,ICON  FROM MD_MENU_TREE ORDER BY SEQUENCE ")
    List<MdMenuTree> getMenuTreeList();
}
