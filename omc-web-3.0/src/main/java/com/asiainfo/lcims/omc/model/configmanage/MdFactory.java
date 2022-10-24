package com.asiainfo.lcims.omc.model.configmanage;

/**
 * 表MD_FACTORY对应的JAVA类
 * 
 * @author zhujiansheng
 * @date 2018年7月25日 上午10:15:35
 * @version V1.0
 */
public class MdFactory {

    private String id;

    private String factory_name;

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFactory_name() {
        return factory_name;
    }

    public void setFactory_name(String factory_name) {
        this.factory_name = factory_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MdFactory [id=" + id + ", factory_name=" + factory_name + ", description="
                + description + "]";
    }

}
