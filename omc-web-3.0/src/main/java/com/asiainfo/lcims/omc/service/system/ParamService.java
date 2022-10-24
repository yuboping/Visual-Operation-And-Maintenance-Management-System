package com.asiainfo.lcims.omc.service.system;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.persistence.system.MdParamDAO;

@Service(value = "paramService")
public class ParamService {
    @Inject
    private MdParamDAO paramDAO;
    
    /**
     * 根据type返回配置参数信息
     * @param type
     * @return
     */
    public List<MdParam> getMdParamList(String type) {
        return paramDAO.getParamByType(type);
    }
}
