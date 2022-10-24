package com.asiainfo.lcims.omc.model.maintool;

import com.asiainfo.lcims.omc.util.Constant;

/**
 * radius 一键直通
 * @author zhul
 *
 */
public class RadiusResp {
    private String uuid;
    private String host_ip; 
    private int operate_type; // 操作类型
    private int operate_state; //操作结果
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getHost_ip() {
        return host_ip;
    }
    public void setHost_ip(String host_ip) {
        this.host_ip = host_ip;
    }
    public int getOperate_type() {
        return operate_type;
    }
    public void setOperate_type(int operate_type) {
        this.operate_type = operate_type;
    }
    public int getOperate_state() {
        return operate_state;
    }
    public void setOperate_state(int operate_state) {
        this.operate_state = operate_state;
    }
    
    /**
     * 
     * @param line 1|||874b36b1-6e6d-4d66-8aba-16ec49599ef9|||4.4.4.4|||0
     * @return
     */
    public static RadiusResp createRadiusResp(String line) {
        if(line.contains(Constant.RADIUS_OPERATE_END)) {
            line = line.replaceAll(Constant.RADIUS_OPERATE_END, "");
        }
        RadiusResp resp = new RadiusResp();
        String [] arr = line.split(Constant.RADIUS_OPERATE_SPLIT);
        resp.setOperate_type(Integer.parseInt(arr[0]));
        resp.setUuid(arr[1]);
        resp.setHost_ip(arr[2]);
        resp.setOperate_state(Integer.parseInt(arr[3]));
        return resp;
    }
    
}
