package com.asiainfo.lcims.omc.model.gdcu.obs.resp;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 在线用户统计---根据basip查询用户在线数
 * 
 * @author luohuawuyin
 *
 */
public class CheckBasUserResp extends BaseData {
    private String serialno;
    private String returncode;
    private String errordescription;
    private Map<String, Integer> numinfo;
    private Integer total;

    public CheckBasUserResp() {

    }

    public CheckBasUserResp(String serialno, String returncode, String errordescription) {
        this.serialno = serialno;
        this.returncode = returncode;
        this.errordescription = errordescription;
    }
    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public String getErrordescription() {
        return errordescription;
    }

    public void setErrordescription(String errordescription) {
        this.errordescription = errordescription;
    }

    public Map<String, Integer> getNuminfo() {
        return numinfo;
    }

    public void setNuminfo(String numinfo) {
        if (log.isDebugEnabled()) {
            log.info("numinfo=" + numinfo);
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < numinfo.length(); i++) {
            if (numinfo.charAt(i) == '{') {
                stack.push(new Integer(i));
            }
            if (numinfo.charAt(i) == '}') {
                int begin = stack.pop().intValue();
                try {
                    String value = numinfo.substring(begin + 1, i);
                    String[] nums = value.split("=");
                    map.put(nums[0], Integer.parseInt(nums[1].trim()));
                } catch (Exception e) {
                    log.error("解析根据basip查询用户在线数接口响应失败", e);
                }
            }
        }
        this.numinfo = map;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
