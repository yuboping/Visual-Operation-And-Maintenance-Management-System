package com.asiainfo.lcims.omc.report.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.business.TimeControl;
import com.asiainfo.lcims.omc.alarm.model.Area;
import com.asiainfo.lcims.omc.alarm.model.MdCollCycleTime;
import com.asiainfo.lcims.omc.alarm.model.MdMetric;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.omc.report.conf.UploadConf;
import com.asiainfo.lcims.omc.report.dao.GdcuMetricDataDAO;
import com.asiainfo.lcims.omc.report.model.FileTarget;
import com.asiainfo.lcims.omc.report.model.ReportChartData;
import com.asiainfo.lcims.omc.report.util.UploadUtil;
import com.asiainfo.lcims.util.FileOperate;
import com.asiainfo.lcims.util.ToolsUtils;
/**
 * 广东联通文件信息上报
 * @author zhul
 *
 */
public class GdcuUploadService extends AbstractUploadService{
    private static final Logger LOG = LoggerFactory.make();
    
    public static final String radius_delay_avg = "radius_delay_avg";
    
    @Override
    public FileTarget makeUploadTarget(UploadConf conf, List<MdCollCycleTime> cycleTimelist) {
        /**
         * 处理时延 = 
         *         radius_delay_avg： 7c5910d955c8458cbced7b00f5755db8
         请求总量 = 认证请求量、未知请求量、计费开始、计费结束、计费中间 总和
             = 认证请求总量(authen_request 7f4078a39fc94a7eb027c835aa63f753) + 计费请求总量（accounting_request 8cb2409faf07460190b807634e23abf1）
         省市 去除：省中心、省际漫游、其他
请求总量：sql
SELECT area.AREANO,SUM(DT.MVALUE) FROM metric_data_multi_08_20 DT 
LEFT JOIN bd_nas nas ON DT.ATTR1 = nas.NAS_IP 
LEFT JOIN md_area area ON nas.AREA_NO = area.AREANO
WHERE (DT.METRIC_ID = '8cb2409faf07460190b807634e23abf1' OR DT.METRIC_ID='7f4078a39fc94a7eb027c835aa63f753')
AND date_format(DT.STIME,'%Y-%m-%d %H:%i')='2019-08-20 10:00'
GROUP BY area.AREANO
处理时延：sql
SELECT DT.ATTR1, ROUND(AVG(DT.MVALUE), 2) FROM statis_data_day_08_20 DT
WHERE (DT.METRIC_ID='7c5910d955c8458cbced7b00f5755db8' OR DT.METRIC_ID='eea86684c2f347b6a377d73a29744c71')
AND DT.ATTR2='BRAS_area'
AND date_format(DT.STIME,'%Y-%m-%d %H:%i')='2019-08-20 10:00'
GROUP BY DT.ATTR1
在线用户数：sql 
在线用户数：c88092f1caab44508ea033f5f9c9a0fe    ,    online_user_num
SELECT b.AREA_NO, SUM(DT.MVALUE) FROM metric_data_multi_08_20 DT
INNER JOIN bd_nas b ON b.NAS_IP = DT.ATTR1
WHERE DT.METRIC_ID='c88092f1caab44508ea033f5f9c9a0fe'
AND date_format(DT.STIME,'%Y-%m-%d %H:%i')='2019-08-20 10:15'
GROUP BY b.AREA_NO

所有指标取：当前周期值
         */
        String stime_5min = get5minStime(cycleTimelist);
        String stime_1hour = get1hourStime(cycleTimelist);
        //查询数据
        List<ReportChartData> datas_req = GdcuMetricDataDAO.getReqTotalOrderArea(stime_5min);
        List<ReportChartData> datas_delay = GdcuMetricDataDAO.getDelayAvgOrderArea(stime_5min);
        List<ReportChartData> datas_onlineUser = GdcuMetricDataDAO.getOnlieUserOrderArea(stime_1hour);
        List<String> dataInfo = new ArrayList<String>();
        //添加表头
        dataInfo.add("时间|地市|总请求量|平均处理时延|在线用户数");
        dataInfo.addAll(makeReportData(stime_5min, datas_req, datas_delay, datas_onlineUser));
        FileTarget target = createUploadTarget(conf, dataInfo);
        return target;
    }
    
    
    private FileTarget createUploadTarget(UploadConf conf, List<String> dataInfo){
        FileTarget target = new FileTarget();
        //monitor_host_yyyyMMddhhmm.txt "/"
        String name = "omc_data_"+TimeControl.getUploadFileTimeName()+".txt";
        name=conf.getUploadProtocol().getLocalDir()+"/"+name;
        File file = FileOperate.createLocalFile(name);
        writeFile(file, dataInfo);
        target.setLocalFile(file);
        target.setUpload_dir(conf.getUploadProtocol().getUploadDir());
        return target;
    }
    
    
    private void writeFile(File file, List<String> dataInfo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : dataInfo) {
                try {
                    bw.write(line +"\n");
                } catch (Exception e) {
                    LOG.error("", e);
                }
            }
            bw.flush();
        } catch (IOException e) {
            LOG.error("create file error", e);
        }
    }
    private List<String> makeReportData(String stime, List<ReportChartData> datas_req, List<ReportChartData> datas_delay,
            List<ReportChartData> datas_onlineUser) {
        List<String> list = new ArrayList<String>();
        List<Area> arealist = getReportArea();
        if(ToolsUtils.ListIsNull(arealist))
            return list;
        stime =stime.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
        for (Area area : arealist) {
            String mark = area.getAreano();
            String line = stime + "|" + area.getName() + "|" + getVal(mark, datas_req) + "|" + getVal(mark, datas_delay)
                    + "|" + getVal(mark, datas_onlineUser);
            list.add(line);
        }
        return list;
    }
    
    private String getVal(String mark, List<ReportChartData> data){
        String val = "";
        if(ToolsUtils.ListIsNull(data))
            return val;
        for (ReportChartData reportChartData : data) {
            if(mark.equals(reportChartData.getMark())){
                val = reportChartData.getValue();
                break;
            }
        }
        return val;
    }
    
    private List<Area> getReportArea(){
        List<Area> arealist = new ArrayList<Area>();
        for (Area area : InitParam.getAreas()) {
            if (area.getAreano().equals("0022") || area.getAreano().equals("0023") || area.getAreano().equals("0024")) {
                continue;
            } else {
                arealist.add(area);
            }
        }
        return arealist;
    }
    
    /**
     * 获取5分钟周期时间
     * @param cycleTimelist
     * @return
     */
    private String get5minStime(List<MdCollCycleTime> cycleTimelist){
        MdMetric metric = InitParam.getMetricByIdentity(GdcuUploadService.radius_delay_avg);
        String stime = UploadUtil.getCurrenttime(metric, cycleTimelist);
        if(ToolsUtils.StringIsNull(stime)){
            stime = UploadUtil.getCurrenttime(metric, cycleTimelist);
        }
        return stime;
    }
    
    /**
     * 获取1hour周期时间
     * @param cycleTimelist
     * @return
     */
    private String get1hourStime(List<MdCollCycleTime> cycleTimelist){
        MdMetric metric = InitParam.getMetricByIdentity("online_user_num");
        String stime = UploadUtil.getCurrenttime(metric, cycleTimelist);
        if(ToolsUtils.StringIsNull(stime)){
            stime = UploadUtil.getCurrenttime(metric, cycleTimelist);
        }
        return stime;
    }
    
}
