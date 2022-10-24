package com.asiainfo.lcims.omc.boot;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.lcbmi.utils.server.AbstractServer;
import com.asiainfo.lcims.lcbmi.utils.server.AbstractServerFactory;
import com.asiainfo.lcims.lcbmi.utils.server.ExitCode;
import com.asiainfo.lcims.lcbmi.utils.server.FrameWork;
import com.asiainfo.lcims.lcbmi.utils.server.ServerConf;
import com.asiainfo.lcims.lcbmi.utils.server.ServerHelper;

/**
 * 主程序入口
 * 
 * @author sillywolf
 * 
 */
public class MainServer {
    public static final JettyServerConf conf = new JettyServerConf();
    private static final Logger LOG = LoggerFactory.getLogger(MainServer.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        AbstractServer server;
        LOG.info("MainServer Called");
        try {
            FrameWork frameWorkType = FrameWork.valueOf(conf.getServFrameWork());
            Class<? extends AbstractServerFactory> factoryClass = frameWorkType.getServerFatory();
            Constructor<? extends AbstractServerFactory> con = factoryClass
                    .getConstructor(ServerConf.class);
            AbstractServerFactory serverFactory = con.newInstance(conf);
            server = serverFactory.getServer();
            server.startServer();
        } catch (Exception e) {
            LOG.error("Server Start Failed : ", e);
            ServerHelper.shutDownProcess(ExitCode.FATAL, MainServer.class);
        }

    }

}
