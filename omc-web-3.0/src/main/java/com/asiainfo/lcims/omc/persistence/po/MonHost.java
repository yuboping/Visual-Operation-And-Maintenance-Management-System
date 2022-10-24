package com.asiainfo.lcims.omc.persistence.po;

public class MonHost {
    private String hostid;
    private String addr;
    private String hostname;
    private String nodeid;
    private Integer status;
    private String os;
    private String provider;
    private String cpu;
    private String memory;
    private String diskspace;
    private String productname;
    private Integer moduleid;
    private String hosttype;// 主机类型，对应md_param表中type=2的内容
    private String hosttypename;//主机类型名称,用于页面显示
    private String nodeidname;//节点类型名称,用于页面显示
    private int alarmnum;//告警数量
    
    private String serialnumber;//序列号
    private String ipv4;//IPV4地址信息，包含公网、内网地址
    private String ipv6;//IPV6地址信息:默认为空
    private String host_room;//机房位置
    private String rack_num;//机柜号
    
    private String networkType = "host";
    
    private String location;

    private String ips;
    private String vmid;
    /**
     * 自动部署新增字段
     */
    private String ssh_user;
    private String ssh_password;
    private Integer ssh_port;
    private Integer deploy_status;
    private String deploy_des;
    private String deploy_status_name;

    /**
     * 采集机管理新增增字段
     * @return
     */
    private Integer is_collect;
    
    public String getHostid() {
        return hostid;
    }

    public void setHostid(String hostid) {
        this.hostid = hostid;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getDiskspace() {
        return diskspace;
    }

    public void setDiskspace(String diskspace) {
        this.diskspace = diskspace;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public Integer getModuleid() {
        return moduleid;
    }

    public void setModuleid(Integer moduleid) {
        this.moduleid = moduleid;
    }

    public String getHosttype() {
        return hosttype;
    }

    public void setHosttype(String hosttype) {
        this.hosttype = hosttype;
    }

    public String getHosttypename() {
        return hosttypename;
    }

    public void setHosttypename(String hosttypename) {
        this.hosttypename = hosttypename;
    }

    public String getNodeidname() {
        return nodeidname;
    }

    public void setNodeidname(String nodeidname) {
        this.nodeidname = nodeidname;
    }

	public int getAlarmnum() {
		return alarmnum;
	}

	public void setAlarmnum(int alarmnum) {
		this.alarmnum = alarmnum;
	}

	public String getIpv4() {
		return ipv4;
	}

	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}

	public String getIpv6() {
		return ipv6;
	}

	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}

	public String getHost_room() {
		return host_room;
	}

	public void setHost_room(String host_room) {
		this.host_room = host_room;
	}

	public String getRack_num() {
		return rack_num;
	}

	public void setRack_num(String rack_num) {
		this.rack_num = rack_num;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

    public String getSsh_user() {
        return ssh_user;
    }

    public void setSsh_user(String ssh_user) {
        this.ssh_user = ssh_user;
    }

    public String getSsh_password() {
        return ssh_password;
    }

    public void setSsh_password(String ssh_password) {
        this.ssh_password = ssh_password;
    }

    public Integer getSsh_port() {
        return ssh_port;
    }

    public void setSsh_port(Integer ssh_port) {
        this.ssh_port = ssh_port;
    }

    public Integer getDeploy_status() {
        return deploy_status;
    }

    public void setDeploy_status(Integer deploy_status) {
        this.deploy_status = deploy_status;
    }

    public String getDeploy_des() {
        return deploy_des;
    }

    public void setDeploy_des(String deploy_des) {
        this.deploy_des = deploy_des;
    }

    public String getDeploy_status_name() {
        return deploy_status_name;
    }

    public void setDeploy_status_name(String deploy_status_name) {
        this.deploy_status_name = deploy_status_name;
    }

    @Override
    public String toString() {
        return "MonHost{" +
                "hostid='" + hostid + '\'' +
                ", addr='" + addr + '\'' +
                ", hostname='" + hostname + '\'' +
                ", nodeid='" + nodeid + '\'' +
                ", status=" + status +
                ", os='" + os + '\'' +
                ", provider='" + provider + '\'' +
                ", cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                ", diskspace='" + diskspace + '\'' +
                ", productname='" + productname + '\'' +
                ", moduleid=" + moduleid +
                ", hosttype='" + hosttype + '\'' +
                ", hosttypename='" + hosttypename + '\'' +
                ", nodeidname='" + nodeidname + '\'' +
                ", alarmnum=" + alarmnum +
                ", serialnumber='" + serialnumber + '\'' +
                ", ipv4='" + ipv4 + '\'' +
                ", ipv6='" + ipv6 + '\'' +
                ", host_room='" + host_room + '\'' +
                ", rack_num='" + rack_num + '\'' +
                ", networkType='" + networkType + '\'' +
                ", location='" + location + '\'' +
                ", ssh_user='" + ssh_user + '\'' +
                ", ssh_password='" + ssh_password + '\'' +
                ", ssh_port=" + ssh_port +
                ", deploy_status=" + deploy_status +
                ", deploy_des='" + deploy_des + '\'' +
 ", deploy_status_name='" + deploy_status_name + '\'' +
                '}';
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public String getVmid() {
        return vmid;
    }

    public void setVmid(String vmid) {
        this.vmid = vmid;
    }

    public Integer getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(Integer is_collect) {
        this.is_collect = is_collect;
    }
}
