package com.asiainfo.lcims.omc.service.hncu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfoHn;
import com.asiainfo.lcims.omc.model.configmanage.MdAlarmMode;
import com.asiainfo.lcims.omc.model.hncu.RealTimeAlarmInfo;
import com.asiainfo.lcims.omc.model.hncu.RealTimeAlarmResult;
import com.asiainfo.lcims.omc.model.hncu.SendAddress;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.hncu.RealTimeAlarmDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.service.snmp.SnmpTrapService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import com.asiainfo.lcims.omc.util.page.Page;

@Service
public class RealTimeAlarmService {

    private static final Logger LOG = LoggerFactory.getLogger(RealTimeAlarmService.class);

    @Autowired
    private RealTimeAlarmDAO realTimeAlarmDAO;

    @Resource(name = "operateHisService")
    OperateHisService operateHisService;

    public RealTimeAlarmResult handlerRealTimeAlarm(RealTimeAlarmInfo realTimeAlarmInfo) {
        RealTimeAlarmResult result = new RealTimeAlarmResult();
        String createtime = realTimeAlarmInfo.getCreatetime();
        String createNewTime = getFormatTime(createtime);
        realTimeAlarmInfo.setCreatetime(createNewTime);
        String cleartime = realTimeAlarmInfo.getCleartime();
        String clearNewTime = getFormatTime(cleartime);
        realTimeAlarmInfo.setCleartime(clearNewTime);
        try {
            int addFlag = realTimeAlarmDAO.insertRealTimeAlarm(realTimeAlarmInfo);
            if (addFlag == 0) {
                LOG.error("alarmid {} insert real time alarm fail", realTimeAlarmInfo.getAlarmid());
                result.setRet("1");
                result.setDesc("插入数据库失败");
                return result;
            }
            MdAlarmMode alarmMode = CommonInit.getAlarmModeByModetype("4");
            LOG.info("alarm mode : {}", alarmMode);
            if (alarmMode != null) {
                String modeAttr = alarmMode.getModeattr();
                List<Object> addrList = analysisAddr(modeAttr);
                SnmpTrapService snmpTrapSender = new SnmpTrapService();
                result = snmpTrapSender.sendMsg(addrList, realTimeAlarmInfo);
            } else {
                LOG.warn("alarm mode is null");
                result.setRet("1");
                result.setDesc("失败");
            }
            LOG.info("send snmp trap result alarm : {}", result);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            result.setRet("1");
            result.setDesc("失败");
        }
        return result;
    }

    public String getFormatTime(String formatTime) {
        String newTime = "";
        if (StringUtils.isBlank(formatTime)) {
            newTime = "";
        } else {
            DateTools dateTools = new DateTools("yyyy-MM-dd,HH:mm:ss");
            newTime = dateTools.getFormatDay(formatTime, "yyyy-MM-dd HH:mm:ss");
        }
        return newTime;
    }

    private List<Object> analysisAddr(String attrs) {
        List<Object> list = new ArrayList<>();
        if (StringUtils.isBlank(attrs)) {
            return list;
        }
        String[] addrs = StringUtils.split(attrs, ";");
        for (String addr : addrs) {
            if (isSyslogAddr(addr)) {
                String[] values = StringUtils.split(addr, ":");
                SendAddress send = new SendAddress(values[0], Integer.parseInt(values[1]));
                list.add(send);
            }
        }
        return list;
    }

    /**
     * 判断地址是否符合规范
     * 
     * @param addr
     * @return
     */
    private boolean isSyslogAddr(String addr) {
        boolean flag = false;
        if (ToolsUtils.isSyslogAddr(addr))
            return true;
        return flag;
    }

    /**
     * 查询告警信息
     * @param mdAlarmInfoHn
     * @param params
     * @param pageNumber
     * @return
     */
    public Page getAlarmInfoHnList(MdAlarmInfoHn mdAlarmInfoHn, Map<String, Object> params, int pageNumber){

        Page page = new Page(0);

        try {
            page.setPageNumber(pageNumber);

            //查询总数
            int totalCount = realTimeAlarmDAO.getAlarmInfoHnCount(mdAlarmInfoHn);

            page.setTotalCount(totalCount);
            //查询分页数据
            if (totalCount > 0) {
                List<MdAlarmInfoHn> data = realTimeAlarmDAO.getAlarmInfoHnList(mdAlarmInfoHn, page);
                page.setPageList(data);
            }
        }catch (Exception e){
            LOG.info("get alarminfolist error ! because : " + e.getMessage(), e);
        }
        return page;
    }

    /**
     * 确认告警信息
     * @param alarmArray
     * @return
     */
    public WebResult confirmAlarmInfo(String[] alarmArray) {
        String logDesc = "";
        if (alarmArray != null && alarmArray.length != 0) {
            for (String alarmId : alarmArray) {
                MdAlarmInfoHn mdAlarmInfoHn = realTimeAlarmDAO.getAlarmInfoWithId(alarmId);
                logDesc = "告警信息:" + mdAlarmInfoHn.getAlarm_msg();
                realTimeAlarmDAO.confirmAlarmInfoById(alarmId);
            }
        }
        //用户日志记录
        logDesc = logDesc.substring(1, logDesc.length());
        operateHisService.insertOperateHistory( Constant.OPERATE_HIS_ALARM_QUERY_HN, "确认数据[" + logDesc + "]");
        return new WebResult(true, "确认成功!");
    }

}
