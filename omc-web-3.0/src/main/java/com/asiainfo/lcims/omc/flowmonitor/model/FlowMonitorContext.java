package com.asiainfo.lcims.omc.flowmonitor.model;

import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowAction;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowDetail;

public class FlowMonitorContext {
    public FlowMonitorContext() {
    }

    public FlowMonitorContext(String taskId, String serialNum) {
        this.taskId = taskId;
        this.serialNum = serialNum;
    }

    private String isEnd;

    private String serialNum;
    private String taskId;
    private String nodeId;

    private MdFlowAction action;
    private MdFlowDetail detail;

    private String resultFlag;
    private String resultData;

    public String getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(String resultFlag) {
        this.resultFlag = resultFlag;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public MdFlowAction getAction() {
        return action;
    }

    public void setAction(MdFlowAction action) {
        this.action = action;
    }

    public MdFlowDetail getDetail() {
        return detail;
    }

    public void setDetail(MdFlowDetail detail) {
        this.detail = detail;
    }
}
