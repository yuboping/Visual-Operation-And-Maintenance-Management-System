package com.asiainfo.lcims.omc.util;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
    private static Random random = new Random();

    static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

    static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);// 表示忽略大小写

    /**
     * 
     * <p>
     * Description:将list map数据转换为文件存储数据
     * </p>
     * 
     * @author
     * @date 2016年3月16日上午11:48:02
     * @param params
     *            需要转换的list map
     * @param sign
     *            分隔符,比如|
     * @return 以|分割的字符串的值
     */
    public static String mapToStr(List<Map<?, ?>> params, String sign) {
        StringBuffer result = new StringBuffer();

        if (null != params && params.size() > 0) {
            for (Map<?, ?> map : params) {
                result.append(mapToStr(map, sign));
            }
        }

        return result.toString();
    }

    /**
     * 
     * <p>
     * Description:将 map数据转换为文件存储数据
     * </p>
     * 
     * @author
     * @date 2016年4月8日下午3:44:08
     * @param params
     * @param sign
     * @return
     */
    public static String mapToStr(Map<?, ?> params, String sign) {
        StringBuffer result = new StringBuffer();
        int sum = 1;
        for (Map.Entry<?, ?> entry : params.entrySet()) {
            if (!"".equals(entry.getValue()) && null != entry.getValue()) {
                if (sum == params.size()) {
                    result.append(entry.getValue());
                } else {
                    result.append(entry.getValue()).append(sign);
                }
            } else {
                if (sum == params.size()) {
                    result.append("");
                } else {
                    result.append("").append(sign);
                }
            }
            sum++;
        }
        return result.toString();
    }

    /**
     * 
     * <p>
     * Description:list bean 数据转换为文件存储数据
     * </p>
     * 
     * @author
     * @date 2016年3月16日下午2:16:27
     * @param params
     *            需要转换的list bean
     * @param sign
     *            分隔符,比如|
     * @return 以|分割的字符串的值
     * @throws Exception
     */
    public static String beanToStr(List<?> params, String sign) throws Exception {
        StringBuffer result = new StringBuffer();

        if (null != params && params.size() > 0) {
            for (int i = 0; i < params.size(); i++) {
                result.append(beanToStr(params.get(i), sign));
            }
        }

        return result.toString();
    }

    /**
     * 
     * <p>
     * Description:bean 数据转换为文件存储数据
     * </p>
     * 
     * @author
     * @param <T>
     * @date 2016年3月16日下午2:16:27
     * @param params
     *            需要转换的list bean
     * @param sign
     *            分隔符,比如|
     * @return 以|分割的字符串的值
     * @throws Exception
     */
    public static <T> String beanToStr(T t, String sign) throws Exception {
        StringBuffer result = new StringBuffer();

        // java 反射获取bean属性
        Field[] fields = t.getClass().getDeclaredFields();
        // 属性被声明为private的,需要将setAccessible设置为true,默认的值为false
        Field.setAccessible(fields, true);
        for (int j = 0; j < fields.length; j++) {
            if (null != fields[j].get(t)) {
                if (j == fields.length - 1) {
                    result.append(fields[j].get(t));
                } else {
                    result.append(fields[j].get(t)).append(sign);
                }
            } else {
                if (j == fields.length - 1) {
                    result.append("");
                } else {
                    result.append("").append(sign);
                }
            }
        }

        return result.toString();
    }

    /**
     * 
     * <p>
     * Description:将分割的字符串转换为list map
     * </p>
     * 
     * @author
     * @date 2016年3月21日下午5:36:36
     * @param title
     *            map key值,自定义
     * @param content
     *            map value值,分割获取
     * @return
     */
    public static List<Map<String, Object>> strToMap(String[] title, String content) {
        List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
        if (null != title && title.length > 0) {
            if (content.contains("|")) {
                String[] cont = content.split("\\|");
                if (title.length == cont.length) {
                    for (int i = 0; i < title.length; i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(title[i], cont[i]);
                        resultMap.add(map);
                    }
                }
            }
        }
        return resultMap;
    }

    /**
     * 该方法只返回一条数据
     * <p>
     * Description:将分割的字符串分割成list bean
     * </p>
     * 
     * @author
     * @date 2016年3月22日上午10:19:10
     * @param t
     *            pojo 实体类
     * @param content
     *            分割的字符串
     * @return List bean 字符串的值要对应bean属性的顺序
     * @throws Exception
     */
    public static <T> List<T> strTobean(T t, String content) throws Exception {
        List<T> resultList = new ArrayList<T>();
        if (content.contains("|")) {
            String[] cont = content.split("\\|");
            for (int i = 0; i < cont.length; i++) {
                Field[] fields = t.getClass().getDeclaredFields();
                Field.setAccessible(fields, true);
                fields[i].set(t, cont[i]);
            }
            resultList.add(t);
        }
        return resultList;
    }

    /**
     * 
     * <p>
     * Description:修改分割字符串中的值
     * </p>
     * 
     * @author
     * @date 2016年4月22日下午5:53:11
     * @param spl
     *            需要修改的分割字符串
     * @param sum
     *            需要修改的位置
     * @param value
     *            修改位置对应的值
     * @return
     */
    public static String updToString(String spl, int sum, String value) {
        String[] ct = spl.split("\\|");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ct.length; i++) {
            if (i == sum) {
                ct[i] = value;
                sb.append(ct[i]).append("|");
            } else {
                sb.append(ct[i]).append("|");
            }
        }
        return sb.toString();
    }

    /**
     * 
     * <p>
     * Description:获取2个数的百分比
     * </p>
     * 
     * @author
     * @date 2016年7月14日 下午6:22:36
     * @param num
     * @param sum
     * @return
     */
    public static String getRatiso(int num, int sum) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) num / (float) sum * 100);
        return result + "%";
    }

    /**
     * 
     * <p>
     * Description:生成短信内容
     * </p>
     * 
     * @author
     * @date 2016年8月22日 下午2:36:56
     * @return
     */
    public static String getContent(String code) {
        return "保钱袋的验证码为：" + code + "。请于15分钟内正确输入";
    }

    /**
     * 
     * <p>
     * Description:去除表情包数据
     * </p>
     * 
     * @author
     * @date 2016年9月6日 下午3:16:04
     * @param source
     * @return
     */
    public static String creatBrow(String source) {
        if (source != null && source.length() > 0) {
            return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
        } else {
            return source;
        }
    }

    /**
     * 
     * <p>
     * Description:获取时间戳
     * </p>
     * 
     * @author
     * @date 2016年9月18日 下午4:33:02
     * @return
     */
    public static String getTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 
     * <p>
     * Description:生成随机字符串
     * </p>
     * 
     * @author
     * @date 2016年9月18日 下午5:11:32
     * @param length
     *            字符串的长度
     * @return
     */
    public static String getNonceStr(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFHIJKLMNOPSUVWXYZ";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 
     * <p>
     * Description:UUID创建随机字符串
     * </p>
     * 
     * @author
     * @date 2016年9月18日 下午5:33:49
     * @return
     */
    public static String getUUIDStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 将emoji表情替换成空串
     * 
     * @param source
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source) {
        if (source != null && source.length() > 0) {
            return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
        } else {
            return source;
        }
    }

    /**
     * 
     * @Title: listToString @Description: TODO(list用逗号拼接) @param @param
     *         list @param @return 参数 @return String 返回类型 @throws
     */
    public static String listToString(List<String> list) {
        if (list == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        // 第一个前面不拼接","
        for (String string : list) {
            if (first) {
                first = false;
            } else {
                result.append(",");
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 
     * @Title: SqlFilter @Description: TODO(过滤 Sql 语句字符串中的注入脚本) @param @param
     *         source @param @return 参数 @return String 返回类型 @throws
     */
    public static String SqlFilter(String source) {
        if (null != source) {
            // 单引号替换成两个单引号
            source = source.replaceAll("'", "''");
        }
        return source;
    }

    /**
     * 参数校验
     * 
     * @param str
     *            ep: "or 1=1"
     */
    public static String isSqlValid(String str) {
        Matcher matcher = sqlPattern.matcher(str);
        if (matcher.find()) {
            logger.error("参数存在非法字符，请确认：" + matcher.group());// 获取非法字符：or
            return "";
        }
        return str;
    }

    /**
     * 
     * @Title: SqlQuotation @Description: TODO(单引号替换成三个单引号) @param @param
     *         source @param @return 参数 @return String 返回类型 @throws
     */
    public static String SqlQuotation(String source) {
        if (null != source) {
            // 单引号替换成三个单引号
            source = source.replaceAll("'", "'''");
        }
        return source;
    }

    /**
     * 
     * @Title: isContains @Description: TODO(判断分隔符的字符串中是否包含某个数的实例) @param @param
     *         delimiter @param @param ids @param @param s @param @return
     *         参数 @return boolean 返回类型 @throws
     */
    public static boolean isContains(String delimiter, String ids, String s) {
        boolean result = false;
        String[] values = ids.split(delimiter);
        List<String> list = Arrays.asList(values);
        if (list.contains(s)) {
            result = true;
        }
        return result;
    }

    /**
     * 
     * @Title: isMonthDay @Description: TODO(判断是否是月日) @param @param monthday
     *         MMdd @param @return 参数 @return boolean 返回类型 @throws
     */
    public static boolean isValidMonthDay(String monthday) {
        boolean result = true;
        if (monthday != null && !monthday.equals("")) {
            try {
                // 指定日期格式为两位月份/两位日期，注意MM/dd区分大小写；
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                // 设置lenient为false.
                // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
                format.setLenient(false);
                format.parse("2000" + monthday);
            } catch (Exception e) {
                logger.error("data format transfer error, {}", e);
                // 如果throw
                // java.text.ParseException或者NullPointerException，就说明格式不对
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * 
     * @Title: mapListToArray @Description: TODO(mapList转换2维数组) @param @param
     *         mapList @param @return 参数 @return String[][] 返回类型 @throws
     */
    public static String[][] mapListToArray(List<Map<String, Object>> mapList,
            List<String> keyList) {
        String[][] datas = new String[mapList.size()][keyList.size()];
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> map = mapList.get(i);
            int j = 0;
            for (String key : keyList) {
                datas[i][j] = map.get(key).toString();
                j++;
            }
        }
        return datas;
    }

    /**
     * 
     * @Title: removeLastSplit @Description:
     *         TODO(字符串移除最后一个分隔符的分隔的内容) @param @param delimiter @param @param
     *         source @param @return 参数 @return String 返回类型 @throws
     */
    public static String removeLastSplit(String delimiter, String source) {
        String s[] = source.split(delimiter);
        int length = s.length;
        int lastlength = 0;
        if (length > 0) {
            lastlength = s[length - 1].length() + 1;
        }
        String result = source.substring(0, source.length() - lastlength);
        return result;
    }

    /**
     * 
     * @Title: isIP @Description: TODO(用正则表达式判断是否为IP ) @param @param
     *         addr @param @return 参数 @return boolean 返回类型 @throws
     */
    public static boolean isIP(String addr) {
        Pattern pattern1 = Pattern.compile(
                "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

        Matcher matcher1 = pattern1.matcher(addr);

        Boolean ipAddress = matcher1.matches();

        return ipAddress;
    }

    /**
     * 判断端口号是否合法
     * 
     * @param portStr
     * @return
     */
    public static boolean isPort(String portStr) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(portStr);
        if (isNum.matches() && portStr.length() < 6 && Integer.valueOf(portStr) >= 1
                && Integer.valueOf(portStr) <= 65535) {
            return true;
        }
        return false;
    }
}
