package com.asiainfo.lcims.omc.socket;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.socket.common.OpType;
import com.asiainfo.lcims.omc.socket.model.BaseValue;
import com.asiainfo.lcims.omc.socket.model.MetricWeb2Server;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

public class ChatClientRunner implements Runnable{
	private static final Logger LOG = LoggerFactory.getLogger(ChatClientRunner.class);
	public static final BusinessConf BUSCONF = new BusinessConf();
	private List<String> iplist;
	
	public ChatClientRunner(List<String> iplist){
		this.iplist = iplist;
	}
	
	@Override
	public void run() {
		String result = "";
        String collectServerIp = BUSCONF.getStringValue("collect_server_ip");
        String collectServerPort = BUSCONF.getStringValue("collect_server_port");
        if(ToolsUtils.StringIsNull(collectServerIp)||ToolsUtils.StringIsNull(collectServerPort)){
        	result = "采集服务端ip或端口没有配置collectServerIp="+collectServerIp+";collectServerPort="+collectServerPort;
        	LOG.info(result);
        	return;
        }
        BaseValue<MetricWeb2Server> req = new BaseValue<MetricWeb2Server>();
        List<MetricWeb2Server> infoList = new LinkedList<MetricWeb2Server>();
        for (String ip : iplist) {
    		MetricWeb2Server tmp = new MetricWeb2Server();
            tmp.setIp(ip);
            infoList.add(tmp);
    	}
        String uuid = IDGenerateUtil.getUuid();
        req.setInfo(infoList);
        req.setMark(uuid);
        req.setOptype(OpType.METRIC_WEB_AGENTSERVER.getType());
        LOG.info("下发接口发送{ uuid="+uuid+",iplist="+iplist.toString()+",optype="+OpType.METRIC_WEB_AGENTSERVER.getType()+" }");
        String str = JSON.toJSONString(req);
        SimpleChatClient client = new SimpleChatClient(collectServerIp, Integer.parseInt(collectServerPort));
        try {
			result = client.sendMsg(str);
			LOG.info("下发接口接收 success:"+result);
		} catch (Exception e) {
			result="TIME_OUT";
			LOG.error("下发失败 fail:"+e.getMessage(), e);
		}
	}

}
