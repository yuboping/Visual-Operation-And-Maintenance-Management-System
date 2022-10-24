package com.asiainfo.ais.omcstatistic.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.asiainfo.ais.omcstatistic.common.Constant;
import com.asiainfo.ais.omcstatistic.common.DateUtil;
import com.asiainfo.ais.omcstatistic.common.DbDateFormatReplaceUtil;
import com.asiainfo.ais.omcstatistic.common.RegexUtil;
import com.asiainfo.ais.omcstatistic.common.ServerResponse;
import com.asiainfo.ais.omcstatistic.exception.StatisticException;
import com.asiainfo.ais.omcstatistic.mapper.MdStatisticRuleMapper;
import com.asiainfo.ais.omcstatistic.pojo.MdStatisticRule;
import com.asiainfo.ais.omcstatistic.pojo.MdStatisticSql;
import com.asiainfo.ais.omcstatistic.pojo.StatisData;

@Component
public class ExcuteStatisticService {

    private static final Logger LOG = LoggerFactory.getLogger(ExcuteStatisticService.class);

    @Autowired
    MdStatisticRuleMapper mdStatisticRuleMapper;

    @Value("${mapper.identity}")
    private String dbType;

    public ServerResponse excuteSingleSql(String formatdate, String interval, String statisticSql,
            Map<String, MdStatisticSql> sqlMap, String expression, String offset) {

        List<StatisData> statisDataList;
        int statisDataRs;

        try {
            // 获取需替换的数据
            List<String> replaceList = RegexUtil.getReplaceWithRegex(statisticSql);
            // 替换sql变量
            statisticSql = getReplaceSql(formatdate, interval, replaceList, statisticSql, sqlMap,
                    expression, offset);

            // 根据定义的格式数据再次转换
            String replacedate = DateUtil.getNormalTime(formatdate, Constant.MONTH_REPORT);
            statisticSql = DbDateFormatReplaceUtil.mkReplaceSqlStr(statisticSql, replacedate);

            LOG.info("Id: " + expression + ", 二次统计sql转换后为：" + statisticSql);

            if (statisticSql.toLowerCase().contains("insert")) {
                statisDataRs = mdStatisticRuleMapper.getStatisDataListWithInsert(statisticSql);
                LOG.info("Id: " + expression + "执行完成.");
                return ServerResponse.createBySuccess(statisDataRs);
            } else {
                statisDataList = mdStatisticRuleMapper.getStatisDataList(statisticSql);
                return ServerResponse.createBySuccess(statisDataList);
            }

        } catch (Exception e) {
            LOG.error("二次统计sql执行失败:" + e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    public ServerResponse excuteMultiSql(String formatdate, String interval, String statisticSql,
            Map<String, MdStatisticSql> sqlMap, String expression, String offset) {

        StatisData statisData = new StatisData();

        try {
            // 获取需替换的数据
            List<String> replaceList = RegexUtil.getReplaceWithRegex(statisticSql);
            // 替换sql变量
            statisticSql = getReplaceSql(formatdate, interval, replaceList, statisticSql, sqlMap,
                    expression, offset);

            // 根据定义的格式数据再次转换
            String replacedate = DateUtil.getNormalTime(formatdate, Constant.MONTH_REPORT);
            statisticSql = DbDateFormatReplaceUtil.mkReplaceSqlStr(statisticSql, replacedate);

            int mvalueResult = RegexUtil.calcByString(statisticSql);

            statisData.setMvalue(String.valueOf(mvalueResult));

            LOG.debug("二次统计sql转换后为：" + statisticSql);

        } catch (StatisticException e) {
            LOG.error(e.getErrorMsg());
            return ServerResponse.createByErrorMessage(e.getErrorMsg());
        } catch (Exception e) {
            LOG.error("二次统计sql执行失败:" + e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
        return ServerResponse.createBySuccess(statisData);
    }

    public ServerResponse saveStatisticResult(String formatdate, String interval,
            List<StatisData> statisDataList, String tableName, MdStatisticRule mdStatisticRule,
            String offset) {
        int successCount = 0;
        // 根据规则表的表名 动态拼接表名
        if (Constant.DAY_STATISTIC.equals(tableName)) {
            // 获取指定格式的时间
            if (Constant.EXCUTE_SPACE_MINUTE.equals(interval)
                    || Constant.EXCUTE_SPACE_MINUTE_10.equals(interval)
                    || Constant.EXCUTE_SPACE_MINUTE_15.equals(interval)
                    || Constant.EXCUTE_SPACE_MINUTE_20.equals(interval)
                    || Constant.EXCUTE_SPACE_MINUTE_30.equals(interval)) {
                tableName = tableName + "_" + DateUtil.getFormatTime(formatdate,
                        Constant.NOW_FORMAT, Constant.REPORT_SUFFIX_DAY_FORMAT, offset);
            } else if (Constant.EXCUTE_SPACE_HOUR.equals(interval)) {
                tableName = tableName + "_" + DateUtil.getFormatTime(formatdate,
                        Constant.PREHOUR_FORMAT, Constant.REPORT_SUFFIX_DAY_FORMAT, offset);
            }
        } else if (Constant.MONTH_STATISTIC.equals(tableName)) {
            tableName = tableName + "_" + DateUtil.getFormatTime(formatdate,
                    Constant.YESTERDAY_FORMAT, Constant.REPORT_SUFFIX_MONTH_FORMAT, offset);
        }

        try {
            for (StatisData statisData : statisDataList) {
                statisData = setStatisData(statisData, mdStatisticRule);
                LOG.debug("插入数据为:" + statisData.toString());
                int successInsert = 0;
                if (dbType.equals("MYSQL")) {
                    successInsert = mdStatisticRuleMapper.insertStatisticResultWithMysql(statisData,
                            tableName);
                } else {
                    successInsert = mdStatisticRuleMapper.insertStatisticResult(statisData,
                            tableName);

                }
                successCount += successInsert;
            }
        } catch (Exception e) {
            LOG.error("二次统计结果插入失败:" + e);
        }

        return ServerResponse.createBySuccess(successCount);
    }

    /**
     * 获取固定格式中的变量
     * 
     * @param replaceInfo
     * @param format
     * @return
     */
    private String getReplaceValue(String replaceInfo, String format) {
        String temp = replaceInfo.replace(format, "");
        temp = temp.substring(0, temp.length() - 1);
        return temp;
    }

    /**
     * 替换sql中的固定变量
     * 
     * @param replaceList
     * @return
     */
    private String getReplaceSql(String formatdate, String interval, List<String> replaceList,
            String statisticSql, Map<String, MdStatisticSql> sqlMap, String expression,
            String offset) {
        for (String replaceInfo : replaceList) {
            // 根据格式替换sql中相应数据
            if (replaceInfo.contains(Constant.TIME_FORMAT)) {

                String formattime = null;

                // 截取time中的时间格式
                String temp = getReplaceValue(replaceInfo, Constant.TIME_FORMAT);

                // 获取指定格式的时间
                if (Constant.EXCUTE_SPACE_MINUTE.equals(interval)
                        || Constant.EXCUTE_SPACE_MINUTE_10.equals(interval)
                        || Constant.EXCUTE_SPACE_MINUTE_15.equals(interval)
                        || Constant.EXCUTE_SPACE_MINUTE_20.equals(interval)
                        || Constant.EXCUTE_SPACE_MINUTE_30.equals(interval)) {
                    formattime = DateUtil.getFormatTime(formatdate, Constant.NOW_FORMAT, temp,
                            offset);
                } else if (Constant.EXCUTE_SPACE_HOUR.equals(interval)) {
                    formattime = DateUtil.getFormatTime(formatdate, Constant.PREHOUR_FORMAT, temp,
                            offset);
                } else if (Constant.EXCUTE_SPACE_DAY.equals(interval)) {
                    formattime = DateUtil.getFormatTime(formatdate, Constant.YESTERDAY_FORMAT, temp,
                            offset);
                } else if (Constant.EXCUTE_SPACE_WEEK.equals(interval)) {
                    formattime = DateUtil.getFormatTime(formatdate, Constant.YESTERWEEK_FORMAT,
                            temp, offset);
                } else if (Constant.EXCUTE_SPACE_MONTH.equals(interval)) {
                    // formattime = DateUtil.getFormatTime(formatdate,
                    // Constant.YESTERMONTH_FORMAT, temp, offset);
                    formattime = DateUtil.getFormatTimeWithCal(formatdate,
                            Constant.YESTERMONTH_FORMAT, temp, offset);
                }

                // Pattern.quote 因replaceAll为正则，若包含特殊字符会报错，需转义
                statisticSql = statisticSql.replaceAll(Pattern.quote(replaceInfo), formattime);
            } else if (replaceInfo.contains(Constant.ID_FORMAT)) {
                // 截取id
                String temp = getReplaceValue(replaceInfo, Constant.ID_FORMAT);
                // 根据id获取执行sql
                String excuteSql = sqlMap.get(temp).getStatisticSql();
                // 执行sql
                ServerResponse response = excuteSingleSql(formatdate, interval, excuteSql, sqlMap,
                        expression, offset);

                List<StatisData> dataList = (List) response.getData();
                if (dataList.isEmpty()) {
                    throw new StatisticException(
                            "执行sql:" + excuteSql + "中，id为:" + replaceInfo + "的sql执行结果为空");
                }

                String mvalue = dataList.get(0).getMvalue();

                statisticSql = statisticSql.replaceAll(Pattern.quote(replaceInfo), mvalue);
            }
        }
        return statisticSql;
    }

    private StatisData setStatisData(StatisData statisData, MdStatisticRule mdStatisticRule) {
        // 若获取不到值，则获取规则表中默认值
        statisData.setAttr1(StringUtils.isEmpty(statisData.getAttr1()) ? mdStatisticRule.getAttr1()
                : statisData.getAttr1());

        statisData.setAttr2(StringUtils.isEmpty(statisData.getAttr2()) ? mdStatisticRule.getAttr2()
                : statisData.getAttr2());

        statisData.setAttr3(StringUtils.isEmpty(statisData.getAttr3()) ? mdStatisticRule.getAttr3()
                : statisData.getAttr3());

        statisData.setAttr4(StringUtils.isEmpty(statisData.getAttr4()) ? mdStatisticRule.getAttr4()
                : statisData.getAttr4());

        statisData.setMetricId(
                StringUtils.isEmpty(statisData.getMetricId()) ? mdStatisticRule.getMetricId()
                        : statisData.getMetricId());

        // 此字段主要是为了在折线图展示横轴
        statisData.setStime(mdStatisticRule.getStime());

        statisData.setCreateTime(DateUtil.getFormatTime(Constant.CREATE_TIME_FORMAT));

        return statisData;
    }

}
