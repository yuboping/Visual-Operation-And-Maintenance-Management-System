package com.asiainfo.lcims.omc.agentserver;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.netty.CollectServer;
import com.asiainfo.lcims.omc.agentserver.service.DbDataCache;

public class MainServer {
    private static final Logger log = LoggerFactory.make();

    public static void main(String[] args) {
        log.info("loading init data...");
        // 服务端启动前主动加载常用配置信息
        DbDataCache.loading();
        log.info("starting server...");
        // 启动采集服务端
        new CollectServer().start();
    }
}
