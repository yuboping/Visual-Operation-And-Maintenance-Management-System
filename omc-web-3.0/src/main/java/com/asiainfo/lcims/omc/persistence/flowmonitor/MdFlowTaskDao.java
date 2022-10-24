package com.asiainfo.lcims.omc.persistence.flowmonitor;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowTask;

public interface MdFlowTaskDao {
    @Select("SELECT * FROM MD_FLOW_TASK WHERE TASK_ID=#{taskId}")
    MdFlowTask selectByTaskId(@Param("taskId") String taskId);

    @Select("SELECT * FROM MD_FLOW_TASK")
    List<MdFlowTask> selectAll();
    
    @Select("SELECT * FROM MD_FLOW_TASK WHERE TASK_ID=#{task_id}")
    MdFlowTask queryTaskById(String task_id);

    @Update("<script>"
            + "UPDATE MD_FLOW_TASK SET CRON=#{task.cron},update_time=#{task.update_time}"
            + "<if test=\"task.send_type != null\">,SEND_TYPE=#{task.send_type}</if>"
            + "<if test=\"task.receiver != null\">,RECEIVER=#{task.receiver}</if>"
            + " WHERE TASK_ID=#{task.task_id}"
            + "</script>")
    int editFlowTask(@Param("task") MdFlowTask task);
}
