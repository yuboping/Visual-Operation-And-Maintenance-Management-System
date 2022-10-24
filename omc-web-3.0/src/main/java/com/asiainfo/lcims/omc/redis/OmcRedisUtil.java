package com.asiainfo.lcims.omc.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

/**
 * 向Redis中存放缓存、获取缓存数据
 *
 */
public class OmcRedisUtil {

    private static final Logger LOG = LoggerFactory.getLogger(OmcRedisUtil.class);

    /**
     * 将数值存放到redis缓存中
     * 
     * @param key
     * @param value
     * @param lifeSeconds
     */
    public static void putKeyValue(String key, String value, int lifeSeconds) {
        Jedis jedis = null;
        try {
            jedis = OmcJedisPool.getJedis();
            jedis.set(key, value, SetParams.setParams().ex(lifeSeconds));
        } catch (Exception e) {
            LOG.error("Jedis put [{}] error.", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static void putKeyValue(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = OmcJedisPool.getJedis();
            jedis.set(key, value);
        } catch (Exception e) {
            LOG.error("Jedis put [{}] error.", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static String getValueByKey(final String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = OmcJedisPool.getJedis();
            value = jedis.get(key);
        } catch (Exception e) {
            LOG.error("Jedis read [{}] error.", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    public static Long delByKey(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = OmcJedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            LOG.error("del key:{} error", key, e);
            return result;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

}
