package com.asiainfo.lcims.omc.model;

/**
 * 返回信息，包括option
 * 
 */
public class WebResult {

    public static final WebResult OPER_SUCC = new WebResult();
    /** 操作成功标识 默认为true */
    private boolean opSucc = true;

    private String message;

    /**
     * 数据对象
     */
    private Object data;

    /** Construct ResultMsg for Operation Success */
    private WebResult() {
        opSucc = true;
    }

    /** Construct ResultMsg for Operation Fail */
    public WebResult(boolean opflag, String message) {
        this.opSucc = opflag;
        this.message = message;
    }

    public WebResult(boolean opflag, String message, Object optiondata) {
        this.opSucc = opflag;
        this.message = message;
        this.data = optiondata;
    }

    /**
     * 创建操作失败的消息
     * 
     * @param message
     *            操作失败的提示信息
     * @return Fail resultMsg with <code>message</code>
     */
    public static final WebResult createFailedMsg(String message) {
        return new WebResult(false, message);
    }

    /**
     * 判断当前操作是否成功，操作失败可以通过{@link #getMessage()}提取操作信息
     * 
     * @return true,操作成功;false,操作失败
     */
    public boolean operSucc() {
        return opSucc;
    }

    /**
     * 判断当前操作是否成功，操作失败可以通过{@link #getMessage()}提取操作信息
     * 
     * @return true,操作失败;false,操作成功
     */
    public boolean operFail() {
        return !operSucc();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOpSucc() {
        return opSucc;
    }

    public void setOpSucc(boolean opSucc) {
        this.opSucc = opSucc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
