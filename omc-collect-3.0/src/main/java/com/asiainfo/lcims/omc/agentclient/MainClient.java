package com.asiainfo.lcims.omc.agentclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.agentclient.netty.CollectClient;

public class MainClient {

    private static final Logger LOG = LoggerFactory.getLogger(MainClient.class);

    public static void main(String[] args) throws InterruptedException {
        LOG.info("------------collect client start------------");
        CollectClient.start();
    }
}
