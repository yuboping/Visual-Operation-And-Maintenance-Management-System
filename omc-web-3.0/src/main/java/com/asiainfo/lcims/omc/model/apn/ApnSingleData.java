package com.asiainfo.lcims.omc.model.apn;

public class ApnSingleData {

    private String hostId;

    private String metricId;

    private int mvalue;

    private String item;

    private String stime;

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public int getMvalue() {
        return mvalue;
    }

    public void setMvalue(int mvalue) {
        this.mvalue = mvalue;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    @Override
    public String toString() {
        return "ApnSingleData [hostId=" + hostId + ", metricId=" + metricId + ", mvalue=" + mvalue
                + ", item=" + item + ", stime=" + stime + "]";
    }

}
