package com.asiainfo.lcims.omc.flowmonitor.common;

public interface FlowMonitorParam {
    public static final String FLOW_NODE_BEGIN = "TASK_BEGIN";// 流程开始的默认节点名字
    public static final String FLOW_NODE_END = "TASK_END";// 流程结束的默认节点名字
    public static final String FLOW_NODE_RESULT = "RESULT";// 流程执行结果

    public static final String RESULT_FLAG_SUCCESS = "SUCCESS";// 执行结果_成功
    public static final String RESULT_FLAG_FAILED = "FAILED";// 执行结果_失败
    public static final String RESULT_FLAG_RUNNING = "RUNNING";// 执行结果_失败

    public static final String FLOW_TYPE_FLOW = "FLOW";//
    public static final String FLOW_TYPE_NODE = "NODE";//
    public static final String FLOW_TYPE_ACTION = "ACTION";//
    public static final String FLOW_TYPE_DETAIL = "DETAIL";//

    public static final String ACTION_TYPE_SLEEP = "SLEEP";
    public static final String ACTION_TYPE_LOCAL_SHELL = "LOCAL_SHELL";
    public static final String ACTION_TYPE_SEND_SHELL = "SEND_SHELL";

    public static final String FLOW_END_FLAG = "1";
    
    public static final String BREAK_FLAG_SUCCESS_BREAK = "1";
    public static final String BREAK_FLAG_FAILED_BREAK = "2";
    public static final String BREAK_FLAG_WAIT_END = "3";
}
