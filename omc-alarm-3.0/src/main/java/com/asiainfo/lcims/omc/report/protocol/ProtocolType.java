package com.asiainfo.lcims.omc.report.protocol;

/**
 * 协议类型
 * 
 * @author luohuawuyin
 *
 */
public enum ProtocolType {
    SFTP,
    FTP, 
    TELNET,
    SSH2,
    ;

    private ProtocolType() {
    }

    public static ProtocolType getType(String type) {
        for (ProtocolType protocol : ProtocolType.values()) {
            if (protocol.name().equalsIgnoreCase(type)) {
                return protocol;
            }
        }
        return ProtocolType.SFTP;
    }
}
