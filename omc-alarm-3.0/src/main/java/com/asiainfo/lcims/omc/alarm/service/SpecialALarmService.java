package com.asiainfo.lcims.omc.alarm.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.business.AlarmService;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.model.MdMetric;
import com.asiainfo.lcims.omc.alarm.model.MdSpecialALarmInfo;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.omc.alarm.province.hncm.dao.QueryOnlineUserDAO;
import com.asiainfo.lcims.omc.alarm.province.hncm.model.MdOnlineUser;
import com.asiainfo.lcims.omc.report.conf.UploadConf;
import com.asiainfo.lcims.omc.report.model.FileTarget;
import com.asiainfo.lcims.omc.report.util.UploadUtil;
import com.asiainfo.lcims.util.AlarmSeqUtil;
import com.asiainfo.lcims.util.FileOperate;
import com.asiainfo.lcims.util.MetricUtil;
import com.asiainfo.lcims.util.ToolsUtils;

/**
 * 特殊告警处理service
 * 
 * @author zhul
 *
 */
public class SpecialALarmService {
    private static final Logger log = LoggerFactory.getLogger(SpecialALarmService.class);

    /**
     * 返回序列号,作为文件名使用
     * 
     * @param rule
     * @param time
     * @param alarmOtherInfo
     * @return
     */
    public static void addAlarmOtherService(MdAlarmRuleDetail rule,
            MdSpecialALarmInfo alarmOtherInfo) {
        alarmOtherInfo.setAlarm_id(rule.getAlarm_id());
        alarmOtherInfo.setRule_id(rule.getRule_id());
        alarmOtherInfo.setAlarm_level(rule.getAlarm_level());
        alarmOtherInfo.setAlarm_num(1);
        alarmOtherInfo.setAlarm_rule(rule.getAlarm_rule());
        alarmOtherInfo.setMetric_id(rule.getMetric_id());
        Integer alarm_seq = AlarmSeqUtil.getAlarmSeq();
        alarmOtherInfo.setAlarm_seq_new(alarm_seq);
        alarmOtherInfo.setClear_time(null);

        // 根据olt地址查询影响的用户，生成文件
        MdMetric metirc = InitParam.getMetricById(rule.getMetric_id());
        alarmOtherInfo.setAttr(MetricUtil.getSpecificProblemID(metirc.getMetric_identity()));
        if (MetricUtil.METRIC_OLT.equals(metirc.getMetric_identity())) {
            alarmOtherInfo.setAlarm_filename(alarm_seq.toString());
            alarmOtherInfo
                    .setFile_path(AlarmService.conf.getValueOrDefault("hncm_nms_file_path", ""));
            // olt 指标告警
            getOnlineUsersDataByOlt(metirc, alarmOtherInfo);
        }
        // 入库：当前告警信息、及历史告警信息
        addDb(alarmOtherInfo);
        // 更新序列号
        AlarmSeqUtil.updateAlarmSeq();
    }

    private static void addDb(MdSpecialALarmInfo alarmOtherInfo) {
        if (alarmOtherInfo.isExist() == true) {
            // update
            // SpecialALarmDAO.updateAlarmOtherInfo(alarmOtherInfo);
        } else if (alarmOtherInfo.isExist() == false) {
            // add
            // SpecialALarmDAO.insertAlarmOtherInfo(alarmOtherInfo);
        }
        // 告警历史记录
        // SpecialALarmDAO.insertAlarmOtherInfoHis(alarmOtherInfo);
    }

    /**
     * 根据信息查询在线用户信息，并生成文件上传至指定目录
     * 
     * @param metric
     * @param alarmOtherInfo
     */
    private static void getOnlineUsersDataByOlt(MdMetric metric,
            MdSpecialALarmInfo alarmOtherInfo) {
        /**
         * 1、查询olt数据库，返回在线用户信息list 2、list为空，生成空文件 3、list不为空，根据list生成文件
         * 4、SFTP上报到指定目录 5、线程后台启动
         */
        String oltIp = alarmOtherInfo.getAttr1();
        String filename = alarmOtherInfo.getAlarm_seq_new().toString();
        List<MdOnlineUser> userlist = QueryOnlineUserDAO.getOnlineUsers(oltIp);
        // List<MdOnlineUser> userlist 转成 list<String>
        List<String> list = new ArrayList<String>();
        for (MdOnlineUser o : userlist) {
            if (!ToolsUtils.StringIsNull(o.getUserlist())) {
                String[] users = o.getUserlist().split("\\|");
                for (String user : users) {
                    list.add(user);
                }
            }
        }
        alarmOtherInfo.setMsg_desc(
                "影响用户数：" + list.size() + "；影响用户列表文件名：" + alarmOtherInfo.getAlarm_seq_new());
        FileTarget target = createUploadTarget(UploadUtil.getUploadConf(), list, filename);
        if (target != null)
            UploadUtil.uploadFileTarget(target);
    }

    /**
     * 生成文件
     * 
     * @param conf
     * @param userlist
     * @param filename
     * @return
     */
    private static FileTarget createUploadTarget(UploadConf conf, List<String> userlist,
            String filename) {
        FileTarget target = new FileTarget();
        String filePath = conf.getUploadProtocol().getLocalDir() + "/" + filename;
        File file = FileOperate.createLocalFile(filePath);
        writeFile(file, userlist);
        target.setLocalFile(file);
        target.setUpload_dir(conf.getUploadProtocol().getUploadDir());
        return target;
    }

    private static void writeFile(File file, List<String> userlist) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String user : userlist) {
                try {
                    bw.write(user + "\n");
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            bw.flush();
        } catch (IOException e) {
            log.error("create file error", e);
        }
    }

    public static void cleanAlarmOtherService(MdAlarmRuleDetail rule,
            MdSpecialALarmInfo alarmOtherInfo) {
        alarmOtherInfo.setAlarm_num(0);
        Integer alarm_seq = AlarmSeqUtil.getAlarmSeq();
        alarmOtherInfo.setAlarm_seq_new(alarm_seq);
        // SpecialALarmDAO.updateAlarmOtherInfo(alarmOtherInfo);
        // 清除告警历史记录
        alarmOtherInfo.setAlarm_seq(alarm_seq);
        // SpecialALarmDAO.insertAlarmOtherInfoHis(alarmOtherInfo);
        // 更新序列号
        // AlarmSeqUtil.updateAlarmSeq();
    }

    public static List<MdSpecialALarmInfo> getAlarmOtherInfos() {
        // return SpecialALarmDAO.getAlarmOtherInfos();
        return null;
    }

}
