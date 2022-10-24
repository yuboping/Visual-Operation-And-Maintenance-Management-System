package com.asiainfo.lcims.omc.service.shcm;

import com.asiainfo.lcims.omc.model.shcm.ChartListVo;
import com.asiainfo.lcims.omc.model.shcm.WideChartVo;
import com.asiainfo.lcims.omc.model.shcm.WideFlowRangeData;
import com.asiainfo.lcims.omc.model.shcm.WideFlowRangeVo;
import com.asiainfo.lcims.omc.persistence.shcm.WideFlowRangeDao;
import com.asiainfo.lcims.omc.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service(value = "wideFlowRangeService")
public class WideFlowRangeService {

    private static final Logger LOG = LoggerFactory.getLogger(WideFlowRangeService.class);

    private static final String QUERY_TYPE_DAY = "1";
    private static final String QUERY_TYPE_MONTH = "2";

    @Inject
    private WideFlowRangeDao wideFlowRangeDao;

    /**
     * 宽带业务流量分析报表——表格
     *
     * @param wideFlowRangeData
     * @return
     */
    public List<WideFlowRangeVo> getWideFlowRangeList(WideFlowRangeData wideFlowRangeData) {
        List<WideFlowRangeVo> wideFlowRangeVoList = null;

        if (QUERY_TYPE_DAY.equals(wideFlowRangeData.getQueryType())) {


            List<String> allDateCycleListByDay = DateUtil.getAllDateCycleListByDay(
                    wideFlowRangeData.getStartDate(),
                    wideFlowRangeData.getEndDate()
            );
            wideFlowRangeData = getMonthCount(wideFlowRangeData);
            wideFlowRangeVoList = wideFlowRangeDao.getWideFlowRangeListWithDay(wideFlowRangeData);
            List<WideFlowRangeVo> wideFlowRangeVoLinkedList = new LinkedList<>();
            for (String day : allDateCycleListByDay) {
                Boolean flag = false;
                for (WideFlowRangeVo wideFlowRangeVo : wideFlowRangeVoList) {
                    if (day.equals(DateUtil.getFormatTimeByFormat(wideFlowRangeVo.getStime(), "yyyy-MM-dd","yyyy-MM-dd"))) {
                        flag = true;
                        wideFlowRangeVoLinkedList.add(wideFlowRangeVo);
                    }
                }
                if (!flag) {
                    WideFlowRangeVo wideFlowRangeVo = new WideFlowRangeVo();
//                    day = day + " 00:00:00";
                    wideFlowRangeVo.setStime(day);
                    wideFlowRangeVo.setInput_v4("0");
                    wideFlowRangeVo.setOutput_v4("0");
                    wideFlowRangeVo.setTotal_v4("0");
                    wideFlowRangeVo.setInput_v6("0");
                    wideFlowRangeVo.setOutput_v6("0");
                    wideFlowRangeVo.setTotal_v6("0");
                    wideFlowRangeVoLinkedList.add(wideFlowRangeVo);
                }
            }
            return wideFlowRangeVoLinkedList;
        } else if (QUERY_TYPE_MONTH.equals(wideFlowRangeData.getQueryType())) {

            List<String> allDateCycleListByMonth = DateUtil.getAllDateCycleListByMonth(
                    wideFlowRangeData.getStartDate(),
                    wideFlowRangeData.getEndDate()
            );
            wideFlowRangeData = getMonthCount(wideFlowRangeData);
            wideFlowRangeVoList = wideFlowRangeDao.getWideFlowRangeListWithMonth(wideFlowRangeData);
            List<WideFlowRangeVo> wideFlowRangeVoLinkedList = new LinkedList<>();
            for (String month : allDateCycleListByMonth) {
                Boolean flag = false;
                for (WideFlowRangeVo wideFlowRangeVo : wideFlowRangeVoList) {
                    if (month.equals(wideFlowRangeVo.getStime())) {
                        flag = true;
                        wideFlowRangeVoLinkedList.add(wideFlowRangeVo);
                    }
                }
                if (!flag) {
                    WideFlowRangeVo wideFlowRangeVo = new WideFlowRangeVo();
                    wideFlowRangeVo.setStime(month);
                    wideFlowRangeVo.setInput_v4("0");
                    wideFlowRangeVo.setOutput_v4("0");
                    wideFlowRangeVo.setTotal_v4("0");
                    wideFlowRangeVo.setInput_v6("0");
                    wideFlowRangeVo.setOutput_v6("0");
                    wideFlowRangeVo.setTotal_v6("0");
                    wideFlowRangeVoLinkedList.add(wideFlowRangeVo);
                }
            }
            return wideFlowRangeVoLinkedList;
        }

        return wideFlowRangeVoList;
    }

    private WideFlowRangeData getMonthCount(WideFlowRangeData wideFlowRangeData) {
        int monthCount = DateUtil.dayCompare(
                wideFlowRangeData.getStartDate(),
                wideFlowRangeData.getEndDate()
        );
        wideFlowRangeData.setMonthCount(monthCount);
        return wideFlowRangeData;
    }

    public ChartListVo getWideFlowRangeChart(WideFlowRangeData wideFlowRangeData) {
        List<WideFlowRangeVo> wideFlowRangeVoList = getWideFlowRangeList(wideFlowRangeData);
        ChartListVo wideChartVo = getWideFlowRangeLineData(
                wideFlowRangeVoList,
                wideFlowRangeData.getQueryType()
        );
        return wideChartVo;
    }

    private ChartListVo getWideFlowRangeLineData(List<WideFlowRangeVo> wideFlowRangeVoList, String queryType) {
        ChartListVo chartListVo = new ChartListVo();
        List<WideChartVo> wideChartVoList = new LinkedList<>();

        for (int i = 0; i < 6; i++) {
            WideChartVo wideChartVo = new WideChartVo();
            List<String> xRateData = new LinkedList<>();
            List<String> yRateData = new LinkedList<>();

            for (WideFlowRangeVo wideFlowRangeVo : wideFlowRangeVoList) {

                if (QUERY_TYPE_DAY.equals(queryType)) {
                    xRateData.add(DateUtil.getFormatTimeByFormat(wideFlowRangeVo.getStime(), "yyyy-MM-dd","yyyy-MM-dd"));
                } else if (QUERY_TYPE_MONTH.equals(queryType)) {
                    xRateData.add(wideFlowRangeVo.getStime());
                }

                if (i==0){
                    yRateData.add(wideFlowRangeVo.getInput_v4());
                    wideChartVo.setDataName("ipv4上行流量");
                }else if (i==1){
                    yRateData.add(wideFlowRangeVo.getOutput_v4());
                    wideChartVo.setDataName("ipv4下行流量");
                }else if (i==2){
                    yRateData.add(wideFlowRangeVo.getTotal_v4());
                    wideChartVo.setDataName("ipv4总流量");
                }else if (i==3){
                    yRateData.add(wideFlowRangeVo.getInput_v6());
                    wideChartVo.setDataName("ipv6上行流量");
                }else if (i==4){
                    yRateData.add(wideFlowRangeVo.getOutput_v6());
                    wideChartVo.setDataName("ipv6下行流量");
                }else if (i==5){
                    yRateData.add(wideFlowRangeVo.getTotal_v6());
                    wideChartVo.setDataName("ipv6总流量");
                }
            }
            wideChartVo.setxRateData(xRateData);
            wideChartVo.setyRateData(yRateData);
            wideChartVoList.add(wideChartVo);
        }

        chartListVo.setName("宽带业务流量分析折线图");
        chartListVo.setWideChartVoList(wideChartVoList);
        return chartListVo;
    }
}
