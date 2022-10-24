package com.asiainfo.ais.omcstatistic.mapper;

import com.asiainfo.ais.omcstatistic.mapper.impl.MdStatisticRuleMapperImpl;
import com.asiainfo.ais.omcstatistic.pojo.MdStatisticRule;
import com.asiainfo.ais.omcstatistic.pojo.StatisData;
import com.asiainfo.ais.omcstatistic.util.MyMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MdStatisticRuleMapper extends MyMapper<MdStatisticRule> {

    @SelectProvider(method = "getStatisDataList", type = MdStatisticRuleMapperImpl.class)
    List<StatisData> getStatisDataList(@Param("sql") String sql);

    @InsertProvider(method = "getStatisDataList", type = MdStatisticRuleMapperImpl.class)
    int getStatisDataListWithInsert(@Param("sql") String sql);

    @Insert("INSERT INTO ${tableName} (STIME, METRIC_ID, ATTR1, ATTR2, ATTR3, ATTR4, MVALUE, CREATE_TIME) " +
            "VALUES (#{statisData.stime}, #{statisData.metricId}, #{statisData.attr1}, #{statisData.attr2}, #{statisData.attr3}, " +
            "#{statisData.attr4}, #{statisData.mvalue}, #{statisData.createTime})")
    int insertStatisticResultWithMysql(@Param("statisData") StatisData statisData, @Param("tableName") String tableName);

    @InsertProvider(method = "insertStatisDataList", type = MdStatisticRuleMapperImpl.class)
    int insertStatisticResult(@Param("statisData") StatisData statisData, @Param("tableName") String tableName);

    @DeleteProvider(method = "deleteStatisDataList", type = MdStatisticRuleMapperImpl.class)
    int deleteStatisticResult(@Param("deleteTime") String deleteTime, @Param("offset") String offset);

//    @InsertProvider(method = "insertStatisDataListWithMysql", type = MdStatisticRuleMapperImpl.class)
//    int insertStatisticResultWithMysql(@Param("statisData") StatisData statisData, @Param("tableName") String tableName);

}