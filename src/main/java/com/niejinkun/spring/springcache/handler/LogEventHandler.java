package com.niejinkun.spring.springcache.handler;

import java.io.IOException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.BinaryLogClient.EventListener;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.QueryEventData;

@Component("eventListener")
public class LogEventHandler implements EventListener {

	@Autowired
	BinaryLogClient binaryLogClient;

	@Override
	public void onEvent(Event event) {
		if (event.getHeader().getEventType() == EventType.QUERY) {
			QueryEventData data = event.getData();
			System.out.println(data.toString());
		}
	}

	@Async
	public Future<String> start() {
		try {
			binaryLogClient.registerEventListener(this);
			binaryLogClient.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new AsyncResult<String>("start ok");
	}
	
	@Async
	public Future<String> stop() {
		try {
			binaryLogClient.unregisterEventListener(this);
			binaryLogClient.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new AsyncResult<String>("stop ok");
	}
}
