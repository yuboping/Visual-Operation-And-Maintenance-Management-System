package com.asiainfo.lcims.omc.service.shcm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.monitor.ChartData;
import com.asiainfo.lcims.omc.model.monitor.ChartInfo;
import com.asiainfo.lcims.omc.model.shcm.AuthenSituationChartData;
import com.asiainfo.lcims.omc.model.shcm.AuthenSituationVo;
import com.asiainfo.lcims.omc.persistence.shcm.AuthenSituationAnalyseDAO;
import com.asiainfo.lcims.omc.util.DateUtil;

@Service
public class AuthenSituationAnalyseService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenSituationAnalyseService.class);

    @Autowired
    private AuthenSituationAnalyseDAO authenSituationAnalyseDAO;

    public WebResult getAuthenSituationList(Map<String, Object> params) {
        WebResult result = new WebResult(true, "查询成功");
        List<List<?>> list = new ArrayList<>();
        List<AuthenSituationVo> tableData;
        List<ChartInfo> chartInfos;
        // 数据结果集
        List<AuthenSituationChartData> authenSituationList;
        try {
            authenSituationList = authenSituationAnalyseDAO
                    .getAuthenSituationListWithDay(params);
            // 获取完整时间list
            String startDate = (String) params.get("startDate");
            String endDate = (String) params.get("endDate");
            String queryType = (String) params.get("queryType");
            List<String> datelist = getAllDateCycleList(startDate, endDate, queryType);
            AuthenSituationVo authenSituationVo = new AuthenSituationVo();
            authenSituationVo.setCycleTime("总计");

            // 组装表格展示
            tableData = assemblyAuthenSituationTable(datelist, authenSituationList, authenSituationVo);
            // 组装折线图展示
            chartInfos = assemblyAuthenSituationLine(datelist, tableData);

            tableData.add(authenSituationVo);
        } catch (Exception e) {
            LOG.info("assembly authen situation table or line error: ", e);
            result.setOpSucc(false);
            result.setMessage("查询异常");
            tableData = new ArrayList<>();
            chartInfos = new ArrayList<>();
        }
        list.add(tableData);
        list.add(chartInfos);
        result.setData(list);
        return result;
    }

    // 组装折线图展示
    private List<ChartInfo> assemblyAuthenSituationLine(List<String> datelist,
            List<AuthenSituationVo> tableData) {
        List<ChartInfo> chartInfos = new ArrayList<>();
        ChartInfo totalChart = new ChartInfo();
        totalChart.setLegend("认证总数");
        List<ChartData> totalChartDatas = new ArrayList<>();

        ChartInfo successChart = new ChartInfo();
        successChart.setLegend("认证成功数");
        List<ChartData> successChartDatas = new ArrayList<>();

        ChartInfo failChart = new ChartInfo();
        failChart.setLegend("认证失败数");
        List<ChartData> failChartDatas = new ArrayList<>();

        for (AuthenSituationVo authenSituation : tableData) {
            totalChartDatas.add(new ChartData(authenSituation.getCycleTime(),
                    authenSituation.getAuthen_total()));
            successChartDatas.add(new ChartData(authenSituation.getCycleTime(),
                    authenSituation.getAuthen_success()));
            failChartDatas.add(new ChartData(authenSituation.getCycleTime(),
                    authenSituation.getAuthen_fail()));
        }

        totalChart.setData(totalChartDatas);
        successChart.setData(successChartDatas);
        failChart.setData(failChartDatas);
        chartInfos.add(totalChart);
        chartInfos.add(successChart);
        chartInfos.add(failChart);
        return chartInfos;
    }

    // 组装表格展示
    private List<AuthenSituationVo> assemblyAuthenSituationTable(List<String> datelist,
            List<AuthenSituationChartData> authenSituationList,
            AuthenSituationVo authenSituationVo) {
        List<AuthenSituationVo> list = new ArrayList<>();
        authenSituationVo.setAuthen_total("0").setAuthen_fail("0").setAuthen_success("0");
        for (String stime : datelist) {
            AuthenSituationVo vo = getAuthenSituationVo(stime, authenSituationList,
                    authenSituationVo);
            list.add(vo);
        }
        return list;
    }

    private AuthenSituationVo getAuthenSituationVo(String stime,
            List<AuthenSituationChartData> authenSituationList,
            AuthenSituationVo authenSituationVo) {
        AuthenSituationVo authSituation = new AuthenSituationVo(stime);
        Integer num;
        for (AuthenSituationChartData chartData : authenSituationList) {
            if (chartData.getMark().equals(stime)) {
                if ("authen_total".equals(chartData.getAuthen_type())) {
                    authSituation.setAuthen_total(chartData.getValue());
                    num = Integer.parseInt(authenSituationVo.getAuthen_total())
                            + Integer.parseInt(chartData.getValue());
                    authenSituationVo.setAuthen_total(num.toString());
                } else if ("authen_success".equals(chartData.getAuthen_type())) {
                    authSituation.setAuthen_success(chartData.getValue());
                    num = Integer.parseInt(authenSituationVo.getAuthen_success())
                            + Integer.parseInt(chartData.getValue());
                    authenSituationVo.setAuthen_success(num.toString());
                } else if ("authen_fail".equals(chartData.getAuthen_type())) {
                    authSituation.setAuthen_fail(chartData.getValue());
                    num = Integer.parseInt(authenSituationVo.getAuthen_fail())
                            + Integer.parseInt(chartData.getValue());
                    authenSituationVo.setAuthen_fail(num.toString());
                }
            }
        }
        return authSituation;
    }

    private List<String> getAllDateCycleList(String startDate, String endDate, String queryType) {
        List<String> datelist = new ArrayList<String>();
        if (ShcmReportService.QUERY_TYPE_DAY.equals(queryType)) {
            // 天类型
            datelist = DateUtil.getAllDateCycleListByDay(startDate, endDate);
        } else if (ShcmReportService.QUERY_TYPE_MONTH.equals(queryType)) {
            // 月类型
            datelist = DateUtil.getAllDateCycleListByMonth(startDate, endDate);
        }
        return datelist;
    }

    public List<AuthenSituationVo> getAuthenSituationReport(Map<String, Object> params) {
        // 组装表格展示
        List<AuthenSituationVo> tableData;
        try {
            List<AuthenSituationChartData> authenSituationList = authenSituationAnalyseDAO
                    .getAuthenSituationListWithDay(params);
            // 获取完整时间list
            String startDate = (String) params.get("startDate");
            String endDate = (String) params.get("endDate");
            String queryType = (String) params.get("queryType");
            List<String> datelist = getAllDateCycleList(startDate, endDate, queryType);
            AuthenSituationVo authenSituationVo = new AuthenSituationVo();
            authenSituationVo.setCycleTime("总计");
            tableData = assemblyAuthenSituationTable(datelist,
                    authenSituationList, authenSituationVo);
            tableData.add(authenSituationVo);
        } catch (Exception e) {
            LOG.info("report authen situation error:", e);
            tableData = new ArrayList<>();
        }
        return tableData;
    }

}
