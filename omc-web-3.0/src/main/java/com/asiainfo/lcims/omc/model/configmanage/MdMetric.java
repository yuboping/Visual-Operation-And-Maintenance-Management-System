package com.asiainfo.lcims.omc.model.configmanage;

import java.util.List;

public class MdMetric {
    private String id;

    private String metric_identity;

    private String metric_name;

    private Integer cycle_id;

    private String script;

    private String script_param;

    private Integer script_return_type;

    private String metric_type;

    private String description;

    private String cyclename;

    private String metric_type_name;

    private String mdparamdescription;
    
    private String server_type;

    private List<String> metric_attr;

    
    
    public String getServer_type() {
		return server_type;
	}

	public void setServer_type(String server_type) {
		this.server_type = server_type;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMetric_identity() {
        return metric_identity;
    }

    public void setMetric_identity(String metric_identity) {
        this.metric_identity = metric_identity;
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

    public String getMetric_type() {
        return metric_type;
    }

    public void setMetric_type(String metric_type) {
        this.metric_type = metric_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCyclename() {
        return cyclename;
    }

    public void setCyclename(String cyclename) {
        this.cyclename = cyclename;
    }

    public String getMetric_type_name() {
        return metric_type_name;
    }

    public void setMetric_type_name(String metric_type_name) {
        this.metric_type_name = metric_type_name;
    }

    public String getMdparamdescription() {
        return mdparamdescription;
    }

    public void setMdparamdescription(String mdparamdescription) {
        this.mdparamdescription = mdparamdescription;
    }

    public List<String> getMetric_attr() {
        return metric_attr;
    }

    public void setMetric_attr(List<String> metric_attr) {
        this.metric_attr = metric_attr;
    }
}