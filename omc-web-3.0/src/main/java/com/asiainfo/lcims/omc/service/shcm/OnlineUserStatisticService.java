package com.asiainfo.lcims.omc.service.shcm;

import com.asiainfo.lcims.omc.model.shcm.LineChartData;
import com.asiainfo.lcims.omc.model.shcm.OnlineUserStatisticVo;
import com.asiainfo.lcims.omc.model.shcm.StatisData;
import com.asiainfo.lcims.omc.persistence.shcm.OnlineUserStatisticDao;
import com.asiainfo.lcims.omc.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

@Service(value = "onlineUserStatisticService")
public class OnlineUserStatisticService {

    private static final Logger LOG = LoggerFactory.getLogger(OnlineUserStatisticService.class);

    private static final String QUERY_TYPE_HOUR = "0";
    private static final String QUERY_TYPE_DAY = "1";
    private static final String QUERY_TYPE_MONTH = "2";

    @Inject
    private OnlineUserStatisticDao onlineUserStatisticDao;

    public List<StatisData> getOnlineUserStatisticList(OnlineUserStatisticVo onlineUserStatisticVo){
        List<StatisData> onlineUserStatisticList = null;
        if (QUERY_TYPE_HOUR.equals(onlineUserStatisticVo.getQueryType())) {
            String tableSuffix = DateUtil.getFormatTimeByFormat(
                    onlineUserStatisticVo.getEndDate(),
                    "today",
                    "MM");
            onlineUserStatisticVo.setTableName(tableSuffix);
            onlineUserStatisticList = onlineUserStatisticDao.getOnlineUserStatisticListWithHour(onlineUserStatisticVo);
        } else if (QUERY_TYPE_DAY.equals(onlineUserStatisticVo.getQueryType())) {
            List<String> allDateCycleListByDay = DateUtil.getAllDateCycleListByDay(
                    onlineUserStatisticVo.getStartDate(),
                    onlineUserStatisticVo.getEndDate()
            );
            onlineUserStatisticVo = getMonthCount(onlineUserStatisticVo);
            onlineUserStatisticList = onlineUserStatisticDao.getOnlineUserStatisticListWithDay(onlineUserStatisticVo);
            List<StatisData> statisDataList = new LinkedList<>();
            for (String day : allDateCycleListByDay) {
                Boolean flag = false;
                for (StatisData statisData : onlineUserStatisticList) {
                    if (day.equals(DateUtil.getFormatTimeByFormat(statisData.getStime(), "yyyy-MM-dd"))) {
                        flag = true;
                        statisDataList.add(statisData);
                    }
                }
                if (!flag) {
                    StatisData statisData = new StatisData();
                    day = day + " 10:00:00";
                    statisData.setStime(day);
                    statisData.setMvalue("0");
                    statisDataList.add(statisData);
                }
            }
            return statisDataList;
        } else if (QUERY_TYPE_MONTH.equals(onlineUserStatisticVo.getQueryType())) {
            List<String> allDateCycleListByMonth = DateUtil.getAllDateCycleListByMonth(
                    onlineUserStatisticVo.getStartDate(),
                    onlineUserStatisticVo.getEndDate()
            );
            onlineUserStatisticVo = getMonthCount(onlineUserStatisticVo);
            onlineUserStatisticList = onlineUserStatisticDao.getOnlineUserStatisticListWithMonth(onlineUserStatisticVo);
            List<StatisData> statisDataList = new LinkedList<>();
            for (String month : allDateCycleListByMonth) {
                Boolean flag = false;
                for (StatisData statisData : onlineUserStatisticList) {
                    if (month.equals(DateUtil.getFormatTimeByFormat(statisData.getStime(), "yyyy-MM"))) {
                        flag = true;
                        statisDataList.add(statisData);
                    }
                }
                if (!flag) {
                    StatisData statisData = new StatisData();
                    month = month + "-15 10:00:00";
                    statisData.setStime(month);
                    statisData.setMvalue("0");
                    statisDataList.add(statisData);
                }
            }
            return statisDataList;
        }
        return onlineUserStatisticList;
    }

    private OnlineUserStatisticVo getMonthCount(OnlineUserStatisticVo onlineUserStatisticVo) {
        int monthCount = DateUtil.dayCompare(
                onlineUserStatisticVo.getStartDate(),
                onlineUserStatisticVo.getEndDate()
        );
        onlineUserStatisticVo.setMonthCount(monthCount);
        return onlineUserStatisticVo;
    }

    public LineChartData getOnlineUserStatisticWithGraph(OnlineUserStatisticVo onlineUserStatisticVo){
        List<StatisData> onlineUserStatisticList = getOnlineUserStatisticList(onlineUserStatisticVo);
        LineChartData oltLineData = getOnlineUserStatisticLineData(
                onlineUserStatisticList,
                onlineUserStatisticVo.getQueryType()
        );
        return oltLineData;
    }

    private LineChartData getOnlineUserStatisticLineData(List<StatisData> statisDataList, String queryType) {
        LineChartData lineChartData = new LineChartData();
        List<String> xRateData = new LinkedList<>();
        List<String> yRateData = new LinkedList<>();
        for (StatisData statisData : statisDataList) {
            if (QUERY_TYPE_HOUR.equals(queryType)) {
                xRateData.add(DateUtil.getFormatTimeByFormat(statisData.getStime(), "HH:mm"));
            } else if (QUERY_TYPE_DAY.equals(queryType)) {
                xRateData.add(DateUtil.getFormatTimeByFormat(statisData.getStime(), "yyyy-MM-dd"));
            } else if (QUERY_TYPE_MONTH.equals(queryType)) {
                xRateData.add(DateUtil.getFormatTimeByFormat(statisData.getStime(), "yyyy-MM"));
            }
            yRateData.add(statisData.getMvalue());

        }
        lineChartData.setxRateData(xRateData);
        lineChartData.setyRateData(yRateData);
        lineChartData.setTitle("在线用户情况分析");
        return lineChartData;
    }

}
