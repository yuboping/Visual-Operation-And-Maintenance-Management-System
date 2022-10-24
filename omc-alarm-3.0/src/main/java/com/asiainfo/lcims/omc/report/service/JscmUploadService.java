package com.asiainfo.lcims.omc.report.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.business.TimeControl;
import com.asiainfo.lcims.omc.alarm.model.Host;
import com.asiainfo.lcims.omc.alarm.model.MdCollCycleTime;
import com.asiainfo.lcims.omc.alarm.model.MdMetric;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.omc.report.conf.UploadConf;
import com.asiainfo.lcims.omc.report.dao.ReportMetricDataDAO;
import com.asiainfo.lcims.omc.report.model.FileTarget;
import com.asiainfo.lcims.omc.report.model.ReportChartData;
import com.asiainfo.lcims.omc.report.util.UploadUtil;
import com.asiainfo.lcims.util.FileOperate;
import com.asiainfo.lcims.util.ToolsUtils;

/**
 * 江苏移动文件信息上报
 * @author zhul
 *
 */
public class JscmUploadService extends AbstractUploadService{
    private static final Logger LOG = LoggerFactory.make();
    
    private Map<String, String> mapVal = new HashMap<String, String>();
    private List<String> metricidList = new ArrayList<String>();
    
    @Override
    public FileTarget makeUploadTarget(UploadConf conf, List<MdCollCycleTime> cycleTimelist) {
      //查询指标值信息
        String metricIdentitys = conf.getUploadMetricIdentitys();
        String[] metricIdentityArr = metricIdentitys.split(",");
        String stime = "";
        String lastTime = "";
        for (String metric_identity : metricIdentityArr) {
            MdMetric metric = InitParam.getMetricByIdentity(metric_identity);
            //查询指标采集值当前周期值信息
            stime = UploadUtil.getCurrenttime(metric, cycleTimelist);
            if(ToolsUtils.StringIsNull(stime)){
                stime = UploadUtil.getCurrenttime(metric, cycleTimelist);
            }
            List<ReportChartData> datas = ReportMetricDataDAO.getSimpleData(metric.getId(), stime);
            putMapVal(datas);
            metricidList.add(metric.getId());
            // 时间转上个周期时间
            String cyclename = InitParam.getCollCycleName(metric.getCycle_id());
            lastTime = TimeControl.lasttime(stime, cyclename, -1);
        }
        //组装文件信息 时间由当前周期时间修改为上个周期时间
        lastTime = lastTime.replaceAll("-", "/");
        FileTarget target = createUploadTarget(lastTime, conf);
        return target;
    }
    
    private FileTarget createUploadTarget(String time, UploadConf conf){
        FileTarget target = new FileTarget();
        //monitor_host_yyyyMMddhhmm.txt "/"
        String name = "monitor_host_"+TimeControl.getUploadFileTimeName()+".txt";
        name=conf.getUploadProtocol().getLocalDir()+"/"+name;
        File file = FileOperate.createLocalFile(name);
        writeFile(file,time);
        target.setLocalFile(file);
        target.setUpload_dir(conf.getUploadProtocol().getUploadDir());
        return target;
    }
    
    private void writeFile(File file,String time) {
        //查询所有主机信息
        List<Host> hostlist = InitParam.getHosts();
        int size = hostlist.size();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < size; i++) {
                try {
                    bw.write(makeContent(hostlist.get(i),time));
                } catch (Exception e) {
                    LOG.error("", e);
                }
            }
            bw.flush();
        } catch (IOException e) {
            LOG.error("create file error", e);
        }
    }
    
    private String makeContent(Host host,String time){
        String content = host.getHostname()+"("+host.getAddr()+")#"+time;
        for (String metricId : metricidList) {
            content = content + "#" + getMapVal(host.getHostid()+"_"+metricId);
        }
        content = content +"\n";
        return content;
    }
    
    private String getMapVal(String key){
        String value = mapVal.get(key);
        if(ToolsUtils.StringIsNull(value)){
            return "";
        }
        return value+"%";
    }
    
    private void putMapVal(List<ReportChartData> datas){
        if(ToolsUtils.ListIsNull(datas)){
            return;
        }
        for (ReportChartData reportChartData : datas) {
            mapVal.put(reportChartData.getMark()+"_"+reportChartData.getMetricid(), reportChartData.getValue());
        }
    }
    
    
}
