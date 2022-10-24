package com.asiainfo.lcims.omc.model.gdcu.obs.resp;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.asiainfo.lcims.omc.util.FindClassByPack;

public abstract class ResponseFactory {
    protected static final Logger log = LoggerFactory.getLogger(ResponseFactory.class);

    protected static final List<Class<?>> LIST = FindClassByPack
            .addClasses(ResponseFactory.class.getPackage().getName());// 加载Response类所在包路径下所有class文件
    
    protected static List<BaseData> resolveClassList(String receive, Class<?> cla) {
    	List<BaseData> resplist = new ArrayList<BaseData>();
    	try {
            if(!receive.contains("|")){
            	return resplist;
            }
            String [] receiveList = receive.split("\\|");
            if(receiveList.length==1){
            	return resplist;
            }
            for(int i=1;i<receiveList.length;i++){
            	resplist.add(resolveClass(receiveList[i],cla));
            }
        } catch (Exception e1) {
            log.error("", e1);
        }
    	return resplist;
    }
    
    protected static BaseData resolveClass(String receive, Class<?> cla) {
        BaseData resp = null;
        try {
            resp = (BaseData) cla.newInstance();
            Field[] fields = cla.getDeclaredFields();
            List<String> splits = splitReceive(receive);
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (i < splits.size()) {
                    Method method = getMethod(cla, field.getName());
                    method.invoke(resp, splits.get(i));
                }
            }
        } catch (Exception e1) {
            log.error("", e1);
        }
        return resp;

    }

    private static List<String> splitReceive(String receive) {
        List<String> list = new ArrayList<String>();
        while (receive.length() > 2) {
            receive = getString(list, receive);
        }
        return list;
    }

    private static String getString(List<String> list, String value) {
        int left = 0;
        int right = 0;
        for (int i = 0; i < value.length(); i++) {
            if ('{' == value.charAt(i)) {
                left++;
            } else if ('}' == value.charAt(i)) {
                right++;
            }
            if (left == right) {
                list.add(value.substring(1, i));
                return (i + 2) < value.length() ? value.substring(i + 2) : "";
            }
        }
        return "";
    }

    private static Method getMethod(Class<?> cla, String name) throws NoSuchMethodException {
        String fname = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        Method m = cla.getMethod(fname, String.class);
        return m;
    }

    public static BaseData decode(String receive, String classname) {
        if (receive == null || "".equals(receive.trim()) || receive.trim().length() < 3) {
            return null;
        }
        receive = receive.trim();
        for (Class<?> cla : LIST) {
            try {
                if (cla.getSimpleName().equals(classname)) {
                    return resolveClass(receive, cla);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return null;
    }
    
    
    public static List<BaseData> decodeList(String receive, String classname) {
        if (receive == null || "".equals(receive.trim()) || receive.trim().length() < 3) {
            return null;
        }
        receive = receive.trim();
        for (Class<?> cla : LIST) {
            try {
                if (cla.getSimpleName().equals(classname)) {
                    return resolveClassList(receive, cla);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return null;
    }
    
}
