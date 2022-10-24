package com.asiainfo.lcims.omc.model.monitor;

import java.util.List;
import java.util.Map;

public class ChartWithGraph {

    private List<Map<String,Object>> pieData;

    private List<String> barXdata;

    private List<Integer> barYdata;

    private List<String> lineXdata;

    private List<Integer> lineYRepdata;

    private List<Integer> lineYUserData;

    public List<Integer> getLineYRepdata() {
        return lineYRepdata;
    }

    public void setLineYRepdata(List<Integer> lineYRepdata) {
        this.lineYRepdata = lineYRepdata;
    }

    public List<Integer> getLineYUserData() {
        return lineYUserData;
    }

    public void setLineYUserData(List<Integer> lineYUserData) {
        this.lineYUserData = lineYUserData;
    }

    public List<Map<String,Object>> getPieData() {
        return pieData;
    }

    public void setPieData(List<Map<String,Object>> pieData) {
        this.pieData = pieData;
    }

    public List<String> getBarXdata() {
        return barXdata;
    }

    public void setBarXdata(List<String> barXdata) {
        this.barXdata = barXdata;
    }

    public List<Integer> getBarYdata() {
        return barYdata;
    }

    public void setBarYdata(List<Integer> barYdata) {
        this.barYdata = barYdata;
    }

    public List<String> getLineXdata() {
        return lineXdata;
    }

    public void setLineXdata(List<String> lineXdata) {
        this.lineXdata = lineXdata;
    }


    @Override
    public String toString() {
        return "ErrorLogWithGraph{" +
                "pieData=" + pieData +
                ", barXdata=" + barXdata +
                ", barYdata=" + barYdata +
                ", lineXdata=" + lineXdata +
                ", lineYRepdata=" + lineYRepdata +
                ", lineYUserData=" + lineYUserData +
                '}';
    }
}
