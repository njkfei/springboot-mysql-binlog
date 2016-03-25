package com.niejinkun.spring.springcache.web;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niejinkun.spring.springcache.handler.LogEventHandler;

@RestController
public class JobContoller 
{
	@Autowired
	private LogEventHandler eventListener;	
	
	
	@RequestMapping("/start")
	public String jobSatart(){
		System.out.println("job start ==========>");
		Future<String> rest = eventListener.start();
		
		System.out.println(rest.isDone());
		return "JobStartOk";
	}
	
	@RequestMapping("/stop")
	public String jobStop(){
		System.out.println("job stop ==========>");
		
		Future<String> rest = eventListener.stop();
		
		System.out.println(rest.isDone());
		System.out.println("job end ==========>");
		return "JobStopOk";
	}
	
}
