package com.asiainfo.lcims.omc.persistence.maintool;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.model.gdcu.BatOptRec;
import com.asiainfo.lcims.omc.model.gdcu.OfflineLog;
import com.asiainfo.lcims.omc.model.gdcu.OptRecord;
import com.asiainfo.lcims.omc.persistence.maintool.impl.BatOptRecDAOIpml;

public interface BatOptRecDAO {

    @SelectProvider(method = "getBatOptLog", type = BatOptRecDAOIpml.class)
    List<OfflineLog> getBatOptLog(@Param("startdate") String startdate,
            @Param("enddate") String enddate, @Param("admin") String admin,
            @Param("opttype") String opttype, @Param("brasip") String brasip,
            @Param("date") String date);

    @Select("INSERT INTO BAT_OPT_REC (SERNO,OPTTYPE,NASIP,ADMIN,IPADDRESS,OPTTIME,OPTREASON,RETURNCODE,RESULTFILE) "
            + "VALUES(#{reason.serno},#{reason.opttype},#{reason.nasip},#{reason.admin},#{reason.ipaddress},"
            + "#{reason.opttime},#{reason.optreason},#{reason.returncode},#{reason.resultfile})")
    Integer insert(@Param("reason") BatOptRec reason);

    @Select("SELECT * FROM BAT_OPT_REC opt WHERE opt.opttype=#{opttype} ORDER BY opt.OPTTIME DESC,opt.SERNO ASC")
    List<OptRecord> getByOpt(@Param("opttype") String opttype);

    @Select("SELECT AREA_NO FROM BD_NAS WHERE NAS_IP = #{brasip}")
    String getArea(@Param("brasip") String brasip);

}
