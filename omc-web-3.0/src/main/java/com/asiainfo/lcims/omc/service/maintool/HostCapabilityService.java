package com.asiainfo.lcims.omc.service.maintool;

import com.asiainfo.lcims.omc.model.maintool.HostCapability;
import com.asiainfo.lcims.omc.persistence.maintool.HostCapabilityDao;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service(value = "hostCapabilityService")
public class HostCapabilityService {

    private static final Logger LOG = LoggerFactory.getLogger(HostCapabilityService.class);

    @Inject
    private HostCapabilityDao hostCapabilityDao;

    public List<HostCapability> getHostCapabilityList(String hostname) {
        String tableName = Constant.HOST_CAP_TABLE_PREFIX + DateUtil.getFormatTime(0, "MM_dd");
        List<HostCapability> list = hostCapabilityDao.getHostCapabilityList(hostname, tableName);
        List<HostCapability> hostCapabilityList = new ArrayList<>();
        for(HostCapability hostCapability: list){
            if(StringUtils.isEmpty(hostCapability.getCpu_use_percent()) && StringUtils.isEmpty(hostCapability.getMemory_use_percent())){
                hostCapability.setStime("");
            }
            hostCapabilityList.add(hostCapability);
        }
        return hostCapabilityList;
    }

}
