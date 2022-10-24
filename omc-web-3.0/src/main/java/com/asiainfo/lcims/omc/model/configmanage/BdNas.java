package com.asiainfo.lcims.omc.model.configmanage;

/**
 * BD_NAS表对应的JAVA类
 * 
 * @author zhujiansheng
 * @date 2018年8月8日 下午3:22:28
 * @version V1.0
 */
public class BdNas {

    private String id;

    private String nas_name;

    private String nas_ip;

    private String equip_id;

    private Integer access_type;

    private String factory_id;

    private String area_no;

    private Integer decode_mode;

    private Integer ip_type;

    private String description;

    private String nas_type_name;

    private String access_type_name;

    private String decode_mode_name;

    private String ip_type_name;

    // 用于展示
    private String iptype;

    private String modelname;

    private String factoryname;

    private String areaname;

    private String otype;

    private String factory_name;

    private String equip_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getAccess_type() {
        return access_type;
    }

    public void setAccess_type(Integer access_type) {
        this.access_type = access_type;
    }

    public Integer getDecode_mode() {
        return decode_mode;
    }

    public void setDecode_mode(Integer decode_mode) {
        this.decode_mode = decode_mode;
    }

    public Integer getIp_type() {
        return ip_type;
    }

    public void setIp_type(Integer ip_type) {
        this.ip_type = ip_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNas_type_name() {
        return nas_type_name;
    }

    public void setNas_type_name(String nas_type_name) {
        this.nas_type_name = nas_type_name;
    }

    public String getAccess_type_name() {
        return access_type_name;
    }

    public void setAccess_type_name(String access_type_name) {
        this.access_type_name = access_type_name;
    }

    public String getDecode_mode_name() {
        return decode_mode_name;
    }

    public void setDecode_mode_name(String decode_mode_name) {
        this.decode_mode_name = decode_mode_name;
    }

    public String getIp_type_name() {
        return ip_type_name;
    }

    public void setIp_type_name(String ip_type_name) {
        this.ip_type_name = ip_type_name;
    }

    public String getEquip_id() {
        return equip_id;
    }

    public void setEquip_id(String equip_id) {
        this.equip_id = equip_id;
    }

    public String getFactory_id() {
        return factory_id;
    }

    public void setFactory_id(String factory_id) {
        this.factory_id = factory_id;
    }

    public String getArea_no() {
        return area_no;
    }

    public void setArea_no(String area_no) {
        this.area_no = area_no;
    }

    public String getIptype() {
        return iptype;
    }

    public void setIptype(String iptype) {
        this.iptype = iptype;
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

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getFactory_name() {
        return factory_name;
    }

    public void setFactory_name(String factory_name) {
        this.factory_name = factory_name;
    }

    public String getEquip_name() {
        return equip_name;
    }

    public void setEquip_name(String equip_name) {
        this.equip_name = equip_name;
    }
}