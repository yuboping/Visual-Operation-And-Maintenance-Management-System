package com.asiainfo.lcims.omc.report.protocol.ftp;

import com.asiainfo.lcims.omc.report.protocol.ProtocolDetail;
import com.asiainfo.lcims.omc.report.protocol.ProtocolType;

public class FTPconfig extends ProtocolDetail {

    public FTPconfig() {
        super(ProtocolType.FTP);
    }

    @Override
    public String toString() {
        return "FTPconfig [type=" + getType() + ", serverIP=" + getServerIP() + ", serverPort="
                + getServerPort() + ", retry=" + getRetry() + "]";
    }
}
