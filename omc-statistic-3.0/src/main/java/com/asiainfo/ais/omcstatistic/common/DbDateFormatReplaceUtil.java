package com.asiainfo.ais.omcstatistic.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据sql中关于时间的正则表达式替换成 date中对应的数据,sql中的时间格式不区分大小写 大括号中前半截为对应的时间格式,num为相应的数字
 * <p>
 * index为关联下标,如果需要拆分{YYYY} {MM} {DD}的话,可以通过index进行关联标记，相同index的{YYYY} {MM} {DD}
 * 会进行整体的计算后再根据格式替换时间
 * <p>
 * {DD} {DD+num} {DD-num} {DD_index} {DD_index+num} {DD_index-num}
 * <p>
 * {MM} {MM+num} {MM-num} {MM_index} {MM_index+num} {MM_index-num}
 * <p>
 * {YYYY} {YYYY+num} {YYYY-num} {YYYY_index} {YYYY_index+num} {YYYY_index-num}
 * <p>
 * {YYYYMM} {YYYYMM+num} {YYYYMM-num} {YYYYMM_index} {YYYYMM_index+num}
 * {YYYYMM_index-num}
 * <p>
 * {YYYYMMDD} {YYYYMMDD+num} {YYYYMMDD-num} {YYYYMMDD_index}
 * {YYYYMMDD_index+num} {YYYYMMDD_index-num}
 *
 * @author XHT
 *
 */
public class DbDateFormatReplaceUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DbDateFormatReplaceUtil.class);

    private static final String DATE_FORMAT_INDEX_STR = "_";
    private static final String FIND_NUM = "([+|-][0-9]+)";
    private static final String PLUS_NUM_FOR_TMP = "(}[+|-][0-9]+})";
    private static final String PLUS_NUM_FOR_INDEX = "([^\\}][+|-][0-9]+})";

    private static String ALL_DATE_INFO = "";
    private static String DATE_FORMAT = "";
    static {
        ALL_DATE_INFO = ALL_DATE_INFO + "(\\{{1,2}DD([_][0-9]+)?([+|-][0-9]+)?}([+|-][0-9]+})?)";
        ALL_DATE_INFO = ALL_DATE_INFO + "|(\\{{1,2}MM([_][0-9]+)?([+|-][0-9]+)?}([+|-][0-9]+})?)";
        ALL_DATE_INFO = ALL_DATE_INFO + "|(\\{{1,2}YYYY([_][0-9]+)?([+|-][0-9]+)?}([+|-][0-9]+})?)";
        ALL_DATE_INFO = ALL_DATE_INFO
                + "|(\\{{1,2}YYYYMM([_][0-9]+)?([+|-][0-9]+)?}([+|-][0-9]+})?)";
        ALL_DATE_INFO = ALL_DATE_INFO
                + "|(\\{{1,2}YYYYMMDD([_][0-9]+)?([+|-][0-9]+)?}([+|-][0-9]+})?)";

        DATE_FORMAT = DATE_FORMAT + "(\\{DD([_][0-9]+)?([+|-]{1,1}|}))";
        DATE_FORMAT = DATE_FORMAT + "|(\\{MM([_][0-9]+)?([+|-]{1,1}|}))";
        DATE_FORMAT = DATE_FORMAT + "|(\\{YYYY([_][0-9]+)?([+|-]{1,1}|}))";
        DATE_FORMAT = DATE_FORMAT + "|(\\{YYYYMM([_][0-9]+)?([+|-]{1,1}|}))";
        DATE_FORMAT = DATE_FORMAT + "|(\\{YYYYMMDD([_][0-9]+)?([+|-]{1,1}|}))";
    }

    private DbDateFormatReplaceUtil() {
    }

    public static void main(String[] args) {
        String sql = "{YYYYMMDD_11},{YYYY_11-1},{MM_11+1},{MM_11-1},{DD_11}";
        System.out.println(sql);
        LocalDate date = LocalDate.of(2017, 1, 31);
        String result = mkReplaceSqlStr(sql, String.valueOf(date));
        System.out.println(result);
    }

    public static String mkReplaceSqlStr(String sql, String date) {
        Set<String> needReplaceStrSet = findAllNeedRepalceStr(sql);
        List<DbSqlReplaceNode> nodeIndexRepList = makeStrToNodeList(needReplaceStrSet);
        LocalDate localDate = LocalDate.parse(date);
        nodeIndexRepList = cvDateStrToReplaceInfo(nodeIndexRepList, localDate);
        String result = replaceSqlStr(sql, nodeIndexRepList);
        return result;
    }

    private static String replaceSqlStr(String sql, List<DbSqlReplaceNode> list) {
        // 双括号的数据包含有单括号的数据，所以需要遍历两边，先替换双括号的数据。
        for (DbSqlReplaceNode node : list) {
            if (node.isPlusNumForTmpFlag()) {
                sql = sql.replace(node.getNeedReplaceStr(), node.getReplaceStrTo());
            }
        }
        for (DbSqlReplaceNode node : list) {
            if (!node.isPlusNumForTmpFlag()) {
                sql = sql.replace(node.getNeedReplaceStr(), node.getReplaceStrTo());
            }
        }
        return sql;
    }

    private static List<DbSqlReplaceNode> cvDateStrToReplaceInfo(List<DbSqlReplaceNode> list,
                                                                 LocalDate date) {
        Map<String, MyNodeDate> indexTypeDateMap = new HashMap<>();
        for (DbSqlReplaceNode node : list) {
            String dateFormat = node.getDateFormat();
            Integer plusNumForIndex = node.getPlusNumForIndex();
            String index = node.getIndex();
            if (index != null) {
                // 包含index的时间格式需要先全程计算，然后再计算
                MyNodeDate tmpNodeDate = indexTypeDateMap.get(index);
                if (tmpNodeDate == null) {
                    tmpNodeDate = new MyNodeDate(date);
                }
                tmpNodeDate = datePlusWithType(tmpNodeDate, dateFormat, plusNumForIndex);
                indexTypeDateMap.put(index, tmpNodeDate);
            } else {
                LocalDate tmpDate = datePlusWithType(date, dateFormat, plusNumForIndex);
                node.setReplaceStrTo(getFormatStrWithType(tmpDate, dateFormat));
            }
        }
        mkDateStrInfoForIndex(list, indexTypeDateMap);
        return list;
    }

    // 处理包含index的时间
    private static void mkDateStrInfoForIndex(List<DbSqlReplaceNode> list,
                                              Map<String, MyNodeDate> indexTypeDateMap) {
        for (DbSqlReplaceNode node : list) {
            String index = node.getIndex();
            if (index == null) {
                continue;
            }
            LocalDate tmpDate = indexTypeDateMap.get(index).getDateAfterPlus();

            String dateFormat = node.getDateFormat();
            int plusNumForTmp = node.getPlusNumForTmp();
            if (plusNumForTmp != 0) {
                // {{mm_11+1}+2} 有双括号的数据需要临时更新时间变量。根据MM对{mm_11+1}的时间再 +2
                LocalDate dateForTmp = datePlusWithType(tmpDate, dateFormat, plusNumForTmp);
                node.setReplaceStrTo(getFormatStrWithType(dateForTmp, dateFormat));
            } else {
                node.setReplaceStrTo(getFormatStrWithType(tmpDate, dateFormat));
            }
        }
    }

    // 提取 plusNumForIndex,plusNumForTmp, index,dateFormat等信息到Node
    private static List<DbSqlReplaceNode> makeStrToNodeList(Set<String> needReplaceStrSet) {
        List<DbSqlReplaceNode> nodeList = new LinkedList<>();
        for (String str : needReplaceStrSet) {
            String dateFormat = getDateFormat(str);
            if (dateFormat == null) {
                continue;
            }

            // 提取时间的格式
            DbSqlReplaceNode node = new DbSqlReplaceNode(str);
            if (dateFormat.contains(DATE_FORMAT_INDEX_STR)) {
                node.setIndex(getIndex(dateFormat));
                node.setDateFormat(getDatePre(dateFormat, true));
            } else {
                node.setDateFormat(getDatePre(dateFormat, false));
            }

            // 获取plusNumForTmp
            findAndSetPLusNumForTmp(str, node);

            // 获取plusNumFor'Index
            findAndSetPLusNumForIndex(str, node);
            nodeList.add(node);
        }
        return nodeList;
    }

    private static void findAndSetPLusNumForTmp(String str, DbSqlReplaceNode node) {
        Pattern plusNumForTmpP = Pattern.compile(PLUS_NUM_FOR_TMP);
        Matcher plusNumForTmpM = plusNumForTmpP.matcher(str);
        while (plusNumForTmpM.find()) {
            node.setPlusNumForTmp(getNum(plusNumForTmpM.group()));
            node.setPlusNumForTmpFlag(true);
        }
    }

    private static void findAndSetPLusNumForIndex(String str, DbSqlReplaceNode node) {
        Pattern plusNumForIndexP = Pattern.compile(PLUS_NUM_FOR_INDEX);
        Matcher plusNumForIndexM = plusNumForIndexP.matcher(str);
        while (plusNumForIndexM.find()) {
            node.setPlusNumForIndex(getNum(plusNumForIndexM.group()));
        }
    }

    // test: {YYYY_1 获取得到_1
    private static String getIndex(String str) {
        int startInedx = str.indexOf(DATE_FORMAT_INDEX_STR);
        return str.substring(startInedx, str.length() - 1);
    }

    // test: {YYYY_1 获取得到YYYY
    private static String getDatePre(String str, boolean flag) {
        int endInedx = 0;
        if (flag) {
            endInedx = str.indexOf(DATE_FORMAT_INDEX_STR);
        } else {
            endInedx = str.length() - 1;
        }

        return str.substring(1, endInedx);
    }

    // test:{YYYY_1+1} 获取得到 {YYYY_1+
    private static String getDateFormat(String str) {
        Pattern dateFormatP = Pattern.compile(DATE_FORMAT);
        Matcher dateFormatM = dateFormatP.matcher(str);
        if (dateFormatM.find()) {
            return dateFormatM.group();
        }
        return null;
    }

    // test:{YYYY_1+1} 获取得到 +1
    private static Integer getNum(String str) {
        Pattern plusNumP = Pattern.compile(FIND_NUM);
        Matcher plusNumM = plusNumP.matcher(str);
        int plusNum = 0;
        if (plusNumM.find()) {
            try {
                plusNum = Integer.parseInt(plusNumM.group());
            } catch (Exception e) {
                LOG.error("getNum : "+ e.getMessage());
            }
        }
        return plusNum;
    }

    // 获取所有需要替换的字符串
    private static Set<String> findAllNeedRepalceStr(String sql) {
        // 整个需要替换的时间格式的正则表达式
        Pattern allDateInfoP = Pattern.compile(ALL_DATE_INFO);
        Matcher allDateInfoM = allDateInfoP.matcher(sql);
        Set<String> needReplaceStrSet = new HashSet<>();

        Pattern plusNumForTmpP = Pattern.compile(PLUS_NUM_FOR_TMP);
        while (allDateInfoM.find()) {
            String info = allDateInfoM.group();
            if (info.startsWith("{{")) {// 双大括号开头的数据，如果结尾不是大括号结束,则过滤掉不规范的数据
                Matcher plusNumForTmpM = plusNumForTmpP.matcher(info);
                if (!plusNumForTmpM.find()) {
                    continue;
                }
            }
            needReplaceStrSet.add(info);
        }
        return needReplaceStrSet;
    }

    private static LocalDate datePlusWithType(LocalDate date, String dateType,
                                              Integer datePlusNum) {
        // 根据dateType,对时间date 进行相应的时间计算，计算的大小为datePlusNum
        LocalDate tmpDate = date;
        if ("DD".equals(dateType)) {
            tmpDate = date.plusDays(datePlusNum);
        } else if ("MM".equals(dateType)) {
            tmpDate = date.plusMonths(datePlusNum);
        } else if ("YYYY".equals(dateType)) {
            tmpDate = date.plusYears(datePlusNum);
        } else if ("YYYYMM".equals(dateType)) {
            tmpDate = date.plusMonths(datePlusNum);
        } else if ("YYYYMMDD".equals(dateType)) {
            tmpDate = date.plusDays(datePlusNum);
        }
        return tmpDate;
    }

    private static MyNodeDate datePlusWithType(MyNodeDate date, String dateType, Integer plusNum) {
        if ("DD".equals(dateType)) {
            date.addDayNum(plusNum);
        } else if ("MM".equals(dateType)) {
            date.addMonNum(plusNum);
        } else if ("YYYY".equals(dateType)) {
            date.addYearNum(plusNum);
        } else if ("YYYYMM".equals(dateType)) {
            date.addMonNum(plusNum);
        } else if ("YYYYMMDD".equals(dateType)) {
            date.addDayNum(plusNum);
        }
        return date;
    }

    private static String getFormatStrWithType(LocalDate date, String dateType) {
        // 根据dateType,对时间date 进行相应的时间计算，计算的大小为datePlusNum
        String result = "";
        if ("DD".equals(dateType)) {
            int dayOfMonth = date.getDayOfMonth();
            result = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
        } else if ("MM".equals(dateType)) {
            int month = date.getMonthValue();
            result = month < 10 ? "0" + month : String.valueOf(month);
        } else if ("YYYY".equals(dateType)) {
            result = String.valueOf(date.getYear());
        } else if ("YYYYMM".equals(dateType)) {
            int month = date.getMonthValue();
            String monthStr = month < 10 ? "0" + month : String.valueOf(month);
            result = date.getYear() + monthStr;
        } else if ("YYYYMMDD".equals(dateType)) {
            int month = date.getMonthValue();
            String monthStr = month < 10 ? "0" + month : String.valueOf(month);
            int dayOfMonth = date.getDayOfMonth();
            String dayStr = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
            result = date.getYear() + monthStr + dayStr;
        }
        return result;
    }

    private static class DbSqlReplaceNode {
        DbSqlReplaceNode(String needReplaceStr) {
            this.needReplaceStr = needReplaceStr;
        }

        private String index;
        private String needReplaceStr;
        private int plusNumForIndex;
        private int plusNumForTmp;
        private boolean plusNumForTmpFlag;
        private String dateFormat;
        private String replaceStrTo;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getNeedReplaceStr() {
            return needReplaceStr;
        }

        public String getReplaceStrTo() {
            return replaceStrTo;
        }

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        public void setReplaceStrTo(String replaceStrTo) {
            this.replaceStrTo = replaceStrTo;
        }

        public int getPlusNumForTmp() {
            return plusNumForTmp;
        }

        public void setPlusNumForTmp(int plusNumForTmp) {
            this.plusNumForTmp = plusNumForTmp;
        }

        public boolean isPlusNumForTmpFlag() {
            return plusNumForTmpFlag;
        }

        public void setPlusNumForTmpFlag(boolean plusNumForTmpFlag) {
            this.plusNumForTmpFlag = plusNumForTmpFlag;
        }

        public int getPlusNumForIndex() {
            return plusNumForIndex;
        }

        public void setPlusNumForIndex(int plusNumForIndex) {
            this.plusNumForIndex = plusNumForIndex;
        }
    }

    private static class MyNodeDate {
        private LocalDate date;
        private int plusYear;
        private int plusMon;
        private int plusDay;

        protected MyNodeDate(LocalDate date) {
            this.date = date;
        }

        public MyNodeDate addYearNum(int num) {
            plusYear = plusYear + num;
            return this;
        }

        public MyNodeDate addMonNum(int num) {
            plusMon = plusMon + num;
            return this;
        }

        public MyNodeDate addDayNum(int num) {
            plusDay = plusDay + num;
            return this;
        }

        public LocalDate getDateAfterPlus() {
            return date.plusYears(plusYear).plusMonths(plusMon).plusDays(plusDay);
        }
    }
}
