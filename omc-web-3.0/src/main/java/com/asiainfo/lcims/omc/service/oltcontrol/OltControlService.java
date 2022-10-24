package com.asiainfo.lcims.omc.service.oltcontrol;

import com.asiainfo.lcims.omc.model.oltcontrol.*;
import com.asiainfo.lcims.omc.persistence.oltcontrol.OltControlDao;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service(value = "oltControlService")
public class OltControlService {

    private static final Logger LOG = LoggerFactory.getLogger(OltControlService.class);

    @Inject
    private OltControlDao oltControlDao;

    /**
     * 获取表格数据
     * @param oltControlVo
     * @param pageNumber
     * @return
     */
    public Page getOltControlAlarmTableList(OltControlVo oltControlVo, int pageNumber) {
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        page.setPageSize(5);
        int totalCount = oltControlDao.getOltControlAlarmCount(oltControlVo);
        page.setTotalCount(totalCount);
//        查询分页数据
        if(totalCount>0) {
            List<OltControlAlarm> list = oltControlDao.getOltControlAlarmList(oltControlVo, page);
            page.setPageList(list);
        }

        return page;
    }

    /**
     * 获取折线图数据
     * @param oltControlVo
     * @return
     */
    public List<OltLineChartVo> getLineData(OltControlVo oltControlVo){
        List<OltControl> oltControlList = oltControlDao.getLineData(oltControlVo);
        List<OltLineChartVo> oltLineChartVoList = new ArrayList<>();
        if (!StringUtils.isEmpty(oltControlVo.getOltIps())) {
            String[] oltIpArray = oltControlVo.getOltIps().split(",");
            for (String oltIp : oltIpArray) {
                OltLineChartVo oltLineData = getOltLineData(oltControlList, oltIp);
                oltLineChartVoList.add(oltLineData);
            }
        } else {
            OltLineChartVo oltLineData = getOltLineData(oltControlList, oltControlVo.getOltIp());
            oltLineChartVoList.add(oltLineData);
        }
        return oltLineChartVoList;
    }

    private OltLineChartVo getOltLineData(List<OltControl> oltControlList, String oltIp) {
        OltLineChartVo oltLineChartVo = new OltLineChartVo();
        List<String> xRateData = new LinkedList<>();
        List<String> yRateData = new LinkedList<>();
        for (OltControl oltControl : oltControlList) {
            if (oltControl.getItem().equals(oltIp)){
                xRateData.add(DateUtil.getFormatTimeByFormat(oltControl.getStime(), "HH:mm"));
                yRateData.add(oltControl.getMvalue());
            }
        }
        oltLineChartVo.setxRateData(xRateData);
        oltLineChartVo.setyRateData(yRateData);
        oltLineChartVo.setOltIp(oltIp);
        return oltLineChartVo;
    }

    public List<OltControlSelectRespVo> getOltSelectData(){
        List<OltControlSelectRespVo> selectRespVoList = oltControlDao.getOltSelectControlAlarmList();
        if (selectRespVoList == null || selectRespVoList.size() == 0){
            selectRespVoList = oltControlDao.getSelectOltSingleData(Constant.TODAY_CONSTANT);
            if (selectRespVoList == null || selectRespVoList.size() == 0) {
                selectRespVoList = oltControlDao.getSelectOltSingleData(Constant.YESTERDAY_CONSTANT);
            }
        }
        return selectRespVoList;
    }
}
