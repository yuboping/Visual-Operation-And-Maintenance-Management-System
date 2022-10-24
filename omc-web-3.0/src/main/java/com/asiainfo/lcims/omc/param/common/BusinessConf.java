package com.asiainfo.lcims.omc.param.common;

import com.ailk.lcims.lcbmi.config.Configuration;
import com.asiainfo.lcims.omc.util.ReadFile;

public class BusinessConf extends Configuration {
    /** 默认的配置文件名 */
    private static final String DEFAULT_CONFIG_NAME = ReadFile
            .getFileExistFolder("business.properties") + "/business.properties";

    public BusinessConf() {
        super();
        // System.out.println(DEFAULT_CONFIG_NAME);
        this.configLoader = initConfigLoader(DEFAULT_CONFIG_NAME);
        configLoader.loadConfig();
    }

    public BusinessConf(String configName) {
        super();
        this.configLoader = initConfigLoader(configName);
        configLoader.loadConfig();
    }

    /**
     * 批量下线用户接口消息发送ip
     * 
     * @return
     */
    public String offlineBatchuserIp() {
        return getValueOrDefault("obs_offlineBatchuser_ip", "127.0.0.1");
    }

    /**
     * 批量下线用户接口消息发送端口
     * 
     * @return
     */
    public int offlineBatchuserPort() {
        return getIntValueOrDefault("obs_offlineBatchuser_port", 9005);
    }

    /**
     * 在线用户统计---查询用户在线总数接口消息发送ip
     * 
     * @return
     */
    public String checkOnlineUserIp() {
        return getValueOrDefault("obs_checkOnlineUser_ip", "127.0.0.1");
    }

    /**
     * 在线用户统计---查询用户在线总数接口接口发送端口
     * 
     * @return
     */
    public int checkOnlineUserPort() {
        return getIntValueOrDefault("obs_checkOnlineUser_port", 10078);
    }

    /**
     * 在线用户统计---根据basip查询用户在线数接口消息发送ip
     * 
     * @return
     */
    public String checkBasUserIp() {
        return getValueOrDefault("obs_checkBasUser_ip", "127.0.0.1");
    }

    /**
     * 在线用户统计---根据basip查询用户在线数接口发送端口
     * 
     * @return
     */
    public int checkBasUserPort() {
        return getIntValueOrDefault("obs_checkBasUser_port", 10078);
    }

    /**
     * 在线用户查询接口消息发送ip
     * 
     * @return
     */
    public String queryOnlineUserIp() {
        return getValueOrDefault("obs_queryOnlineUser_ip", "127.0.0.1");
    }

    /**
     * 在线用户查询接口发送端口
     * 
     * @return
     */
    public int queryOnlineUserPort() {
        return getIntValueOrDefault("obs_queryOnlineUser_port", 10078);
    }

    /**
     * 认证日志查询接口消息发送ip
     * 
     * @return
     */
    public String qryAccessLogIp() {
        return getValueOrDefault("obs_qryAccessLog_ip", "127.0.0.1");
    }

    /**
     * 认证日志查询接口发送端口
     * 
     * @return
     */
    public int qryAccessLogPort() {
        return getIntValueOrDefault("obs_qryAccessLog_port", 10078);
    }

    /**
     * 删除用户在线信息接口消息发送ip
     * 
     * @return
     */
    public String kickLMUserIp() {
        return getValueOrDefault("obs_kickLMUser_ip", "127.0.0.1");
    }

    /**
     * 删除用户在线信息接口发送端口
     * 
     * @return
     */
    public int kickLMUserPort() {
        return getIntValueOrDefault("obs_kickLMUser_port", 10078);
    }

    /**
     * 踢用户下线接口消息发送ip
     * 
     * @return
     */
    public String kickBRASUserIp() {
        return getValueOrDefault("obs_kickBRASUser_ip", "127.0.0.1");
    }

    /**
     * 踢用户下线接口发送端口
     * 
     * @return
     */
    public int kickBRASUserPort() {
        return getIntValueOrDefault("obs_kickBRASUser_port", 10078);
    }

    /**
     * 用户信息（属地）查询接口消息发送ip
     * 
     * @return
     */
    public String queryUserNodeIp() {
        return getValueOrDefault("obs_queryUserNode_ip", "127.0.0.1");
    }

    /**
     * 用户信息（属地）查询接口发送端口
     * 
     * @return
     */
    public int queryUserNodePort() {
        return getIntValueOrDefault("obs_queryUserNode_port", 10078);
    }

    /**
     * 声音告警 默认false
     * 
     * @return
     */
    public String alarmSound() {
        return getValueOrDefault("alarm_sound", "false");
    }

    /**
     * 声音告警 文件名称
     * 
     * @return
     */
    public String alarmSoundName() {
        return getValueOrDefault("alarm_sound_name", "alarm.mp3");
    }

    /**
     * 声音告警 类型 0：默认告警：告警声音响一次 1：sicu类型 当前存在未确认的告警信息，声音一直响 2：默认公共类型：当前告警一直响
     * 
     * @return
     */
    public String alarmSoundType() {
        return getValueOrDefault("alarm_sound_type", "0");
    }

    /**
     * 计算平均值脚本路径
     * 
     * @return
     */
    public String avgstaticPath() {
        return getValueOrDefault("avgstatic_path", "");
    }

    /**
     * 数据库
     */
    public String getDbName() {
        return getValueOrDefault("db_name", "mysql");
    }

    /**
     * 系统管理员
     */
    public String getRoleAdmin() {
        return getValueOrDefault("role_admin", "0");
    }

    /**
     * 定时巡检邮箱相关信息
     */
    public String getAISSmtpHost() {
        return getValueOrDefault("ais_smtp_host", "smtp.sina.cn");
    }

    /**
     * 定时巡检邮箱相关信息
     */
    public String getAISSmtpUsername() {
        return getValueOrDefault("ais_smtp_username", "omc_wlan@sina.com");
    }

    /**
     * 定时巡检邮箱相关信息
     */
    public String getAISSmtpPassword() {
        return getValueOrDefault("ais_smtp_password", "omc_wlan123");
    }

    /**
     * 定时巡检发送短信标识
     */
    public boolean getAISSmsNotice() {
        return new Boolean(getValueOrDefault("ais_sendSms", "false"));
    }

    /**
     * 定时巡检发送邮件标识
     */
    public boolean getAISEmailNotice() {
        return new Boolean(getValueOrDefault("ais_sendEmail", "false"));
    }

    /**
     * 页面折线图历史信息显示按小时统计，按天统计的标识开关
     * 
     * @return
     */
    public String getPageHistoryMoreFlag() {
        return getValueOrDefault("page_history_more_flag", "false");
    }

    /**
     * Radius主机认证统计报表
     */
    public int getRadiusHostAuthReportId() {
        return getIntValueOrDefault("radiusHostAuthReportId", 1);
    }

    /**
     * 地市认证统计报表
     */
    public int getAreaAuthReportId() {
        return getIntValueOrDefault("areaAuthReportId", 2);
    }

    /**
     * olt指标名称
     */
    public String getOltMetricIdentity() {
        return getValueOrDefault("olt_metric_identity", "aggregate_olt");
    }

    /**
     * 公共方法 返回String类型数据
     */
    public String getStringValue(String param) {
        String valule = getValueOrDefault(param, null);
        // 特殊配置可以用if写
        if (param.equals("host_detail_show")) {
            return getValueOrDefault("host_detail_show", "false");
        }
        return valule;
    }

    public String getStringValue(String param, String defaultstr) {
        String valule = getValueOrDefault(param, defaultstr);
        return valule;
    }

    /**
     * 公共方法 返回int类型数据
     */
    public int getIntValue(String param) {
        return getIntValueOrDefault(param, 1);
    }

    /**
     * 公共方法 返回int类型数据
     */
    public int getIntValue(String param, int val) {
        return getIntValueOrDefault(param, val);
    }

    /**
     * 公共方法 返回int类型数据
     */
    public int getQueryDaysNum() {
        return getIntValueOrDefault("query_days_num", 7);
    }

    /**
     * 
     * @Title: getFileSystemMetricType @Description: (获取文件系统指标ID) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getFileSystemMetricId() {
        return getValueOrDefault("file_system_metric_id", "1172655426554cf4827c255f3e0e0e21");
    }

    /**
     * 
     * @Title: getAuthenFailMetricType @Description:
     *         (获取认证原因失败类别指标ID) @param @return 参数 @return String 返回类型 @throws
     */
    public String getAuthenFailMetricId() {
        return getValueOrDefault("authen_fail_metric_id", "41c05adc28e549eeb0c465d49af15faa");
    }

    /**
     * 
     * @Title: getCpuRateMetricId @Description: (获取cpu占用率指标ID) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getCpuRateMetricId() {
        return getValueOrDefault("cpu_occupancyrate_metric_id", "c89fd55828d34ad88cd78db92d76e8bf");
    }

    /**
     * 
     * @Title: getMemoryMetricId @Description: (获取内存占用率指标ID) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getMemoryMetricId() {
        return getValueOrDefault("memory_occupancyrate_metric_id",
                "f94c4059b49947f5bec8d7c4ce8298c4");
    }

    /**
     * 
     * @Title: getRadiusProcessKeys @Description:
     *         (获取内存Radius进程关键字) @param @return 参数 @return String 返回类型 @throws
     */
    public String getRadiusProcessKeys() {
        return getValueOrDefault("radius_process_keys", "radius");
    }

    /**
     * 
     * @Title: getAuthenSuccessRate @Description: (获取内存认证成功率) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getAuthenSuccessRateMetricId() {
        return getValueOrDefault("authen_success_rate_metric_id",
                "b4e2687e47be4793a14df1435809f6bb");
    }

    /**
     * 
     * @Title: getAuthenSuccess @Description: (获取内存认证成功量) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getAuthenSuccessMetricId() {
        return getValueOrDefault("authen_success_metric_id", "a88d5251f36748f1887380d7dcbd3f0c");
    }

    /**
     * 
     * @Title: getAuthen @Description: (获取内存认证总量) @param @return 参数 @return
     *         String 返回类型 @throws
     */
    public String getAuthenMetricId() {
        return getValueOrDefault("authen_metric_id", "f71671679ec74ec3be76929cca2658d6");
    }

    /**
     * 
     * @Title: getChargingRequests @Description: (获取内存计费请求总量) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getChargingRequestsMetricId() {
        return getValueOrDefault("charging_requests_metric_id", "8cb2409faf07460190b807634e23abf1");
    }

    /**
     * 
     * @Title: getAuthenRequests @Description: (获取内存认证请求数) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getAuthenRequestsMetricId() {
        return getValueOrDefault("authen_requests_metric_id", "7f4078a39fc94a7eb027c835aa63f753");
    }

    /**
     * 
     * @Title: getDiskBusyRateMetricId @Description: (获取磁盘利用率) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getDiskBusyRateMetricId() {
        return getValueOrDefault("disk_busy_rate", "69391e2bcdcb467eba6379fa689b261b");
    }

    /**
     * 
     * @Title: getCpuOccupancyrateMetricId @Description:
     *         (获取cpu占用率) @param @return 参数 @return String 返回类型 @throws
     */
    public String getCpuOccupancyrateMetricId() {
        return getValueOrDefault("cpu_occupancyrate", "c89fd55828d34ad88cd78db92d76e8bf");
    }

    /**
     * 
     * @Title: getMemoryOccupancyrateMetricId @Description:
     *         (获取内存占用率) @param @return 参数 @return String 返回类型 @throws
     */
    public String getMemoryOccupancyrateMetricId() {
        return getValueOrDefault("memory_occupancyrate", "f94c4059b49947f5bec8d7c4ce8298c4");
    }

    /**
     * 
     * @Title: getSysNetConnectableMetricId @Description:
     *         (获取网络联通性) @param @return 参数 @return String 返回类型 @throws
     */
    public String getSysNetConnectableMetricId() {
        return getValueOrDefault("sys_net_connectable", "82b8ee7d204c4c559bdc62f6ea8b197c");
    }

    /**
     * 获取session登录时间 默认 30分钟
     * 
     * @return
     */
    public int getSessionMaxInactiveInterval() {
        return getIntValueOrDefault("session_MaxInactiveInterval", 1800);
    }

    /**
     * 
     * @Title: getAnsibleHostsPath @Description:
     *         (获取ansible的hosts文件路径) @param @return 参数 @return String
     *         返回类型 @throws
     */
    public String getAnsibleHostsFileFullPath() {
        return getValueOrDefault("ansible_hosts_filefullpath",
                "/etc/ansible/playbook/hosts/hosts.hosts");
    }

    /**
     * 
     * @Title: getAnsibleHostsDeleteFileFullPath @Description:
     *         (获取ansible的hosts删除文件路径) @param @return 参数 @return String
     *         返回类型 @throws
     */
    public String getAnsibleHostsDeleteFileFullPath() {
        return getValueOrDefault("ansible_hosts_delete_filefullpath",
                "/etc/ansible/playbook/hosts/hosts_delete.hosts");
    }

    /**
     * 
     * @Title: getAnsibleStartSh @Description: (ansible启动脚本) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getAnsibleStartSh() {
        return getValueOrDefault("ansible_start_sh",
                "/etc/ansible/playbook/sh/start.sh hosts.hosts");
    }

    /**
     * 
     * @Title: getAnsibleCheckStartSh @Description:
     *         (ansible校验连通性启动脚本) @param @return 参数 @return String 返回类型 @throws
     */
    public String getAnsibleCheckStartSh() {
        return getValueOrDefault("ansible_ssh_check_sh",
                "/etc/ansible/playbook/sh/ssh_check.sh hosts_delete.hosts");
    }

    /**
     * 
     * @Title: getAnsibleStartSh @Description: (ansible删除主机启动脚本) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getAnsibleDeleteStartSh() {
        return getValueOrDefault("ansible_stop_sh",
                "/etc/ansible/playbook/sh/stop.sh hosts_delete.hosts");
    }

    /**
     * 
     * @Title: getAnsibleMdFindBusinessFileFullPath @Description:
     *         (business_conf文件路径) @param @return 参数 @return String 返回类型 @throws
     */
    public String getAnsibleMdFindBusinessFileFullPath() {
        return getValueOrDefault("ansible_mdfindbusiness_filefullpath",
                "/etc/ansible/playbook/script/autodeploy/business_conf");
    }

    /**
     * 在线用户数的license
     * 
     * @return
     */
    public int onlineUserLicense() {
        return getIntValueOrDefault("online_user_license", 3900000);
    }

    /**
     * 
     * @Title: getAnalogDialUpUsername @Description: (模拟拨测默认用户名) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getAnalogDialUpUsername() {
        return getValueOrDefault("analogdialup_username", "test1");
    }

    /**
     * 
     * @Title: getAnalogDialUpPassword @Description: (模拟拨测默认密码) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getAnalogDialUpPassword() {
        return getValueOrDefault("analogdialup_password", "aaabbb");
    }

    /**
     * 
     * @Title: getAnalogDialUpNasPort @Description:
     *         (模拟拨测默认接入服务器端口) @param @return 参数 @return String 返回类型 @throws
     */
    public String getAnalogDialUpNasPort() {
        return getValueOrDefault("analogdialup_nas_port", "8888");
    }

    /**
     * 
     * @Title: getAnalogDialUpCallFromId @Description:
     *         (模拟拨测默认主叫号码) @param @return 参数 @return String 返回类型 @throws
     */
    public String getAnalogDialUpCallFromId() {
        return getValueOrDefault("analogdialup_call_from_id", "");
    }

    /**
     * 
     * @Title: getAnalogDialUpCallToId @Description: (模拟拨测默认被叫号码) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getAnalogDialUpCallToId() {
        return getValueOrDefault("analogdialup_call_to_id", "");
    }

    /**
     * 
     * @Title: getAnalogDialUpExt @Description: (模拟拨测默认扩展) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getAnalogDialUpExt() {
        return getValueOrDefault("analogdialup_ext", "");
    }

    public String getAnalogDialUpMetricId() {
        return getValueOrDefault("analogdialup_metric_id", "13145e9391df47ff9fb3fab086fa6b3e");
    }

    /**
     * 
     * @Title: getCpuPeakValMetricId @Description: (CPU峰值) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getCpuPeakValMetricId() {
        return getValueOrDefault("cpu_peak_val", "096e80c2d3334685a3b5dd29e9034cfa");
    }

    /**
     * 
     * @Title: getMemPeakValMetricId @Description: (MEM峰值) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getMemPeakValMetricId() {
        return getValueOrDefault("mem_peak_val", "d3ddd21e5eb34223878a3f76324a0d8d");
    }

    /**
     * 
     * @Title: getDiskBusyRatePeakValMetricId @Description:
     *         (磁盘利用率峰值) @param @return 参数 @return String 返回类型 @throws
     */
    public String getDiskBusyRatePeakValMetricId() {
        return getValueOrDefault("disk_busy_rate_peak_val", "90108f4e017341f190b0af7b8dc9fe6d");
    }

    /**
     * 
     * @Title: getClockJudgmentMetricId @Description: (时钟判断) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getClockJudgmentMetricId() {
        return getValueOrDefault("clock_judgment", "3ba630522c3a4922b62c9b4d3014dcc0");
    }

    /**
     * 
     * @Title: getApnLimitThresholdMetricId @Description:
     *         (APN每日限流阀值指标) @param @return 参数 @return String 返回类型 @throws
     */
    public String getApnLimitThresholdMetricId() {
        return getValueOrDefault("apn_limit_threshold_metric_id",
                "7c15400914c54182a6be26fb153fea2e");
    }

    /**
     * 
     * @Title: getApnLimitThresholdAlarmRuleUrl @Description:
     *         (APN每日限流阀值告警URL) @param @return 参数 @return String 返回类型 @throws
     */
    public String getApnLimitThresholdAlarmRuleUrl() {
        return getValueOrDefault("apn_limit_threshold_alarm_rule_url",
                "/view/class/server/module/apnequipment/apn--/#{apn_id}");
    }

    /**
     * 
     * @Title: getApnLimitThresholdAlarmRule @Description:
     *         (APN每日限流阀值告警规则) @param @return 参数 @return String 返回类型 @throws
     */
    public String getApnLimitThresholdAlarmRule() {
        return getValueOrDefault("apn_limit_threshold_alarm_rule",
                "newest>#{dayvalue}");
    }

    /**
     * 
     * @Title: getRedisHostIp @Description: (Redis的IP地址) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getRedisHostIp() {
        return getValueOrDefault("redis_host_ip", "127.0.0.1");
    }

    /**
     * 
     * @Title: getRedisHostPort @Description: (Redis的端口) @param @return
     *         参数 @return int 返回类型 @throws
     */
    public int getRedisHostPort() {
        return getIntValueOrDefault("redis_host_port", 6379);
    }

    /**
     * 
     * @Title: getRedisHostPassword @Description: (Redis的加密密码) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getRedisHostPassword() {
        return getValueOrDefault("redis_host_password", "");
    }

    /**
     * 
     * @Title: getLimitFlowUrl @Description: (限流策略同步URL) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getLimitFlowUrl() {
        return getValueOrDefault("limit_flow_url", "http://10.21.17.177:8994/iot/limitflow/open");
    }

    /**
     * 
     * @Title: getObsLoginUrl @Description: TODO(OBS登陆) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getObsLoginUrl() {
        return getValueOrDefault("obs_login_url",
                "http://127.0.0.1:26777/omc/login!ajaxLogin.action?loginname=FC71349DD483D931B2A12B9433DF0F46&password=3F9F55860D77E9F9A67CC03BFC1D5195&&flag=omc");
    }

    /**
     * 
     * @Title: getObsReportUrl @Description: TODO(OBS跳转) @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getObsReportUrl() {
        return getValueOrDefault("obs_report_url",
                "http://127.0.0.1:26777/omc/action/report/report!menuAction.action?menuaction=report/report.jsp&report_str=20111220192346336");
    }

}
