package com.aizong.ishtirak.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aizong.ishtirak.bean.LogBean;
import com.aizong.ishtirak.bean.OrderBean;
import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.bean.Tuple;
import com.aizong.ishtirak.common.misc.component.DateRange;
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
import com.aizong.ishtirak.model.Transaction;
import com.aizong.ishtirak.model.Village;

public interface SubscriberService {

    List<Village> getVillages();

    void saveVillage(Village village);
    
    List<Subscriber> getSubscribers();

    void saveSubscriber(Subscriber subscriber);
    
    Subscriber getSubscriberById(Long id);

    void deleteSubscribers(List<Long> subscriberIds);
    
    void saveEngine(Engine engine);
    
    Engine getEngineById(Long id);

    void deleteEngines(List<Long> engineIds);
    
    List<Engine> getEngines();
    
    void saveBundle(Bundle bundle);
    
    Bundle getBundleById(Long id);
    
    List<MonthlyBundle> getMonthlyBundles();
    
    void deleteBundles(List<Long> bundleIds);

    List<SubscriptionBundle> getSubscriptionBundles();

    List<Bundle> getAllBundles();
    
    List<Subscriber> searchSubscribers(SearchCustomerCriteria criteria);
    
    void saveContract(Contract contract, Integer settelementFees, boolean createEmptyCounterHistory,
	    Integer reatctivateSubscriptionFees);
    
    Contract getContractById(Long id);
    
    void deleteContracts(List<Long> ids);

    void saveCounterHistory(CounterHistory history);
    
    List<Contract> getCounterContractBySubscriberId(Long contractId);

    List<LogBean> generateReceipts(LocalDate selectedMonth);

    void saveMaintenanceLog(ExpensesLog maintenaceLog);

    void deleteVillages(List<Long> villageIds);
    
    List<Employee> getEmployees();

    void saveEmployee(Employee employee);
    
    Employee getEmployeeById(Long id);

    void deleteEmployees(List<Long> employeeIds);
    
    List<EmployeeType> getEmployeeTypes();

    void saveEmployeeType(EmployeeType employeeType);
    
    EmployeeType getEmployeeTypeById(Long id);

    void deleteEmployeeTypes(List<Long> employeeTypeIds);

    List<Employee> getEmployeesWithEmployeeTypes();

    Village getVillageById(Long id);

    List<Employee> getActiveEmployees();

    ExpensesLog getExpensesById(Long id);

    void deleteExpenses(List<Long> ids);

    List<Contract> getContractBySubscriberId(Long id);

    CounterHistory getCounterHistoryByContractId(String contractUniqueCode);
    
    boolean login(String user, char[] password);

    void saveOutExpenses(OutExpensesLog outExpensesLog);

    OutExpensesLog getOutExpensesById(Long id);

    void deleteOutExpenses(List<Long> ids);

    void switchSubscription(Contract contract, Integer settelementFees, Long oldContractId);

    List<Contract> getActiveContractBySubscriberId(Long subscriberId);

    void saveCompany(Company newCompany);

    Company getCompany();
    
    Map<String, List<Tuple<String, Double>>> getResult(String fromDate, String endDate);
    
    public Map<Long, Set<String>> getContractUniqueCodesByEngine();  
    
    void updateCounters(Map<String, Long> e, LocalDate selectedDate);

    void updatePaid(Map<String, Boolean> e, LocalDate selectedDate);

    Transaction getTransactionById(Long transactionId);

    void deleteTransactions(List<Long> ids);

    void updatePayment(List<Long> transactionIds, boolean paid);
    
    List<ReceiptBean> getReceipts(List<Long> transactionIds, DateRange dateRange);

    void updateTransaction(Transaction transaction);

    void closeSubscription(Long contractId);

    void generateSelectedContractReceipt(LocalDate now, Long contractId) throws Exception;

    void saveTransaction(LocalDate now, Transaction transaction) throws Exception;

    List<ReceiptBean> getContractReceipt(Long contractId, DateRange dateRange);
    
    void deleteCounterHistory(List<Long> ids);

    List<OrderBean> getContractsForOrderingPurpose(Long villageId);

    void updateContactOrdering(List<OrderBean> rows);
    
}