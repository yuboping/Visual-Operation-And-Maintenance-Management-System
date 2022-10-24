package com.asiainfo.lcims.omc.service.ais;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.lcbmi.utils.TimeTools;
import com.asiainfo.lcims.omc.model.ais.ReportModel;
import com.asiainfo.lcims.omc.persistence.ais.INSReportDAO;
import com.asiainfo.lcims.omc.persistence.po.ais.INSReport;

/**
 * 巡检报告记录表:MON_INS_REPORT
 * 
 * @author luohuawuyin
 *
 */
@Service("insReportService")
public class InsReportService {
    private static final Logger LOG = LoggerFactory.getLogger(InsReportService.class);
    @Inject
    private INSReportDAO insReportDAO;
    /**
     * 获取当页报告，包含分页信息
     * 
     * @param paramters
     * @return
     */
    public Map<String, Object> getReportListWithPage(Map<String, String> parameters) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("currentpage", parameters.get("currentpage"));
        result.put("pagesize", parameters.get("pagesize"));
        List<ReportModel> reportlist = new ArrayList<ReportModel>();
        int reportnum = this.getReportCountByTime(parameters.get("begintime"),
                parameters.get("endtime"), parameters.get("searchkey"));
        LOG.debug("reportnum:" + reportnum);
        if (reportnum == 0) {
            result.put("reportlist", reportlist);
            result.put("totalcount", 0);
        } else {
            int startIndex = (Integer.valueOf(parameters.get("currentpage")) - 1)
                    * Integer.valueOf(parameters.get("pagesize"));
            reportlist = this.getReportListByPage(parameters.get("begintime"),
                    parameters.get("endtime"), parameters.get("searchkey"), startIndex,
                    Integer.valueOf(parameters.get("pagesize")));

            result.put("reportlist", reportlist);
            result.put("totalcount", reportnum);
        }

        return result;

    }

    private int getReportCountByTime(String begintime, String endtime, String searchkey) {
        Integer reportnum = insReportDAO.getRportCountByTime(begintime, endtime, searchkey);
        if (reportnum == null) {
            return 0;
        }
        return reportnum;
    }

    /**
     * 获取所有的report
     * 
     * @return
     */
    public List<INSReport> getReportList() {
        List<INSReport> reports = insReportDAO.getAllReport();
        return reports;
    }

    /**
     * 根据起始时间和结束时间查询当前页的内容
     * 
     * @param begintime
     * @param endtime
     * @param startIndex
     * @param pageCount
     * @return
     */
    public List<ReportModel> getReportListByPage(String begintime, String endtime, String searchkey,
            int startIndex, int pageCount) {
        List<INSReport> reports = insReportDAO.getReportBytime(begintime, endtime, searchkey);
        if (reports != null && !reports.isEmpty()) {
            if (startIndex <= reports.size()) {
                int endIndex = startIndex;
                if (startIndex + pageCount <= reports.size()) {
                    endIndex = startIndex + pageCount;
                } else {
                    endIndex = reports.size();
                }
                INSReport[] reportarray = (INSReport[]) Arrays.copyOfRange(
                        reports.toArray(new INSReport[reports.size()]), startIndex, endIndex);
                if (reportarray != null && reportarray.length > 0) {
                    List<ReportModel> results = new ArrayList<ReportModel>(reportarray.length);
                    for (INSReport item : reportarray) {
                        ReportModel model = new ReportModel();
                        model.setId("" + item.getId());
                        model.setTitle("" + item.getTitle());
                        model.setReportlink(item.getReportlink());
                        model.setShowcreatetime(TimeTools.time2String(item.getCreate_time()));
                        results.add(model);
                    }
                    return results;
                }
            }
        }
        return null;
    }
    
    public String getReportNameById(String reportId){
        String reportName = insReportDAO.getReportNameById(reportId);
        return reportName;
    }
    
}
