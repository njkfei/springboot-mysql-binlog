package com.niejinkun.spring.springcache.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.BinaryLogClient.EventListener;
import com.niejinkun.spring.springcache.handler.LogEventHandler;

@EnableAutoConfiguration
@Configuration
@PropertySource(value = { "classpath:conf/binlog.properties" })
public class BinLogConfig {
	@Value("${binlog.host:192.168.1.1}")
	private String host;

	@Value("${binlog.port:3306}")
	private int port;
	
	@Value("${binlog.user:binlog}")
	private String user;

	@Value("${binlog.password:binlog}")
	private String password;
	
	@Bean BinaryLogClient binaryLogClient(){
		BinaryLogClient client = new BinaryLogClient(host, port, user,password);
		return client;
	}
	
	@Bean
	public EventListener eventListener(){
		EventListener eventListener = new LogEventHandler();
		return eventListener;
	}
}
