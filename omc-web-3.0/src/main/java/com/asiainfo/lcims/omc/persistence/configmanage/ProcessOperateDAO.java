package com.asiainfo.lcims.omc.persistence.configmanage;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.ProcessOperate;
import com.asiainfo.lcims.omc.persistence.SqlSpecialProvider;

public interface ProcessOperateDAO {

    /**
     * 根据OPERATE_ID获取操作表数据 分为Oracle版本和Mysql版本
     * 
     * @param id
     * @return
     */
    @SelectProvider(method = "getProcessOperateById", type = SqlSpecialProvider.class)
    ProcessOperate getProcessOperateById(@Param("id") String id);

    /**
     * 根据OPERATE_ID获取操作表数据 分为Oracle版本和Mysql版本
     * 
     * @param id
     * @return
     */
    @SelectProvider(method = "getProcessOperateByOperateId", type = SqlSpecialProvider.class)
    ProcessOperate getProcessOperateByOperateId(@Param("operateId") String operateId);

    /**
     * 执行脚本若抛出异常则修改操作表状态
     * 
     * @param processOperate
     * @return
     */
    @UpdateProvider(method = "modifyHostProcess", type = SqlSpecialProvider.class)
    Integer modifyProcessOperate(@Param("processOperate") ProcessOperate processOperate);

    /**
     * 向操作表插入记录
     * 
     * @param processOperate
     * @return
     */
    @Insert("INSERT INTO PROCESS_OPERATE (ID, OPERATE_ID, HOST_IP, PROCESS_NAME, PROCESS_SCRIPT, SCRIPT_TYPE, OPERATE_STATE, OPERATE_RESULT, CREATE_TIME, UPDATE_TIME) VALUES(#{processOperate.id},#{processOperate.operate_id},#{processOperate.host_ip},#{processOperate.process_name},#{processOperate.process_script},#{processOperate.script_type},#{processOperate.operate_state},#{processOperate.operate_result},#{processOperate.create_time},#{processOperate.update_time})")
    int insertProcessOpereate(@Param("processOperate") ProcessOperate processOperate);

}
