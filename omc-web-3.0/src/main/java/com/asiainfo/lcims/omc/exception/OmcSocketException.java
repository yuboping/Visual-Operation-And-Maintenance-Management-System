package com.asiainfo.lcims.omc.exception;

public class OmcSocketException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String errorMsg;

    public OmcSocketException(String errorMsg){
        super(errorMsg);
    }

    public OmcSocketException(String errorMsg, String errorCode){
        super(errorMsg);
        this.setErrorMsg(errorMsg);
        this.setErrorCode(errorCode);
    }

    public OmcSocketException(String errorMsg, String errorCode, Throwable cause){
        super(errorMsg, cause);
        this.setErrorMsg(errorMsg);
        this.setErrorCode(errorCode);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
