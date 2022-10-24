package com.asiainfo.lcims.omc.persistence.hncu;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.model.hncu.MdLoginLog;
import com.asiainfo.lcims.omc.persistence.hncu.impl.LoginLogDAOImpl;
import com.asiainfo.lcims.omc.util.page.Page;

public interface LoginLogDAO {

    @SelectProvider(method = "getLoginLogCount", type = LoginLogDAOImpl.class)
    int getLoginLogCount(@Param("loginLog") MdLoginLog loginLog);

    @SelectProvider(method = "getLoginLogList", type = LoginLogDAOImpl.class)
    List<MdLoginLog> getLoginLogList(@Param("loginLog") MdLoginLog loginLog,
            @Param("page") Page page);

    @SelectProvider(method = "exportLoginLogList", type = LoginLogDAOImpl.class)
    List<MdLoginLog> exportLoginLogList(@Param("loginLog") MdLoginLog loginLog);

}
