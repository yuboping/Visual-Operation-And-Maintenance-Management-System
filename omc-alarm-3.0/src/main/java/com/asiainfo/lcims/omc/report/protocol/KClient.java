package com.asiainfo.lcims.omc.report.protocol;

public abstract class KClient {
	
	abstract public void login() throws LoginException;
	
	abstract public void logout() throws LogoutException;
	
	abstract public void active_test() throws ExecuteException;
	
}
