package com.asiainfo.lcims.omc.model.gdcu;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.lcbmi.utils.RandomID;

public abstract class BaseData {
    protected static final RandomID RANDOM = new RandomID(0, 99);

    protected static final Logger log = LoggerFactory.getLogger(BaseData.class);

    protected String getRandomID() {
        return String.valueOf(System.currentTimeMillis()) + RANDOM.getIntID();
    }
    public String encode() {
        String ret = "";
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                String name = field.getName();
                String fname = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method m = this.getClass().getMethod(fname);
                Object value = m.invoke(this);
                if ("command".equals(name)) {
                    ret += (String) value;
                } else if (value != null && value instanceof java.lang.String) {
                    ret += " {" + (String) value + "}";
                } else if (value != null && value instanceof java.util.Map) {
                    ret += " {" + writeMap(value) + "}";
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        if (log.isDebugEnabled()) {
            log.info(ret);
        }
        return ret;
    }

    private String writeMap(Object value) {
        Map<?, ?> map = (Map<?, ?>) value;
        String tmp = "";
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            tmp += "{" + entry.getKey() + "= " + entry.getValue() + "} ";
        }
        return tmp.trim();
    }

}
