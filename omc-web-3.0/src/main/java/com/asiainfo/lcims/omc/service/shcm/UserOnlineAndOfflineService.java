package com.asiainfo.lcims.omc.service.shcm;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.shcm.StatisData;
import com.asiainfo.lcims.omc.persistence.shcm.UserOnlineAndOfflineDao;

@Service(value = "userOnlineAndOfflineService")
public class UserOnlineAndOfflineService {

    private static final Logger LOG = LoggerFactory.getLogger(UserOnlineAndOfflineService.class);

    @Inject
    private UserOnlineAndOfflineDao userOnlineAndOfflineDao;
    
    
    public List<StatisData>  queryUserOnlineAndOffline(Set<String> dateList,String startDate,String endDate,int queryType){
    	List<StatisData> datas=null;
    	try {
    		datas=userOnlineAndOfflineDao.queryUserOnlineAndOffline(dateList,startDate,endDate,queryType);
		} catch (Exception e) {
			LOG.error("queryUserOnlineAndOffline error{}", e);
		}
    	return datas;
    	
    }

}
