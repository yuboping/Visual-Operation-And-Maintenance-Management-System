package com.asiainfo.lcims.omc.agentserver.netty;

import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.HeartMsg;
import com.asiainfo.lcims.omc.common.OpType;
import com.asiainfo.lcims.omc.conf.InitParam;
import com.asiainfo.lcims.omc.utils.IDGenerateUtil;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳handler
 * 
 * @author XHT
 *
 */
public class HeartbeatHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                ClientChannelMap.removeClientByChannel(ctx.channel());
            } else if (e.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(this.mkHeartStr() + InitParam.getDeLimiter());
            }
        }
    }

    private String mkHeartStr() {
        HeartMsg msg = new HeartMsg();
        msg.setMsg("HEART_MSG");
        List<HeartMsg> infoList = new LinkedList<HeartMsg>();
        infoList.add(msg);
        BaseValue<HeartMsg> req = new BaseValue<HeartMsg>();
        req.setOptype(OpType.HEART_SERVER_CLIENT.getType());
        req.setMark(IDGenerateUtil.getUuid());
        req.setInfo(infoList);
        return JSON.toJSONString(req);
    }
}
