package com.asiainfo.lcims.omc.persistence.configmanage.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.lcbmi.password.Password;
import com.asiainfo.lcims.lcbmi.password.PasswordException;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;

/**
 * 
 * @author zhujiansheng
 * @date 2018年8月1日 上午11:03:09
 * @version V1.0
 */
public class MAdminDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(MAdminDAOImpl.class);

    public String getMAdminList(Map<String, Object> paras) {
        MAdmin mAdmin = (MAdmin) paras.get("mAdmin");
        StringBuffer buffer = new StringBuffer(
                "SELECT A.ADMIN,B.ROLEID,C.NAME AS ROLENAME,A.AREANOLIST,A.NODELIST,A.MODULELIST,A.NAME,A.CONTACTPHONE,A.CORPNAME,A.COADDR,A.EMAIL,A.DESCRIPTION ");
        buffer.append(
                "FROM M_ADMIN A,M_ADMIN_ROLE B,MD_ROLE C WHERE A.ADMIN=B.ADMIN AND B.ROLEID=C.ROLEID ");
        if (!StringUtils.isEmpty(mAdmin.getAdmin())) {
            String adminStr = mAdmin.getAdmin();
            buffer.append(" AND A.ADMIN='").append(adminStr).append("'");
        }
        LOG.debug("getMAdminList sql = {}", buffer);
        return buffer.toString();
    }

    public String insertMAdmin(Map<String, Object> paras) {
        MAdmin mAdmin = (MAdmin) paras.get("mAdmin");
        StringBuffer buffer = new StringBuffer(
                "INSERT INTO M_ADMIN(ADMIN,PASSWORD,PASSWORDTYPE,STATUS,NODELIST,AREANOLIST,NAME,CONTACTPHONE,CORPNAME,COADDR,EMAIL,DESCRIPTION) VALUES('");
        buffer.append(mAdmin.getAdmin()).append("','");
        String password = mAdmin.getPassword();
        password = password + Constant.PASSWORD_SALT;
        String encryptjm = encryptjm(password, 1);
        buffer.append(encryptjm).append("',");
        buffer.append(1).append(",'");
        buffer.append(mAdmin.getStatus()).append("','");
        buffer.append(mAdmin.getNodelist()).append("','");
        buffer.append(mAdmin.getAreanolist()).append("','");
        buffer.append(mAdmin.getName()).append("','");
        buffer.append(mAdmin.getContactphone()).append("','");
        buffer.append(mAdmin.getCorpname()).append("','");
        buffer.append(mAdmin.getCoaddr()).append("','");
        buffer.append(mAdmin.getEmail()).append("','");
        buffer.append(mAdmin.getDescription()).append("')");
        LOG.debug("insertMAdmin sql = {}", buffer);
        return buffer.toString();
    }

    public String updateMAdmin(Map<String, Object> paras) {
        MAdmin mAdmin = (MAdmin) paras.get("mAdmin");
        StringBuffer buffer = new StringBuffer("UPDATE M_ADMIN SET ADMIN=ADMIN");
        if (null != mAdmin.getName()) {
            String name = mAdmin.getName();
            buffer.append(",NAME='").append(name).append("'");
        }
        if (null != mAdmin.getContactphone()) {
            String contactphone = mAdmin.getContactphone();
            buffer.append(",CONTACTPHONE='").append(contactphone).append("'");
        }
        if (null != mAdmin.getCorpname()) {
            String corpname = mAdmin.getCorpname();
            buffer.append(",CORPNAME='").append(corpname).append("'");
        }
        if (null != mAdmin.getCoaddr()) {
            String coaddr = mAdmin.getCoaddr();
            buffer.append(",COADDR='").append(coaddr).append("'");
        }
        if (null != mAdmin.getEmail()) {
            String email = mAdmin.getEmail();
            buffer.append(",EMAIL='").append(email).append("'");
        }
        if (null != mAdmin.getDescription()) {
            String description = mAdmin.getDescription();
            buffer.append(",DESCRIPTION='").append(description).append("'");
        }
        buffer.append(" WHERE ADMIN='").append(mAdmin.getAdmin()).append("'");
        LOG.debug("updateMAdmin sql = {}", buffer);
        return buffer.toString();
    }

    public String getAdmin(Map<String, Object> paras) {
        String admin = (String) paras.get("admin");
        String roleid = (String) paras.get("roleid");
        StringBuffer buffer = new StringBuffer(
                "SELECT A.ADMIN,B.ROLEID,C.NAME AS ROLENAME,A.AREANOLIST,A.NODELIST,A.MODULELIST,A.NAME,A.CONTACTPHONE,A.CORPNAME,A.COADDR,A.EMAIL,A.DESCRIPTION ");
        buffer.append(
                "FROM M_ADMIN A,M_ADMIN_ROLE B,MD_ROLE C WHERE A.ADMIN=B.ADMIN AND B.ROLEID=C.ROLEID ");
        if (!StringUtils.isEmpty(admin)) {
            String adminStr = DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(admin));
            buffer.append(" AND A.ADMIN LIKE '%").append(adminStr).append("%'");
        }
        if (!StringUtils.isEmpty(roleid)) {
            String roleidStr = DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(roleid));
            buffer.append(" AND B.ROLEID='").append(roleidStr).append("'");
        }
        LOG.debug("getAdmin sql = {}", buffer);
        return buffer.toString();
    }

    public String modifyPasswd(Map<String, Object> paras) {
        MAdmin mAdmin = (MAdmin) paras.get("mAdmin");
        StringBuffer buffer = new StringBuffer("UPDATE M_ADMIN SET ADMIN=ADMIN");
        if (null != mAdmin.getPassword()) {
            String password = mAdmin.getPassword();
            password = password + Constant.PASSWORD_SALT;
            String newPasswd = encryptjm(password, 1);
            buffer.append(",PASSWORD='").append(newPasswd).append("'");
        }
        buffer.append(" WHERE ADMIN='").append(mAdmin.getAdmin()).append("'");
        LOG.debug("modifyPasswd sql = {}", buffer);
        return buffer.toString();
    }

    public String encryptjm(String password, int passwdType) {
        try {
            String encryptdata = Password.encryptPassword(password, passwdType);
            return encryptdata;
        } catch (PasswordException e) {
            LOG.error("error:{}", e);
            return "";
        }
    }
    
    public String checkType(Map<String, Object> paras) {
    	StringBuffer buffer = new StringBuffer("select count(*) from M_ADMIN  m where ");
    	buffer.append(" m.admin=").append("'"+paras.get("admin")+"'");
    	int checkType=(int) paras.get("checkType");
    	String checkValue=(String) paras.get("checkValue");
    	if(checkType==1) {//手机号码验证
    		buffer.append(" and m.contactphone=").append("'"+checkValue+"'");
    	}else if(checkType==2){//邮箱地址验证
    		buffer.append(" and m.email=").append("'"+checkValue+"'");
    	}else { //验证方式不存在
    		buffer.append(" and m.admin is null");
    	}
    	LOG.info("checkType sql = {}", buffer);
    	return buffer.toString();
    }
}
