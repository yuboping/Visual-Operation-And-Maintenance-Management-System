package com.asiainfo.lcims.omc.service.operateHis;

import com.asiainfo.lcims.omc.model.operatehistory.OperateHis;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.persistence.operatehistory.OperateHisDao;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service(value = "operateHisService")
public class OperateHisService {

    private static final Logger LOG = LoggerFactory.getLogger(OperateHisService.class);

    @Inject
    private OperateHisDao operateHisDao;

    public Page getOperateHistoryList(OperateHis operateHis, int pageNumber) {
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        operateHis.setRole_id(user.getRoleid());
        operateHis.setCurrent_user(user.getAdmin());
        int totalCount = operateHisDao.getOperateHisInfoCount(operateHis);
        page.setTotalCount(totalCount);
        //查询分页数据
        if(totalCount>0) {
            List<OperateHis> list = operateHisDao.getOperateHisList(operateHis, page);
            page.setPageList(list);
        }

        return page;
    }

    public int insertOperateHistory(String operateType, String operateDesc){
        OperateHis operateHis = new OperateHis();
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        operateHis.setOperate_name(user.getAdmin());
        operateHis.setOperate_time(DateTools.getCurrentFormatDate());
        operateHis.setOperate_id(IDGenerateUtil.getUuid());
        operateHis.setOperate_desc("用户" +user.getAdmin() + "于" + DateTools.getCurrentFormatDate() +"  "+ operateDesc);
        operateHis.setOperate_type(operateType);
        int successCount = operateHisDao.insertOperateHis(operateHis);
        return successCount;
    }
}
