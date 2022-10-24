package com.asiainfo.lcims.omc.persistence.hncu;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.model.hncu.MdSystemLog;
import com.asiainfo.lcims.omc.persistence.hncu.impl.SystemLogDAOImpl;
import com.asiainfo.lcims.omc.util.page.Page;

public interface SystemLogDAO {

    @SelectProvider(method = "getSystemLogCount", type = SystemLogDAOImpl.class)
    int getSystemLogCount(@Param("systemLog") MdSystemLog systemLog);

    @SelectProvider(method = "getSystemLogList", type = SystemLogDAOImpl.class)
    List<MdSystemLog> getSystemLogList(@Param("systemLog") MdSystemLog systemLog,
            @Param("page") Page page);

    @SelectProvider(method = "exportSystemLogList", type = SystemLogDAOImpl.class)
    List<MdSystemLog> exportSystemLogList(@Param("systemLog") MdSystemLog systemLog);

}
