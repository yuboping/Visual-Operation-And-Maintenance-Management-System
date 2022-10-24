package com.asiainfo.ais.omcstatistic.statistic;

import com.asiainfo.ais.omcstatistic.common.Constant;
import com.asiainfo.ais.omcstatistic.common.ServerResponse;
import com.asiainfo.ais.omcstatistic.pojo.MdStatisticRule;
import com.asiainfo.ais.omcstatistic.pojo.MdStatisticSql;
import com.asiainfo.ais.omcstatistic.pojo.StatisData;
import com.asiainfo.ais.omcstatistic.service.ExcuteStatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ExcuteStatisticSql {

    private static final Logger LOG = LoggerFactory.getLogger(ExcuteStatisticSql.class);

    Map<String, MdStatisticSql> sqlMap = MemoryUtil.getMdStatisticSqlHashMap();

    @Autowired
    ExcuteStatisticService excuteStatisticService;

    public ServerResponse excuteSqlWithExpression(MdStatisticRule mdStatisticRule){
        List<StatisData> statisDataList = new ArrayList<>();

        boolean saveFlag;

        if(mdStatisticRule == null) {
            return ServerResponse.createByErrorMessage("规则为空，操作失败");
        }

        String interval = mdStatisticRule.getRuleInterval();

        String formatdate = mdStatisticRule.getCycleTime();

        String expression = mdStatisticRule.getExpression();

        String tableName = mdStatisticRule.getRetTable();

        String offset = mdStatisticRule.getOffset();

        try{
            if(!expression.contains(Constant.ID_FORMAT)) {
                if(sqlMap.get(expression) == null){
                    LOG.error("expression:"+expression+"找不到对应数据,请检查配置sql表");
                    return ServerResponse.createByErrorMessage("执行失败");
                }
                String excuteSql = sqlMap.get(expression).getStatisticSql();
                //执行二次统计sql
                ServerResponse response = excuteStatisticService.excuteSingleSql(formatdate, interval, excuteSql, sqlMap, expression, offset);
                if(response.getData() instanceof Integer){
                    saveFlag = false;
                }else {
                    saveFlag = true;
                    statisDataList = (List) response.getData();
                }
            }else{
                ServerResponse response = excuteStatisticService.excuteMultiSql(formatdate, interval, expression, sqlMap, expression, offset);
                if(response.getData() instanceof Integer){
                    saveFlag = false;
                }else {
                    saveFlag = true;
                    StatisData statisData = (StatisData) response.getData();
                    statisDataList.add(statisData);
                }
            }

            LOG.debug("规则:" + mdStatisticRule.getRuleName() + "二次统计成功");
            if (saveFlag) {
                //统计结果入库
                ServerResponse serverResponse = excuteStatisticService.
                        saveStatisticResult(formatdate, interval, statisDataList, tableName, mdStatisticRule, offset);
                LOG.debug("规则:"+ mdStatisticRule.getRuleName() +"二次统计结果入库成功:"+ serverResponse.getData().toString() +"条");
            }

        }catch (Exception e){
            LOG.error("失败原因：" + e.getMessage());
            return ServerResponse.createByErrorMessage("执行失败："+ e.getMessage());
        }

        return ServerResponse.createBySuccessMessage("执行成功");
    }
}
