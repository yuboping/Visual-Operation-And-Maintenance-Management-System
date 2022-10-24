package com.asiainfo.lcims.util;

import com.asiainfo.lcims.omc.alarm.conf.SystemConf;


public class DbSqlUtil {
	
	public static final SystemConf systemConf = new SystemConf();
	
	/**
	 * 日期 函数 ： 获取格式 y-M-d 
	 * @return
	 */
	public static String getDateSql(String columnName) {
		String sql = "";
		if(EnumUtil.DB_ORACLE.equals(systemConf.getDbName())){
			sql = "to_char("+columnName+",'yyyy-MM-dd')";
		}else if(EnumUtil.DB_MYSQL.equals(systemConf.getDbName())){
			sql = "date_format("+columnName+",'%Y-%m-%d')";
		}else{
			sql = "date_format("+columnName+",'%Y-%m-%d')";
		}
		return sql;
	}
	
	/**
	 * 日期 函数 ： 获取格式 y-M-d h:m:s
	 * yyyy-MM-dd hh24:mi:ss
	 * @return
	 */
	public static String getTimeSql(String columnName) {
		String sql = "";
		if(EnumUtil.DB_ORACLE.equals(systemConf.getDbName())){
			sql = "to_char("+columnName+",'yyyy-MM-dd hh24:mi:ss')";
		}else if(EnumUtil.DB_MYSQL.equals(systemConf.getDbName())){
			sql = "date_format("+columnName+",'%Y-%m-%d %H:%i:%s')";
		}else{
			sql = "date_format("+columnName+",'%Y-%m-%d %H:%i:%s')";
		}
		return sql;
	}
	
	/**
	 * 日期 函数 ： 获取格式 y-M-d h:m
	 * yyyy-MM-dd hh24:mi:ss
	 * @return
	 */
	public static String getTimeDHMSql(String columnName) {
		String sql = "";
		if(EnumUtil.DB_ORACLE.equals(systemConf.getDbName())){
			sql = "to_char("+columnName+",'yyyy-MM-dd hh24:mi')";
		}else if(EnumUtil.DB_MYSQL.equals(systemConf.getDbName())){
			sql = "date_format("+columnName+",'%Y-%m-%d %H:%i')";
		}else{
			sql = "date_format("+columnName+",'%Y-%m-%d %H:%i')";
		}
		return sql;
	}
	
	/**
	 * 日期 函数 ： h:m
	 * hh24:mi
	 * @return
	 */
	public static String getTimeHMSql(String columnName) {
		String sql = "";
		if(EnumUtil.DB_ORACLE.equals(systemConf.getDbName())){
			sql = "to_char("+columnName+",'hh24:mi')";
		}else if(EnumUtil.DB_MYSQL.equals(systemConf.getDbName())){
			sql = "date_format("+columnName+",'%H:%i')";
		}else{
			sql = "date_format("+columnName+",'%H:%i')";
		}
		return sql;
	}
	
	/**
	 * 日期 函数 ： m
	 * mi
	 * @return
	 */
	public static String getTimeMSql(String columnName) {
		String sql = "";
		if(EnumUtil.DB_ORACLE.equals(systemConf.getDbName())){
			sql = "to_char("+columnName+",'mi')";
		}else if(EnumUtil.DB_MYSQL.equals(systemConf.getDbName())){
			sql = "date_format("+columnName+",'%i')";
		}else{
			sql = "date_format("+columnName+",'%i')";
		}
		return sql;
	}
	
	public static String getDbName(){
		return systemConf.getDbName();
	}
	
	public static boolean isMysql(){
		if(EnumUtil.DB_MYSQL.equals(systemConf.getDbName())){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isOracle(){
		if(EnumUtil.DB_ORACLE.equals(systemConf.getDbName())){
			return true;
		}else{
			return false;
		}
	}
	
}
