package com.asiainfo.ais.omcstatistic.common;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    /**
     * 替换sql中需替换的字符
     * @param sql
     * @return groupList
     */
    public static List<String> getReplaceWithRegex(String sql){

        Pattern p = Pattern.compile("\\#\\{.*?\\}");

        Matcher m = p.matcher(sql);

        List<String> groupList = new ArrayList<>();

        while(m.find()){
            groupList.add(m.group());
        }

        return groupList;
    }

    /**
     * 将字符串转化为运算公式，并计算结果
     * @param strs
     * @return
     * @throws Exception
     */
    public static int calcByString(String strs) throws Exception{
        ScriptEngine jse = new ScriptEngineManager()
                .getEngineByName("JavaScript");
        int calcResult;
        //计算
        calcResult = Integer.parseInt(String.valueOf(jse.eval(strs)));
        return calcResult;
    }

}
