package com.aizong.ishtirak.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aizong.ishtirak.engine.Engine;
import com.aizong.ishtirak.subscriber.model.Subscriber;
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

    @Override
    public ReportTableModel getEngines() {
	List<Engine> list = subscriberService.getEngines();

	String[] cols = { "الرقم","الاسم", "KVA", "مصروف المازوت/بالساعة" };

	List<Object[]> rows = new ArrayList<>();
	for (Engine object : list) {
	    
	   // Information information = subscriber.getInformation();
	    Object[] row = { object.getId(),object.getName(), object.getKva(), object.getDieselConsumption()
		    /*,information.getVillage(),information.getRegion(),information.getLandLine(),information.getMainPhone(),information.getAlternativePhone(),information.getEmail()*/ };

	    rows.add(row);
	}
	
	Class<?>[] clazzes = { Long.class, String.class, String.class, Integer.class, Double.class };

	return new ReportTableModel(cols, rows, clazzes);
    }

}
