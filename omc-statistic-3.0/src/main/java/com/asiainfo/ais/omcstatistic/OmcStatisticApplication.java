package com.asiainfo.ais.omcstatistic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.asiainfo.ais.omcstatistic.mapper")
@ComponentScan("com.asiainfo.ais.omcstatistic")
@SpringBootApplication
@EnableScheduling
public class OmcStatisticApplication {

	public static void main(String[] args) {
	    SpringApplication.run(OmcStatisticApplication.class, args);

	}
}
