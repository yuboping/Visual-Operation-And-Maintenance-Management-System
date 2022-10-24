package com.asiainfo.lcims.omc.model.sos;

import java.io.Serializable;

public class SosBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String mesCode;
	
	private String message;

	public String getMesCode() {
		return mesCode;
	}

	public void setMesCode(String mesCode) {
		this.mesCode = mesCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
