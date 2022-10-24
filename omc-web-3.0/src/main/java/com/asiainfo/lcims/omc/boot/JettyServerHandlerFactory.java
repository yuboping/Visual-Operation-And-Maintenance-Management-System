package com.asiainfo.lcims.omc.boot;

//import org.apache.axis.transport.http.AdminServlet;
//import org.apache.axis.transport.http.AxisServlet;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.IPAccessHandler;
import org.eclipse.jetty.webapp.WebAppContext;

public final class JettyServerHandlerFactory {

    public static Handler getHandle(JettyServerConf conf) {
        IPAccessHandler ipAccessHandler = getFireWallHandler(conf);
        ipAccessHandler.setHandler(getServletHandler(conf));

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] { ipAccessHandler, new DefaultHandler() });
        return handlers;
    }

    /**
     * 设置IP过滤，默认只允许指定IP访问指定资源
     * 
     * @return
     */
    private static IPAccessHandler getFireWallHandler(JettyServerConf conf) {
        IPAccessHandler ipHandler = new IPAccessHandler();
        if (!conf.ipCheck()) {
            return ipHandler;
        }
        List<String> iplist = conf.getWhiteAddrList();
        int size = iplist.size();
        for (int i = 0; i < size; i++) {
            ipHandler.addWhite((String) iplist.get(i));
        }
        iplist = conf.getBlackAddrList();
        size = iplist.size();
        for (int i = 0; i < size; i++) {
            ipHandler.addBlack((String) iplist.get(i));
        }
        return ipHandler;
    }

    /**
     * 设置servlet监听
     * 
     * @return
     */
    private static Handler getServletHandler(JettyServerConf conf) {
        WebAppContext webContext = new WebAppContext();

        webContext.setContextPath(conf.getContextPath());
        webContext.setDescriptor(conf.getResource() + "/WEB-INF/web.xml");
        webContext.setResourceBase(conf.getResource());
        webContext.setDisplayName(conf.getDisplayName());
        webContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        webContext.setConfigurationDiscovered(true);
        webContext.setParentLoaderPriority(true);
        return webContext;
    }
}
