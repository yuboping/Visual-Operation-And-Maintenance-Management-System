package com.asiainfo.lcims.omc.agentserver.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.dao.MdCollCycleDao;
import com.asiainfo.lcims.omc.agentserver.dao.MdMetricDao;
import com.asiainfo.lcims.omc.agentserver.dao.MonHostDao;
import com.asiainfo.lcims.omc.agentserver.enity.MdCollCycle;
import com.asiainfo.lcims.omc.agentserver.enity.MdMetric;
import com.asiainfo.lcims.omc.agentserver.enity.MonHost;

/**
 * 缓存频繁使用的数据库数据
 * 
 * @author XHT
 *
 */
public class DbDataCache {
    private DbDataCache() {
    }

    private static final Logger log = LoggerFactory.make();
    // MON_HOST表中 hostip_hostid的缓存数据
    private static volatile Map<String, String> hostIpHostIdMap = new HashMap<String, String>();

    // MD_COLL_CYCLE中 cycleid_cron的缓存数据
    private static volatile Map<Integer, String> cycleidCronMap = new HashMap<Integer, String>();

    // MD_METRIC表中identity_MetricId的缓存数据
    private static volatile Map<String, String> identityMetricIdMap = new HashMap<String, String>();

    public static void loading() {
        loadTables();
    }

    private static void loadTables() {
        loadMonHost();
        loadMdCollCycle();
        loadMdMetric();
    }

    /**
     * 根据HOSTIP获取主机信息
     * 
     * @param hostIp
     * @param mark:业务处理的mark标识,方便通过mark跟踪日志.
     * @return
     */
    public static String getHostIdByIp(String hostIp, String mark) {
        String tmp = hostIpHostIdMap.get(hostIp);
        if (tmp == null) {
            log.warn("mark:[" + mark + "].hostIp:{} undefined.", hostIp);
            return null;
        }
        return tmp;
    }

    /**
     * 根据cycleid获取对应采集周期的表达式,如果cycleid未定义则返回null.
     * <p>
     * MD_COLL_CYCLE
     * 
     * @param cycleId
     * @param mark:业务处理的mark标识,方便通过mark跟踪日志.
     * @return
     */
    public static String getCronByCycleId(Integer cycleId, String mark) {
        String tmp = cycleidCronMap.get(cycleId);
        if (tmp == null) {
            log.warn("mark:[" + mark + "].cycleId:{} undefined.", cycleId);
            return null;
        }
        return tmp;
    }

    /**
     * 根据identity获取对应指标的指标ID,如果identity未定义则再返回identity.
     * <p>
     * METRIC_IDENTITY
     * 
     * @param identity
     * @param mark:业务处理的mark标识,方便通过mark跟踪日志.
     * @return
     */
    public static String getMetricIdByIdentity(String identity, String mark) {
        if (identity == null || identity.isEmpty()) {
            log.error("mark:[" + mark + "].metric identity is null, please check the value.");
            return null;
        }
        String tmp = identityMetricIdMap.get(identity);
        if (tmp == null) {
            log.warn("mark:[" + mark + "].metric identity:{} undefined.", identity);
            return identity;
        }
        return tmp;
    }

    private static void loadMonHost() {
        Map<String, String> tmpMap = new HashMap<String, String>();
        try {
            List<MonHost> list = (new MonHostDao()).getAllMonHost();
            if (list != null && !list.isEmpty()) {
                for (MonHost info : list) {
                    if (info == null) {
                        continue;
                    }
                    tmpMap.put(info.getAddr(), info.getHostId());
                }
            }
        } catch (Exception e) {
            log.error("init db data MonHost error:" + e);
        }
        hostIpHostIdMap = tmpMap;
    }

    private static void loadMdCollCycle() {
        Map<Integer, String> tmpMap = new HashMap<Integer, String>();
        try {
            List<MdCollCycle> list = (new MdCollCycleDao()).getAllMdCollCycle();
            if (list != null && !list.isEmpty()) {
                for (MdCollCycle info : list) {
                    if (info == null) {
                        continue;
                    }
                    tmpMap.put(info.getCycleId(), info.getCron());
                }
            }
        } catch (Exception e) {
            log.error("init db data MdCollCycle error:" + e);
        }
        cycleidCronMap = tmpMap;
    }

    private static void loadMdMetric() {
        Map<String, String> tmpMap = new HashMap<String, String>();
        try {
            List<MdMetric> list = (new MdMetricDao()).getAllMdMetric();
            if (list != null && !list.isEmpty()) {
                for (MdMetric info : list) {
                    if (info == null) {
                        continue;
                    }
                    tmpMap.put(info.getMetricIdentity(), info.getId());
                }
            }
        } catch (Exception e) {
            log.error("init db data MdMetric error:" + e);
        }
        identityMetricIdMap = tmpMap;
    }
}
