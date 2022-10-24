package com.asiainfo.lcims.omc.service.shcm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.monitor.ChartData;
import com.asiainfo.lcims.omc.model.monitor.ChartInfo;
import com.asiainfo.lcims.omc.model.shcm.AuthFailResonVo;
import com.asiainfo.lcims.omc.model.shcm.QueryResultChartData;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.shcm.ShcmReportDao;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

@Service(value = "shcmReportService")
public class ShcmReportService {
    private static final Logger LOG = LoggerFactory.getLogger(ShcmReportService.class);
    
    public static final String QUERY_TYPE_HOUR = "0";
    public static final String QUERY_TYPE_DAY = "1";
    public static final String QUERY_TYPE_MONTH = "2";
    
    /** 宽带认证请求数 */
    private static String adsl_request = "adsl_request";
    /** 宽带认证成功数 */
    private static String adsl_success = "adsl_success";
    /** 未注册帐号 */
    private static String adsl_failcode1 = "adsl_failcode1";
    /** 密码错误 */
    private static String adsl_failcode2 = "adsl_failcode2";
    /** 用户加锁 */
    private static String adsl_failcode3 = "adsl_failcode3";
    /** 限时上网 */
    private static String adsl_failcode4 = "adsl_failcode4";
    /** vlan错误 */
    private static String adsl_failcode5 = "adsl_failcode5";
    /** 唯一性拒绝 */
    private static String adsl_failcode6 = "adsl_failcode6";
    
    @Inject
    private ShcmReportDao reportDao;
    
    /**
     * 认证失败原因分析报表 查询功能
     * @param params
     * @return
     */
    public WebResult authFailReasonReportQuery(Map<String, Object> params) {
        WebResult result = new WebResult(true, "查询成功");
        List<List <?>> list = new ArrayList<>();
        List<AuthFailResonVo> tableData;
        List<ChartInfo> chartInfos;
        try {
            MdMetric metric = CommonInit.getMetricByIdentity("sh_auth_fail_reason");
            params.put("metricId", metric.getId());
            //数据结果集
            List<QueryResultChartData> resultList = reportDao.queryAuthFailReasonInfos(params);
            // 获取完整时间list
            String startDate = (String) params.get("startDate");
            String endDate = (String) params.get("endDate");
            String queryType = (String) params.get("queryType");
            List<String> datelist = getAllDateCycleList(startDate, endDate, queryType);
            AuthFailResonVo totalVo = new AuthFailResonVo();
            totalVo.setCycleTime("总计");
            // 组装表格展示
            tableData = assemblyAuthFailReasonTable(datelist, resultList, totalVo);
            // 组装折线图展示
            chartInfos = 
                    assemblyAuthFailReasonLine(datelist, tableData);
            tableData.add(totalVo);
        } catch (Exception e) {
            LOG.info("error:", e);
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
    
    private List<String> getAllDateCycleList(String startDate, String endDate, String queryType) {
        List<String> datelist = new ArrayList<String>();
        if(ShcmReportService.QUERY_TYPE_DAY.equals(queryType)) {
            // 天类型
            datelist = DateUtil.getAllDateCycleListByDay(startDate, endDate);
        } else if(ShcmReportService.QUERY_TYPE_MONTH.equals(queryType)) {
            //月类型
            datelist = DateUtil.getAllDateCycleListByMonth(startDate, endDate);
        }
        return datelist;
    }
    
    private List<AuthFailResonVo> assemblyAuthFailReasonTable(List<String> datelist, 
            List<QueryResultChartData> resultList, AuthFailResonVo totalVo) {
        List<AuthFailResonVo> list = new ArrayList<>();
        totalVo.setAdsl_success("0").setAdsl_request("0").setAdsl_failcode1("0")
               .setAdsl_failcode2("0").setAdsl_failcode3("0").setAdsl_failcode4("0")
               .setAdsl_failcode5("0").setAdsl_failcode6("0");
        if(ToolsUtils.ListIsNull(resultList)) {
            for (String stime : datelist) {
                list.add(new AuthFailResonVo(stime));
            }
            return list;
        }
        
        for (String stime : datelist) {
            AuthFailResonVo vo = getAuthFailResonVo(stime, resultList, totalVo);
            list.add(vo);
        }
        return list;
    }
    
    private AuthFailResonVo getAuthFailResonVo(String stime, List<QueryResultChartData> resultList,
            AuthFailResonVo totalVo) {
        AuthFailResonVo vo = new AuthFailResonVo(stime);
        Integer num;
        for (QueryResultChartData queryResultChartData : resultList) {
            if(queryResultChartData.getMark().equals(stime)) {
                if(adsl_request.equals(queryResultChartData.getAdsl_reason())) {
                    vo.setAdsl_request(queryResultChartData.getValue());
                    num = Integer.parseInt(totalVo.getAdsl_request()) + Integer.parseInt(queryResultChartData.getValue());
                    totalVo.setAdsl_request(num.toString());
                } else if(adsl_success.equals(queryResultChartData.getAdsl_reason())) {
                    vo.setAdsl_success(queryResultChartData.getValue());
                    num = Integer.parseInt(totalVo.getAdsl_success()) + Integer.parseInt(queryResultChartData.getValue());
                    totalVo.setAdsl_success(num.toString());
                } else if(adsl_failcode1.equals(queryResultChartData.getAdsl_reason())) {
                    vo.setAdsl_failcode1(queryResultChartData.getValue());
                    num = Integer.parseInt(totalVo.getAdsl_failcode1()) + Integer.parseInt(queryResultChartData.getValue());
                    totalVo.setAdsl_failcode1(num.toString());
                } else if(adsl_failcode2.equals(queryResultChartData.getAdsl_reason())) {
                    vo.setAdsl_failcode2(queryResultChartData.getValue());
                    num = Integer.parseInt(totalVo.getAdsl_failcode2()) + Integer.parseInt(queryResultChartData.getValue());
                    totalVo.setAdsl_failcode2(num.toString());
                } else if(adsl_failcode3.equals(queryResultChartData.getAdsl_reason())) {
                    vo.setAdsl_failcode3(queryResultChartData.getValue());
                    num = Integer.parseInt(totalVo.getAdsl_failcode3()) + Integer.parseInt(queryResultChartData.getValue());
                    totalVo.setAdsl_failcode3(num.toString());
                } else if(adsl_failcode4.equals(queryResultChartData.getAdsl_reason())) {
                    vo.setAdsl_failcode4(queryResultChartData.getValue());
                    num = Integer.parseInt(totalVo.getAdsl_failcode4()) + Integer.parseInt(queryResultChartData.getValue());
                    totalVo.setAdsl_failcode4(num.toString());
                } else if(adsl_failcode5.equals(queryResultChartData.getAdsl_reason())) {
                    vo.setAdsl_failcode5(queryResultChartData.getValue());
                    num = Integer.parseInt(totalVo.getAdsl_failcode5()) + Integer.parseInt(queryResultChartData.getValue());
                    totalVo.setAdsl_failcode5(num.toString());
                } else if(adsl_failcode6.equals(queryResultChartData.getAdsl_reason())) {
                    vo.setAdsl_failcode6(queryResultChartData.getValue());
                    num = Integer.parseInt(totalVo.getAdsl_failcode6()) + Integer.parseInt(queryResultChartData.getValue());
                    totalVo.setAdsl_failcode6(num.toString());
                }
            }
        }
        return vo;
    }
    /**
     * 组装折线图信息
     * @param datelist
     * @param resultList
     * @return
     */
    private List<ChartInfo> assemblyAuthFailReasonLine(List<String> datelist, 
            List<AuthFailResonVo> tableData) {
        List<ChartInfo> chartInfos = new ArrayList<>();
        
        ChartInfo requestChart = new ChartInfo();
        requestChart.setLegend("宽带认证请求数");
        List<ChartData> requestChartDatas = new ArrayList<>();
        
        ChartInfo successChart = new ChartInfo();
        successChart.setLegend("宽带认证成功数");
        List<ChartData> successChartDatas = new ArrayList<>();
        
        ChartInfo failcode1Chart = new ChartInfo();
        failcode1Chart.setLegend("未注册帐号");
        List<ChartData> failcode1ChartDatas = new ArrayList<>();
        
        ChartInfo failcode2Chart = new ChartInfo();
        failcode2Chart.setLegend("密码错误");
        List<ChartData> failcode2ChartDatas = new ArrayList<>();
        
        ChartInfo failcode3Chart = new ChartInfo();
        failcode3Chart.setLegend("用户加锁");
        List<ChartData> failcode3ChartDatas = new ArrayList<>();
        
        ChartInfo failcode4Chart = new ChartInfo();
        failcode4Chart.setLegend("限时上网");
        List<ChartData> failcode4ChartDatas = new ArrayList<>();
        
        ChartInfo failcode5Chart = new ChartInfo();
        failcode5Chart.setLegend("vlan错误");
        List<ChartData> failcode5ChartDatas = new ArrayList<>();
        
        ChartInfo failcode6Chart = new ChartInfo();
        failcode6Chart.setLegend("唯一性拒绝");
        List<ChartData> failcode6ChartDatas = new ArrayList<>();
        
        for (AuthFailResonVo vo : tableData) {
            requestChartDatas.add(new ChartData(vo.getCycleTime(),vo.getAdsl_request()));
            successChartDatas.add(new ChartData(vo.getCycleTime(),vo.getAdsl_success()));
            failcode1ChartDatas.add(new ChartData(vo.getCycleTime(),vo.getAdsl_failcode1()));
            failcode2ChartDatas.add(new ChartData(vo.getCycleTime(),vo.getAdsl_failcode2()));
            failcode3ChartDatas.add(new ChartData(vo.getCycleTime(),vo.getAdsl_failcode3()));
            failcode4ChartDatas.add(new ChartData(vo.getCycleTime(),vo.getAdsl_failcode4()));
            failcode5ChartDatas.add(new ChartData(vo.getCycleTime(),vo.getAdsl_failcode5()));
            failcode6ChartDatas.add(new ChartData(vo.getCycleTime(),vo.getAdsl_failcode6()));
        }
        requestChart.setData(requestChartDatas);
        successChart.setData(successChartDatas);
        failcode1Chart.setData(failcode1ChartDatas);
        failcode2Chart.setData(failcode2ChartDatas);
        failcode3Chart.setData(failcode3ChartDatas);
        failcode4Chart.setData(failcode4ChartDatas);
        failcode5Chart.setData(failcode5ChartDatas);
        failcode6Chart.setData(failcode6ChartDatas);
        
        chartInfos.add(requestChart);
        chartInfos.add(successChart);
        chartInfos.add(failcode1Chart);
        chartInfos.add(failcode2Chart);
        chartInfos.add(failcode3Chart);
        chartInfos.add(failcode4Chart);
        chartInfos.add(failcode5Chart);
        chartInfos.add(failcode6Chart);
        return chartInfos;
    }

    public List<AuthFailResonVo> getAuthFailReasonReport(Map<String, Object> params) {
        List<AuthFailResonVo> tableData;
        try {
            MdMetric metric = CommonInit.getMetricByIdentity("sh_auth_fail_reason");
            params.put("metricId", metric.getId());
            //数据结果集
            List<QueryResultChartData> resultList = reportDao.queryAuthFailReasonInfos(params);
            // 获取完整时间list
            String startDate = (String) params.get("startDate");
            String endDate = (String) params.get("endDate");
            String queryType = (String) params.get("queryType");
            List<String> datelist = getAllDateCycleList(startDate, endDate, queryType);
            AuthFailResonVo totalVo = new AuthFailResonVo();
            totalVo.setCycleTime("总计");
            // 组装表格展示
            tableData = assemblyAuthFailReasonTable(datelist, resultList, totalVo);
            tableData.add(totalVo);
        } catch (Exception e) {
            LOG.info("error:", e);
            tableData = new ArrayList<>();
        }
        return tableData;
    }
}
