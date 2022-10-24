package com.asiainfo.lcims.omc.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPubSub;

public class OmcJedisKeyListener extends JedisPubSub {

    private static final Logger LOG = LoggerFactory.getLogger(OmcJedisKeyListener.class);

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        LOG.info("onPSubscribe pattern : [{}], subscribedChannels : [{}]", pattern,
                subscribedChannels);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        LOG.info("onPMessage pattern : [{}], channel : [{}], message : [{}]", pattern, channel,
                message);
    }

}
