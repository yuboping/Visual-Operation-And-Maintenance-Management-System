package com.asiainfo.lcims.omc.report.model;

public class UploadProtocol {

    /** 上报IP */
    private String serverIp;

    private String serverIpSB;

    /** 上报端口 */
    private int serverPort;

    /** 登录用户名 */
    private String loginName;

    private String loginNameSB;

    /** 上报IP */
    private String password;

    private String passwordSB;

    /** 上报地址 */
    private String uploadDir;

    /** 性能上报地址 */
    private String uploadDirPM;

    private String uploadDirSb;

    /** 本地文件存放地址 */
    private String localDir;

    /** 上报协议 */
    private String protocol;

    private String protocolSB;

    /** 上传文件完成后是否上报.ok文件 */
    private boolean uploadOk = false;

    /** 上传文件完成后是否追加文件后缀 */
    private boolean addSuffix = false;

    private String suffix = "";

    /** 北向接口数据接口类型 */
    private String dataInterType = "";

    /** 资源文件名前缀 */
    private String resourceFileNamePrefix = "";

    /** 性能文件名前缀 */
    private String performFileNamePrefix = "";

    /** 设备供应商 */
    private String vendorName = "";

    /** 网元类型 */
    private String elementType = "";

    /** 资源数据版本 */
    private String cmVersion = "";

    /** 性能数据版本 */
    private String pmVersion = "";

    /** 网络资源类别名称 */
    private String objectType = "";

    /** 网络资源对象的全网唯一标识 */
    private String rmUID = "";

    public String getServerIp() {
        return serverIp;
    }

    public UploadProtocol setServerIp(String serverIp) {
        this.serverIp = serverIp;
        return this;
    }

    public int getServerPort() {
        return serverPort;
    }

    public UploadProtocol setServerPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public String getLoginName() {
        return loginName;
    }

    public UploadProtocol setLoginName(String loginName) {
        this.loginName = loginName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UploadProtocol setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public UploadProtocol setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
        return this;
    }

    public String getUploadDirPM() {
        return uploadDirPM;
    }

    public UploadProtocol setUploadDirPM(String uploadDirPM) {
        this.uploadDirPM = uploadDirPM;
        return this;
    }

    public String getLocalDir() {
        return localDir;
    }

    public UploadProtocol setLocalDir(String localDir) {
        this.localDir = localDir;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public UploadProtocol setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public boolean isUploadOk() {
        return uploadOk;
    }

    public UploadProtocol setUploadOk(boolean uploadOk) {
        this.uploadOk = uploadOk;
        return this;
    }

    public boolean isAddSuffix() {
        return addSuffix;
    }

    public UploadProtocol setAddSuffix(boolean addSuffix) {
        this.addSuffix = addSuffix;
        return this;
    }

    public String getSuffix() {
        return suffix;
    }

    public UploadProtocol setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public String getUploadDirSb() {
        return uploadDirSb;
    }

    public UploadProtocol setUploadDirSb(String uploadDirSb) {
        this.uploadDirSb = uploadDirSb;
        return this;
    }

    public String getServerIpSB() {
        return serverIpSB;
    }

    public UploadProtocol setServerIpSB(String serverIpSB) {
        this.serverIpSB = serverIpSB;
        return this;
    }

    public String getLoginNameSB() {
        return loginNameSB;
    }

    public UploadProtocol setLoginNameSB(String loginNameSB) {
        this.loginNameSB = loginNameSB;
        return this;
    }

    public String getPasswordSB() {
        return passwordSB;
    }

    public UploadProtocol setPasswordSB(String passwordSB) {
        this.passwordSB = passwordSB;
        return this;
    }

    public String getProtocolSB() {
        return protocolSB;
    }

    public UploadProtocol setProtocolSB(String protocolSB) {
        this.protocolSB = protocolSB;
        return this;
    }

    public String getDataInterType() {
        return dataInterType;
    }

    public UploadProtocol setDataInterType(String dataInterType) {
        this.dataInterType = dataInterType;
        return this;
    }

    public String getResourceFileNamePrefix() {
        return resourceFileNamePrefix;
    }

    public UploadProtocol setResourceFileNamePrefix(String resourceFileNamePrefix) {
        this.resourceFileNamePrefix = resourceFileNamePrefix;
        return this;
    }

    public String getPerformFileNamePrefix() {
        return performFileNamePrefix;
    }

    public UploadProtocol setPerformFileNamePrefix(String performFileNamePrefix) {
        this.performFileNamePrefix = performFileNamePrefix;
        return this;
    }

    public String getVendorName() {
        return vendorName;
    }

    public UploadProtocol setVendorName(String vendorName) {
        this.vendorName = vendorName;
        return this;
    }

    public String getElementType() {
        return elementType;
    }

    public UploadProtocol setElementType(String elementType) {
        this.elementType = elementType;
        return this;
    }

    public String getCmVersion() {
        return cmVersion;
    }

    public UploadProtocol setCmVersion(String cmVersion) {
        this.cmVersion = cmVersion;
        return this;
    }

    public String getPmVersion() {
        return pmVersion;
    }

    public UploadProtocol setPmVersion(String pmVersion) {
        this.pmVersion = pmVersion;
        return this;
    }

    public String getObjectType() {
        return objectType;
    }

    public UploadProtocol setObjectType(String objectType) {
        this.objectType = objectType;
        return this;
    }

    public String getRmUID() {
        return rmUID;
    }

    public UploadProtocol setRmUID(String rmUID) {
        this.rmUID = rmUID;
        return this;
    }

}
