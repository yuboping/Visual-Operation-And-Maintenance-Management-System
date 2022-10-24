package com.asiainfo.lcims.omc.agentclient.service.business;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentclient.netty.CollectClient;
import com.asiainfo.lcims.omc.agentserver.model.BaseData;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.utils.IDGenerateUtil;

/**
 * AgentClient业务处理
 * 
 * @author XHT
 *
 * @param <T>
 */
public abstract class AbstractClientBusiness<T extends BaseData> {
    private static final Logger log = LoggerFactory.make();

    protected String mark = IDGenerateUtil.getUuid();

    /**
     * 方法入口,开始进行对应的业务处理
     * 
     * @param mark:请求消息中的mark信息
     * @param jsonInfo:请求的json格式数据
     * @param channel:当前请求的通道信息.
     */
    public void doBusiness(String mark, String jsonInfo) {
        // 请求消息中的mark覆盖 随机生成的mark.
        this.mark = mark;

        // 解析JSON数据
        BaseValue<T> req = this.parseObject(jsonInfo);

        // 如果请求数据不为空,则进行对应的业务处理
        if (req != null) {
            this.doAction(req.getInfo());
        } else {
            log.error("mark:[" + mark + "].infoList is empty.please check the request msg.");
        }
    }

    /**
     * 具体业务操作
     * 
     * @param infoList:请求中的具体信息.
     * @param channel:当前请求的通道信息.
     */
    protected abstract void doAction(List<T> infoList);

    /**
     * 解析JSON
     * 
     * @param jsonInfo
     * @return
     */
    protected BaseValue<T> parseObject(String jsonInfo) {
        BaseValue<T> req = null;
        try {
            Type sType = getClass().getGenericSuperclass();
            Type[] generics = ((ParameterizedType) sType).getActualTypeArguments();
            Type tt = generics[0];
            req = JSON.parseObject(jsonInfo, new TypeReference<BaseValue<T>>(tt) {
            });
        } catch (Exception e) {
            log.error("mark:[" + mark + "].JSON.parseObject error:", e);
        }
        return req;
    }

    // 向对应客户端主机发送消息
    protected void sendInfo2Server(String info) {
        CollectClient.getInstance().sendData(info);
    }
}
