package com.asiainfo.lcims.omc.persistence.operatehistory;

import com.asiainfo.lcims.omc.model.operatehistory.OperateHis;
import com.asiainfo.lcims.omc.persistence.SqlOperateHisProvider;
import com.asiainfo.lcims.omc.util.page.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface OperateHisDao {

    @Insert("INSERT INTO MD_OPERATE_HIS (OPERATE_ID, OPERATE_NAME, OPERATE_TYPE, OPERATE_DESC, OPERATE_TIME) " +
            "VALUES(#{operateHis.operate_id}, #{operateHis.operate_name}, #{operateHis.operate_type}, #{operateHis.operate_desc}, #{operateHis.operate_time})")
    int insertOperateHis(@Param("operateHis") OperateHis operateHis);

    @SelectProvider(method = "getOperateHisList", type = SqlOperateHisProvider.class)
    List<OperateHis> getOperateHisList(@Param("operateHis") OperateHis operateHis, @Param("page") Page page);

    @SelectProvider(method = "getOperateHisInfoCount", type = SqlOperateHisProvider.class)
    int getOperateHisInfoCount(@Param("operateHis") OperateHis operateHis);

}
