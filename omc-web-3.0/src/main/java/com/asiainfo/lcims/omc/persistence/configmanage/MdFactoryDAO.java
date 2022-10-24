package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.MdFactory;
import com.asiainfo.lcims.omc.persistence.SqlComplexProvider;

/**
 * 工厂管理DAO层
 * 
 * @author zhujiansheng
 * @date 2018年7月25日 上午10:17:02
 * @version V1.0
 */
public interface MdFactoryDAO {

    @SelectProvider(method = "getMdFactory", type = SqlComplexProvider.class)
    List<MdFactory> getMdFactory(@Param("mdFactory") MdFactory mdFactory);

    @Insert("INSERT INTO MD_FACTORY (ID,FACTORY_NAME,DESCRIPTION) VALUES(#{mdFactory.id},#{mdFactory.factory_name},#{mdFactory.description})")
    int insert(@Param("mdFactory") MdFactory mdFactory);

    @UpdateProvider(method = "updateMdFactory", type = SqlComplexProvider.class)
    int update(@Param("mdFactory") MdFactory mdFactory);

    @Delete("DELETE FROM MD_FACTORY WHERE ID NOT IN (SELECT FACTORY_ID FROM MD_EQUIPMENT_MODEL) AND ID IN (#{factoryId})")
    int delete(@Param("factoryId") String factoryId);

    @Select("SELECT ID,FACTORY_NAME,DESCRIPTION FROM MD_FACTORY WHERE ID = #{factoryId}")
    MdFactory getOneFactory(@Param("factoryId") String factoryId);

    @Select("SELECT ID,FACTORY_NAME,DESCRIPTION FROM MD_FACTORY")
    List<MdFactory> getAll();

    @Select("SELECT ID,FACTORY_NAME,DESCRIPTION FROM MD_FACTORY WHERE FACTORY_NAME = #{factoryName}")
    MdFactory getFactoryByName(@Param("factoryName") String factoryName);
}
