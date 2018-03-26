package com.aizong.ishtirak.common;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aizong.ishtirak.service.ReportServiceImpl;
import com.aizong.ishtirak.service.SubscriberService;

@Component
public class ServiceProvider {

    @Autowired
    SubscriberService subscriberService;
    
    @Autowired
    ReportServiceImpl reportServiceImpl;
    
    @Autowired
    Message message;
    
   private static ServiceProvider serviceProvider;
    
    @PostConstruct
    public void init() {
	serviceProvider = this;
    }
    
    public static ServiceProvider get() {
	return serviceProvider;
    }

    public SubscriberService getSubscriberService() {
        return subscriberService;
    }

    public ReportServiceImpl getReportServiceImpl() {
	return reportServiceImpl;
    }
    
    public Message getMessage() {
	return message;
    }
    
    
}
