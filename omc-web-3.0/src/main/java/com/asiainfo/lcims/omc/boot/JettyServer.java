package com.asiainfo.lcims.omc.boot;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.asiainfo.lcims.lcbmi.utils.server.AbstractServer;
import com.asiainfo.lcims.lcbmi.utils.server.ExitCode;
import com.asiainfo.lcims.lcbmi.utils.server.ServerConf;
import com.asiainfo.lcims.lcbmi.utils.server.ServerHelper;

public class JettyServer extends AbstractServer {
    private Server server;

    public JettyServer(String servName) {
        super();
        super.setServName(servName);
    }

    public JettyServer(ServerConf conf) {
        super(conf);
    }

    @Override
    protected void init() {
        super.init();
        JettyServerConf conf = (JettyServerConf) config;
        setServPort(conf.getServPort());

        // start jetty configig
        server = new Server();
        if (conf.getHttpsFlag()) {
            server.setConnectors(new Connector[] { getSSLConnector(conf, server) });
        } else {
            server.setConnectors(new Connector[] { getNioConnector(conf, server) });
        }
        server.setHandler(JettyServerHandlerFactory.getHandle(conf));
    }

    private ServerConnector getNioConnector(JettyServerConf conf, Server server) {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(getServPort());
        connector.setIdleTimeout(conf.getMaxIdleTime());
        connector.setAcceptQueueSize(conf.getPoolSize());
        return connector;
    }

    private ServerConnector getSSLConnector(JettyServerConf conf, Server server) {// 设置ssl连接器
        SslContextFactory cf = new SslContextFactory();
        cf.setKeyStorePath(conf.getResource() + "/WEB-INF/jetty.keystore");
        cf.setKeyStorePassword("egbvbmpf");
        cf.setKeyManagerPassword("egbvbmpf");
        cf.setProtocol("TLSv1.2");
        ServerConnector ssl_connector = new ServerConnector(server, cf);
        ssl_connector.setPort(getServPort());
        ssl_connector.setIdleTimeout(conf.getMaxIdleTime());
        ssl_connector.setAcceptQueueSize(conf.getPoolSize());
        return ssl_connector;
    }

    @Override
    public void startServer() {
        welcome();
        LOG.info("Server Instance[{}] Call Start : port {}", this.getClass().getSimpleName(),
                config.getServPort());
        if (startFlag.compareAndSet(false, true)) {
            try {
                init();
                server.start();
                server.join();
                LOG.info("Server Instance[{}] Start Success At Port[{}]  ", this.getClass()
                        .getSimpleName(), this.getServPort());
            } catch (Exception e) {
                LOG.error("Server Instance[{}] Start Failed At Port[{}] , {}", getClass()
                        .getSimpleName(), getServPort(), e);
                ServerHelper.shutDownProcess(ExitCode.FATAL, this.getClass());
            }
        }
        LOG.info("Server Instance[{}] End Start ", this.getClass().getSimpleName());

    }

    /**
     * 输出版本等信息
     */
    private void welcome() {
        LOG.info(getWelcomeMessage());
    }

    @Override
    public void stopServer() {
        if (startFlag.get() && server != null) {
            try {
                server.stop();
                server.destroy();
                startFlag.set(false);
                LOG.info("Server Instance[{}] Stop Success ! ", this.getClass().getSimpleName());
            } catch (Exception e) {
                LOG.error("Server Instance[{}] Stop Failed For {} ! ", this.getClass()
                        .getSimpleName(), e);
            }
        }
        ServerHelper.shutDownProcess(ExitCode.USERCALL, this.getClass());
    }

    @Override
    public String dump() {
        return "Hello ,This is JettyServer (" + this.getServName() + ") At " + getAddr();
    }

    @Override
    protected String getWelcomeMessage() {
        return null;
    }

}
