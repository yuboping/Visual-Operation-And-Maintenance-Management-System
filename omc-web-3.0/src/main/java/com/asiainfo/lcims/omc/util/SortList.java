package com.asiainfo.lcims.omc.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.service.gdcu.HBaseUtils;

public class SortList<E> {
    private static Logger log = LoggerFactory.getLogger(SortList.class);
    
    public  void Sort(List<E> list, final String method, final String sort) {
        Collections.sort(list, new Comparator() {
            public int compare(Object a, Object b) {
                int ret = 0;
                try {
                    Method m1 = ((E) a).getClass().getMethod(method, null);
                    Method m2 = ((E) b).getClass().getMethod(method, null);
                    if (sort != null && "desc".equals(sort))// 倒序
                        ret = m2.invoke(((E) b), null).toString()
                                .compareTo(m1.invoke(((E) a), null).toString());
                    else
                        // 正序
                        ret = m1.invoke(((E) a), null).toString()
                                .compareTo(m2.invoke(((E) b), null).toString());
                } catch (NoSuchMethodException ne) {
                    log.info("NoSuchMethodException",ne);
                } catch (IllegalAccessException ie) {
                    log.info("IllegalAccessException",ie);
                } catch (InvocationTargetException it) {
                    log.info("IllegalAccessException",it);
                }
                return ret;
            }
        });
    }
}
