package com.asiainfo.lcims.omc.utils;

import java.util.List;
import java.util.Map;

public class ToolsUtils {
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean StringIsNull(String str){
    	if(null==str || "".equals(str))
    		return true;
    	return false;
    }
    
    public static boolean ListIsNull(List<?> list){
    	if(null==list || list.isEmpty())
    		return true;
    	return false;
    }
    
    public boolean isKeyInMap(Map<String, String > map,String key){
        if(map.containsKey(key)){
            return true;
        }
        return false;
    }
    
}
