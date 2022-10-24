package com.asiainfo.lcims.omc.alarm.http;

public class HttpALarmInfo {
    private String alarmSource;
    private String alarmId;
    private String alarmMsg;
    private String alarmLeve;
    private Integer isClear;
    private String occurTime;
    private String clearTime;
    private String neName;
    private String neIp;
    private Integer alarmType;
    private String equipmentType;
    private String LocateNeType;
    private String alarmText;
    private String mesCode;
    private String mesgDesc;

    public String getAlarmSource() {
        return alarmSource;
    }

    public void setAlarmSource(String alarmSource) {
        this.alarmSource = alarmSource;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmMsg() {
        return alarmMsg;
    }

    public void setAlarmMsg(String alarmMsg) {
        this.alarmMsg = alarmMsg;
    }

    public String getAlarmLeve() {
        return alarmLeve;
    }

    public void setAlarmLeve(String alarmLeve) {
        this.alarmLeve = alarmLeve;
    }

    public Integer getIsClear() {
        return isClear;
    }

    public void setIsClear(Integer isClear) {
        this.isClear = isClear;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getClearTime() {
        return clearTime;
    }

    public void setClearTime(String clearTime) {
        this.clearTime = clearTime;
    }

    public String getNeName() {
        return neName;
    }

    public void setNeName(String neName) {
        this.neName = neName;
    }

    public String getMesCode() {
        return mesCode;
    }

    public void setMesCode(String mesCode) {
        this.mesCode = mesCode;
    }

    public String getMesgDesc() {
        return mesgDesc;
    }

    public void setMesgDesc(String mesgDesc) {
        this.mesgDesc = mesgDesc;
    }

    public String getNeIp() {
        return neIp;
    }

    public void setNeIp(String neIp) {
        this.neIp = neIp;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getLocateNeType() {
        return LocateNeType;
    }

    public void setLocateNeType(String locateNeType) {
        LocateNeType = locateNeType;
    }

    public String getAlarmText() {
        return alarmText;
    }

    public void setAlarmText(String alarmText) {
        this.alarmText = alarmText;
    }
}
