package com.asiainfo.lcims.omc.persistence.monitor;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.model.monitor.MdChartDetail;
import com.asiainfo.lcims.omc.model.monitor.MdPageCharts;
import com.asiainfo.lcims.omc.persistence.SqlProvider;

public interface PageChartDAO {

    @Select("SELECT C.* FROM MD_PAGE_CHARTS P LEFT JOIN MD_CHART_DETAIL C ON P.CHART_NAME=C.CHART_NAME WHERE P.URL=#{uri} ORDER BY P.SEQUENCE")
    List<MdChartDetail> getChartInfosByUrl(@Param("uri") String uri);

    @Select("SELECT * FROM MD_PAGE_CHARTS ORDER BY SEQUENCE")
    List<MdPageCharts> getPageChartsAll();

    @Select("SELECT URL FROM MD_PAGE_CHARTS GROUP BY URL")
    List<Map<String, Object>> getGroupURL();

    @Select("SELECT mcd.METRIC_IDS FROM MD_PAGE_CHARTS mpc LEFT JOIN MD_CHART_DATASET mcd ON mcd.CHART_NAME = mpc.CHART_NAME WHERE mpc.URL=#{URL}")
    List<Map<String, Object>> getMetricIdByUrl(@Param("URL") String URL);

    @Select("SELECT * FROM MD_PAGE_CHARTS WHERE URL=#{URL}")
    List<MdPageCharts> getMdPageChartsByUrl(@Param("URL") String URL);

    @SelectProvider(method = "getChartNamePageChart", type = SqlProvider.class)
    List<Map<String, Object>> getChartNamePageChart(@Param("URL") String URL,
            @Param("metric_id") String metric_id);

    @Select("SELECT mcde.CHART_NAME,mcde.CHART_TITLE,mpc.CHART_TYPE FROM MD_PAGE_CHARTS mpc LEFT JOIN MD_CHART_DETAIL mcde ON mcde.CHART_NAME = mpc.CHART_NAME WHERE URL=#{URL} GROUP BY mcde.CHART_NAME,mcde.CHART_TITLE,mpc.CHART_TYPE")
    List<Map<String, Object>> getChartsByUrl(@Param("URL") String URL);

    @Select("SELECT mcd.METRIC_IDS FROM MD_PAGE_CHARTS mpc LEFT JOIN MD_CHART_DATASET mcd ON mcd.CHART_NAME = mpc.CHART_NAME WHERE mpc.URL=#{URL} AND mpc.CHART_NAME=#{chart_name}")
    List<Map<String, Object>> getMetricIdByUrlAndChartName(@Param("URL") String URL,
            @Param("chart_name") String chart_name);
}
