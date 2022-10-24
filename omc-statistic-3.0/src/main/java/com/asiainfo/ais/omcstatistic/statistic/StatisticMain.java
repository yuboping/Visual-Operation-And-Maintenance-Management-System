package com.asiainfo.ais.omcstatistic.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class StatisticMain implements CommandLineRunner{

    private static final Logger LOG = LoggerFactory.getLogger(StatisticMain.class);

    @Autowired
    MemoryUtil memoryUtil;

    @Autowired
    ExcuteStatisticSql excuteStatisticSql;

    @Override
    public void run(String... var1) throws Exception{

        //查询规则放入缓存
        memoryUtil.setRuleToMemory();

        LOG.info("memory store success");
    }

}
