package com.aizong.ishtirak.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aizong.ishtirak.bean.ContractConsumptionBean;
import com.aizong.ishtirak.bean.LogBean;
import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.bean.Tuple;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.Message;
import com.aizong.ishtirak.common.misc.utils.PasswordUtils;
import com.aizong.ishtirak.dao.SubscriberDao;
import com.aizong.ishtirak.demo.ReceiptBean;
import com.aizong.ishtirak.model.Bundle;
import com.aizong.ishtirak.model.Company;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.CounterHistory;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.EmployeeType;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.ExpensesLog;
import com.aizong.ishtirak.model.MonthlyBundle;
import com.aizong.ishtirak.model.OutExpensesLog;
import com.aizong.ishtirak.model.Subscriber;
import com.aizong.ishtirak.model.SubscriptionBundle;
import com.aizong.ishtirak.model.SubscriptionHistory;
import com.aizong.ishtirak.model.Transaction;
import com.aizong.ishtirak.model.User;
import com.aizong.ishtirak.model.Village;

@Service
@Transactional
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    SubscriberDao subscriberDao;

    @Autowired
    Message message;

    @Override
    public List<Village> getVillages() {
	return subscriberDao.findAll(Village.class);
    }

    @Override
    public void saveVillage(Village village) {
	if (village.getId() != null) {
	    subscriberDao.update(village);
	} else {
	    subscriberDao.save(Arrays.asList(village));
	}

    }

    @Override
    public void deleteVillages(List<Long> villageIds) {
	subscriberDao.deleteVillages(villageIds);
    }

    @Override
    public List<Subscriber> getSubscribers() {
	return subscriberDao.findAll(Subscriber.class);
    }

    @Override
    public void saveSubscriber(Subscriber subscriber) {
	if (subscriber.getId() != null) {
	    subscriber.getInformation().setId(subscriber.getId());
	    subscriberDao.update(subscriber.getInformation());
	    subscriberDao.update(subscriber);
	} else {
	    subscriberDao.save(Arrays.asList(subscriber));
	    subscriber.getInformation().setId(subscriber.getId());
	    subscriberDao.save(Arrays.asList(subscriber.getInformation()));

	}

    }

    @Override
    public Subscriber getSubscriberById(Long id) {
	return subscriberDao.find(Subscriber.class, id);
    }

    @Override
    public void deleteSubscribers(List<Long> subscriberIds) {
	subscriberDao.deleteSubscribers(subscriberIds);

	for (Long subscriberId : subscriberIds) {
	    List<Long> contractIds = subscriberDao.getContractIdsBySubscriberId(subscriberId);
	    deleteContracts(contractIds);
	}
    }

    @Override
    public void saveEngine(Engine engine) {
	if (engine.getId() != null) {
	    subscriberDao.update(engine);

	} else {

	    subscriberDao.save(Arrays.asList(engine));
	}

    }

    @Override
    public Engine getEngineById(Long id) {
	return subscriberDao.find(Engine.class, id);
    }

    @Override
    public void deleteEngines(List<Long> engineIds) {
	subscriberDao.deleteEngines(engineIds);

    }

    @Override
    public List<Engine> getEngines() {
	return subscriberDao.findAll(Engine.class);
    }

    @Override
    public void saveBundle(Bundle bundle) {
	if (bundle.getId() != null) {
	    subscriberDao.update(bundle);
	} else {
	    subscriberDao.save(Arrays.asList(bundle));
	}

    }

    @Override
    public Bundle getBundleById(Long id) {
	return subscriberDao.find(Bundle.class, id);
    }

    @Override
    public List<MonthlyBundle> getMonthlyBundles() {
	return subscriberDao.findAll(MonthlyBundle.class);
    }

    @Override
    public List<SubscriptionBundle> getSubscriptionBundles() {
	return subscriberDao.findAll(SubscriptionBundle.class);
    }

    @Override
    public void deleteBundles(List<Long> bundleIds) {
	subscriberDao.deleteBundles(bundleIds);

    }

    @Override
    public List<Bundle> getAllBundles() {
	return subscriberDao.findAll(Bundle.class);
    }

    @Override
    public List<Subscriber> searchSubscribers(SearchCustomerCriteria criteria) {
	return subscriberDao.searchSubscribers(criteria);
    }

    /**
     * save new contact and deactivate old one
     */
    @Override
    public void switchSubscription(Contract contract, Integer settelementFees, Long oldContractId) {

	// deactivate old contract

	if (oldContractId != null) {
	    closeSubscription(oldContractId);
	}

	saveContract(contract, settelementFees, false, null);

    }

    /**
     * in case of edit, settelementFees should not be changed because it paid
     * once at creation of subscription
     */
    @Override
    public void saveContract(Contract contract, Integer settelementFees, boolean createEmptyCounterHistory,
	    Integer reatctivateSubscriptionFees) {

	if (contract.getId() != null) {
	    subscriberDao.update(contract);
	    if (reatctivateSubscriptionFees != null) {
		Transaction transaction = new Transaction();
		transaction.setAmount(Double.valueOf(reatctivateSubscriptionFees));
		transaction.setContractId(contract.getId());
		transaction.setTransactionType(TransactionType.SETTELMENT_FEES);
		subscriberDao.save(Arrays.asList(transaction));
	    }
	} else {
	    subscriberDao.save(Arrays.asList(contract));

	    if (settelementFees != null && settelementFees.doubleValue() > 0) {
		Transaction transaction = new Transaction();
		transaction.setAmount(Double.valueOf(settelementFees.doubleValue()));
		transaction.setContractId(contract.getId());
		transaction.setTransactionType(TransactionType.SETTELMENT_FEES);
		subscriberDao.save(Arrays.asList(transaction));
	    }

	    if (createEmptyCounterHistory) {
		CounterHistory counterHistory = new CounterHistory();
		counterHistory.setConsumption(0l);
		counterHistory.setContractUniqueCode(contract.getContractUniqueCode());
		if (DateUtil.isCountedAsCurrentMonth(LocalDate.now())) {
		    counterHistory.setInsertDate(
			    Date.from(LocalDateTime.now().minusMonths(1).atZone(ZoneId.systemDefault()).toInstant()));
		}
		saveCounterHistory(counterHistory);
	    }
	}

    }

    @Override
    public Contract getContractById(Long id) {
	return subscriberDao.find(Contract.class, id);
    }

    @Override
    public void deleteContracts(List<Long> contractIds) {

	List<Long> relatedTransactionIds = subscriberDao.getTransactionIdsByContractIds(contractIds);
	subscriberDao.deleteTransactions(relatedTransactionIds);
	subscriberDao.deleteSubscriptionHistory(relatedTransactionIds);
	subscriberDao.deleteCounterHistory(contractIds);
	subscriberDao.deleteContracts(contractIds);

    }

    @Override
    public void saveCounterHistory(CounterHistory history) {
	DateRange effectiveCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	CounterHistory counterHistoryByContractId = subscriberDao.getCounterHistoryByContractId(
		history.getContractUniqueCode(), effectiveCurrentMonth.getStartDateAsString(),
		effectiveCurrentMonth.getEndDateAsString());
	if (counterHistoryByContractId == null) {
	    subscriberDao.save(Arrays.asList(history));
	} else {
	    counterHistoryByContractId.setConsumption(history.getConsumption());
	    counterHistoryByContractId.setContractUniqueCode(history.getContractUniqueCode());
	    subscriberDao.updateCounterHistory(counterHistoryByContractId);
	}

    }

    @Override
    public CounterHistory getCounterHistoryByContractId(String contractUniqueCode) {
	DateRange effectiveCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	return subscriberDao.getCounterHistoryByContractId(contractUniqueCode,
		effectiveCurrentMonth.getStartDateAsString(), effectiveCurrentMonth.getEndDateAsString());
    }

    @Override
    public List<Contract> getCounterContractBySubscriberId(Long subscriberId) {
	return subscriberDao.getCounterContractBySubscriberId(subscriberId);
    }

    @Override
    public List<Contract> getContractBySubscriberId(Long subscriberId) {
	return subscriberDao.getContractBySubscriberId(subscriberId, null);
    }

    @Override
    public List<Contract> getActiveContractBySubscriberId(Long subscriberId) {
	return subscriberDao.getContractBySubscriberId(subscriberId, true);
    }

    public List<Contract> getActiveContracts() {
	return subscriberDao.getActiveContracts();
    }

    @Override
    public List<LogBean> generateReceipts(LocalDate selectedMonth) {
	return generateReceipts(selectedMonth, getActiveContracts());
    }

    private List<LogBean> generateReceipts(LocalDate selectedMonth, List<Contract> contracts) {

	List<LogBean> failedContract = new ArrayList<>();

	// filter all active contracts created after specific time before they
	// should not
	// into computation
	contracts = contracts.stream()
		.filter(e -> DateUtil.isCountedAsCurrentMonth(
			e.getInsertDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
		.collect(Collectors.toList());

	DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth(selectedMonth);

	// create only the contracts haven't created yet
	// get contracts that have already created
	Set<Long> alreadyCreatedContracts = new HashSet<>(subscriberDao.getCreatedContractsForCurrentMonth(contracts.stream().map(e->e.getId()).collect(Collectors.toList()),
		dateRange.getStartDateAsString(), dateRange.getEndDateAsString()));

	// get all bundles monthly and subscription types
	List<Bundle> allBundles = getAllBundles();

	// group bundle by id
	Map<Long, Bundle> map = allBundles.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
	List<Transaction> transactions = new ArrayList<>();
	List<SubscriptionHistory> subscriptionHistoryList = new ArrayList<>();

	// get counter history for curret month and pervious month
	List<ContractConsumptionBean> counterHistories = subscriberDao.getCounterHistory(dateRange,
		DateUtil.getStartEndDateOfCurrentMonth(selectedMonth.minusMonths(1)));
	Map<String, ContractConsumptionBean> counterHistory = counterHistories.stream()
		.collect(Collectors.toMap(e -> e.getContractUniqueCode(), e -> e));

	// 1-create the transaction for each bundle type
	// 2-create the subscription history for each counter subscription
	// N.B amount for counter subscription is : monthly fees + consumption *
	// price/kb

	for (Contract contract : contracts) {

	    if (alreadyCreatedContracts.contains(contract.getId())) {
		failedContract.add(LogBean.createWarning(message.getMessage("transaction.generation.alreadyExists", contract.getContractUniqueCode())));
		continue;
	    }

	    Bundle bundle = map.get(contract.getBundleId());
	    if (bundle instanceof MonthlyBundle) {
		Transaction transaction = new Transaction();
		transaction.setAmount(((MonthlyBundle) bundle).getFees());
		transaction.setContractId(contract.getId());
		transaction.setTransactionType(TransactionType.MONTHLY_PAYMENT);
		transaction.setInsertDate(dateRange.getStartDate());

		transactions.add(transaction);
	    } else if (bundle instanceof SubscriptionBundle) {

		ContractConsumptionBean contractConsumptionBean = counterHistory.get(contract.getContractUniqueCode());
		if (contractConsumptionBean != null && contractConsumptionBean.getConsumption().isPresent()) {
		    long consumption = contractConsumptionBean.getConsumption().get();
		    SubscriptionBundle subscriptionBundle = (SubscriptionBundle) bundle;
		    Transaction transaction = new Transaction();
		    transaction.setAmount(
			    subscriptionBundle.getSubscriptionFees() + subscriptionBundle.getCostPerKb() * consumption);
		    transaction.setContractId(contract.getId());
		    transaction.setTransactionType(TransactionType.COUNTER_PAYMENT);
		    transaction.setInsertDate(dateRange.getStartDate());
		    transactions.add(transaction);

		    SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
		    subscriptionHistory.setConsumption(consumption);
		    subscriptionHistory.setPreviousCounter(contractConsumptionBean.getPreviousCounterValue());
		    subscriptionHistory.setCurrentCounter(contractConsumptionBean.getCurrentCounterValue());
		    subscriptionHistory.setCostPerKb(subscriptionBundle.getCostPerKb());
		    subscriptionHistory.setSubscriptionFees(subscriptionBundle.getSubscriptionFees());
		    subscriptionHistory.setTransaction(transaction);
		    subscriptionHistory.setInsertDate(transaction.getInsertDate());

		    subscriptionHistoryList.add(subscriptionHistory);

		} else {
		    failedContract.add(LogBean.createWarning(message.getMessage("transaction.generation.noCounter",
			    contract.getContractUniqueCode(), DateUtil.getCurrentMonthLabel(selectedMonth))));
		}
	    }
	}

	subscriberDao.save(new ArrayList<>(transactions));
	subscriberDao.save(new ArrayList<>(subscriptionHistoryList));
    
	return failedContract;
	
    }

    @Override
    public void saveMaintenanceLog(ExpensesLog maintenaceLog) {
	if (maintenaceLog.getId() != null) {
	    subscriberDao.update(maintenaceLog);
	} else {
	    subscriberDao.save(Arrays.asList(maintenaceLog));
	}

    }

    @Override
    public void saveOutExpenses(OutExpensesLog outExpensesLog) {
	if (outExpensesLog.getId() != null) {
	    subscriberDao.update(outExpensesLog);
	} else {
	    subscriberDao.save(Arrays.asList(outExpensesLog));
	}

    }

    @Override
    public List<Employee> getEmployees() {
	return subscriberDao.findAll(Employee.class);
    }

    @Override
    public List<Employee> getEmployeesWithEmployeeTypes() {
	List<Employee> employees = subscriberDao.findAll(Employee.class);
	for (Employee employee : employees) {
	    Hibernate.initialize(employee.getEmployeeTypeId());
	}
	return employees;
    }

    @Override
    public void saveEmployee(Employee employee) {
	if (employee.getId() != null) {
	    employee.getInformation().setId(employee.getId());
	    subscriberDao.update(employee.getInformation());
	    subscriberDao.update(employee);
	} else {
	    subscriberDao.save(Arrays.asList(employee));
	    employee.getInformation().setId(employee.getId());
	    subscriberDao.save(Arrays.asList(employee.getInformation()));
	}

    }

    @Override
    public Employee getEmployeeById(Long id) {
	return subscriberDao.find(Employee.class, id);
    }

    @Override
    public void deleteEmployees(List<Long> employeeIds) {
	subscriberDao.deleteEmployees(employeeIds);

    }

    @Override
    public List<EmployeeType> getEmployeeTypes() {
	return subscriberDao.findAll(EmployeeType.class);
    }

    @Override
    public void saveEmployeeType(EmployeeType employeeType) {
	if (employeeType.getId() != null) {
	    subscriberDao.update(employeeType);
	} else {
	    subscriberDao.save(Arrays.asList(employeeType));
	}

    }

    @Override
    public EmployeeType getEmployeeTypeById(Long id) {
	return subscriberDao.find(EmployeeType.class, id);
    }

    @Override
    public void deleteEmployeeTypes(List<Long> employeeTypeIds) {
	subscriberDao.deleteEmployeeType(employeeTypeIds);

    }

    @Override
    public Village getVillageById(Long id) {
	return subscriberDao.find(Village.class, id);
    }

    @Override
    public List<Employee> getActiveEmployees() {
	return subscriberDao.getEmployees(true);
    }

    @Override
    public ExpensesLog getExpensesById(Long id) {
	return subscriberDao.find(ExpensesLog.class, id);
    }

    @Override
    public OutExpensesLog getOutExpensesById(Long id) {
	return subscriberDao.find(OutExpensesLog.class, id);
    }

    @Override
    public void deleteExpenses(List<Long> ids) {
	subscriberDao.deleteExpenses(ids);

    }

    @Override
    public boolean login(String userName, char[] password) {
	User user = subscriberDao.getUserByName(userName);
	Optional<String> hashPass = PasswordUtils.hashPass(new String(password));
	return user != null && hashPass.isPresent() && hashPass.get().equals(user.getPassword());
    }

    @Override
    public void deleteOutExpenses(List<Long> ids) {
	subscriberDao.deleteOutExpenses(ids);

    }

    @Override
    public void saveCompany(Company newCompany) {
	if (newCompany.getId() != null) {
	    subscriberDao.update(newCompany);
	} else {
	    subscriberDao.save(Arrays.asList(newCompany));
	}
    }

    @Override
    public Company getCompany() {
	List<Company> companies = subscriberDao.findAll(Company.class);
	return companies.isEmpty() ? null : companies.get(0);
    }

    @Override
    public Map<String, List<Tuple<String, Double>>> getResult(String fromDate, String endDate) {
	return subscriberDao.getResult(fromDate, endDate);
    }

    @Override
    public Map<Long, Set<String>> getContractUniqueCodesByEngine() {
	return subscriberDao.getContractUniqueCodesByEngine();
    }

    @Override
    public void updateCounters(Map<String, Long> e, LocalDate selectedDate) {
	subscriberDao.updateCounters(e, selectedDate);
    }

    @Override
    public void updatePaid(Map<String, Boolean> e, LocalDate selectedDate) {
	subscriberDao.updatePaid(e, selectedDate);
    }

    @Override
    public Transaction getTransactionById(Long transactionId) {
	return subscriberDao.find(Transaction.class, transactionId);
    }

    @Override
    public void deleteTransactions(List<Long> ids) {
	subscriberDao.deleteSubscriptionHistory(ids);
	subscriberDao.deleteTransactions(ids);

    }

    @Override
    public void updatePayment(List<Long> transactionIds, boolean paid) {
	subscriberDao.updatePayment(transactionIds, paid);
    }

    @Override
    public List<ReceiptBean> getReceipts(List<Long> transactionIds, DateRange dateRange) {
	return subscriberDao.getReceipts(transactionIds, dateRange);
    }

    @Override
    public void updateTransaction(Transaction transaction) {
	subscriberDao.update(transaction);

    }

    @Override
    public void closeSubscription(Long contractId) {
	Contract oldContract = getContractById(contractId);
	if (oldContract != null && oldContract.getCloseDate() == null) {
	    oldContract.setActive(false);
	    oldContract.setCloseDate(new Date());
	    subscriberDao.save(Arrays.asList(oldContract));
	}
    }

    @Override
    public void generateSelectedContractReceipt(LocalDate now, Long contractId) throws Exception {

	Contract contract = getContractById(contractId);
	if (contract != null) {
	    List<LogBean> logs = generateReceipts(now, Arrays.asList(contract));
	    if (!logs.isEmpty()) {
		throw new Exception(logs.get(0).getText());
	    }
	}

    }

    @Override
    public void saveTransaction(LocalDate now, Transaction transaction) throws Exception {

	Contract contract = getContractById(transaction.getContractId());

	LocalDate contractInsertDate = DateUtil.localDate(contract.getInsertDate());
	
	if(contractInsertDate.isBefore(now)) {
	   subscriberDao.save(Arrays.asList(transaction));
	}else if(contractInsertDate.getMonth()==now.getMonth() && contractInsertDate.getYear() == now.getYear()) {
	    transaction.setInsertDate(contract.getInsertDate());
	    subscriberDao.save(Arrays.asList(transaction));
	}
	else {
	    throw new Exception(message.getMessage("transaction.date.passed"));
	}

    }

    @Override
    public List<ReceiptBean> getContractReceipt(Long contractId, DateRange dateRange) {
	
	List<Long> transactionIds = subscriberDao.getTransactionIdsByContractId(contractId, dateRange.getStartDateAsString(), dateRange.getEndDateAsString());
	if(transactionIds.isEmpty()) {
	    return new ArrayList<>();
	}
	return getReceipts(transactionIds, dateRange);
	
    }
}
