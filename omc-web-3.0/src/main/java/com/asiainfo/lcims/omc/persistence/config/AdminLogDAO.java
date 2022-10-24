package com.asiainfo.lcims.omc.persistence.config;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.persistence.config.impl.AdminLogDAOImpl;
import com.asiainfo.lcims.omc.persistence.po.config.MdAdminLog;

public interface AdminLogDAO {

    @SelectProvider(method = "insertAdminLog", type = AdminLogDAOImpl.class)
    void insert(@Param("adminLog") MdAdminLog adminLog);

}
