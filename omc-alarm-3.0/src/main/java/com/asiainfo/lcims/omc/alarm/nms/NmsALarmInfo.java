package com.asiainfo.lcims.omc.alarm.nms;

public class NmsALarmInfo {
    private int alarmSeq;
    private String alarmTitle;
    private int alarmStatus;
    private String alarmType;
    private int origSeverity;
    private String eventTime;
    private String alarmId;
    private String specificProblemID;
    private String specificProblem;
    private String neUID;
    private String neName;
    private String neType;
    private String objectUID;
    private String objectName;
    private String objectType;
    private String locationInfo;
    private String addInfo;
    public int getAlarmSeq() {
        return alarmSeq;
    }
    public void setAlarmSeq(int alarmSeq) {
        this.alarmSeq = alarmSeq;
    }
    public String getAlarmTitle() {
        return alarmTitle;
    }
    public void setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
    }
    public int getAlarmStatus() {
        return alarmStatus;
    }
    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }
    public String getAlarmType() {
        return alarmType;
    }
    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }
    public int getOrigSeverity() {
        return origSeverity;
    }
    public void setOrigSeverity(int origSeverity) {
        this.origSeverity = origSeverity;
    }
    public String getEventTime() {
        return eventTime;
    }
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
    public String getAlarmId() {
        return alarmId;
    }
    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }
    public String getSpecificProblemID() {
        return specificProblemID;
    }
    public void setSpecificProblemID(String specificProblemID) {
        this.specificProblemID = specificProblemID;
    }
    public String getSpecificProblem() {
        return specificProblem;
    }
    public void setSpecificProblem(String specificProblem) {
        this.specificProblem = specificProblem;
    }
    public String getNeUID() {
        return neUID;
    }
    public void setNeUID(String neUID) {
        this.neUID = neUID;
    }
    public String getNeName() {
        return neName;
    }
    public void setNeName(String neName) {
        this.neName = neName;
    }
    public String getNeType() {
        return neType;
    }
    public void setNeType(String neType) {
        this.neType = neType;
    }
    public String getObjectUID() {
        return objectUID;
    }
    public void setObjectUID(String objectUID) {
        this.objectUID = objectUID;
    }
    public String getObjectName() {
        return objectName;
    }
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    public String getObjectType() {
        return objectType;
    }
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
    public String getLocationInfo() {
        return locationInfo;
    }
    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }
    public String getAddInfo() {
        return addInfo;
    }
    public void setAddInfo(String addInfo) {
        this.addInfo = addInfo;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{")
              .append("\"alarmSeq\":"+alarmSeq+",")
              .append("\"alarmTitle\":\""+alarmTitle+"\",")
              .append("\"alarmStatus\":"+alarmStatus+",")
              .append("\"alarmType\":\""+alarmType+"\",")
              .append("\"origSeverity\":"+origSeverity+",")
              .append("\"eventTime\":\""+eventTime+"\",")
              .append("\"alarmId\":\""+alarmId+"\",")
              .append("\"specificProblemID\":\""+specificProblemID+"\",")
              .append("\"specificProblem\":\""+specificProblem+"\",")
              .append("\"neUID\":\""+neUID+"\",")
              .append("\"neName\":\""+neName+"\",")
              .append("\"neType\":\""+neType+"\",")
              .append("\"objectUID\":\""+objectUID+"\",")
              .append("\"objectName\":\""+objectName+"\",")
              .append("\"objectType\":\""+objectType+"\",")
              .append("\"locationInfo\":\""+locationInfo+"\",")
              .append("\"addInfo\":\""+addInfo+"\"");
        buffer.append("}");
        return buffer.toString();
    }
    
    public static void main(String[] args) {
        NmsALarmInfo nmsALarmInfo = new NmsALarmInfo();
        System.out.println(nmsALarmInfo.toString());
    }
}
