package com.asiainfo.lcims.omc.utils;

/**
 * 
 * @author zhujiansheng
 * @date 2018年7月5日 上午10:34:23
 * @version V1.0
 */
public class IDGenerateUtil {

    /**
     * 随机生成一个唯一随机数UUID
     * 
     */
    public static String getUuid() {
        long nanoTime = System.nanoTime();
        long random = (long) (10000 + Math.random() * 90000);
        String uuid = String.valueOf(nanoTime) + String.valueOf(random);
        return uuid;
    }

}
