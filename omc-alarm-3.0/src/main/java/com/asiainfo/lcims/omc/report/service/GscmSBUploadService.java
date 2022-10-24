package com.asiainfo.lcims.omc.report.service;

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
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GscmSBUploadService extends AbstractUploadService {

    private static final Logger LOG = LoggerFactory.make();

    @Override
    public FileTarget makeUploadTarget(UploadConf conf, List<MdCollCycleTime> cycleTimelist) {
        //查询指标值信息
        String metricIdentitys = conf.getUploadMetricIdentitysSb();
        String[] metricIdentityArr = metricIdentitys.split(",");
        String stime = "";
//        String lastTime = "";
        FileTarget target = new FileTarget();
        String fileName = null;

        String time = TimeControl.getUploadFileTimeName();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = null;
        try {
            date = format.parse(time);
            Date afterDate = new Date(date .getTime());
            time = format.format(afterDate);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }


        for (String metric_identity : metricIdentityArr) {
            MdMetric metric = InitParam.getMetricByIdentity(metric_identity);
            //查询指标采集值当前周期值信息
            stime = UploadUtil.getCurrenttime(metric, cycleTimelist);
            if(ToolsUtils.StringIsNull(stime)){
                stime = UploadUtil.getCurrenttime(metric, cycleTimelist);
            }
            List<ReportChartData> datas = new ArrayList<>();
            if("read_wrtn_speed".equals(metric_identity)) {
                fileName = "op_host_disk_"+time+".csv";
                datas = ReportMetricDataDAO.getReadWrtnSpeedData(metric.getId(), stime);
            }
            if ("multi_cpu_rate".equals(metric_identity)){
                fileName = "op_host_global_cpu_"+time+".csv";
                datas = ReportMetricDataDAO.getCpuUseRateData(metric.getId(), stime);
            }
            if ("filesys_use_rate".equals(metric_identity)){
                fileName = "op_host_global_disk_"+time+".csv";
                datas = ReportMetricDataDAO.getFilesysUseRateData(metric.getId(), stime);
            }
            if ("memory_use_rate".equals(metric_identity)){
                fileName = "op_host_mem_"+time+".csv";
                datas = ReportMetricDataDAO.getMemoryUseRateData(metric.getId(), stime);
            }

            // 时间转上个周期时间
//            String cyclename = InitParam.getCollCycleName(metric.getCycle_id());
//            lastTime = TimeControl.lasttime(stime, cyclename, -1);
            target.getFileTargetList().add(createUploadTarget(stime, conf, fileName, datas));
        }
        return target;
    }

    private FileTarget createUploadTarget(String time, UploadConf conf,String fileName, List<ReportChartData> datas) {
        FileTarget target = new FileTarget();
        String name = conf.getUploadProtocol().getLocalDir()+"/"+fileName;
        String head = null;

        if (fileName.startsWith("op_host_disk_")) {
            head="设备IP地址|磁盘名称|读速率|写速率|数据开始时间\r\n";
        }
        if (fileName.startsWith("op_host_global_cpu_")) {
            head="设备IP|cpuId|使用率|数据开始时间\r\n";
        }
        if (fileName.startsWith("op_host_global_disk_")) {
            head="设备IP|磁盘路径|使用率|数据开始时间\r\n";
        }
        if(fileName.startsWith("op_host_mem_")) {
            head="设备IP|内存使用率|数据开始时间\r\n";
        }

        File file = FileOperate.createLocalFile(name);
        writeFile(file,time,head,fileName,datas);
        target.setLocalFile(file);
        target.setUpload_dir(conf.getUploadProtocol().getUploadDirSb());
        return target;
    }

    private void writeFile(File file,
                           String time,
                           String head,
                           String fileName,
                           List<ReportChartData> datas) {
        //查询所有主机信息
        List<Host> hostlist = InitParam.getHosts();
        FileWriter fw = null;
        int size = hostlist.size();
        try {
            fw = new FileWriter(file); // true表示追加
            if(head!=null) {
                fw.write(head);
            }
            for (int i = 0; i < size; i++) {
                try {
                    fw.write(makeContent(hostlist.get(i),time,fileName,datas));
                } catch (Exception e) {
                    LOG.error("", e);
                }
            }
            fw.flush();
        } catch (IOException e) {
            LOG.error("create file error", e);
        }finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                }
            }
        }
    }

    private String makeContent(Host host,
                               String time,
                               String fileName,
                               List<ReportChartData> datas){
        String content = "";
        Boolean flag = true;
        for (ReportChartData data : datas) {
            if (host.getHostid().equals(data.getMark())) {
                flag = false;
                content += host.getAddr();
                if (fileName.startsWith("op_host_disk_")) {
                    content += "|" + data.getAttr() + "|" + data.getAttr1() + "|" + data.getItem() + "|" + time + "|";
                }
                if (fileName.startsWith("op_host_global_cpu_")) {
                    content += "|" + data.getItem() + "|" + data.getValue() + "|" + time + "|";
                }
                if (fileName.startsWith("op_host_global_disk_")) {
                    content += "|" + data.getAttr() + "|" + data.getValue() + "|" + time + "|";
                }
                if (fileName.startsWith("op_host_mem_")) {
                    content += "|" + data.getValue() + "|" + time + "|";
                }
                content += "\n";
            }
        }
        if (flag) {
            content += host.getAddr();
            if (fileName.startsWith("op_host_disk_")) {
                content += "|" + "|" + "|" + "|" + time + "|";
            }
            if (fileName.startsWith("op_host_global_cpu_")) {
                content += "|" + "|" + "|" + time + "|";
            }
            if (fileName.startsWith("op_host_global_disk_")) {
                content += "|" + "|" + "|" + time + "|";
            }
            if (fileName.startsWith("op_host_mem_")) {
                content += "|" + "|" + time + "|";
            }
            content += "\n";
        }

        return content;
    }

}
