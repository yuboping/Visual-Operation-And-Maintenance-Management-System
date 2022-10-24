package com.asiainfo.lcims.omc.persistence.IOT;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.apn.MdApnLimitRule;
import com.asiainfo.lcims.omc.persistence.IOT.impl.CurrentLimitingDaoImpl;

public interface CurrentLimitingDAO {

    // 查询
    @SelectProvider(method = "getApnList", type = CurrentLimitingDaoImpl.class)
    List<MdApnLimitRule> getApnList(@Param("mdApnLimitRule") MdApnLimitRule mdApnLimitRule);

    // 新增
    @InsertProvider(method = "addApn", type = CurrentLimitingDaoImpl.class)
    int addApn(@Param("mdApnLimitRule") MdApnLimitRule mdApnLimitRule);

    // 修改
    @UpdateProvider(method = "updateMdApn", type = CurrentLimitingDaoImpl.class)
    int update(@Param("mdApnLimitRule") MdApnLimitRule mdApnLimitRule);

    // 删除
    @Delete("DELETE FROM MD_APN_LIMITRULE WHERE APNID = #{apnId}")
    int delete(@Param("apnId") String id);

}
