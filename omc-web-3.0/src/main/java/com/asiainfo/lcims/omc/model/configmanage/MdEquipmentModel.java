package com.asiainfo.lcims.omc.model.configmanage;

/**
 * 表MD_EQUIPMENT_MODEL对应的JAVA类
 * 
 * @author zhujiansheng
 * @date 2018年7月25日 下午4:54:05
 * @version V1.0
 */
public class MdEquipmentModel {

    private String id;

    private String model_name;

    private String factory_id;

    private String factory_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getFactory_id() {
        return factory_id;
    }

    public void setFactory_id(String factory_id) {
        this.factory_id = factory_id;
    }

    public String getFactory_name() {
        return factory_name;
    }

    public void setFactory_name(String factory_name) {
        this.factory_name = factory_name;
    }

    @Override
    public String toString() {
        return "MdEquipmentModel [id=" + id + ", model_name=" + model_name + ", factory_id="
                + factory_id + ", factory_name=" + factory_name + "]";
    }

}
