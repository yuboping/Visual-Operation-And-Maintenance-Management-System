package com.asiainfo.lcims.omc.util.ais;

public class TrapUtil {

    /** 告警流水号，动态唯一标识一条告警 **/
    public static final String HNCM_TRAP_ALARM_CSN = "1.3.6.1.4.1.9555.2.15.2.4.3.3.1";

    /** 告警标题 **/
    public static final String HNCM_TRAP_ALARM_TITLE = "1.3.6.1.4.1.9555.2.15.2.4.3.3.30";

    /** 设备厂家，本平台为固定值：亚信 **/
    public static final String HNCM_TRAP_ALARM_SOURCE = "1.3.6.1.4.1.9555.2.15.2.4.3.3.5";

    /** 设备厂家，本平台为固定值：亚信 **/
    public static final String HNCM_TRAP_ALARM_SOURCE_VAL = "亚信";

    /** 设备类型，本平台为固定值：radius **/
    public static final String HNCM_TRAP_ALARM_DEVICETYPE = "1.3.6.1.4.1.9555.2.15.2.4.3.3.6";

    /** 设备类型，本平台为固定值：radius **/
    public static final String HNCM_TRAP_ALARM_DEVICETYPE_VAL = "Radius";

    /** 告警对象类型 **/
    public static final String HNCM_TRAP_ALARM_OBJECTTYPE = "1.3.6.1.4.1.9555.2.15.2.4.3.3.7";
    /** 告警对象类型 默认：主机 **/
    public static final String HNCM_TRAP_ALARM_OBJECTTYPE_VAL = "主机";

    /** 告警类别：1：故障告警 2：清除告警 **/
    public static final String HNCM_TRAP_ALARM_CATEGORY = "1.3.6.1.4.1.9555.2.15.2.4.3.3.2";

    /** 告警产生时间 **/
    public static final String HNCM_TRAP_ALARM_OCCURTIME = "1.3.6.1.4.1.9555.2.15.2.4.3.3.3";

    /** 产生告警的网元名称 **/
    public static final String HNCM_TRAP_ALARM_MONAME = "1.3.6.1.4.1.9555.2.15.2.4.3.3.4";

    /** TRAPOID，告警的静态唯一标识 **/
    public static final String HNCM_TRAP_ALARM_ID = "1.3.6.1.4.1.9555.2.15.2.4.3.3.9";

    /** 告警级别 **/
    public static final String HNCM_TRAP_ALARM_LEVEL = "1.3.6.1.4.1.9555.2.15.2.4.3.3.11";

    /** 告警清除状态 **/
    public static final String HNCM_TRAP_ALARM_RESTORE = "1.3.6.1.4.1.9555.2.15.2.4.3.3.12";

    /** 告警清除时间 **/
    public static final String HNCM_TRAP_ALARM_RESTORE_TIME = "1.3.6.1.4.1.9555.2.15.2.4.3.3.15";

    // hncm 河南移动对应标识 end

    /** 被管理设备上一次初始化网络到本 Trap 发送以来的累积时间 */
    public static final String SYS_UP_TIME = "1.3.6.1.2.1.1.3.0";

    /** snmpTrapOID– 表示本 PDU 是一个 Trap，有固定的值 */
    public static final String SNMP_TRAP_OID = "1.3.6.1.6.3.1.1.4.1.0";

    public static final String SNMP_TRAP_OID_VAL = "1.3.6.1.4.1.9555.2";

}
