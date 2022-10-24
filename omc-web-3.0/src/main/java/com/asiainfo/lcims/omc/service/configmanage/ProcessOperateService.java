package com.asiainfo.lcims.omc.service.configmanage;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.configmanage.ProcessOperate;
import com.asiainfo.lcims.omc.persistence.configmanage.ProcessOperateDAO;

@Service(value = "processOperateService")
public class ProcessOperateService {

    @Inject
    private ProcessOperateDAO processOperateDAO;

    public ProcessOperate getProcessOperateInfo(String operateId){
        ProcessOperate processOperate = processOperateDAO.getProcessOperateByOperateId(operateId);
        return processOperate;
    }

}
