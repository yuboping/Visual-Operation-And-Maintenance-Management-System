package com.asiainfo.lcims.omc.filter;

import com.asiainfo.lcims.omc.util.ToolsUtils;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedHashMap;
import java.util.Map;

public class XssHttpServletRequest extends HttpServletRequestWrapper {
    HttpServletRequest orgRequest = null;

    public XssHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    // html过滤
    private final static HTMLFilter htmlFilter = new HTMLFilter();

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (!ToolsUtils.StringIsNull(value)) {
            value = xssEncode(value);
        }
        return StringEscapeUtils.unescapeHtml(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] value = super.getParameterValues(name);
        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                value[i] = xssEncode(value[i]);
                value[i] = StringEscapeUtils.unescapeHtml(value[i]);
            }
        }
        return value;
    }


    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new LinkedHashMap<>();
        Map<String, String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                values[i] = xssEncode(values[i]);
                values[i] = StringEscapeUtils.unescapeHtml(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (!ToolsUtils.StringIsNull(value)) {
            value = xssEncode(value);
        }
        return StringEscapeUtils.unescapeHtml(value);
    }

    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符 在保证不删除数据的情况下保存
     * 
     * @param
     * @return 过滤后的值
     */
    public static String xssEncode(String value) {
//        if (ToolsUtils.StringIsNull(value)) {
//            return value;
//        }
//        value = value.replaceAll("eval\\((.*)\\)", "");
//        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
//        value = value.replaceAll("(?i)<script.*?>.*?<script.*?>", "");
//        value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
//        value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
//        value = value.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");
//        return value;
        return htmlFilter.filter(value);
    }
}
