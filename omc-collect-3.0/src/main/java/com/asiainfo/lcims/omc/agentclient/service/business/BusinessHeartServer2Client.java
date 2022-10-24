package com.asiainfo.lcims.omc.agentclient.service.business;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.HeartMsg;
import com.asiainfo.lcims.omc.common.OpType;

/**
 * 接收到agentServer心跳包,将当前主机的系统时间计入心跳内容并返回给服务端.
 * 
 * @author XHT
 *
 */
public class BusinessHeartServer2Client extends AbstractClientBusiness<HeartMsg> {

    @Override
    protected void doAction(List<HeartMsg> infoList) {
        String heartMsg = this.mkHeartStr();
        this.sendInfo2Server(heartMsg);
    }

    private String mkHeartStr() {
        HeartMsg msg = new HeartMsg();
        msg.setMsg("HEART_MSG");
        msg.setDateTimeStr(String.valueOf(new DateTime().getMillis()));
        List<HeartMsg> infoList = new LinkedList<HeartMsg>();
        infoList.add(msg);
        BaseValue<HeartMsg> req = new BaseValue<HeartMsg>();
        req.setOptype(OpType.HEART_CLIENT_SERVER.getType());
        req.setMark(mark);
        req.setInfo(infoList);
        return JSON.toJSONString(req);
    }

}
