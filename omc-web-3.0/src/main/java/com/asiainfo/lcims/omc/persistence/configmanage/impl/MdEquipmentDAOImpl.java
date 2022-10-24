package com.asiainfo.lcims.omc.persistence.configmanage.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.model.configmanage.MdEquipmentModel;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;

public class MdEquipmentDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(MdEquipmentDAOImpl.class);

    public String getMdEquipment(Map<String, Object> paras) {
        MdEquipmentModel equipment = (MdEquipmentModel) paras.get("mdEquip");
        StringBuffer buffer = new StringBuffer(
                "SELECT T1.ID,T1.MODEL_NAME,T1.FACTORY_ID,T2.FACTORY_NAME FROM MD_EQUIPMENT_MODEL T1 LEFT JOIN MD_FACTORY T2 ON T1.FACTORY_ID=T2.ID WHERE 1=1");
        if (!StringUtils.isEmpty(equipment.getModel_name())) {
            String modelName = DbSqlUtil
                    .replaceSpecialStr(StringUtil.SqlFilter(equipment.getModel_name()));
            buffer.append(" AND T1.MODEL_NAME LIKE '%").append(modelName).append("%'");
        }
        if (!StringUtils.isEmpty(equipment.getFactory_id())) {
            String factoryId = DbSqlUtil
                    .replaceSpecialStr(StringUtil.SqlFilter(equipment.getFactory_id()));
            buffer.append(" AND T1.FACTORY_ID='").append(factoryId).append("'");
        }
        if (!StringUtils.isEmpty(equipment.getId())) {
            buffer.append(" AND T1.ID='").append(equipment.getId()).append("'");
        }
        LOG.debug("getMdEquipment sql = {}", buffer.toString());
        return buffer.toString();
    }

    public String addMdEquipment(Map<String, Object> paras) {
        MdEquipmentModel equipment = (MdEquipmentModel) paras.get("mdEquip");
        StringBuffer buffer = new StringBuffer(
                "INSERT INTO MD_EQUIPMENT_MODEL(ID,MODEL_NAME,FACTORY_ID) VALUES(");
        buffer.append(equipment.getId()).append(",");
        buffer.append(equipment.getModel_name()).append(",");
        buffer.append(equipment.getFactory_id()).append(")");
        LOG.debug("addMdEquipment sql = {}", buffer.toString());
        return buffer.toString();
    }

    public String updateMdEquipment(Map<String, Object> paras) {
        MdEquipmentModel equipment = (MdEquipmentModel) paras.get("mdEquip");
        StringBuffer buffer = new StringBuffer("UPDATE MD_EQUIPMENT_MODEL SET ID=ID");
        if (!StringUtils.isEmpty(equipment.getModel_name())) {
            buffer.append(",MODEL_NAME='").append(equipment.getModel_name()).append("'");
        }
        if (!StringUtils.isEmpty(equipment.getFactory_id())) {
            buffer.append(",FACTORY_ID='").append(equipment.getFactory_id()).append("'");
        }
        buffer.append(" WHERE ID='").append(equipment.getId()).append("'");
        LOG.debug("updateMdEquipment sql = {}", buffer.toString());
        return buffer.toString();
    }
}
