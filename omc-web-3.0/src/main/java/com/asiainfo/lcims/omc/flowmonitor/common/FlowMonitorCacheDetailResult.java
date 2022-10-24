package com.asiainfo.lcims.omc.flowmonitor.common;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.asiainfo.lcims.omc.flowmonitor.model.FlowMonitorRJson;

public class FlowMonitorCacheDetailResult {
    private static ConcurrentHashMap<String, ConcurrentLinkedQueue<FlowMonitorRJson>> dataMap = new ConcurrentHashMap<>();

    /**
     * 根据taskId 判断当前缓存中是否有对应的序列号,如果有则返回对应的序列号,如果没有则重新生成新的序列号.
     * 
     * @param taskId
     * @return
     */
    public static synchronized void putValue(String serialNum, FlowMonitorRJson result) {
        ConcurrentLinkedQueue<FlowMonitorRJson> list = dataMap.get(serialNum);
        if (list == null) {
            ConcurrentLinkedQueue<FlowMonitorRJson> newList = new ConcurrentLinkedQueue<>();
            newList.add(result);
            dataMap.put(serialNum, newList);
        } else {
            list.add(result);
        }
    }

    public static void removeBySerialNum(String serialNum) {
        dataMap.remove(serialNum);
    }

    /**
     * 返回对应的数据
     * 
     * @param serialNum
     * @return
     */
    public static List<FlowMonitorRJson> getInfoBySerialNum(String serialNum) {
        ConcurrentLinkedQueue<FlowMonitorRJson> tmpList = dataMap.get(serialNum);
        List<FlowMonitorRJson> result = new LinkedList<>();
        if (tmpList != null) {
            for (FlowMonitorRJson s : tmpList) {
                result.add(s);
            }
        }
        return result;
    }
}
