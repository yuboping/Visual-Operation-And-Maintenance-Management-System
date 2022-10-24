package com.asiainfo.lcims.omc.report.protocol.sftp;

import com.asiainfo.lcims.omc.report.protocol.ProtocolDetail;
import com.asiainfo.lcims.omc.report.protocol.ProtocolType;

/**
 * SFTP协议相关配置
 * 
 * @author luohuawuyin
 *
 */
public class SFTPconfig extends ProtocolDetail {
    private int sessionTimeout = 60000;
    private int channelTimeout = 1000;

    public SFTPconfig() {
        super(ProtocolType.SFTP);
    }
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getChannelTimeout() {
        return channelTimeout;
    }

    public void setChannelTimeout(int channelTimeout) {
        this.channelTimeout = channelTimeout;
    }

    @Override
    public String toString() {
        return "SFTPconfig [type=" + getType() + ", serverIP=" + getServerIP() + ", serverPort="
                + getServerPort() + ",  retry=" + getRetry() + "sessionTimeout=" + sessionTimeout
                + ", channelTimeout=" + channelTimeout + "]";
    }
}
