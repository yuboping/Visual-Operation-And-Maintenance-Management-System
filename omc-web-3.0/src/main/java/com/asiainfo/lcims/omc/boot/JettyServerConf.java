package com.asiainfo.lcims.omc.boot;

import com.ailk.lcims.lcbmi.config.Configuration;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.lcbmi.utils.server.AbstractServer;
import com.asiainfo.lcims.lcbmi.utils.server.ServerConf;
import org.slf4j.Logger;

import java.util.*;

/**
 * 服务端配置文件
 * 
 * @author qinwoli
 * 
 */
public class JettyServerConf extends Configuration implements ServerConf {

    private static final Logger log = LoggerFactory.make();

    /** 默认的配置文件名 */
    private static final String DEFAULT_CONFIG_NAME = "JettyServer.properties";

    /** 防火墙配置的section name */
    private static final String FIREWALL_SECTION = "firewall";

    /** handler section name */
    private static final String HANDLER_SECTION = "handler";

    /** 默认的server监听端口 */
    private static final int DEFAULT_PORT = 9999;

    public JettyServerConf() {
        super();
        this.configLoader = initConfigLoader(DEFAULT_CONFIG_NAME);
        configLoader.loadConfig();
    }

    public JettyServerConf(String configName) {
        super();
        this.configLoader = initConfigLoader(configName);
        configLoader.loadConfig();
    }

    /**
     * 服务端框架
     * 
     * @return
     */
    public String getServFrameWork() {
        return getValueOrDefault("framework", "jetty").toUpperCase();
    }

    /**
     * 传输层协议
     */
    public String getProtocol() {
        return getValueOrDefault("protocol", "tcp").toUpperCase();
    }

    /**
     * 服务端绑定的端口
     * 
     * @return
     */
    public int getServPort() {
        return getIntValueOrDefault("port", DEFAULT_PORT);
    }

    /**
     * 是否启用https
     * 
     * @return
     */
    public boolean getHttpsFlag() {
        return "true".equals(getValueOrDefault("httpsflag", "false")) ? true : false;
    }
    
    /**
     * 是否启用忘记密码
     * 
     * @return
     */
    public boolean getForgetFlag() {
        return "true".equals(getValueOrDefault("forgetflag", "false")) ? true : false;
    }
    
    /**
     * 服务端绑定地址,用于多网卡环境下指定需要绑定的网卡,默认不指定,绑定所有地址
     * 
     * @return 返回bindaddr对应的属性,默认返回null
     */
    public String getBindAddr() {
        return getValueOrDefault("bindaddr", null);
    }

    /**
     * 返回server端的白名单,whitelist对应的多个地址,以<b>';'</b>分隔
     * 
     * @return 返回server端的白名单,默认返回emptyList;
     */
    public List<String> getWhiteAddrList() {
        String strAddrs = getFireWallProperty("whitelist");
        if (strAddrs == null) {
            return Collections.emptyList();
        }
        List<String> whiteAddrs = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(strAddrs, ";");
        while (st.hasMoreTokens()) {
            whiteAddrs.add(st.nextToken());
        }
        return whiteAddrs;
    }

    /**
     * 返回server端的黑名单<br>
     * blacklist对应的多个地址,以
     * 
     * <pre class="code">
     * <b>;</b>
     * </pre>
     * 
     * 分隔
     * 
     * @return 返回server端的黑名单,默认返回emptyList;
     */
    public List<String> getBlackAddrList() {
        String strAddrs = getFireWallProperty("blacklist");
        if (strAddrs == null) {
            return Collections.emptyList();
        }
        List<String> blackAddrs = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(strAddrs, ";");
        while (st.hasMoreTokens()) {
            blackAddrs.add(st.nextToken());
        }
        return blackAddrs;
    }

    /**
     * 返回section为firewall的属性值，无对应值则返回null
     * 
     * @param propName
     * @return
     */
    public String getFireWallProperty(String propName) {
        String value = getSectionValueOrDefault(propName, FIREWALL_SECTION, null);
        if (null == value || ("").equals(value.trim())) {
            return null;
        }
        return value;
    }

    /**
     * server基于链式处理模型，此处应返回对应的处理链，并且保持对应的顺序
     * 
     * @return
     */
    public Map<String, String> getHandlers() {
        return configLoader.getSectionAllValues(HANDLER_SECTION);
    }

    public Class<? extends AbstractServer> getTCPServer() {
        try {
            String name = getValueOrDefault("tcpserver", "");
            Class<? extends AbstractServer> subClass = Class.forName(name)
                    .asSubclass(AbstractServer.class);
            return subClass;
        } catch (ClassNotFoundException e) {
            log.error("getTCPServer error : {}", e);
            return null;
        }
    }

    public Class<? extends AbstractServer> getUDPServer() {
        try {
            String name = getValueOrDefault("udpserver", "");
            Class<? extends AbstractServer> subClass = Class.forName(name)
                    .asSubclass(AbstractServer.class);
            return subClass;
        } catch (ClassNotFoundException e) {
            log.error("getUDPServer error : {}", e);
            return null;
        }
    }

    public int getMaxIdleTime() {
        return getIntValueOrDefault("maxIdleTime", 3000);
    }

    public int getPoolSize() {
        return getIntValueOrDefault("poolSize", 100);
    }

    public boolean ipCheck() {
        return "true".equals(getValueOrDefault("checkIp", "false"));
    }

    public String getResource() {
        return getValueOrDefault("resource", "src/main/webapp");
    }

    public String getContextPath() {
        return getValueOrDefault("contextPath", "/3adap");
    }

    public String getDisplayName() {
        return getValueOrDefault("displayName", "3adap");
    }

    public String getProvince() {
        return getValueOrDefault("province", "jscu");
    }

    public String getProvinceName() {
        return getValueOrDefault("provincename", "江苏");
    }

    public String getHomePage() {
        return getValueOrDefault("homepage", "/view/monitor/app");
    }

    public String getSystemName() {
        return getValueOrDefault("systemname", "WLAN监控管理系统");
    }

    public String getShortName() {
        return getValueOrDefault("shortname", "监控");
    }

    public boolean getShowDateSelector() {
        return "true".equals(getValueOrDefault("showDateSelector", "false"));
    }

    /**
     * 是否开启定时任务
     * 
     * @return
     */
    public boolean openTimedTask() {
        String value = getValueOrDefault("timedTask", "true");
        return "true".equals(value);
    }

    /**
     * 
     * @Title: getAutoDeploySwitch
     * @Description: TODO(自动部署开关)
     * @param @return 参数
     * @return boolean 返回类型
     * @throws
     */
    public boolean getAutoDeploySwitch() {
        String value = getValueOrDefault("autodeploySwitch", "false");
        return "true".equals(value);
    }
}
