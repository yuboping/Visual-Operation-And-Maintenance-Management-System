package com.asiainfo.lcims.omc.model.configmanage;

/**
 * 导出Excel需要的属性
 * 
 * @author zhujiansheng
 * @date 2018年8月20日 下午2:29:18
 * @version V1.0
 */
public class MdBras {

    private String nas_name;
    
    private String nas_ip;
    
    private String modelname;
    
    private String factoryname;
    
    private String areaname;
    
    private String iptype;
    
    private String description;

    public String getNas_name() {
        return nas_name;
    }

    public void setNas_name(String nas_name) {
        this.nas_name = nas_name;
    }

    public String getNas_ip() {
        return nas_ip;
    }

    public void setNas_ip(String nas_ip) {
        this.nas_ip = nas_ip;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getFactoryname() {
        return factoryname;
    }

    public void setFactoryname(String factoryname) {
        this.factoryname = factoryname;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getIptype() {
        return iptype;
    }

    public void setIptype(String iptype) {
        this.iptype = iptype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
