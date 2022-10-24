package com.asiainfo.lcims.omc.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceUtil {
	private static final int POOLSIZE = 20;
	private static  ExecutorService fixedThreadPool;
	
	public static ExecutorService getExecutorService(){
		if(fixedThreadPool==null){
			fixedThreadPool = Executors.newFixedThreadPool(POOLSIZE);
		}
		return fixedThreadPool;
	}
}
