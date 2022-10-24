package com.asiainfo.lcims.omc.persistence.apn;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.apn.ApnSingleData;
import com.asiainfo.lcims.omc.model.apn.MdApnRecord;
import com.asiainfo.lcims.omc.persistence.apn.impl.MdApnRecordDAOImpl;

public interface MdApnRecordDAO {

    @Select("SELECT APNID,APN,UPDATE_TIME FROM MD_APN_RECORD")
    List<MdApnRecord> getAllApn();

    @Select("SELECT APN FROM MD_APN_RECORD WHERE APNID=#{apnId}")
    MdApnRecord getApnNameById(@Param("apnId") String apnId);

    @InsertProvider(method = "insertMetricDataSingle", type = MdApnRecordDAOImpl.class)
    int insertMetricDataSingle(@Param("apnSingleData") ApnSingleData apnSingleData);

    @Select("SELECT attr1 as APN FROM ${tableName} WHERE metric_id='3b88cb510d7749a1bfb4673245117c00' AND attr1 !='' AND attr1 IS NOT NULL GROUP BY attr1")
    List<MdApnRecord> getAddApn(@Param("tableName") String tableName);

    @Insert("INSERT INTO MD_APN_RECORD (APNID,APN,UPDATE_TIME) VALUES(#{mdApnRecord.apnid},#{mdApnRecord.apn},now())")
    int insert(@Param("mdApnRecord") MdApnRecord mdApnRecord);
}
