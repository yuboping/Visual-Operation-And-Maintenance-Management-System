package com.asiainfo.lcims.omc.report.model;

/**
 * 表MD_RESOURCE_OMC对应的JAVA类
 * 
 * @author zhujiansheng
 * @version V1.0
 */
public class ResourceOmcData {

    private String nativeName;

    private String vendor;

    private String equipmemtDomain;

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getEquipmemtDomain() {
        return equipmemtDomain;
    }

    public void setEquipmemtDomain(String equipmemtDomain) {
        this.equipmemtDomain = equipmemtDomain;
    }

    @Override
    public String toString() {
        return "ResourceOmcData [nativeName=" + nativeName + ", vendor=" + vendor
                + ", equipmemtDomain=" + equipmemtDomain + "]";
    }

}
