package com.aizong.ishtirak.service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aizong.ishtirak.bean.ContractConsumptionBean;
import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.dao.SubscriberDao;
import com.aizong.ishtirak.model.Bundle;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.CounterHistory;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.EmployeeType;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.ExpensesLog;
import com.aizong.ishtirak.model.MonthlyBundle;
import com.aizong.ishtirak.model.Subscriber;
import com.aizong.ishtirak.model.SubscriptionBundle;
import com.aizong.ishtirak.model.SubscriptionHistory;
import com.aizong.ishtirak.model.Transaction;
import com.aizong.ishtirak.model.Village;

@Service
@Transactional
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    SubscriberDao subscriberDao;

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
	subscriberDao.deleteContents(subscriberIds);
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

    @Override
    public void saveContract(Contract contract) {
	if (contract.getId() != null) {
	    subscriberDao.update(contract);
	} else {
	    subscriberDao.save(Arrays.asList(contract));

	    Bundle bundle = getBundleById(contract.getBundleId());

	    if (bundle != null) {
		Transaction transaction = new Transaction();
		transaction.setAmount(bundle.getSettlementFees());
		transaction.setContractId(contract.getId());
		transaction.setTransactionType(TransactionType.SETTELMENT_FEES);
		subscriberDao.save(Arrays.asList(transaction));
	    }
	}

    }

    @Override
    public Contract getContractById(Long id) {
	return subscriberDao.find(Contract.class, id);
    }

    @Override
    public void deleteContracts(List<Long> ids) {
	subscriberDao.deleteContracts(ids);

    }

    @Override
    public void saveConsumptionHistory(CounterHistory history) {
	if (history.getId() != null) {
	    subscriberDao.update(history);
	} else {

	    subscriberDao.save(Arrays.asList(history));
	}

    }

    @Override
    public List<Contract> getCounterContractBySubscriberId(Long subscriberId) {
	return subscriberDao.getCounterContractBySubscriberId(subscriberId);
    }
    
    @Override
    public List<Contract> getContractBySubscriberId(Long subscriberId) {
	return subscriberDao.getContractBySubscriberId(subscriberId);
    }

    private List<Contract> getActiveContracts() {
	return subscriberDao.getActiveContracts();
    }

    private List<ContractConsumptionBean> getContractConsupmtion() {
	LocalDateTime currentTime = LocalDateTime.now();
	Month month = currentTime.getMonth();
	int currentMonth = month.getValue();
	// back up nbOfDaysBeforeToday of current date
	LocalDateTime dateMinusMonths = currentTime.minusMonths(1);
	int previousMonth = dateMinusMonths.getMonth().getValue();

	return subscriberDao.getCounterHistory(previousMonth, currentMonth);
    }

    @Override
    public void generateReceipts() {

	// get active contracts
	List<Contract> contracts = getActiveContracts();
	
	//create only the contracts already created yet
	Set<Long> alreadyCreatedContracts = new HashSet<>(subscriberDao.getCreatedContractsForCurrentMonth(contracts, DateUtil.getCurrentMonth()));

	// get all bundles monthly and subscription types
	List<Bundle> allBundles = getAllBundles();

	// group bundle by id
	Map<Long, Bundle> map = allBundles.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
	List<Transaction> transactions = new ArrayList<>();
	List<SubscriptionHistory> subscriptionHistoryList = new ArrayList<>();

	List<ContractConsumptionBean> counterHistories = getContractConsupmtion();
	Map<Long, ContractConsumptionBean> counterHistory = counterHistories.stream()
		.collect(Collectors.toMap(e -> e.getContractId(), e -> e));

	// 1-create the transaction for each bundle type
	// 2-create the subscription history for each counter subscription
	// N.B amount for counter subscription is : monthly fees + consumption *
	// price/kb
	for (Contract contract : contracts) {
	    
	    if(alreadyCreatedContracts.contains(contract.getId())) {
		continue;
	    }
	    
	    Bundle bundle = map.get(contract.getBundleId());
	    if (bundle instanceof MonthlyBundle) {
		Transaction transaction = new Transaction();
		transaction.setAmount(((MonthlyBundle) bundle).getFees());
		transaction.setContractId(contract.getId());
		transaction.setTransactionType(TransactionType.MONTHLY_PAYMENT);

		transactions.add(transaction);
	    } else if (bundle instanceof SubscriptionBundle) {

		ContractConsumptionBean contractConsumptionBean = counterHistory.get(contract.getId());
		if (contractConsumptionBean != null && contractConsumptionBean.getConsumption().isPresent()) {
		    long consumption = contractConsumptionBean.getConsumption().get();
		    SubscriptionBundle subscriptionBundle = (SubscriptionBundle) bundle;
		    Transaction transaction = new Transaction();
		    transaction.setAmount(
			    subscriptionBundle.getSubscriptionFees() + subscriptionBundle.getCostPerKb() * consumption);
		    transaction.setContractId(contract.getId());
		    transaction.setTransactionType(TransactionType.MONTHLY_PAYMENT);
		    transactions.add(transaction);

		    SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
		    subscriptionHistory.setConsumption(consumption);
		    subscriptionHistory.setCostPerKb(subscriptionBundle.getCostPerKb());
		    subscriptionHistory.setSubscriptionFees(subscriptionBundle.getSubscriptionFees());
		    subscriptionHistory.setTransaction(transaction);
		    subscriptionHistoryList.add(subscriptionHistory);

		}
	    }
	}

	subscriberDao.save(new ArrayList<>(transactions));
	subscriberDao.save(new ArrayList<>(subscriptionHistoryList));
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
    public void deleteExpenses(List<Long> ids) {
	subscriberDao.deleteExpenses(ids);
	
    }
}
