package com.asiainfo.lcims.omc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    /**
     * 替换sql中需替换的字符
     * @param str
     * @return groupList
     */
    public static List<String> getReplaceWithRegex(String str){

        Pattern p = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+ \\| UNREACHABLE");

        Matcher m = p.matcher(str);

        List<String> groupList = new ArrayList<>();

        while(m.find()){

            String hostStr = subStrHost(m.group());
            groupList.add(hostStr);
        }

        return groupList;
    }

    /**
     * 截取hosts
     *
     * @param hostStr
     * @return
     */
    private static String subStrHost(String hostStr) {

        hostStr = hostStr.substring(0, hostStr.indexOf(" "));
        return hostStr;
    }

    public static void main(String[] args) {
        System.out.println(getReplaceWithRegex("10.1.198.82 | UNREACHABLE! => { \"changed\": false, \"msg\":" +
                " \"[Errno None] Unable to connect to port 220 on 10.1.198.82\", \"unreachable\": true }" +
                " 10.21.20.114 | SUCCESS => { \"changed\": false, \"ping\": \"pong\" } 10.1.198.83 | UNREACHABLE! " +
                "=> { \"changed\": false, \"msg\": \"[Errno None] Unable to connect to port 220 on 10.1.198.82\", " +
                "\"unreachable\": true } 10.21.20.115 | SUCCESS => { \"changed\": false, \"ping\": \"pong\" }"));
    }

}
