package com.asiainfo.lcims.omc.redis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.util.Base64Util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class OmcJedisPool {

    private static final Logger log = LoggerFactory.getLogger(OmcJedisPool.class);
    public static final BusinessConf BUSCONF = new BusinessConf();

    private static JedisPool pool;

    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(8);
            config.setMaxTotal(18);
            String basePassword = BUSCONF.getRedisHostPassword();
            String password = Base64Util.decoder(basePassword);
            pool = new JedisPool(config, BUSCONF.getRedisHostIp(), BUSCONF.getRedisHostPort(), 3000,
                    password);
            // 如果配置文件中没有配置过期事件的话则通过此配置项配置
            Jedis jedis = pool.getResource();
            String parameter = "notify-keyspace-events";
            List<String> notify = jedis.configGet(parameter);
            if (notify.get(1).equals("")) {
                jedis.configSet(parameter, "Ex"); // 过期事件
            }
            jedis.close();
        } catch (Exception e) {
            log.error("Jedis error :[{}] .", e);
        }

        new Thread(() -> {
            OmcJedisPool.getJedis().psubscribe(new OmcJedisKeyListener(), "__keyevent@*__:expired");
        }).start();
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

}
