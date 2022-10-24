package com.asiainfo.lcims.omc.model.radius;

public class BusinessData {

    private String logTime;

    private String hostName;

    private String passwdError;

    private String userNotFound;

    private String lmError;

    private String bindError;

    private String lockError;

    private String other;

    private String authCount;

    private String authOk;

    private String logTimeYmd;

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPasswdError() {
        return passwdError;
    }

    public void setPasswdError(String passwdError) {
        this.passwdError = passwdError;
    }

    public String getUserNotFound() {
        return userNotFound;
    }

    public void setUserNotFound(String userNotFound) {
        this.userNotFound = userNotFound;
    }

    public String getLmError() {
        return lmError;
    }

    public void setLmError(String lmError) {
        this.lmError = lmError;
    }

    public String getBindError() {
        return bindError;
    }

    public void setBindError(String bindError) {
        this.bindError = bindError;
    }

    public String getLockError() {
        return lockError;
    }

    public void setLockError(String lockError) {
        this.lockError = lockError;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getAuthCount() {
        return authCount;
    }

    public void setAuthCount(String authCount) {
        this.authCount = authCount;
    }

    public String getAuthOk() {
        return authOk;
    }

    public void setAuthOk(String authOk) {
        this.authOk = authOk;
    }

    public String getLogTimeYmd() {
        return logTimeYmd;
    }

    public void setLogTimeYmd(String logTimeYmd) {
        this.logTimeYmd = logTimeYmd;
    }
}
