package com.asiainfo.lcims.omc.model;

/**
 * 返回信息，用于业务校验对象信息返回
 * 
 * @author sillywolf
 * 
 */
public class ResultMsg {

    private static final String DEFAULT_MESSAGE = "Ops , The message is not set!";

    public static final ResultMsg OPER_SUCC = new ResultMsg();
    /** 操作成功标识 默认为true */
    private boolean opSucc = true;

    private String message = DEFAULT_MESSAGE;

    /** Construct ResultMsg for Operation Success */
    private ResultMsg() {
        opSucc = true;
    }

    /** Construct ResultMsg for Operation Fail */
    public ResultMsg(boolean opflag, String message) {
        this.opSucc = opflag;
        this.message = message;
    }

    /**
     * 创建操作失败的消息
     * 
     * @param message
     *            操作失败的提示信息
     * @return Fail resultMsg with <code>message</code>
     */
    public static final ResultMsg createFailedMsg(String message) {
        return new ResultMsg(false, message);
    }

    /**
     * 判断当前操作是否成功，操作失败可以通过{@link #getMessage()}提取操作信息
     * 
     * @return true,操作失败;false,操作成功
     */
    public boolean operFail() {
        return !isOpSucc();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 判断当前操作是否成功，操作失败可以通过{@link #getMessage()}提取操作信息
     * 
     * @return true,操作成功;false,操作失败
     */
    public boolean isOpSucc() {
        return opSucc;
    }

}
