package com.asiainfo.lcims.omc.service.configmanage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.MonHostDAO;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.ExcelUtil;
import com.asiainfo.lcims.omc.util.StringUtil;

@Service(value = "hostResourcesStatisticsService")
public class HostResourcesStatisticsService {

    private static String cpuRateMetricId = CommonInit.BUSCONF.getCpuRateMetricId();
    private static String memoryMetricId = CommonInit.BUSCONF.getMemoryMetricId();

    @Inject
    private MonHostDAO monHostDAO;

    /**
     * 
     * @Title: getDayReport
     * @Description: TODO(获取日报表数据)
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    public List<Map<String, Object>> getDayReport(Map<String, Object> map) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (map.get("addr") == null || map.get("addr").equals("") || map.get("date") == null
                || map.get("date").equals("")) {
            return result;
        } else {
            String addr = map.get("addr").toString();
            String date = map.get("date").toString();
            String tableName = Constant.HOST_CAP_TABLE_PREFIX
                    + DateUtil.parseStr(DateUtil.parseDate(date, "yyyy-MM-dd"), "MM_dd");
            result = monHostDAO.getDayReport(cpuRateMetricId, memoryMetricId, tableName, addr);
        }
        return result;
    }

    /**
     * 
     * @Title: getWeekReport
     * @Description: TODO(获取周报表数据)
     * @param @param map
     * @param @return 参数
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    public List<Map<String, Object>> getWeekReport(Map<String, Object> map) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (map.get("date") == null || map.get("date").equals("")) {
            return result;
        } else {
            String addr = map.get("addr").toString();
            String date = map.get("date").toString();
            List<String> tableNameList = new ArrayList<String>();
            Calendar c = Calendar.getInstance();
            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.setTime(DateUtil.parseDate(date, "yyyy-MM-dd"));
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
            for (int i = 1; i <= 7; i++) {
                Date d = c.getTime();
                String tableName = Constant.HOST_CAP_TABLE_PREFIX + DateUtil.parseStr(d, "MM_dd");
                tableNameList.add(tableName);
                c.add(Calendar.DATE, 1);
            }
            result = monHostDAO.getWMReport(cpuRateMetricId, memoryMetricId, tableNameList, addr);
        }
        return result;
    }

    /**
     * 
     * @Title: getMonthReport
     * @Description: TODO(获取月报表数据)
     * @param @param map
     * @param @return 参数
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    public List<Map<String, Object>> getMonthReport(Map<String, Object> map) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (map.get("date") == null || map.get("date").equals("")) {
            return result;
        } else {
            String addr = map.get("addr").toString();
            String date = map.get("date").toString();
            List<String> tableNameList = new ArrayList<String>();
            Calendar c = Calendar.getInstance();
            c.setTime(DateUtil.parseDate(date, "yyyyMM"));
            int totalDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int i = 1; i <= totalDays; i++) {
                c.set(Calendar.DAY_OF_MONTH, i);
                Date d = c.getTime();
                String tableName = Constant.HOST_CAP_TABLE_PREFIX + DateUtil.parseStr(d, "MM_dd");
                tableNameList.add(tableName);
            }
            result = monHostDAO.getWMReport(cpuRateMetricId, memoryMetricId, tableNameList, addr);
        }
        return result;
    }

    /**
     * 
     * @Title: exportExcel
     * @Description: TODO(报表导出)
     * @param @param map
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    public WebResult exportExcel(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> map) {
        WebResult result = new WebResult(false, "导出失败");
        if (map.get("type") == null || map.get("type").equals("")) {
            return result;
        } else {
            String type = map.get("type").toString();
            switch (type) {
            case Constant.EXPORT_DAY_REPORT:
                result = exportDayExcel(request, response, map);
                break;
            case Constant.EXPORT_WEEK_REPORT:
                result = exportWeekExcel(request, response, map);
                break;
            case Constant.EXPORT_MONTH_REPORT:
                result = exportMonthExcel(request, response, map);
                break;
            }
        }
        return result;
    }

    /**
     * 
     * @Title: exportDayExcel
     * @Description: TODO(导出日报表)
     * @param @param map
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    public WebResult exportDayExcel(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> map) {
        WebResult result = new WebResult(false, "导出失败");
        List<Map<String, Object>> dayReportData = getDayReport(map);
        if (dayReportData != null) {
            List<String> keyList = new ArrayList<String>();
            keyList.add("STIME");
            keyList.add("CPUMVALUE");
            keyList.add("MEMORYMVALUE");
            String[][] datas = StringUtil.mapListToArray(dayReportData, keyList);
            String title = "DAY_REPORT_EXPORT";
            String querytime = map.get("date").toString();
            String[] fields = { "时间", "cpu占用率", "内存占用率" };
            ExcelUtil.downloadExcelFile(title, querytime, fields, request, response, datas);
            result = new WebResult(true, "导出成功！");
        }
        return result;
    }

    /**
     * 
     * @Title: exportWeekExcel
     * @Description: TODO(导出周报表)
     * @param @param map
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    public WebResult exportWeekExcel(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> map) {
        WebResult result = new WebResult(false, "导出失败");
        List<Map<String, Object>> weekReportData = getWeekReport(map);
        if (weekReportData != null) {
            List<String> keyList = new ArrayList<String>();
            keyList.add("HOSTNAME");
            keyList.add("ADDR");
            keyList.add("AVGCPUMVALUE");
            keyList.add("MINCPUMVALUE");
            keyList.add("MAXCPUMVALUE");
            keyList.add("AVGMEMORYMVALUE");
            keyList.add("MINMEMORYMVALUE");
            keyList.add("MAXMEMORYMVALUE");
            String[][] datas = StringUtil.mapListToArray(weekReportData, keyList);
            String title = "WEEK_REPORT_EXPORT";
            String querytime = map.get("date").toString();
            String[] fields = { "设备名称", "IP", "cpu占用率(平均)", "cpu占用率(最小)", "cpu占用率(最大)",
                    "内存占用率(平均)", "内存占用率(最小)", "内存占用率(最大)" };
            ExcelUtil.downloadExcelFile(title, querytime, fields, request, response, datas);
            result = new WebResult(true, "导出成功！");
        }
        return result;
    }

    /**
     * 
     * @Title: exportMonthExcel
     * @Description: TODO(导出月报表)
     * @param @param map
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    public WebResult exportMonthExcel(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> map) {
        WebResult result = new WebResult(false, "导出失败");
        List<Map<String, Object>> monthReportData = getMonthReport(map);
        if (monthReportData != null) {
            List<String> keyList = new ArrayList<String>();
            keyList.add("HOSTNAME");
            keyList.add("ADDR");
            keyList.add("AVGCPUMVALUE");
            keyList.add("MINCPUMVALUE");
            keyList.add("MAXCPUMVALUE");
            keyList.add("AVGMEMORYMVALUE");
            keyList.add("MINMEMORYMVALUE");
            keyList.add("MAXMEMORYMVALUE");
            String[][] datas = StringUtil.mapListToArray(monthReportData, keyList);
            String title = "MONTH_REPORT_EXPORT";
            String querytime = map.get("date").toString();
            String[] fields = { "设备名称", "IP", "cpu占用率(平均)", "cpu占用率(最小)", "cpu占用率(最大)",
                    "内存占用率(平均)", "内存占用率(最小)", "内存占用率(最大)" };
            ExcelUtil.downloadExcelFile(title, querytime, fields, request, response, datas);
            result = new WebResult(true, "导出成功！");
        }
        return result;
    }
}
