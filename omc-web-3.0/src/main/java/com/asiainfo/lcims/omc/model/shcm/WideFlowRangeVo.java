package com.asiainfo.lcims.omc.model.shcm;

public class WideFlowRangeVo {

    private String input_v4;    //Ipv4上行流量

    private String output_v4;   //Ipv4下行流量

    private String total_v4;   //Ipv4总流量

    private String input_v6;   //Ipv6下行流量

    private String output_v6;   //Ipv6下行流量

    private String total_v6;   //Ipv6总流量

    private String stime;

    public String getInput_v4() {
        return input_v4;
    }

    public void setInput_v4(String input_v4) {
        this.input_v4 = input_v4;
    }

    public String getOutput_v4() {
        return output_v4;
    }

    public void setOutput_v4(String output_v4) {
        this.output_v4 = output_v4;
    }

    public String getTotal_v4() {
        return total_v4;
    }

    public void setTotal_v4(String total_v4) {
        this.total_v4 = total_v4;
    }

    public String getInput_v6() {
        return input_v6;
    }

    public void setInput_v6(String input_v6) {
        this.input_v6 = input_v6;
    }

    public String getOutput_v6() {
        return output_v6;
    }

    public void setOutput_v6(String output_v6) {
        this.output_v6 = output_v6;
    }

    public String getTotal_v6() {
        return total_v6;
    }

    public void setTotal_v6(String total_v6) {
        this.total_v6 = total_v6;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }
}
