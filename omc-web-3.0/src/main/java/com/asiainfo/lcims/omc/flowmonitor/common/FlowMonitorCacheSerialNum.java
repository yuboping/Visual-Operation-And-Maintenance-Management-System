package com.asiainfo.lcims.omc.flowmonitor.common;

import java.util.concurrent.ConcurrentHashMap;

import com.asiainfo.lcims.omc.flowmonitor.model.SerialNumInfo;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

/**
 * taskId对应序列号的缓存数据.
 * 
 * @author XHT
 *
 */
public class FlowMonitorCacheSerialNum {
    // 序列号缓存
    private static ConcurrentHashMap<String, String> serialNumMap = new ConcurrentHashMap<>();

    /**
     * 根据taskId生成对应的序列号,如果当前taskId已经有序列号则返回已存在的序列号
     * 
     * @param taskId
     * @return
     */
    public static synchronized SerialNumInfo getSerialNum(String taskId) {
        SerialNumInfo info = new SerialNumInfo();
        boolean containsKey = serialNumMap.containsKey(taskId);
        if (containsKey) {
            info.setNewFlag(false);
            info.setSerialNum(serialNumMap.get(taskId));
        } else {
            info.setNewFlag(true);
            String uuid = IDGenerateUtil.getUuid();
            serialNumMap.put(taskId, uuid);
            info.setSerialNum(uuid);
        }
        return info;
    }

    /**
     * 移除taksId对应的序列号
     * <p>
     * 移除序列号对应的缓存数据
     * 
     * @param taskId
     */
    public static void removeTaskId(String taskId) {
        String serialNum = serialNumMap.remove(taskId);
        FlowMonitorCacheDetailResult.removeBySerialNum(serialNum);
    }
}
