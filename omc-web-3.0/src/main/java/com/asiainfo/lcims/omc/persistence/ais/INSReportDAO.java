package com.asiainfo.lcims.omc.persistence.ais;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.persistence.SqlAisProvider;
import com.asiainfo.lcims.omc.persistence.SqlProvider;
import com.asiainfo.lcims.omc.persistence.po.ais.INSReport;

public interface INSReportDAO {

    @Select("SELECT ID,TITLE,CREATETIME,REPORTLINK FROM WD_INS_REPORT ORDER BY CREATETIME DESC")
    public List<INSReport> getAllReport();

    @SelectProvider(method = "getReportBytime", type = SqlAisProvider.class)
    public List<INSReport> getReportBytime(@Param("begintime") String begintime, @Param("endtime") String endtime, @Param("searchkey") String searchkey);

    @SelectProvider(method = "getRportCountByTime", type = SqlAisProvider.class)
    public Integer getRportCountByTime(@Param("begintime") String begintime, @Param("endtime") String endtime, @Param("searchkey") String searchkey);

    @Insert("INSERT INTO WD_INS_REPORT(ID,TITLE,CREATE_TIME,REPORTLINK) VALUES(#{report.id},#{report.title},#{report.create_time},#{report.reportlink})")
    public int insert(@Param("report") INSReport report);
    
    @Select("SELECT REPORTLINK FROM WD_INS_REPORT WHERE ID=#{reportId}")
    public String getReportNameById(@Param("reportId") String reportId);
    
}
