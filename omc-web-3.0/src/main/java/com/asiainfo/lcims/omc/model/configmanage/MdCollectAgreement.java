package com.asiainfo.lcims.omc.model.configmanage;

import java.util.List;

public class MdCollectAgreement {
    private String id;

    private String protocol_identity;

    private String protocol_name;

    private Integer cycle_id;

    private String script;

    private String script_param;

    private Integer script_return_type;

    private String protocol_type;

    private String description;

    private String cyclename;

    private String metric_type_name;

    private String mdparamdescription;

    private String server_type;

    private List<String> metric_attr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProtocol_identity() {
        return protocol_identity;
    }

    public void setProtocol_identity(String protocol_identity) {
        this.protocol_identity = protocol_identity;
    }

    public String getProtocol_name() {
        return protocol_name;
    }

    public void setProtocol_name(String protocol_name) {
        this.protocol_name = protocol_name;
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

    public String getProtocol_type() {
        return protocol_type;
    }

    public void setProtocol_type(String protocol_type) {
        this.protocol_type = protocol_type;
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

    public String getServer_type() {
        return server_type;
    }

    public void setServer_type(String server_type) {
        this.server_type = server_type;
    }

    public List<String> getMetric_attr() {
        return metric_attr;
    }

    public void setMetric_attr(List<String> metric_attr) {
        this.metric_attr = metric_attr;
    }
}
