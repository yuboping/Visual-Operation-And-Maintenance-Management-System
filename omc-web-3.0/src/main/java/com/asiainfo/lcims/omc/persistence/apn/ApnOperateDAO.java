package com.asiainfo.lcims.omc.persistence.apn;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.apn.MdApnRecord;

public interface ApnOperateDAO {

    @Insert("INSERT INTO MD_APN_RECORD(APNID,APN,UPDATE_TIME) VALUES(#{mdApnRecord.apnid},#{mdApnRecord.apn},#{mdApnRecord.update_time})")
    int addApn(@Param("mdApnRecord") MdApnRecord mdApnRecord);

    @Delete("DELETE FROM MD_APN_RECORD WHERE APN=#{apn}")
    int deleteApn(@Param("apn") String apn);

    @Select("SELECT APNID FROM MD_APN_RECORD WHERE APN=#{apn}")
    MdApnRecord getApnIdByName(@Param("apn") String apn);

    @Select("SELECT APNID,APN,UPDATE_TIME FROM MD_APN_RECORD WHERE APN=#{apn}")
    MdApnRecord getApnRecordByName(@Param("apn") String apn);

}
