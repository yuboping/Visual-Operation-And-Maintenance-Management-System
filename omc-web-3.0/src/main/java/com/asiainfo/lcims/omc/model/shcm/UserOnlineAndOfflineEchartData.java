package com.asiainfo.lcims.omc.model.shcm;

import java.util.List;
import java.util.Map;

public class UserOnlineAndOfflineEchartData {
	
	private List<String> rateData;
	
	private Map<String,List<String>> yData;

	public List<String> getRateData() {
		return rateData;
	}

	public void setRateData(List<String> rateData) {
		this.rateData = rateData;
	}

	public Map<String, List<String>> getyData() {
		return yData;
	}

	public void setyData(Map<String, List<String>> yData) {
		this.yData = yData;
	}
	
	

}
