package com.asiainfo.lcims.omc.service.hncu;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.hncu.MdSystemLog;
import com.asiainfo.lcims.omc.persistence.hncu.SystemLogDAO;
import com.asiainfo.lcims.omc.util.page.Page;

@Service
public class SystemLogService {

    private static final Logger LOG = LoggerFactory.getLogger(SystemLogService.class);

    @Autowired
    private SystemLogDAO systemLogDAO;

    public Page getSystemLogList(MdSystemLog systemLog, int pageNumber) {
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        int totalCount = systemLogDAO.getSystemLogCount(systemLog);
        LOG.info("system log total count is : {}", totalCount);
        page.setTotalCount(totalCount);
        if (totalCount > 0) {
            List<MdSystemLog> list = systemLogDAO.getSystemLogList(systemLog, page);
            page.setPageList(list);
        }
        return page;
    }

    public List<MdSystemLog> exportSystemLogList(MdSystemLog systemLog) {
        List<MdSystemLog> systemLogList = systemLogDAO.exportSystemLogList(systemLog);
        return systemLogList;
    }
}
