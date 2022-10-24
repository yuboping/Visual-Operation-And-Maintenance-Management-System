package com.asiainfo.lcims.omc.agentserver.service.business;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.dao.AlarmFaultHandleHisDao;
import com.asiainfo.lcims.omc.agentserver.enity.AlarmFaultHandleHis;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.FaultShellServer2Client;
import com.asiainfo.lcims.omc.agentserver.model.Result;
import com.asiainfo.lcims.omc.agentserver.model.ShellWeb2Server;
import com.asiainfo.lcims.omc.agentserver.netty.ClientChannelMap;
import com.asiainfo.lcims.omc.common.AlarmFaultDetaliState;
import com.asiainfo.lcims.omc.common.OpType;
import com.asiainfo.lcims.omc.common.ResultType;
import com.asiainfo.lcims.omc.conf.InitParam;

import io.netty.channel.Channel;

/**
 * 接收web端故障脚本信息
 */
public class BusinessFaultShellWeb2Server extends AbstractServerBusiness<ShellWeb2Server> {
    private static final Logger log = LoggerFactory.make();

    @Override
    protected void doAction(List<ShellWeb2Server> infoList, Channel channel) {
        //返回web端信息
        this.sendResult2Web(channel);
        List<String> idList = this.mkIdListToStr(infoList);
        //获取下发信息 MD_ALARM_FAULT_HANDLE_HIS
        Map<String, List<FaultShellServer2Client>> shellData = this.getAlarmFaultScriptById(idList);
        this.sendShell2Client(shellData);
    }

    // 向对应的客户端下发脚本
    private void sendShell2Client(Map<String, List<FaultShellServer2Client>> shellData) {
        for (String clientIp : shellData.keySet()) {
            Channel channel = ClientChannelMap.getChannelByIp(clientIp);
            if (channel == null) {
                log.error("mark:[" + mark + "]. [" + clientIp + "] not a collect_client.");
                //状态修改为 未下发
                boolean flag = new AlarmFaultHandleHisDao().updateFaultDetailById(shellData.get(clientIp),
                        AlarmFaultDetaliState.UN_SEND);
                log.info("mark:[" + mark + "]. ALARM_FAULT_DETAIL updated state is un_send:" + flag);
                continue;
            }
            String msg = this.mkMsgSend2Client(shellData.get(clientIp));
            log.info("mark:[" + mark + "].send to [" + clientIp + "] shellMsg:"
                    + msg.replace(InitParam.getDeLimiter(), ""));
            channel.writeAndFlush(msg);
            //状态修改为 已下发
            boolean flag = new AlarmFaultHandleHisDao().updateFaultDetailById(shellData.get(clientIp),
                    AlarmFaultDetaliState.SEND_SUCCESS);
            log.info("mark:[" + mark + "]. ALARM_FAULT_DETAIL updated state is send success:" + flag);
        }
    }

    // 构造向客户端发送的消息
    private String mkMsgSend2Client(List<FaultShellServer2Client> msgList) {
        BaseValue<FaultShellServer2Client> req = new BaseValue<FaultShellServer2Client>();
        req.setMark(mark);
        req.setOptype(OpType.SHELL_FAULT_AGENTSERVER_CLIENT.getType());
        req.setInfo(msgList);

        return JSON.toJSONString(req) + InitParam.getDeLimiter();
    }

    // 将数据信息转换成数据库sql中in方法识别的字符串
    private List<String> mkIdListToStr(List<ShellWeb2Server> infoList) {
        List<String> newIpList = new LinkedList<String>();
        for (ShellWeb2Server tmp : infoList) {
            newIpList.add(tmp.getId());
        }
        return newIpList;
    }

    // 根据接收到的id读取所有的操作列表,并根据主机ip进行分类
    private Map<String, List<FaultShellServer2Client>> getAlarmFaultScriptById(List<String> idList) {
        AlarmFaultHandleHisDao dao = new AlarmFaultHandleHisDao();
        List<AlarmFaultHandleHis> list = dao.getAlarmFaultHandleHisById(idList);
        Map<String, List<FaultShellServer2Client>> shellMap = new HashMap<String, List<FaultShellServer2Client>>();
        for (AlarmFaultHandleHis o : list) {
            String ip = o.getHostIp();
            List<FaultShellServer2Client> tmpList = shellMap.get(ip);
            if (tmpList == null) {
                tmpList = new LinkedList<FaultShellServer2Client>();
            }
            FaultShellServer2Client shell = new FaultShellServer2Client();
            shell.setId(o.getId());
            shell.setAlarmFaultId(o.getAlarmFaultId());
            shell.setShell(o.getFaultScript());
            tmpList.add(shell);
            shellMap.put(ip, tmpList);
        }

        return shellMap;
    }

    // 向WEB端返回接收标识
    private void sendResult2Web(Channel channel) {
        String msg = this.mkMsgSendWeb(ResultType.SUCCESS);
        log.info(
                "mark:[" + mark + "].return response:" + msg.replace(InitParam.getDeLimiter(), ""));
        channel.writeAndFlush(msg);
    }

    private String mkMsgSendWeb(String resultType) {
        // 向调用方返回操作结果
        BaseValue<Result> resp = new BaseValue<Result>();
        List<Result> res_list = new LinkedList<Result>();
        Result result = new Result();
        result.setResult(resultType);
        res_list.add(result);
        resp.setInfo(res_list);
        resp.setMark(mark);
        resp.setOptype(OpType.SHELL_FAULT_AGENTSERVER_WEB.getType());
        return JSON.toJSONString(resp) + InitParam.getDeLimiter();
    }
}
