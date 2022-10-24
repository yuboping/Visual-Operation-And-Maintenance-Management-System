package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.MdEquipmentModel;
import com.asiainfo.lcims.omc.model.configmanage.MdFactory;
import com.asiainfo.lcims.omc.persistence.configmanage.impl.MdEquipmentDAOImpl;

/**
 * 设置型号管理DAO层
 * 
 * @author zhujiansheng
 * @date 2018年7月25日 下午5:17:52
 * @version V1.0
 */
public interface MdEquipmentDAO {

    @SelectProvider(method = "getMdEquipment", type = MdEquipmentDAOImpl.class)
    List<MdEquipmentModel> getMdEquipment(@Param("mdEquip") MdEquipmentModel mdEquip);
    
    @Insert("INSERT INTO MD_EQUIPMENT_MODEL(ID,MODEL_NAME,FACTORY_ID) VALUES(#{mdEquip.id},#{mdEquip.model_name},#{mdEquip.factory_id})")
    int insert(@Param("mdEquip") MdEquipmentModel mdEquip);

    @UpdateProvider(method = "updateMdEquipment", type = MdEquipmentDAOImpl.class)
    int update(@Param("mdEquip") MdEquipmentModel mdEquip);

    @Select("SELECT ID,FACTORY_NAME,DESCRIPTION FROM MD_FACTORY")
    List<MdFactory> getMdFactoryList();

    @Delete("DELETE FROM MD_EQUIPMENT_MODEL WHERE ID NOT IN (SELECT EQUIP_ID FROM BD_NAS WHERE EQUIP_ID IS NOT NULL) AND ID IN (#{equipid})")
    int delete(@Param("equipid") String equipid);

    @Select("SELECT ID,MODEL_NAME FROM MD_EQUIPMENT_MODEL WHERE ID = #{equipid}")
    MdEquipmentModel getOneEquipment(@Param("equipid") String equipid);

    @Select("SELECT ID,MODEL_NAME,FACTORY_ID FROM MD_EQUIPMENT_MODEL")
    List<MdEquipmentModel> getAll();

    @Select("SELECT ID,MODEL_NAME FROM MD_EQUIPMENT_MODEL WHERE MODEL_NAME = #{equipName} AND FACTORY_ID = #{factoryId}")
    MdEquipmentModel getEquipmentByName(@Param("equipName") String equipName,
            @Param("factoryId") String factoryId);
}
