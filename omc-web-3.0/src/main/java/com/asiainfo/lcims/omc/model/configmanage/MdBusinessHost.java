package com.asiainfo.lcims.omc.model.configmanage;

/**
 * 
 * @ClassName: MdBusinessHost
 * @Description: TODO(主机业务关系表)
 * @author yubp@asiainfo-sec.com
 * @date 2018年8月10日 下午3:57:22
 *
 */
public class MdBusinessHost {

    private String id;

    private String name;

    private String hostid;

    private String hostip;

    private String business_link;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostid() {
        return hostid;
    }

    public void setHostid(String hostid) {
        this.hostid = hostid;
    }

    public String getHostip() {
        return hostip;
    }

    public void setHostip(String hostip) {
        this.hostip = hostip;
    }

    public String getBusiness_link() {
        return business_link;
    }

    public void setBusiness_link(String business_link) {
        this.business_link = business_link;
    }
}