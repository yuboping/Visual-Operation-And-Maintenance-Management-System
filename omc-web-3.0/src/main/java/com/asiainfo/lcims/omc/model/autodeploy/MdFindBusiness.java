package com.asiainfo.lcims.omc.model.autodeploy;

import com.asiainfo.lcims.omc.util.ToolsUtils;

/**
 * 对应业务发现配置表MD_FIND_BUSINESS
 * @author zhul
 *
 */
public class MdFindBusiness {
    private String  business_tag;
    private String  process_key;
    private String  process_params;
    
    public String getBusiness_tag() {
        return business_tag;
    }
    public void setBusiness_tag(String business_tag) {
        this.business_tag = business_tag;
    }
    public String getProcess_key() {
        return process_key;
    }
    public void setProcess_key(String process_key) {
        this.process_key = process_key;
    }
    
    public String getProcess_params() {
        return process_params;
    }
    public void setProcess_params(String process_params) {
        this.process_params = process_params;
    }
    
    public String getProcessParam(){
        if(ToolsUtils.StringIsNull(process_params))
            return process_key;
        return process_params;
    }
}
