package com.aizong.ishtirak.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aizong.ishtirak.subscriber.Subscriber;
import com.aizong.ishtirak.table.ReportTableModel;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    SubscriberService subscriberService;

    @Override
    public ReportTableModel getSubscribers() {
	List<Subscriber> subscribers = subscriberService.getSubscribers();

	String[] cols = { "الرقم","الاسم", "اسم الأب", "العائلة", "اللقب" };

	List<Object[]> rows = new ArrayList<>();
	for (Subscriber subscriber : subscribers) {
	    
	   // Information information = subscriber.getInformation();
	    Object[] row = { subscriber.getId(),subscriber.getName(), subscriber.getFatherName(), subscriber.getLastName(),
		    subscriber.getIdentifier()/*,information.getVillage(),information.getRegion(),information.getLandLine(),information.getMainPhone(),information.getAlternativePhone(),information.getEmail()*/ };

	    rows.add(row);
	}
	
	Class<?>[] clazzes = { Long.class, String.class, String.class, String.class, String.class };

	return new ReportTableModel(cols, rows, clazzes);
    }

}
