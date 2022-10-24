package com.asiainfo.lcims.omc.agentserver.service.business;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.dao.ProcessOperateDao;
import com.asiainfo.lcims.omc.agentserver.enity.ProcessOperate;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.Result;
import com.asiainfo.lcims.omc.agentserver.model.ShellServer2Client;
import com.asiainfo.lcims.omc.agentserver.model.ShellWeb2Server;
import com.asiainfo.lcims.omc.agentserver.netty.ClientChannelMap;
import com.asiainfo.lcims.omc.common.OpType;
import com.asiainfo.lcims.omc.common.ResultType;
import com.asiainfo.lcims.omc.conf.InitParam;

import io.netty.channel.Channel;

/**
 * 接收WEB端 执行脚本的命令.
 * <p>
 * 消息中的ID为 process_operate表中的ID.
 * 
 * @author XHT
 *
 */
public class BusinessShellWeb2Server extends AbstractServerBusiness<ShellWeb2Server> {
    private static final Logger log = LoggerFactory.make();

    @Override
    protected void doAction(List<ShellWeb2Server> infoList, Channel channel) {
        this.sendResult2Web(channel);
        List<String> idList = this.mkIdListToStr(infoList);
        Map<String, List<ShellServer2Client>> shellData = this.getOperateById(idList);
        this.sendShell2Client(shellData);
    }

    // 向对应的客户端下发脚本
    private void sendShell2Client(Map<String, List<ShellServer2Client>> shellData) {
        for (String clientIp : shellData.keySet()) {
            Channel channel = ClientChannelMap.getChannelByIp(clientIp);
            if (channel == null) {
                log.error("mark:[" + mark + "]. [" + clientIp + "] not a collect_client.");
                continue;
            }
            String msg = this.mkMsgSend2Client(shellData.get(clientIp));
            log.info("mark:[" + mark + "].send to [" + clientIp + "] shellMsg:"
                    + msg.replace(InitParam.getDeLimiter(), ""));
            channel.writeAndFlush(msg);
        }
    }

    // 构造向客户端发送的消息
    private String mkMsgSend2Client(List<ShellServer2Client> msgList) {
        BaseValue<ShellServer2Client> req = new BaseValue<ShellServer2Client>();
        req.setMark(mark);
        req.setOptype(OpType.SHELL_AGENTSERVER_CLIENT.getType());
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
    private Map<String, List<ShellServer2Client>> getOperateById(List<String> idList) {
        ProcessOperateDao dao = new ProcessOperateDao();
        List<ProcessOperate> list = dao.getMdHostMetricById(idList);
        Map<String, List<ShellServer2Client>> shellMap = new HashMap<String, List<ShellServer2Client>>();
        for (ProcessOperate o : list) {
            String ip = o.getHostIp();
            List<ShellServer2Client> tmpList = shellMap.get(ip);
            if (tmpList == null) {
                tmpList = new LinkedList<ShellServer2Client>();
            }
            ShellServer2Client shell = new ShellServer2Client();
            shell.setId(o.getId());
            shell.setShell(o.getProcessScript());
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
        resp.setOptype(OpType.SHELL_AGENTSERVER_WEB.getType());
        return JSON.toJSONString(resp) + InitParam.getDeLimiter();
    }
}
