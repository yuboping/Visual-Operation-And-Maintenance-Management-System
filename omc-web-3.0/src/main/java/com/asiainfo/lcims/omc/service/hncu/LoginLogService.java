package com.asiainfo.lcims.omc.service.hncu;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.hncu.MdLoginLog;
import com.asiainfo.lcims.omc.persistence.hncu.LoginLogDAO;
import com.asiainfo.lcims.omc.util.page.Page;

@Service
public class LoginLogService {

    private static final Logger LOG = LoggerFactory.getLogger(LoginLogService.class);

    @Autowired
    private LoginLogDAO loginLogDAO;

    public Page getLoginLogList(MdLoginLog loginLog, int pageNumber) {
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        int totalCount = loginLogDAO.getLoginLogCount(loginLog);
        LOG.info("login log total count is : {}", totalCount);
        page.setTotalCount(totalCount);
        if (totalCount > 0) {
            List<MdLoginLog> list = loginLogDAO.getLoginLogList(loginLog, page);
            page.setPageList(list);
        }
        return page;
    }

    public List<MdLoginLog> exportLoginLogList(MdLoginLog loginLog) {
        List<MdLoginLog> loginLogList = loginLogDAO.exportLoginLogList(loginLog);
        return loginLogList;
    }
}
