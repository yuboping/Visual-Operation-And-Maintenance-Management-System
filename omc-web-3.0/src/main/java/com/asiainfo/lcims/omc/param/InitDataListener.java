package com.asiainfo.lcims.omc.param;

import javax.inject.Inject;

import org.springframework.beans.factory.InitializingBean;

import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.monitor.MonitorInit;

/**
 * 初始化加载数据到内存
 * 
 * @author yuboping
 * 
 */
public class InitDataListener implements InitializingBean {
	@Inject
    private CommonInit com;
    @Inject
    MonitorInit monitorInit;
    
    @Override
    public void afterPropertiesSet() throws Exception {
    	com.init();
    	monitorInit.init();
    }
}
