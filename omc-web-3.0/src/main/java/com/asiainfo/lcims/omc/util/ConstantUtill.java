package com.asiainfo.lcims.omc.util;

public class ConstantUtill {
    public static final String operatetype_host = "2";
    public static final String operatetype_metric = "1";
    public static final String SCRIPT_RETURN_TYPE = "7";
    public static final String HOST_METRIC_STATE = "8";

    /** 已下发 */
    public static final Integer HOST_METRIC_STATE_PUBLISH = 1;
    /** 未下发 */
    public static final Integer HOST_METRIC_STATE_UNPUBLISH = 0;
    /** 待删除 */
    public static final Integer HOST_METRIC_STATE_DELETE = 3;

    /** 主机进程关联关系页面下拉菜单 */
    public static final String HOST_PROCESS_TYPE = "10";

    /** 主机进程关联关系脚本类型 */
    public static final String HOST_PROCESS_SCRIPT_TYPE = "11";

    /** 角色管理属地节点下拉菜单 */
    public static final String ROLE_PARAM_LIST = "12";

    /** 布尔值-true */
    public static final Boolean BOOLEAN_TRUE = true;
    /** 布尔值-false */
    public static final Boolean BOOLEAN_FALSE = false;

    /** 启动中 */
    public static final Integer PROCESS_START = 1;
    /** 失败 */
    public static final Integer PROCESS_FAILURE = 2;
    /** 成功 */
    public static final Integer PROCESS_SUCCESS = 3;
    /** 失败返回结果 */
    public static final String PROCESS_RESULT_FAILURE = "FAILED";

    /** 脚本类型 启动 */
    public static final Integer START_SCRIPT_TYPE = 1;
    /** 脚本类型 停止 */
    public static final Integer STOP_SCRIPT_TYPE = 2;

    /** 脚本执行状态 */
    public static final String PROCESS_STATE = "13";

    /** 参数表中主机对应编码 */
    public static final String PARAM_HOST = "1";
    /** 参数表中进程对应编码 */
    public static final String PARAM_PROCESS = "2";

    /** URL 分割符 */
    public static final String URL_SPLIT = "--";

    /** 默认进程指标的类型是1,每次初始化数据的时候定义,后期不允许改变 */
    public static final String PROCESSMETRICTYPE = "1";

    /** 进程指标.进程关键字分隔符. 分割符 */
    public static final String PROCESSMETRICSPLITSTR = "#";

    /** SELECT * FROM MD_PARAM WHERE TYPE = 1; 认证原因失败类别 */
    public static final String AUTHENFAILMDPARAMTYPE = "1";

    /** RADIUS_HOST_TYPE 为 2 */
    public static final String RADIUS_HOST_TYPE = "2";

    /** OBS_HOST_TYPE 为 2 */
    public static final String OBS_HOST_TYPE = "6";

    /** 模拟拨测结果在 MD_PARAM 的 TYPE = '50' */
    public static final String ANALOGDIALUPRESULT_TYPE = "50";

    /** 巡检表头类型 */
    public static final String WDINSSHEETHEADER_HEADERTYPE = "header";

    /** 巡检页脚类型 */
    public static final String WDINSSHEETHEADER_FOOTERTYPE = "footer";

    /** 巡检主机表格类型 */
    public static final String WDINSSHEET_SHEETTYPE_HOST = "host";

    /** 巡检应用表格类型 */
    public static final String WDINSSHEET_SHEETTYPE_APPLICATION = "application";

    /** Radius主机类型 */
    public static final String HOSTTYPE_RADIUS = "2";

    /** billing服务器类型 */
    public static final String HOSTTYPE_BILLING = "13";

    /** 数据库服务器类型 */
    public static final String HOSTTYPE_DB = "14";

    /** PARAM服务器类型 */
    public static final String PARAM_HOSTTYPE = "3";

    public static final String PERCENT_SIGN = "%";

    public static final String NO_DATA = "--";

    /** PARAM主机type */
    public static final String MD_PARAM_TYPE_HOST = "3";

    public static final String YES = "是";

    public static final String NO = "否";

    public static final String FIREWALL = "firewall";

    public static final String THIRDPARTY = "thirdparty";

    public static final String PROTOCOL = "protocol";

    public static final String FIREWALL_NAME = "防火墙设备";

    public static final String THIRDPARTY_NAME = "第三方接口设备";

    public static final String PROTOCOL_NAME = "采集协议插件";

    /** 已清除告警 **/
    public static final String TRAP_CLEAN_FLAG = "1";
}
