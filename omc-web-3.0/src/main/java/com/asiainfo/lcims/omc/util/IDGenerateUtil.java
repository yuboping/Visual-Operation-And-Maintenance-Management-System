package com.asiainfo.lcims.omc.util;

import java.util.UUID;

/**
 * 
 * @author zhujiansheng
 * @date 2018年7月5日 上午10:17:11
 * @version V1.0
 */
public class IDGenerateUtil {

    /**
     * 根据时间和随机数生成一个32位的唯一随机数
     * 
     */
    public static String getUuid() {
    	return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
