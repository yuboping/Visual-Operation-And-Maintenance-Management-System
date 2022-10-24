package com.asiainfo.lcims.omc.report.conf;

import com.ailk.lcims.lcbmi.config.Configuration;
import com.asiainfo.lcims.omc.report.model.UploadProtocol;
import com.asiainfo.lcims.util.ToolsUtils;

/**
 * 上报方式配置
 * @author zhul
 *
 */
public class UploadConf extends Configuration {

    /** 默认的配置文件名 */
    private static final String DEFAULT_CONFIG_NAME = "uploadconfig.properties";


    public UploadConf() {
        super();
        this.configLoader = initConfigLoader(DEFAULT_CONFIG_NAME);
        configLoader.loadConfig();
    }

    public UploadConf(String configName) {
        super();
        this.configLoader = initConfigLoader(configName);
        configLoader.loadConfig();
    }

    /**
     * 获取upload配置信息
     * @return
     */
    public UploadProtocol getUploadProtocol(){
        UploadProtocol uploadProtocol = new UploadProtocol();
        String serverIp = getValueOrDefault("serverIp", "");
        String serverIpSB = getValueOrDefault("serverIpSB", "");
        int serverPort = getIntValueOrDefault("serverPort",22);
        String protocol = getValueOrDefault("protocol", "");
        String protocolSB = getValueOrDefault("protocolSB", "");
        String loginName = getValueOrDefault("loginName", "");
        String loginNameSB = getValueOrDefault("loginNameSB", "");
        String password = getValueOrDefault("password", "");
        String passwordSB = getValueOrDefault("passwordSB", "");
        String localDir = getValueOrDefault("localDir", "");
        String uploadDir = getValueOrDefault("uploadDir", "");
        String uploadDirPM = getValueOrDefault("uploadDirPM", "");
        String uploadDirSb = getValueOrDefault("uploadDirSb", "");
        boolean uploadOk = getBooleanValueOrDefault("uploadOk");
        String suffix = getValueOrDefault("suffix", "");
        boolean addSuffix = getBooleanValueOrDefault("addSuffix");
        String dataInterType = getValueOrDefault("dataInterType", "");
        String vendorName = getValueOrDefault("vendorName", "");
        String resourceFileNamePrefix = getValueOrDefault("resourceFileNamePrefix", "");
        String performFileNamePrefix = getValueOrDefault("performFileNamePrefix", "");
        String elementType = getValueOrDefault("elementType", "");
        String cmVersion = getValueOrDefault("cmVersion", "");
        String pmVersion = getValueOrDefault("pmVersion", "");
        String objectType = getValueOrDefault("objectType", "");
        String rmUID = getValueOrDefault("rmUID", "");
        uploadProtocol.setServerIp(serverIp).setServerPort(serverPort).setProtocol(protocol)
                .setLoginName(loginName).setPassword(password).setLocalDir(localDir)
                .setUploadDir(uploadDir).setUploadOk(uploadOk).setAddSuffix(addSuffix)
                .setUploadDirPM(uploadDirPM).setSuffix(suffix).setUploadDirSb(uploadDirSb)
                .setServerIpSB(serverIpSB).setLoginNameSB(loginNameSB).setPasswordSB(passwordSB)
                .setProtocolSB(protocolSB).setDataInterType(dataInterType)
                .setResourceFileNamePrefix(resourceFileNamePrefix)
                .setPerformFileNamePrefix(performFileNamePrefix).setVendorName(vendorName)
                .setElementType(elementType).setCmVersion(cmVersion).setPmVersion(pmVersion)
                .setObjectType(objectType).setRmUID(rmUID);
        return uploadProtocol;
    }

    public boolean getBooleanValueOrDefault(String name){
        String val = getValueOrDefault(name, "");
        boolean v = false;
        if(!ToolsUtils.StringIsNull(val)){
            if(val.equals("true")){
                v = true;
            }else if (val.equals("false")) {
                v = false;
            }
        }
        return v ;
    }

    public String getUploadMetricIdentitys(){
        return getValueOrDefault("metric_identitys", null);
    }

    public String getUploadMetricIdentitysSb(){
        return getValueOrDefault("metric_identitysSb", null);
    }
}