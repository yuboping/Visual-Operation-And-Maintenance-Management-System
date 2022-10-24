package com.asiainfo.lcims.omc.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectClassUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectClassUtil.class);

	public static Object reflect(Object obj,String fieldname) {
		try {
			if (obj == null)
				return null;
			Field[] fields = obj.getClass().getDeclaredFields();
			for (int j = 0; j < fields.length; j++) {
				fields[j].setAccessible(true);
				if (fields[j].getName().equals(fieldname)) {
					return fields[j].get(obj);
				}
			}
        } catch (Exception e) {
            LOG.error("reflect field error, reason : {}", e);
		}
		return null;
	}
}
