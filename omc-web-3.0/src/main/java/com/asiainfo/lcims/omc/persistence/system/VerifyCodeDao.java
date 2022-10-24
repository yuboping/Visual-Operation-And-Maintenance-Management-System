package com.asiainfo.lcims.omc.persistence.system;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
public interface VerifyCodeDao {
	
//	@Insert(value = "insert into MD_VERIFY_CODE(id,admin,verify_code,verify_type,verify_add,is_apply,create_time)"
//			+ "values(#{verifyCode.id},#{verifyCode.admin},#{verifyCode.verifyCode},#{verifyCode.verifyType},#{verifyCode.verifyAdd},#{verifyCode.isApply},#{verifyCode.createTime})")
//	public void addVerifyCode(@Param("verifyCode") VerifyCode verifyCode);
//
//	@Select(value = "select id,admin,verify_code as verifyCode,verify_type AS verifyType,"
//			+ "verify_add AS verifyAdd,is_apply AS isApply,create_time AS createTime from MD_VERIFY_CODE c where c.admin=#{admin} and c.is_apply=#{isApply} limit 1")
//	public VerifyCode queryVerifyCodeByAdminAndIsApply(@Param("admin") String admin,@Param("isApply") int isApply);
	
	@Update(value="update MD_VERIFY_CODE set is_apply=0,update_time=sysdate() where id=#{id}")
	public void updateVerifyCodeById(@Param("id") String id);
}
