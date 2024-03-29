package com.aizong.ishtirak.common.misc.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aizong.ishtirak.bean.CurrencyManager;
import com.aizong.ishtirak.model.Company;
import com.aizong.ishtirak.service.ReportServiceImpl;
import com.aizong.ishtirak.service.SubscriberService;

@Component
public class ServiceProvider {

	@Autowired
	SubscriberService subscriberService;

	@Autowired
	ReportServiceImpl reportServiceImpl;

	@Autowired
	CurrencyManager currencyManager;

	@Autowired
	Message message;

	private Company company;

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

	public Company getCompany() {
		if (company == null) {
			company = subscriberService.getCompany();
		}
		return company == null ? new Company() : company;
	}

	public void revalidate() {
		company = null;

	}

	public CurrencyManager getCurrencyManager() {
		return currencyManager;
	}

}
