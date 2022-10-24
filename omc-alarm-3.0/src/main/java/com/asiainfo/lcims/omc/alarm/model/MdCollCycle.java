package com.asiainfo.lcims.omc.alarm.model;

public class MdCollCycle {
    private Integer cycleid;

    private String cyclename;

    private Integer cycle;

    private Integer runday;
    
    private String cron;
    
    private String description;
    
    public Integer getCycleid() {
        return cycleid;
    }

    public void setCycleid(Integer cycleid) {
        this.cycleid = cycleid;
    }

    public String getCyclename() {
        return cyclename;
    }

    public void setCyclename(String cyclename) {
        this.cyclename = cyclename == null ? null : cyclename.trim();
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public Integer getRunday() {
        return runday;
    }

    public void setRunday(Integer runday) {
        this.runday = runday;
    }

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    @Override
    public String toString() {
        return "MdCollCycle [cycleid=" + cycleid + ", cyclename=" + cyclename + ", cycle=" + cycle
                + ", runday=" + runday + ", cron=" + cron + ", description=" + description + "]";
    }

}