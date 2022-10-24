package com.asiainfo.lcims.omc.util;

public class StringHelper {

    public static Integer getMaxInteger(Integer i, Integer j) {
        if (i.compareTo(j) > 0) {
            return i;
        } else {
            return j;
        }
    }

    public static Boolean isContainInteger(Integer i, Integer min, Integer max) {
        if (i.compareTo(min) >= 0) {
            if (max.compareTo(i) >= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static Integer getMinInteger(Integer i, Integer j) {
        if (i.compareTo(j) > 0) {
            return j;
        } else {
            return i;
        }
    }

}
