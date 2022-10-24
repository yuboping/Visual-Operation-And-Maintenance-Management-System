package com.asiainfo.lcims.omc.alarm.model;

import java.util.ArrayList;
import java.util.List;

import com.asiainfo.lcims.omc.alarm.param.SendMode;
import com.asiainfo.lcims.util.ToolsUtils;

/**
 * 告警方式定义表MD_ALARM_MODE
 * 
 * @author luohuawuyin
 *
 */
public class AlarmMode {
    private String modeid;
    private String modename;
    private String modeattr;
    private Integer modetype;
    
    private List<Object> addrs;
    
    public String getModename() {
        return modename;
    }

    public void setModename(String modename) {
        this.modename = modename;
    }

    public String getModeattr() {
        return modeattr;
    }

    public void setModeattr(String modeattr) {
        this.modeattr = modeattr;
        if (this.modetype == SendMode.SYSLOG.getType()
                || this.modetype == SendMode.SNMPTRAP.getType()
                || this.modetype == SendMode.REPORTNMS.getType()) {
            this.addrs = analysisAddr(modeattr);
        } else {
            this.addrs = analysisEmailAddr(modeattr);
        }
    }

    public List<Object> getAddrs() {
        return addrs;
    }

    public void setAddrs(List<Object> addrs) {
        this.addrs = addrs;
    }

    public Integer getModetype() {
        return modetype;
    }

    public void setModetype(Integer modetype) {
        this.modetype = modetype;
    }

    /**
     * modeattr格式：132.96.231.102:514;132.96.231.103:514
     * 以双引号分割，ip1:port1;ip2:port2
     * @param am
     * @return
     */
    private List<Object> analysisAddr(String attrs) {
        List<Object> list = new ArrayList<Object>();
        if (attrs == null || "".equals(attrs.trim())) {
            return list;
        }
        String[] addrs = attrs.split(";");
        for (String addr : addrs) {
        	//addr 是否符合 syslog ip:端口 组成规范
        	if(isSyslogAddr(addr)){
        		String[] values = addr.split(":");
                SendAddress send = new SendAddress(values[0], Integer.parseInt(values[1]));
                list.add(send);
        	}
        }
        return list;
    }
    
    /**
     * 判断syslog地址是否符合规范
     * @param addr
     * @return
     */
    private boolean isSyslogAddr(String addr){
    	boolean flag = false;
    	if(ToolsUtils.isSyslogAddr(addr))
    		return true;
    	return flag;
    }
    
    private List<Object> analysisEmailAddr(String attrs) {
        List<Object> list = new ArrayList<Object>();
        if (attrs == null || "".equals(attrs.trim())) {
            return list;
        }
        String[] addrs = attrs.split(";");
        for (String addr : addrs) {
            list.add(addr);
        }
        return list;
    }

	public String getModeid() {
		return modeid;
	}

	public void setModeid(String modeid) {
		this.modeid = modeid;
	}
}
