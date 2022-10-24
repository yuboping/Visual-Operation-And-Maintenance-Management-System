package com.asiainfo.lcims.omc.service.system;
/**
 * 采集service公用
 */
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.system.MdCollCycle;
import com.asiainfo.lcims.omc.persistence.system.MdCollCycleDAO;

@Service(value = "collCycleService")
public class CollCycleService {
    @Inject
    private MdCollCycleDAO cycleDAO;
    
    /**
     * 根据type返回配置参数信息
     * @param type
     * @return
     */
    public List<MdCollCycle> getMdCollCycleList() {
        return cycleDAO.getMdCollCycle();
    }
}
