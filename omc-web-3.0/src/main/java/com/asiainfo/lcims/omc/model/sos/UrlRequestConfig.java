package com.asiainfo.lcims.omc.model.sos;

public class UrlRequestConfig {
    private String type;
    private String requestType;
    private String url;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getRequestType() {
        return requestType;
    }
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    
    @Override
    public String toString() {
        return "{type:"+type+",requestType:"+requestType+",url:"+url+"}";
    }
}
