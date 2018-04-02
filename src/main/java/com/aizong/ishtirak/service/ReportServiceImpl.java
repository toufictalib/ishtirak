package com.aizong.ishtirak.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.Message;
import com.aizong.ishtirak.dao.ReportDao;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.EmployeeType;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.MonthlyBundle;
import com.aizong.ishtirak.model.Subscriber;
import com.aizong.ishtirak.model.SubscriptionBundle;
import com.aizong.ishtirak.model.Village;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    SubscriberService subscriberService;

    @Autowired
    ReportDao reportDao;

    @Autowired
    Message message;
    
    @Override
    public ReportTableModel getSubscribers() {
	List<Subscriber> subscribers = subscriberService.getSubscribers();

	String[] cols = { "الرقم", "الاسم", "اسم الأب", "العائلة", "اللقب" };

	List<Object[]> rows = new ArrayList<>();
	for (Subscriber subscriber : subscribers) {

	    // Information information = subscriber.getInformation();
	    Object[] row = { subscriber.getId(), subscriber.getName(), subscriber.getFatherName(),
		    subscriber.getLastName(),
		    subscriber.getIdentifier()/*
					       * ,information.getVillage(),
					       * information.getRegion(),
					       * information.getLandLine(),
					       * information.getMainPhone(),
					       * information.getAlternativePhone
					       * (),information.getEmail()
					       */ };

	    rows.add(row);
	}

	Class<?>[] clazzes = { Long.class, String.class, String.class, String.class, String.class };

	return new ReportTableModel(cols, rows, clazzes);
    }

    @Override
    public ReportTableModel getEngines() {
	List<Engine> list = subscriberService.getEngines();

	String[] cols = { "الرقم", "الاسم", "KVA", "مصروف المازوت/بالساعة" };

	List<Object[]> rows = new ArrayList<>();
	for (Engine object : list) {

	    // Information information = subscriber.getInformation();
	    Object[] row = { object.getId(), object.getName(), object.getKva(), object.getDieselConsumption() };

	    rows.add(row);
	}

	Class<?>[] clazzes = { Long.class, String.class, String.class, Integer.class, Double.class };

	return new ReportTableModel(cols, rows, clazzes);
    }

    public ReportTableModel getMonthlyBundles(boolean monthly) {
	if (monthly) {
	    List<MonthlyBundle> list = subscriberService.getMonthlyBundles();

	    String[] cols = { "الرقم", "الاسم", "رسم الإشتراك", "قيمة الإشتراك" };

	    List<Object[]> rows = new ArrayList<>();
	    for (MonthlyBundle object : list) {
		// Information information = subscriber.getInformation();
		Object[] row = { object.getId(), object.getName(), object.getSettlementFees(), object.getFees() };

		rows.add(row);
	    }

	    Class<?>[] clazzes = { Long.class, String.class, Double.class, Double.class };

	    return new ReportTableModel(cols, rows, clazzes);
	} else {
	    List<SubscriptionBundle> list = subscriberService.getSubscriptionBundles();
	    String[] cols = { "الرقم", "الاسم", "رسم الإشتراك", "سعر الكيلو", "كلفة إشتراك الشهر" };
	    List<Object[]> rows = new ArrayList<>();
	    for (SubscriptionBundle object : list) {
		Object[] row = { object.getId(), object.getName(), object.getSettlementFees(), object.getCostPerKb(),
			object.getSubscriptionFees() };

		rows.add(row);
	    }

	    Class<?>[] clazzes = { Long.class, String.class, Double.class, Double.class, Double.class };

	    return new ReportTableModel(cols, rows, clazzes);
	}
    }

    @Override
    public ReportTableModel getEmployees() {
	List<Employee> employees = subscriberService.getEmployeesWithEmployeeTypes();

	String[] cols = { "الرقم", "الاسم", "العائلة", "المعاش", "نوع العمل" };

	List<Object[]> rows = new ArrayList<>();
	for (Employee employee : employees) {

	    Object[] row = { employee.getId(), employee.getName(), employee.getLastName(), employee.getSalary(),
		    employee.getEmployeeTypeId().getName() };

	    rows.add(row);
	}

	Class<?>[] clazzes = { Long.class, String.class, String.class, Double.class, String.class };

	return new ReportTableModel(cols, rows, clazzes);
    }

    @Override
    public ReportTableModel getVillages() {
	List<Village> employees = subscriberService.getVillages();

	String[] cols = { "الرقم", "الاسم" };

	List<Object[]> rows = new ArrayList<>();
	for (Village employee : employees) {

	    Object[] row = { employee.getId(), employee.getName() };

	    rows.add(row);
	}

	Class<?>[] clazzes = { Long.class, String.class };

	return new ReportTableModel(cols, rows, clazzes);
    }

    @Override
    public ReportTableModel getEmployeeTypes() {
	List<EmployeeType> employees = subscriberService.getEmployeeTypes();

	String[] cols = { "الرقم", "الاسم" };

	List<Object[]> rows = new ArrayList<>();
	for (EmployeeType employee : employees) {

	    Object[] row = { employee.getId(), employee.getName() };

	    rows.add(row);
	}

	Class<?>[] clazzes = { Long.class, String.class };

	return new ReportTableModel(cols, rows, clazzes);
    }

    @Override
    public ReportTableModel getSubscriptionsIncomeReport() {
	DateRange startEndDateOfCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	List<Object[]> rows = reportDao.getSubscriptionsIncomeReport(startEndDateOfCurrentMonth.getStartDate(),startEndDateOfCurrentMonth.getEndDate());


	Object value = null;
	for(Object[] row:rows) {
	    value = row[7];
	    if(value!=null) {
		row[7] = message.getEnumLabel(value.toString(), TransactionType.class);
	    }
	}
	
	String[] cols = { "contractId", "name", "lastName", "address", "subscriptionBundle", "amount", "paid" ,"transactionType"};


	Class<?>[] clazzes = { Long.class, String.class, String.class, String.class, String.class, Double.class, Boolean.class,String.class };

	return new ReportTableModel(cols, rows, clazzes);
    }
    
    @Override
    public ReportTableModel getExpenses() {
	DateRange startEndDateOfCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	List<Object[]> rows = reportDao.getExpenses(startEndDateOfCurrentMonth.getStartDate(),startEndDateOfCurrentMonth.getEndDate());

	Object value = null;
	for(Object[] row:rows) {
	    value = row[4];
	    if(value!=null) {
		row[4] = message.getEnumLabel(value.toString(), ExpensesType.class);
	    }
	}
	String[] cols = { "maintenanceId", "engine", "amount", "description", "maintenanceType", "insertDate" };
	
	
	Class<?>[] clazzes = { Long.class, String.class, Double.class, String.class, String.class, Date.class };

	return new ReportTableModel(cols, rows, clazzes);
    }

    @Override
    public ReportTableModel getOutExpenses() {
	DateRange startEndDateOfCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	List<Object[]> rows = reportDao.getOutExpenses(startEndDateOfCurrentMonth.getStartDate(),startEndDateOfCurrentMonth.getEndDate());

	Object value = null;
	for(Object[] row:rows) {
	    value = row[4];
	    if(value!=null) {
		row[4] = message.getEnumLabel(value.toString(), ExpensesType.class);
	    }
	}
	String[] cols = { "maintenanceId", "withdrawerName","withdrawerLastName", "amount", "description", "maintenanceType", "insertDate" };
	
	
	Class<?>[] clazzes = { Long.class, String.class,String.class, Double.class, String.class, String.class, Date.class };

	return new ReportTableModel(cols, rows, clazzes);
    }

}
