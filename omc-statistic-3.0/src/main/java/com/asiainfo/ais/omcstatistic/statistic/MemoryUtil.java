package com.asiainfo.ais.omcstatistic.statistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asiainfo.ais.omcstatistic.common.Constant;
import com.asiainfo.ais.omcstatistic.common.ServerResponse;
import com.asiainfo.ais.omcstatistic.mapper.MdCollCycleMapper;
import com.asiainfo.ais.omcstatistic.mapper.MdStatisticRuleMapper;
import com.asiainfo.ais.omcstatistic.mapper.MdStatisticSqlMapper;
import com.asiainfo.ais.omcstatistic.pojo.MdCollCycle;
import com.asiainfo.ais.omcstatistic.pojo.MdStatisticRule;
import com.asiainfo.ais.omcstatistic.pojo.MdStatisticSql;

@Component
public class MemoryUtil {

    @Autowired
    MdStatisticRuleMapper mdStatisticRuleMapper;

    @Autowired
    MdStatisticSqlMapper mdStatisticSqlMapper;

    @Autowired
    MdCollCycleMapper mdCollCycleMapper;

    // 静态变量 存放 按分钟间隔执行的规则
    private static List<MdStatisticRule> minuteRuleList = new ArrayList();
    // 静态变量 存放 按小时间隔执行的规则
    private static List<MdStatisticRule> hourRuleList = new ArrayList();
    // 静态变量 存放 按天间隔执行的规则
    private static List<MdStatisticRule> dayRuleList = new ArrayList();
    // 静态变量 存放 按周间隔执行的规则
    private static List<MdStatisticRule> weekRuleList = new ArrayList();
    // 静态变量 存放 按月间隔执行的规则
    private static List<MdStatisticRule> monthRuleList = new ArrayList();

    // 静态hashmap key存放id value存放mdStatisticSql类
    private static HashMap<String, MdStatisticSql> mdStatisticSqlHashMap = new HashMap<>();

    // 静态hashmap key存放id value存放Cron表达式
    private static HashMap<Integer, String> mdCollCycleHashMap = new HashMap<>();

    public static List<MdStatisticRule> getMinuteRuleList(String excute_space) {
        List<MdStatisticRule> mdStatisticRuleList = new ArrayList<MdStatisticRule>();
        for (MdStatisticRule mdStatisticRule : minuteRuleList) {
            if (excute_space.equals(mdStatisticRule.getRuleInterval())) {
                mdStatisticRuleList.add(mdStatisticRule);
            }
        }
        return mdStatisticRuleList;
    }

    public static List<MdStatisticRule> getHourRuleList() {
        return hourRuleList;
    }

    public static List<MdStatisticRule> getDayRuleList() {
        return dayRuleList;
    }

    public static List<MdStatisticRule> getWeekRuleList() {
        return weekRuleList;
    }

    public static List<MdStatisticRule> getMonthRuleList() {
        return monthRuleList;
    }

    public static HashMap<String, MdStatisticSql> getMdStatisticSqlHashMap() {
        return mdStatisticSqlHashMap;
    }

    public static HashMap<Integer, String> getMdCollCycleHashMap() {
        return mdCollCycleHashMap;
    }

    /**
     * 将表中的规则放入内存中
     * 
     * @return
     */
    public ServerResponse setRuleToMemory() {
        List<MdStatisticRule> ruleList = mdStatisticRuleMapper.selectAll();

        // 清除list中数据
        minuteRuleList.clear();
        hourRuleList.clear();
        dayRuleList.clear();
        weekRuleList.clear();
        monthRuleList.clear();

        for (MdStatisticRule mdStatisticRule : ruleList) {
            if (Constant.EXCUTE_SPACE_MINUTE.equals(mdStatisticRule.getRuleInterval())
                    || Constant.EXCUTE_SPACE_MINUTE_10.equals(mdStatisticRule.getRuleInterval())
                    || Constant.EXCUTE_SPACE_MINUTE_15.equals(mdStatisticRule.getRuleInterval())
                    || Constant.EXCUTE_SPACE_MINUTE_20.equals(mdStatisticRule.getRuleInterval())
                    || Constant.EXCUTE_SPACE_MINUTE_30.equals(mdStatisticRule.getRuleInterval())) {
                minuteRuleList.add(mdStatisticRule);
            } else if (Constant.EXCUTE_SPACE_HOUR.equals(mdStatisticRule.getRuleInterval())) {
                hourRuleList.add(mdStatisticRule);
            } else if (Constant.EXCUTE_SPACE_DAY.equals(mdStatisticRule.getRuleInterval())) {
                dayRuleList.add(mdStatisticRule);
            } else if (Constant.EXCUTE_SPACE_WEEK.equals(mdStatisticRule.getRuleInterval())) {
                weekRuleList.add(mdStatisticRule);
            } else if (Constant.EXCUTE_SPACE_MONTH.equals(mdStatisticRule.getRuleInterval())) {
                monthRuleList.add(mdStatisticRule);
            }
        }

        // 存放待执行sql
        List<MdStatisticSql> mdStatisticSqlList = mdStatisticSqlMapper.selectAll();
        for (MdStatisticSql mdStatisticSql : mdStatisticSqlList) {
            mdStatisticSqlHashMap.put(mdStatisticSql.getId(), mdStatisticSql);
        }

        // 存放cron表达式
        List<MdCollCycle> mdCollCycleList = mdCollCycleMapper.selectAll();
        for (MdCollCycle mdCollCycle : mdCollCycleList) {
            mdCollCycleHashMap.put(mdCollCycle.getCycleid(), mdCollCycle.getCron());
        }

        return ServerResponse.createBySuccessMessage("获取规则成功");
    }

}
