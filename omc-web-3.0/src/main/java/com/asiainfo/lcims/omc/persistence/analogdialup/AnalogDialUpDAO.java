package com.asiainfo.lcims.omc.persistence.analogdialup;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.analogdialup.AnalogDialUp;
import com.asiainfo.lcims.omc.persistence.analogdialup.impl.AnalogDialUpDAOIpml;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.page.Page;

/**
 * 
 * @ClassName: AnalogDialUpDAO
 * @Description: TODO(模拟拨测)
 * @author yubp@asiainfo-sec.com
 * @date 2020年3月31日 下午5:50:52
 *
 */
public interface AnalogDialUpDAO {
    @Select("SELECT adu.ID, adu.HOST_ID, adu.CRON_ERP, adu.USERNAME, adu.PASSWORD, adu.NAS_PORT, adu.CALL_FROM_ID, adu.CALL_TO_ID, adu.EXT, h.ADDR AS HOST_IP FROM ANALOG_DIAL_UP adu LEFT JOIN MON_HOST h ON (h.HOSTID = adu.HOST_ID)")
    List<AnalogDialUp> findAllAnalogDialUp();

    @SelectProvider(method = "getAnalogDialUpList", type = AnalogDialUpDAOIpml.class)
    List<AnalogDialUp> getAnalogDialUpList(@Param("analogDialUp") AnalogDialUp analogDialUp);

    @SelectProvider(method = "getAnalogDialUpResultList", type = AnalogDialUpDAOIpml.class)
    List<AnalogDialUp> getAnalogDialUpResultList(@Param("analogDialUp") AnalogDialUp analogDialUp);

    @Select("SELECT adu.ID, adu.HOST_ID, adu.CRON_ERP, adu.USERNAME, adu.PASSWORD, adu.NAS_PORT, adu.CALL_FROM_ID, adu.CALL_TO_ID, adu.EXT, h.ADDR AS HOST_IP FROM ANALOG_DIAL_UP adu LEFT JOIN MON_HOST h ON (h.HOSTID = adu.HOST_ID) WHERE adu.ID = #{id}")
    AnalogDialUp getAnalogDialUp(@Param("id") String id);

    @Insert("INSERT INTO ANALOG_DIAL_UP (ID,HOST_ID,CRON_ERP,USERNAME,PASSWORD,NAS_PORT,CALL_FROM_ID,CALL_TO_ID,EXT) VALUES(#{analogDialUp.id},#{analogDialUp.host_id},#{analogDialUp.cron_erp},#{analogDialUp.username},#{analogDialUp.password},#{analogDialUp.nas_port},#{analogDialUp.call_from_id},#{analogDialUp.call_to_id},#{analogDialUp.ext})")
    int insert(@Param("analogDialUp") AnalogDialUp analogDialUp);

    @Delete("DELETE FROM ANALOG_DIAL_UP WHERE ID IN (#{id})")
    public int delete(@Param("id") String id);

    @UpdateProvider(method = "updateAnalogDialUp", type = AnalogDialUpDAOIpml.class)
    int update(@Param("analogDialUp") AnalogDialUp analogDialUp);

    @InsertProvider(method = "insertAnalogDialUpResult", type = AnalogDialUpDAOIpml.class)
    int insertResult(@Param("analogDialUp") AnalogDialUp analogDialUp);

    @InsertProvider(method = "insertMetricDataMulti", type = AnalogDialUpDAOIpml.class)
    int insertMetricDataMulti(@Param("analogDialUp") AnalogDialUp analogDialUp);

    @Select("SELECT CODE,DESCRIPTION FROM MD_PARAM WHERE TYPE = '"
            + ConstantUtill.ANALOGDIALUPRESULT_TYPE + "'")
    List<Map<String, Object>> getReturncodeName();

    @SelectProvider(method = "getAnalogDialUpResultTotalCount", type = AnalogDialUpDAOIpml.class)
    int getAnalogDialUpResultTotalCount(@Param("analogDialUp") AnalogDialUp analogDialUp);

    @SelectProvider(method = "getAnalogDialUpResultPage", type = AnalogDialUpDAOIpml.class)
    List<AnalogDialUp> getAnalogDialUpResultPage(@Param("analogDialUp") AnalogDialUp analogDialUp,
            @Param("page") Page page);
}
