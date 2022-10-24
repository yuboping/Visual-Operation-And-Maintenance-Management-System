package com.asiainfo.lcims.omc.model.configmanage;

public class MdHostMetric {
    private String id;
    private String host_id;
    private String hostname;
    private String addr;
    private String metric_id;
    private String metric_name;
    private Integer cycle_id;
    private String  cyclename;
    private String cron;
    private String cycle_description;
    private String script;
    private String script_param;
    private Integer script_return_type;
    private String returntypename;
    private String metric_type;// MD_METRIC中字段,通过ID关联
    
    /** 状态 1:已下发;0:未下发, 3:待删除 */
    private Integer state;
    /** 状态名称 */
    private String statename;
    /** -1 表示未匹配 */
    private String flag;
    
    private String update_time;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMetric_name() {
        return metric_name;
    }

    public void setMetric_name(String metric_name) {
        this.metric_name = metric_name;
    }

    public Integer getCycle_id() {
        return cycle_id;
    }

    public void setCycle_id(Integer cycle_id) {
        this.cycle_id = cycle_id;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScript_param() {
        return script_param;
    }

    public void setScript_param(String script_param) {
        this.script_param = script_param;
    }

    public Integer getScript_return_type() {
        return script_return_type;
    }

    public void setScript_return_type(Integer script_return_type) {
        this.script_return_type = script_return_type;
    }

	public String getHost_id() {
		return host_id;
	}

	public void setHost_id(String host_id) {
		this.host_id = host_id;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getMetric_id() {
		return metric_id;
	}

	public void setMetric_id(String metric_id) {
		this.metric_id = metric_id;
	}

	public String getCyclename() {
		return cyclename;
	}

	public void setCyclename(String cyclename) {
		this.cyclename = cyclename;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStatename() {
		return statename;
	}

	public void setStatename(String statename) {
		this.statename = statename;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getCycle_description() {
		return cycle_description;
	}

	public void setCycle_description(String cycle_description) {
		this.cycle_description = cycle_description;
	}

	public String getReturntypename() {
		return returntypename;
	}

	public void setReturntypename(String returntypename) {
		this.returntypename = returntypename;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

    public String getMetric_type() {
        return metric_type;
    }

    public void setMetric_type(String metric_type) {
        this.metric_type = metric_type;
    }
}