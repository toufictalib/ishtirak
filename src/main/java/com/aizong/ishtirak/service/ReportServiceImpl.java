package com.aizong.ishtirak.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SearchBean;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.Message;
import com.aizong.ishtirak.dao.ReportDao;
import com.aizong.ishtirak.model.Bundle;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.EmployeeType;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.MonthlyBundle;
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
	List<Object[]> rows = reportDao.getSubscribers();

	String[] cols = { "id", "name", "fatherName", "lastName", "identifier", "mainPhone", "village", "address" };

	Class<?>[] clazzes = { Long.class, String.class, String.class, String.class, String.class, String.class,
		String.class, String.class };

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
	    String[] cols = { "codeId", "description", "subscriptionFees", "kbPrice", "counterRenting" };
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
	List<Object[]> rows = reportDao.getSubscriptionsIncomeReport(startEndDateOfCurrentMonth.getStartDate(),
		startEndDateOfCurrentMonth.getEndDate());

	Object value = null;
	for (Object[] row : rows) {
	    value = row[7];
	    if (value != null) {
		row[7] = message.getEnumLabel(value.toString(), TransactionType.class);
	    }
	}

	String[] cols = { "contractId", "name", "lastName", "address", "subscriptionBundle", "amount", "paid",
		"transactionType" };

	Class<?>[] clazzes = { Long.class, String.class, String.class, String.class, String.class, Double.class,
		Boolean.class, String.class };

	return new ReportTableModel(cols, rows, clazzes);
    }

    @Override
    public ReportTableModel getExpenses() {
	DateRange startEndDateOfCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	List<Object[]> rows = reportDao.getExpenses(null, startEndDateOfCurrentMonth.getStartDate(),
		startEndDateOfCurrentMonth.getEndDate());

	Object value = null;
	for (Object[] row : rows) {
	    value = row[4];
	    if (value != null) {
		row[4] = message.getEnumLabel(value.toString(), ExpensesType.class);
	    }
	}
	String[] cols = { "maintenanceId", "engine", "amount", "description", "maintenanceType", "insertDate" };

	Class<?>[] clazzes = { Long.class, String.class, Double.class, String.class, String.class, Date.class };

	return new ReportTableModel(cols, rows, clazzes);
    }
    
    @Override
    public ReportTableModel getExpenses(ExpensesType expensesType, SearchBean searchBean) {
	DateRange startEndDateOfCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	List<Object[]> rows = reportDao.getExpenses(expensesType, startEndDateOfCurrentMonth.getStartDate(),
		startEndDateOfCurrentMonth.getEndDate());

	Object value = null;
	for (Object[] row : rows) {
	    value = row[4];
	    if (value != null) {
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
	List<Object[]> rows = reportDao.getOutExpenses(startEndDateOfCurrentMonth.getStartDate(),
		startEndDateOfCurrentMonth.getEndDate());

	Object value = null;
	for (Object[] row : rows) {
	    value = row[4];
	    if (value != null) {
		row[4] = message.getEnumLabel(value.toString(), ExpensesType.class);
	    }
	}
	String[] cols = { "maintenanceId", "withdrawerName", "withdrawerLastName", "amount", "description",
		"maintenanceType", "insertDate" };

	Class<?>[] clazzes = { Long.class, String.class, String.class, Double.class, String.class, String.class,
		Date.class };

	return new ReportTableModel(cols, rows, clazzes);
    }

    @Override
    public ReportTableModel getSubscriptionsHistory(Long contractId, SearchBean searchBean) throws Exception {

	Contract contractById = subscriberService.getContractById(contractId);
	if (contractById == null) {
	    throw new Exception("subscriber.noSubscription");
	}

	Bundle bundle = subscriberService.getBundleById(contractById.getBundleId());
	if (bundle == null) {
	    throw new Exception("bundle.notFound");
	}

	TransactionType transactionType = (bundle instanceof SubscriptionBundle) ? TransactionType.COUNTER_PAYMENT : TransactionType.MONTHLY_PAYMENT;
	List<Object[]> rows = reportDao.getSubscriptionsHistory(contractId, searchBean.getFromDate(),
		searchBean.getEndDate(),transactionType );

	Object value = null;
	for (Object[] row : rows) {
	    value = row[2];
	    if (value != null) {
		row[2] = message.getEnumLabel(value.toString(), TransactionType.class);
	    }
	}
	
	String[] cols = null;
	Class<?>[] clazzes = null;
	if(transactionType==TransactionType.COUNTER_PAYMENT) {
	    cols = new String [] { "maintenanceId",  "amount", "transactionType","consumption","previous_counter","current_counter","costPerKb","subtotal_kb_multiply_consumption",
			"subscriptionFees", "insertDate" };
	    clazzes = new Class[]{ Long.class, Double.class, String.class, Double.class, Double.class, Double.class,
		    Double.class, Date.class };
	}else {
	    cols =  new String [] { "maintenanceId",  "amount", "transactionType", "insertDate" };
	    clazzes = new Class[]{ Long.class, Double.class, String.class,Date.class };
	}


	return new ReportTableModel(cols, rows, clazzes);

    }

    @Override
    public ReportTableModel getActiveIshtirakWithoutReceipts() {
	List<Long> contractIds = reportDao.getActiveContractWithoutReceipts();
	
	return getActiveIshtirakInfo(contractIds);
    }
    @Override
    public ReportTableModel getActiveIshtirakInfo(List<Long> contractIds) {
	List<Object[]> rows = reportDao.getActiveIshtirakInfo(contractIds);

	String[] cols = { "codeId", "name", "lastName", "village", "mainPhone", "counterId", "bundle", "engine" };

	Class<?>[] clazzes = clazzes(rows, cols);
	return new ReportTableModel(cols, rows, clazzes);
    }

    private Class<?>[] clazzes(List<Object[]> rows, String[] cols) {
	Class<?>[] clazzes = new Class<?>[cols.length];
	
	int i=0;
	
	if (rows.size() > 0) {
	    for (Object value : rows.get(0)) {
		clazzes[i++] = value == null ? String.class : value.getClass();
	    }
	}else {
	    Arrays.fill(clazzes, String.class);
	}
	return clazzes;
    }

    @Override
    public ReportTableModel getEmployeesPayments(Long employeeId, SearchBean searchBean) {
	List<Object[]> rows = reportDao.getEmployeesPayments(employeeId, searchBean.getFromDate(), searchBean.getEndDate());

	String[] cols = { "codeId", "job","name", "lastName", "amount", "insert_date" };

	Class<?>[] clazzes = clazzes(rows, cols);
	return new ReportTableModel(cols, rows, clazzes);
    }

    @Override
    public ReportTableModel getCounterHistory(Long subscriberId, SearchBean searchBean) {
	List<Object[]> rows = reportDao.getCounterHistory(subscriberId, searchBean.getFromDate(), searchBean.getEndDate());

	String[] cols = { "codeId", "counter","engine", "counterAmount", "insert_date" };

	Class<?>[] clazzes = clazzes(rows, cols);
	return new ReportTableModel(cols, rows, clazzes);
    }
    
}
