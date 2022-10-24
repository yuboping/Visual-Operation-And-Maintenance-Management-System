package com.asiainfo.lcims.omc.util;

import java.util.HashMap;
import java.util.Map;

import com.asiainfo.lcims.omc.model.VerifyCode;

public class Constant {

    /** 用户登录密码盐值 */
    public static final String PASSWORD_SALT = "zpp_20151205_salt";
    /** 角色权限表 menu 菜单权限 */
    public static final String PERMISSION_MENU = "menu";
    /** 角色权限表 node 节点权限 */
    public static final String PERMISSION_NODE = "node";
    /** 角色权限表 area 属地权限 */
    public static final String PERMISSION_AREA = "area";
    /** 动态菜单标识_节点 */
    public static final String DYNAMICTYPE_NODE = "node";
    /** 动态菜单标识_属地 */
    public static final String DYNAMICTYPE_AREA = "area";
    /** 动态菜单标识_主机 */
    public static final String DYNAMICTYPE_HOST = "host";
    /** 动态菜单标识_bras */
    public static final String DYNAMICTYPE_BRAS = "bras";
    /** 动态菜单标识_apn */
    public static final String DYNAMICTYPE_APN = "apn";
    /** 动态菜单标识_总览 */
    public static final String DYNAMICTYPE_SUMMARY = "summary";
    /** 动态菜单标识_静态：0 */
    public static final String DYNAMICTYPE_STATIC = "0";
    /** 动态标识_指标 */
    public static final String DYNAMICTYPE_METRIC = "metric";
    /** 业务关联主机_指标 */
    public static final String DYNAMICTYPE_BUSINESSHOST = "businesshost";

    /** 角色 超级管理员 角色id */
    public static final String ADMIN_ROLEID = "0";
    /** 角色 超级管理员 角色id */
    public static final String ADMIN_ROLEID_STRING = "roleid";
    /** 角色 超级管理员 角色名称 */
    public static final String ADMIN_ROLENAME = "admin";

    public static final String CURRENT_USER = "currentUser";

    /** 设置BRAS时session的key */
    public static final String BRAS_USER = "brasuser";

    /** 设置防火墙时session的key */
    public static final String FIREWALL_USER = "firewalluser";

    /** 设置第三方接口设备时session的key */
    public static final String THIRDPARTY_USER = "thirdpartyuser";

    /** 角色权限修改 */
    public static final String ROLE_PERMISSION_MODIFY = "1";
    /** 角色权限详情 */
    public static final String ROLE_PERMISSION_DETAIL = "2";

    /** 菜单半选状态 */
    public static final String MENU_TYPE_HALF = "0";
    /** 菜单全选状态 */
    public static final String MENU_TYPE_ALL = "1";

    /** 复选框不可修改 */
    public static final String CHECKBOX_DISABLED = "0";
    /** 复选框可修改 */
    public static final String CHECKBOX_ABLED = "1";

    /** 菜单权限子节点 属性 菜单 */
    public static final String PERMISSION_CHILDREN_MENU = "0";
    /** 菜单权限子节点 属性 属地 */
    public static final String PERMISSION_CHILDREN_AREA = "area";
    /** 菜单权限子节点 属性 节点 */
    public static final String PERMISSION_CHILDREN_NODE = "node";
    /** 菜单权限子节点 属性 主机 */
    public static final String PERMISSION_CHILDREN_HOST = "host";
    /** 菜单权限子节点 属性 bras */
    public static final String PERMISSION_CHILDREN_BRAS = "bras";
    /** 菜单权限子节点 属性 apn */
    public static final String PERMISSION_CHILDREN_APN = "apn";

    /** 维度_节点 */
    public static final String DIMENSION_NODE = "节点维度";
    /** 维度_属地 */
    public static final String DIMENSION_AREA = "属地维度";
    /** 维度_主机 */
    public static final String DIMENSION_HOST = "主机维度";
    /** 维度_bras */
    public static final String DIMENSION_BRAS = "bras维度";
    /** 维度_apn */
    public static final String DIMENSION_APN = "apn维度";

    /** 全部_节点 */
    public static final String ALL_NODE = "全部节点";
    /** 全部_属地 */
    public static final String ALL_AREA = "全部属地";
    /** 全部_主机 */
    public static final String ALL_HOST = "全部主机";
    /** 全部_bras */
    public static final String ALL_BRAS = "全部bras";
    /** 全部_apn */
    public static final String ALL_APN = "全部apn";

    /** 监控目标类型 ：总览 */
    public static final String MONITORTARGET_SUMMARY = "0";
    /** 监控目标类型 ：全部 */
    public static final String MONITORTARGET_ALL = "1";
    /** 监控目标类型 ：单个 */
    public static final String MONITORTARGET_SINGLE = "2";

    /** 单个_节点 */
    public static final String SINGLE_NODE = "单个节点";
    /** 单个_属地 */
    public static final String SINGLE_AREA = "单个属地";
    /** 单个_主机 */
    public static final String SINGLE_HOST = "单个主机";
    /** 单个_bras */
    public static final String SINGLE_BRAS = "单个bras";
    /** 单个_apn */
    public static final String SINGLE_APN = "单个apn";

    /** 查询条件-全部主机 */
    public static final String ALL_HOST_WITH_QUERY = "全部主机";
    /** 查询条件-全部节点 */
    public static final String ALL_NODE_WITH_QUERY = "全部节点";

    /** 告警方式在 MD_PARAM 的 TYPE = '4' */
    public static final String ALARM_MODE_TYPE = "4";

    /** 维度类型 1--> node/summary * node/summary： 维度1 节点总览 */
    public static final int DIMENSIONTYPE_NODE_SUMMARY = 1;
    /** 维度类型2--> area/summary * area/summary: 维度1 地市总览 */
    public static final int DIMENSIONTYPE_AREA_SUMMARY = 2;
    /** 维度类型3--> host/summary * host/summary 服务器总览 */
    public static final int DIMENSIONTYPE_HOST_SUMMARY = 3;
    /** 维度类型4--> node(all) * node(全部) 节点 ： 维度1：节点名称， 维度2：空 */
    public static final int DIMENSIONTYPE_NODE_ALL = 4;
    /** 维度类型5--> node(single) * node(单个) 节点 ： 维度1：节点名称， 维度2：空 */
    public static final int DIMENSIONTYPE_NODE_SINGLE = 5;
    /** 维度类型6--> area(all) * area(全部) 地市 ： 维度1：地市名称， 维度2：空 */
    public static final int DIMENSIONTYPE_AREA_ALL = 6;
    /** 维度类型7--> area(single) * area(单个) 地市 ： 维度1：地市名称， 维度2：空 */
    public static final int DIMENSIONTYPE_AREA_SINGLE = 7;
    /** 维度类型8--> host(all) * host(全部) 服务器： 维度1：主机名称， 维度2：空 */
    public static final int DIMENSIONTYPE_HOST_ALL = 8;
    /** 维度类型9--> host(single) * host(单个) 服务器： 维度1：主机名称， 维度2：空 */
    public static final int DIMENSIONTYPE_HOST_SINGLE = 9;
    /**
     * 维度类型10--> node(all)/host(all) * node/host 节点(全部) --> 主机(全部) ： 维度1：节点名称，
     * 维度2：主机IP
     */
    public static final int DIMENSIONTYPE_NODE_ALL_HOST_ALL = 10;
    /**
     * 维度类型11--> node(single)/host(all) * node/host 节点(单个) --> 主机(全部) ：
     * 维度1：节点名称， 维度2：主机IP
     */
    public static final int DIMENSIONTYPE_NODE_SINGLE_HOST_ALL = 11;
    /**
     * 维度类型12--> node(single)/host(single) * node/host 节点(单个) --> 主机(单个) ：
     * 维度1：节点名称， 维度2：主机IP
     */
    public static final int DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE = 12;

    /**
     * 维度类型13--> node(all)/area(all) * node/area 节点(全部) --> 地市(全部) ： 维度1：节点名称，
     * 维度2：地市名称
     */
    public static final int DIMENSIONTYPE_NODE_ALL_AREA_ALL = 13;
    /**
     * 维度类型14--> node(single)/area(all) * node/area 节点(单个) --> 地市(全部) ：
     * 维度1：节点名称， 维度2：地市名称
     */
    public static final int DIMENSIONTYPE_NODE_SINGLE_AREA_ALL = 14;
    /**
     * 维度类型15--> node(single)/area(single) * node/area 节点(单个) --> 地市(单个) ：
     * 维度1：节点名称， 维度2：地市名称
     */
    public static final int DIMENSIONTYPE_NODE_SINGLE_AREA_SINGLE = 15;

    /**
     * 维度类型16--> area(all)/bras(all) * area/bras 地市(全部) --> bras(全部) : 维度1：地市 ，
     * 维度2：brasip
     */
    public static final int DIMENSIONTYPE_AREA_ALL_BRAS_ALL = 16;
    /**
     * 维度类型17--> area(single)/bras(all) * area/bras 地市(单个) --> bras(全部) : 维度1：地市
     * ， 维度2：brasip
     */
    public static final int DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL = 17;
    /**
     * 维度类型18--> area(single)/bras(single) * area/bras 地市(单个) --> bras(单个) :
     * 维度1：地市 ， 维度2：brasip
     */
    public static final int DIMENSIONTYPE_AREA_SINGLE_BRAS_SINGLE = 18;

    /**
     * 维度类型19--> area(all)/node(all) * area/node 地市(全部) --> 节点(全部) ： 维度1：地市名称，
     * 维度2：节点名称
     */
    public static final int DIMENSIONTYPE_AREA_ALL_NODE_ALL = 19;
    /**
     * 维度类型20--> area(single)/node(all) * area/node 地市(单个) --> 节点(全部) ：
     * 维度1：地市名称， 维度2：节点名称
     */
    public static final int DIMENSIONTYPE_AREA_SINGLE_NODE_ALL = 20;
    /**
     * 维度类型21--> area(single)/node(single) * area/node 地市(单个) --> 节点(单个) ：
     * 维度1：地市名称， 维度2：节点名称
     */
    public static final int DIMENSIONTYPE_AREA_SINGLE_NODE_SINGLE = 21;

    /** 维度类型22--> bras/summary * bras/summary bras总览 */
    public static final int DIMENSIONTYPE_BRAS_SUMMARY = 22;

    /** 维度类型23--> bras(all) bras ： 维度1：空， 维度2：空 */
    public static final int DIMENSIONTYPE_BRAS_ALL = 23;

    /** 维度类型24--> bras(single) bras ： 维度1：brasip， 维度2：空 */
    public static final int DIMENSIONTYPE_BRAS_SINGLE = 24;

    /** 维度类型25--> apn/summary * apn/summary apn总览 */
    public static final int DIMENSIONTYPE_APN_SUMMARY = 25;

    /** 维度类型26--> apn(all) apn ： 维度1：空， 维度2：空 */
    public static final int DIMENSIONTYPE_APN_ALL = 26;

    /** 维度类型27--> apn(single) apn ： 维度1：apn名称， 维度2：空 */
    public static final int DIMENSIONTYPE_APN_SINGLE = 27;

    /** 维度类型 1--> node/summary * node/summary： 维度1 节点总览 */
    public static final String DIMENSIONTYPE_NODE_SUMMARY_NAME = "节点总览";
    /** 维度类型2--> area/summary * area/summary: 维度1 地市总览 */
    public static final String DIMENSIONTYPE_AREA_SUMMARY_NAME = "属地总览";
    /** 维度类型3--> host/summary * host/summary 服务器总览 */
    public static final String DIMENSIONTYPE_HOST_SUMMARY_NAME = "服务器总览 ";
    /** 维度类型4--> node(all) * node(全部) 节点 ： 维度1：节点名称， 维度2：空 */
    public static final String DIMENSIONTYPE_NODE_ALL_NAME = "全部节点";
    /** 维度类型5--> node(single) * node(单个) 节点 ： 维度1：节点名称， 维度2：空 */
    public static final String DIMENSIONTYPE_NODE_SINGLE_NAME = "单个节点";
    /** 维度类型6--> area(all) * area(全部) 地市 ： 维度1：地市名称， 维度2：空 */
    public static final String DIMENSIONTYPE_AREA_ALL_NAME = "全部属地";
    /** 维度类型7--> area(single) * area(单个) 地市 ： 维度1：地市名称， 维度2：空 */
    public static final String DIMENSIONTYPE_AREA_SINGLE_NAME = "单个属地";
    /** 维度类型8--> host(all) * host(全部) 服务器： 维度1：主机名称， 维度2：空 */
    public static final String DIMENSIONTYPE_HOST_ALL_NAME = "全部服务器";
    /** 维度类型9--> host(single) * host(单个) 服务器： 维度1：主机名称， 维度2：空 */
    public static final String DIMENSIONTYPE_HOST_SINGLE_NAME = "单个服务器";
    /**
     * 维度类型10--> node(all)/host(all) * node/host 节点(全部) --> 主机(全部) ： 维度1：节点名称，
     * 维度2：主机IP
     */
    public static final String DIMENSIONTYPE_NODE_ALL_HOST_ALL_NAME = "全部节点全部服务器";
    /**
     * 维度类型11--> node(single)/host(all) * node/host 节点(单个) --> 主机(全部) ：
     * 维度1：节点名称， 维度2：主机IP
     */
    public static final String DIMENSIONTYPE_NODE_SINGLE_HOST_ALL_NAME = "单个节点全部服务器";
    /**
     * 维度类型12--> node(single)/host(single) * node/host 节点(单个) --> 主机(单个) ：
     * 维度1：节点名称， 维度2：主机IP
     */
    public static final String DIMENSIONTYPE_NODE_SINGLE_HOST_SINGLE_NAME = "单个节点单个服务器";

    /**
     * 维度类型13--> node(all)/area(all) * node/area 节点(全部) --> 地市(全部) ： 维度1：节点名称，
     * 维度2：地市名称
     */
    public static final String DIMENSIONTYPE_NODE_ALL_AREA_ALL_NAME = "全部节点全部属地";
    /**
     * 维度类型14--> node(single)/area(all) * node/area 节点(单个) --> 地市(全部) ：
     * 维度1：节点名称， 维度2：地市名称
     */
    public static final String DIMENSIONTYPE_NODE_SINGLE_AREA_ALL_NAME = "单个节点全部属地";
    /**
     * 维度类型15--> node(single)/area(single) * node/area 节点(单个) --> 地市(单个) ：
     * 维度1：节点名称， 维度2：地市名称
     */
    public static final String DIMENSIONTYPE_NODE_SINGLE_AREA_SINGLE_NAME = "单个节点单个属地";

    /**
     * 维度类型16--> area(all)/bras(all) * area/bras 地市(全部) --> bras(全部) : 维度1：地市 ，
     * 维度2：brasip
     */
    public static final String DIMENSIONTYPE_AREA_ALL_BRAS_ALL_NAME = "全部属地全部BRAS";
    /**
     * 维度类型17--> area(single)/bras(all) * area/bras 地市(单个) --> bras(全部) : 维度1：地市
     * ， 维度2：brasip
     */
    public static final String DIMENSIONTYPE_AREA_SINGLE_BRAS_ALL_NAME = "单个属地全部BRAS";
    /**
     * 维度类型18--> area(single)/bras(single) * area/bras 地市(单个) --> bras(单个) :
     * 维度1：地市 ， 维度2：brasip
     */
    public static final String DIMENSIONTYPE_AREA_SINGLE_BRAS_SINGLE_NAME = "单个属地单个BRAS";

    /**
     * 维度类型19--> area(all)/node(all) * area/node 地市(全部) --> 节点(全部) ： 维度1：地市名称，
     * 维度2：节点名称
     */
    public static final String DIMENSIONTYPE_AREA_ALL_NODE_ALL_NAME = "全部属地全部节点";
    /**
     * 维度类型20--> area(single)/node(all) * area/node 地市(单个) --> 节点(全部) ：
     * 维度1：地市名称， 维度2：节点名称
     */
    public static final String DIMENSIONTYPE_AREA_SINGLE_NODE_ALL_NAME = "单个属地全部节点";
    /**
     * 维度类型21--> area(single)/node(single) * area/node 地市(单个) --> 节点(单个) ：
     * 维度1：地市名称， 维度2：节点名称
     */
    public static final String DIMENSIONTYPE_AREA_SINGLE_NODE_SINGLE_NAME = "单个属地单个节点";

    /** 维度类型22--> bras/summary * bras/summary bras总览 */
    public static final String DIMENSIONTYPE_BRAS_SUMMARY_NAME = "BRAS总览 ";

    /** 维度类型23--> bras(all) bras ： 维度1：空， 维度2：空 */
    public static final String DIMENSIONTYPE_BRAS_ALL_NAME = "全部BRAS";

    /** 维度类型24--> bras(single) bras ： 维度1：brasip， 维度2：空 */
    public static final String DIMENSIONTYPE_BRAS_SINGLE_NAME = "单个BRAS";

    /** 维度类型25--> apn/summary * apn/summary apn总览 */
    public static final String DIMENSIONTYPE_APN_SUMMARY_NAME = "APN总览 ";

    /** 维度类型26--> apn(all) apn ： 维度1：空， 维度2：空 */
    public static final String DIMENSIONTYPE_APN_ALL_NAME = "全部APN";

    /** 维度类型27--> apn(single) apn ： 维度1：apn名称， 维度2：空 */
    public static final String DIMENSIONTYPE_APN_SINGLE_NAME = "单个APN";

    /** 调用获取最新数据sql */
    public static final int NORMAL_DATA_ACTION = 0;

    /** 调用获取最新统计数据sql */
    public static final int SPECIAL_DATA_ACTION = 1;

    /** 菜单根目录ID */
    public static final String SYS_MENU_ID = "0";
    /** 菜单一级菜单级别 */
    public static final int SYS_MENU_LEVEL_1 = 1;

    /**
     * 单维度按照日分表表名
     */
    public static final String HOST_CAP_TABLE_PREFIX = "METRIC_DATA_SINGLE_";

    /**
     * 导出日报表类型
     */
    public static final String EXPORT_DAY_REPORT = "day";

    /**
     * 导出周报表类型
     */
    public static final String EXPORT_WEEK_REPORT = "week";

    /**
     * 导出月报表类型
     */
    public static final String EXPORT_MONTH_REPORT = "month";

    /**
     * 参数表-操作历史类别
     */
    public static final String PARAM_TYPE_OPERATE_HIS = "20";

    /**
     * 操作历史类别-用户日常操作
     */
    public static final String OPERATE_HIS_USER = "1";

    /**
     * 操作历史类别-角色管理
     */
    public static final String OPERATE_HIS_ROLE_MANAGE = "2";

    /**
     * 操作历史类别-属地管理
     */
    public static final String OPERATE_HIS_AREA_MANAGE = "3";

    /**
     * 操作历史类别-主机关联信息
     */
    public static final String OPERATE_HIS_HOST_PROCESS = "4";

    /**
     * 操作历史类别-告警信息查询
     */
    public static final String OPERATE_HIS_ALARM_QUERY = "5";

    /** 操作历史类别-主机指标配置管理 */
    public static final String OPERATE_HIS_HOSTMETRICMANAGE_QUERY = "6";

    /** 操作历史类别-服务器管理 */
    public static final String OPERATE_HIS_SERVER_MANAGE = "7";

    /** 操作历史类别-设备型号管理 */
    public static final String OPERATE_HIS_EQUIPMENT_MANAGE = "8";

    /** 操作历史类别-厂家管理 */
    public static final String OPERATE_HIS_FACTORY_MANAGE = "9";

    /** 操作历史类别-管理员管理 */
    public static final String OPERATE_HIS_ADMIN_MANAGE = "10";

    /** 操作历史类别-BRAS管理 */
    public static final String OPERATE_HIS_BRAS_MANAGE = "11";

    /** 操作历史类别-告警方式管理 */
    public static final String OPERATE_HIS_ALARMMODE_MANAGE = "12";

    /** 操作历史类别-指标管理 */
    public static final String OPERATE_HIS_METRIC_MANAGE = "13";

    /** 操作历史类别-指标类型管理 */
    public static final String OPERATE_HIS_METRICTYPE_MANAGE = "14";

    /** 操作历史类别-节点管理 */
    public static final String OPERATE_HIS_NODE_MANAGE = "15";

    /** 操作历史类别-告警规则管理 */
    public static final String OPERATE_HIS_ALARMRULE_MANAGE = "16";

    /** 操作历史类别-业务关联主机管理 */
    public static final String OPERATE_HIS_BUSINESSHOST_MANAGE = "17";

    /** 操作历史类别-进程管理 */
    public static final String OPERATE_HIS_PROCESS_MANAGE = "18";

    /**
     * 操作历史类别-巡检组管理
     */
    public static final String OPERATE_HIS_AIS_GROUP_MANAGE = "19";

    /**
     * 操作历史类别-巡检组指标管理
     */
    public static final String OPERATE_HIS_AIS_GROUP_METRIC_MANAGE = "20";

    /**
     * 操作历史类别-声音告警管理
     */
    public static final String OPERATE_HIS_SOUND_ALARM_MANAGE = "21";

    /** 操作历史类别-模拟拨测管理 */
    public static final String OPERATE_HIS_ANALOG_DIAL_UP = "22";

    /** 操作历史类别-防火墙设备管理 */
    public static final String OPERATE_HIS_FIREWALL = "23";

    /** 操作历史类别-第三方接口设备管理 */
    public static final String OPERATE_HIS_THIRDPARTY = "24";

    /**
     * 操作历史类别-告警信息查询-河南
     */
    public static final String OPERATE_HIS_ALARM_QUERY_HN = "25";

    /** 物联网apn */
    public static final String IOT_APN = "24";

    /** 操作类型_添加 */
    public static final String OPERATIONTYPE_ADD = "add";
    /** 操作类型_修改 */
    public static final String OPERATIONTYPE_MODIFY = "modify";
    /** 操作类型_删除 */
    public static final String OPERATIONTYPE_DELETE = "delete";

    /** Radius主机类型 */
    public static final int HOST_HOST_TYPE_RADIUS = 2;

    public static final String METRIC_DATA_MULTI_HOST_ID = "HOST_ID";

    public static final String STATIS_DATA_DAY = "STATIS_DATA_DAY_";

    public static final String STATIS_DATA_DAY_ATTR1 = "ATTR1";

    /**
     * 多维度按照日分表表名
     */
    public static final String METRIC_DATA_MULTI = "METRIC_DATA_MULTI_";

    /** 单维度按照日分表表名 */
    public static final String METRIC_DATA_SINGLE = "METRIC_DATA_SINGLE_";

    public static final String HOST_NET_CONNECTABLE_1 = "1";

    /** 日报表类型名字 */
    public static final String DAY_REPORT_NAME = "日报表";

    /** 周报表类型名字 */
    public static final String WEEK_REPORT_NAME = "周报表";

    /** 月报表类型名字 */
    public static final String MONTH_REPORT_NAME = "月报表";

    /** 陕西省市缩写 */
    public static final String PROVINCE_SXCM = "sxcm";

    /** 自动部署模板路径 */
    public static final String AUTO_DEPLOY_MOULD_PATH = "autodeploy/";

    /** 自动部署模板名称 */
    public static final String AUTO_DEPLOY_MOULD_NAME = "自动部署模板.xls";

    /** 生成ansible的Hosts文件分隔符空格 */
    public static final String ANSIBLE_HOSTS_FILE_SPACE = " ";

    /** 生成ansible的MdFindBusiness文件分隔符空格 */
    public static final String ANSIBLE_MDFINDBUSINESS_FILE_SEPARATOR = "#";

    /** 表名日期格式 */
    public static final String CREATE_FORMAT = "yyyy-MM-dd";

    /** 获取昨天日期标志 */
    public static final String YESTERDAY_CONSTANT = "yesterday";

    /** 获取今天日期标志 */
    public static final String TODAY_CONSTANT = "today";

    /** 表名日期格式 */
    public static final String TABLE_NAME_FORMAT = "MM_dd";

    /** 菜单不可见 */
    public static final Integer MENU_IS_SHOW_OFF = 0;

    /** 菜单拼接符 */
    public static final String MENU_SPLIT_SYMBOL = "/";

    /** RADIUS 拼接分割字符 */
    public static final String RADIUS_OPERATE_SPLIT_STR = "|||";
    /** RADIUS 分割字符串 */
    public static final String RADIUS_OPERATE_SPLIT = "\\|\\|\\|";
    /** RADIUS 行结束符 */
    public static final String RADIUS_OPERATE_END = "\n";
    /** RADIUS 接口数据结束符 */
    public static final String END_SOCKET = ":::::::::end:::::::::";

    /** 忘记密码下发的验证码 */
    public static final String FORGET_PWD_CODE = "forget_pwd_code";
    /** 忘记密码下发的验证码时间 */
    public static final String FORGET_PWD_DATE = "forget_pwd_date";

    /** 忘记密码下发的验证码 账号、验证码 */
    private static final Map<String, VerifyCode> FORGET_PWD_CODE_MAP = new HashMap<String, VerifyCode>();

    public static final Map<String, VerifyCode> getForgetPwdCodeMap() {
        return FORGET_PWD_CODE_MAP;
    }

    /** ems ip 分隔符 */
    public static final String IP_SPIT = ",";

    /** 采集文件路径 */
    public static final String COLLECT_FILE_PATH = "collectfile/";

    /** 采集文件名称 */
    public static final String COLLECT_FILE_NAME = "02-02-001-590020000003-20200606144832-0002.5.txt";

    /** 开启限流 */
    public static final String LIMIT_VALID_OPEN = "1";

    /** 不开启限流 */
    public static final String LIMIT_VALID_UNOPEN = "0";

    /** APN新增 */
    public static final String APN_ADD = "1";

    /** APN删除 */
    public static final String APN_DELETE = "2";

    /** APN操作成功 */
    public static final String APN_SUCCESS = "0";

    /** APN操作失败 */
    public static final String APN_FAILED = "1";

}