package com.asiainfo.lcims.omc.agentserver.enity;

import java.util.ArrayList;
import java.util.List;

/**
 * radius 一键直通
 * @author zhul
 *
 */
public class RadiusReq {
    private String uuid;
    private List<String> ips; 
    private int operate_type; // 操作类型
    private String info;
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public int getOperate_type() {
        return operate_type;
    }
    public void setOperate_type(int operate_type) {
        this.operate_type = operate_type;
    }
    public List<String> getIps() {
        return ips;
    }
    public void setIps(List<String> ips) {
        this.ips = ips;
    }
    
    /**
     * 
     * @param info 数据格式： 1|||874b36b1-6e6d-4d66-8aba-16ec49599ef9|||1.1.1.1;2.2.2.2;3.3.3.3;4.4.4.4
     * @return
     */
    public static RadiusReq createRadiusReq(String info) {
        if(info.startsWith("/n")) {
            info = info.replaceAll("\n", "");
        }
        RadiusReq req = new RadiusReq();
        req.setInfo(info);
        String [] arr = info.split("\\|\\|\\|");
        req.setOperate_type(Integer.parseInt(arr[0]));
        req.setUuid(arr[1]);
        List<String> ipList = new ArrayList<String>();
        String [] ips = arr[2].split(";");
        for (String ip : ips) {
            ipList.add(ip);
        }
        req.setIps(ipList);
        return req;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
}
