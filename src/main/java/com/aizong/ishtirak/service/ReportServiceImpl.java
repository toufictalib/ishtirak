package com.aizong.ishtirak.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SearchBean;
import com.aizong.ishtirak.bean.SummaryBean;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.Message;
import com.aizong.ishtirak.dao.ReportDao;
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

	    String[] cols = { "codeId", "description", "settelmentFees", "subscription.monthly.amount" };

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
	    String[] cols = { "codeId", "description", "settelmentFees", "kbPrice", "counterRenting" };
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
	Collections.sort(employees, new Comparator<Village>() {

	    @Override
	    public int compare(Village o1, Village o2) {
		return o1.getOrderIndex() - o2.getOrderIndex();
	    }
	});
	String[] cols = { "number", "name", "order" };

	
	List<Object[]> rows = new ArrayList<>();
	for (Village employee : employees) {

	    Object[] row = { employee.getId(), employee.getName(),employee.getOrderIndex() };

	    rows.add(row);
	}

	Class<?>[] clazzes = clazzes(rows, cols);

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
	List<Object[]> rows = reportDao.getExpenses(null, startEndDateOfCurrentMonth.getStartDateAsString(),
		startEndDateOfCurrentMonth.getEndDateAsString());

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
	List<Object[]> rows = reportDao.getExpenses(expensesType, searchBean.getFromDate(),
		searchBean.getEndDate());

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
	
	
	boolean isCounter = reportDao.hasCounterBundle(contractById.getContractUniqueCode());
	
	List<Object[]> rows = reportDao.getSubscriptionsHistory(contractById.getContractUniqueCode(), searchBean.getFromDate(),
		searchBean.getEndDate(),isCounter ? TransactionType.COUNTER_PAYMENT : TransactionType.MONTHLY_PAYMENT);

	Object value = null;
	for (Object[] row : rows) {
	    value = row[3];
	    if (value!=null) {
		row[3] = message.getEnumLabel(value.toString(), TransactionType.class);
	    }
	}
	
	String[] cols = null;
	Class<?>[] clazzes = null;
	if(isCounter) {
	    cols = new String [] { "maintenanceId",  "amount", "subscriptionBundle",
		    "transactionType","consumption","previous_counter","current_counter",
		    "costPerKb","subtotal_kb_multiply_consumption",
			"counterRenting", "insertDate" };
	    
	}else{
	    cols = new String [] { "maintenanceId",  "amount", "subscriptionBundle",
		    "transactionType","insertDate" };
	}
	clazzes = clazzes(rows, cols);
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

    @Override
    public SummaryBean getSummaryResult(String fromDate, String toDate) {
	String engine = null;
	return new SummaryBean(subscriberService.getEngines(), reportDao.getExpensesPerEngine(engine, fromDate, toDate),
		reportDao.getConsumptionPerEngine(engine, fromDate, toDate), reportDao.getIncomePerEngine(engine, fromDate, toDate));
    }

    @Override
    public ReportTableModel getContractHistoryPerContractOrALl(String uniqueContractId, String fromDate,
	    String toDate, Boolean paid) {
	String[] cols = {"contract_unique_code","fullName","amount","paid","transactionType","subscriptionBundle","engine","village","receiptCreationDate"};
	List<Object[]> rows = reportDao.getContractHistoryPerContractOrALl(uniqueContractId, fromDate, toDate, paid);
	
	Object value = null;
	for (Object[] row : rows) {
	    value = row[4];
	    if (value!=null) {
		row[4] = message.getEnumLabel(value.toString(), TransactionType.class);
	    }
	}
	
	return new ReportTableModel(cols, rows, clazzes(rows, cols));
    }
    
    @Override
    public ReportTableModel getExportedFiles(boolean counterInput) {
        
	
	List<Object[]> rows = reportDao.getExportedFiles(DateUtil.getContractDate());
	
	List<String> cols = new ArrayList<>();
	    cols.add("fullName");
	    cols.add("contract_unique_code");
	if(counterInput) {
	    cols.add("counterAmount");
	}else {
	    cols.add("paid");
	}
	
	
	
	return new ReportTableModel(cols.toArray(new String[0]), rows, clazzes(rows, cols.toArray(new String[0])));
    }
    
}
