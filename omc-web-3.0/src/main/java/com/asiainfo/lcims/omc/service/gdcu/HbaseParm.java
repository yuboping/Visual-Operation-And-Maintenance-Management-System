package com.asiainfo.lcims.omc.service.gdcu;

public class HbaseParm {
    
    private String account;
    private String querydate;
    
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getQuerydate() {
        return querydate;
    }
    public void setQuerydate(String querydate) {
        this.querydate = querydate;
    }
    
    @Override
    public String toString() {
        return "[account="+account+"],[querydate="+querydate+"]";
    }
}
