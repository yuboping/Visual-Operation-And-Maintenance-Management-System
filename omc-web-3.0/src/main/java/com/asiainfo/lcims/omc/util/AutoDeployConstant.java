package com.asiainfo.lcims.omc.util;

/**
 * @Author: YuChao
 * @Date: 2019/3/27 16:51
 */
public class AutoDeployConstant {

    //restful返回失败值
    public static final String CURL_ERROR_RESPONSE = "0";

    //restful返回成功值
    public static final String CURL_SUCCESS_RESPONSE = "1";

    //部署状态
    public static final Integer DEPLOY_STATUS_RUNNING = 0;
    public static final Integer DEPLOY_STATUS_ERROR = -1;
    public static final Integer DEPLOY_STATUS_SUCCESS = 1;

    //默认端口
    public static final Integer DEFAULT_SSH_PORT = 22;
    //默认主机状态
    public static final Integer DEFAULT_HOST_STATUS = 1;
    //默认主机类型
    public static final Integer DEFAULT_HOST_TYPE = 0;

    //错误描述代码
    //数据库校验失败
    public static final String ERROR_CHECK_DATABASE = "1";
    //ssh连通性校验失败
    public static final String ERROR_SSH_CONNECTION = "2";
    //硬件记录信息失败
    public static final String ERROR_HARDWARE_RECORD = "3";
    //采集客户端启动失败
    public static final String ERROR_START_COLLECT_CLIENT = "4";
    //主机业务配置失败
    public static final String ERROR_CONFIG_HOST_METRIC = "5";
    //更新主机业务配置失败
    public static final String ERROR_UPDATE_HOST_METRIC = "6";
    //自动部署成功
    public static final String SUCCESS_AUTO_DEPLOY = "99";
    //自动删除成功
    public static final String SUCCESS_REMOVE_DEPLOY = "100";


    /** 业务列表为空 */
    public static final String BUSINESS_LIST_NULL = "NULL";

    /** 自动部署分割符 # */
    public static final String AUTO_DEPLOY_SPLIT_STR = "#";

    /** 业务来源类型 菜单 */
    public static final int SOURCE_TYLE_MENU = 1;
    /** 业务来源类型 服务器主机 */
    public static final int SOURCE_TYLE_HOST = 2;
    /** 业务来源类型 指标 */
    public static final int SOURCE_TYLE_METRIC = 3;

    /** 操作系统类别 linux系统 */
    public static final String OS_TYPE_LINUX = "linux";
    /** 操作系统类别 sunOs系统 */
    public static final String OS_TYPE_SUNOS = "sunOs";
    /** 操作系统类别 hp_linux系统 */
    public static final String OS_TYPE_HP_LINUX = "hp_linux";
    /** 操作系统类别 IBM AIX系统 */
    public static final String OS_TYPE_AIX = "aix";
    
    /** 默认host业务 */
    public static final String BUSINESS_HOST = "ai-omc-host";
    
    /** 自动部署开始标志 */
    public static final String AUTO_DEPLOY_START = "自动部署开始";

    /** 自动部署结束标志 */
    public static final String AUTO_DEPLOY_END = "自动部署结束";
}
