package com.asiainfo.lcims.omc.persistence.ais;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.persistence.SqlProvider;
import com.asiainfo.lcims.omc.persistence.po.ais.INSItemGroup;
import com.asiainfo.lcims.omc.persistence.po.ais.INSitems;

/**
 * 巡检定义项dao类 处理wd_ins_item_group 和wd_ins_items两个表
 * 
 * @author nuannuan
 * 
 */
public interface INSItemDAO {

    @Select("SELECT GROUP_ID,GROUP_NAME,STATUS,DESCRIPTION,ICON FROM WD_INS_ITEM_GROUP ")
    List<INSItemGroup> getAllItemGroup();

    @Select("SELECT GROUP_ID,GROUP_NAME,STATUS,DESCRIPTION,ICON FROM WD_INS_ITEM_GROUP WHERE GROUPID=#{groupid} ")
    INSItemGroup getGroupById(@Param("groupid") Integer groupid);

    //@SelectProvider(method = "getItemByGroups", type = SqlProvider.class)
    List<INSItemGroup> getItemByGroups(@Param("groups") String groups);

    @Select("SELECT * FROM WD_INS_ITEMS")
    List<INSitems> getAllItem();

    @Select("SELECT DISTINCT GROUPID FROM WD_INS_ITEMS WHERE ITEMID IN (${items})")
    List<String> getGroupidsByitems(@Param("items") String items);

    @Select("SELECT ITEMID FROM WD_INS_ITEMS WHERE GROUPID IN (${groupids})")
    List<String> getItemsByGroupids(@Param("groupids") String groupids);

    @Select("SELECT * FROM WD_INS_ITEMS WHERE GROUPID=#{groupid}")
    List<INSitems> getItemByGroupid(@Param("groupid") Integer groupid);

//    @SelectProvider(method = "getItemByItemid", type = SqlProvider.class)
//    List<INSitems> getItemByItemid(@Param("itemid") String itemid);
    
    @Select("SELECT * FROM WD_INS_ITEMS WHERE ITEMID IN(${itemid}) ORDER BY GROUPID,ITEMID")
    List<INSitems> getItemByItemid(@Param("itemid") String itemid);

    //@SelectProvider(method = "getTotalItem", type = SqlProvider.class)
    int getTotalItem(@Param("groups") String groups);

    @Select("SELECT * FROM WD_INS_ITEMS WHERE ITEMID=#{itemid}")
    INSitems getItemDetail(@Param("itemid") Integer itemid);

}
