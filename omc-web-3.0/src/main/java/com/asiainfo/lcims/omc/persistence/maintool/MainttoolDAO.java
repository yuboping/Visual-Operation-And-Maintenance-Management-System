package com.asiainfo.lcims.omc.persistence.maintool;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.asiainfo.lcims.omc.model.maintool.MdMaintOperateLog;

public interface MainttoolDAO {
    
    /**
     * 批量提交Oracle
     * @param datas
     */
    @Insert("<script> insert all into MAINT_OPERATE_LOG(ID,UUID,HOST_IP,OPERATE_STATE,OPERATE_TYPE,OPERATE_USER,CREATE_TIME) values " +
            "  <foreach collection='datas' item='item' separator=' into MAINT_OPERATE_LOG(ID,UUID,HOST_IP,OPERATE_STATE,OPERATE_TYPE,OPERATE_USER,CREATE_TIME) values' > " +
            "  (#{item.id},#{item.uuid},#{item.host_ip},#{item.operate_state},#{item.operate_type},#{item.operate_user},#{item.create_time}) \n" +
            "  </foreach> ${selectSql} </script>")
    void addBattchOperateLogForOracle(@Param("datas") List<MdMaintOperateLog> datas, @Param("selectSql") String selectSql);
    
    /**
     * 批量提交Mysql
     * @param datas
     */
    @Insert("<script> insert into MAINT_OPERATE_LOG(ID,UUID,HOST_IP,OPERATE_STATE,OPERATE_TYPE,OPERATE_USER,CREATE_TIME) values " +
            "  <foreach collection='datas' item='item' separator=',' > " +
            "  (#{item.id},#{item.uuid},#{item.host_ip},#{item.operate_state},#{item.operate_type},#{item.operate_user},#{item.create_time}) \n" +
            "  </foreach> </script>")
    void addBattchOperateLogForMysql(@Param("datas") List<MdMaintOperateLog> datas, @Param("selectSql") String selectSql);
    
    @Update("UPDATE MAINT_OPERATE_LOG SET OPERATE_STATE=#{state} WHERE UUID=#{uuid} AND OPERATE_STATE!=0")
    int updateRadiusOperateStateByUuid(@Param("uuid") String uuid, @Param("state") int state);
    
    @Update("UPDATE MAINT_OPERATE_LOG SET OPERATE_STATE=#{state} WHERE UUID=#{uuid} AND HOST_IP=#{hosdip}")
    int updateRadiusOperateStateByUuidAndHostip(@Param("uuid") String uuid, @Param("hosdip") String hosdip, @Param("state") int state);
    
    @Select("SELECT OP.UUID,OP.HOST_IP,OP.OPERATE_STATE,P.DESCRIPTION AS OPERATE_STATE_NAME FROM MAINT_OPERATE_LOG OP"
            +" LEFT JOIN (SELECT CODE,DESCRIPTION FROM MD_PARAM WHERE TYPE=37)P ON OP.OPERATE_STATE = P.CODE"
            +" WHERE OP.UUID=#{uuid}")
    List<MdMaintOperateLog> queryOperateRadiusBusinessResult(@Param("uuid") String uuid);
}
