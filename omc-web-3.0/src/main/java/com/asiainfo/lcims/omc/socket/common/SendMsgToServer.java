package com.asiainfo.lcims.omc.socket.common;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.omc.exception.OmcSocketException;
import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.socket.SimpleChatClient;
import com.asiainfo.lcims.omc.socket.model.BaseValue;
import com.asiainfo.lcims.omc.socket.model.ShellWeb2Server;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SendMsgToServer {

    private static final Logger LOG = LoggerFactory.getLogger(SendMsgToServer.class);

    public static final BusinessConf BUSCONF = new BusinessConf();

    public static String sendMsg(List<ShellWeb2Server> paramList) {
        String repMsg = "脚本执行中，请稍后";
        // 获取配置文件配置的参数
        String collectServerIp = BUSCONF.getStringValue("collect_server_ip"); // 下发服务器ip
        String collectServerPort = BUSCONF.getStringValue("collect_server_port"); // 下发服务器端口
        if (ToolsUtils.StringIsNull(collectServerIp)
                || ToolsUtils.StringIsNull(collectServerPort)) {
            repMsg = "采集服务端ip或端口没有配置collectServerIp=" + collectServerIp + ";collectServerPort="
                    + collectServerPort;
            LOG.info(repMsg);
            throw new OmcSocketException(repMsg);
        }
        BaseValue<ShellWeb2Server> req = new BaseValue<ShellWeb2Server>();
        req.setOptype(OpType.SHELL_WEB_AGENTSERVER.getType());
        req.setMark(IDGenerateUtil.getUuid());
        req.setInfo(paramList);
        String str = JSON.toJSONString(req);
        SimpleChatClient client = new SimpleChatClient(collectServerIp,
                Integer.parseInt(collectServerPort));
        try {
            String repMessage = client.sendMsg(str);
            if (ToolsUtils.StringIsNull(repMessage)) {
                throw new OmcSocketException("下发失败");
            }
            LOG.info("下发接口接收 success:" + repMessage);
        } catch (Exception e) {
            repMsg = "TIME_OUT";
            LOG.error("下发失败 fail:" + e.getMessage(), e);
        }
        return repMsg;
    }
}
