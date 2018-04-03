package com.aizong.ishtirak.service;

import java.util.List;

import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.model.Bundle;
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
    
    void saveContract(Contract contract,Integer settelementFees);
    
    Contract getContractById(Long id);
    
    void deleteContracts(List<Long> ids);

    void saveCounterHistory(CounterHistory history) throws Exception;
    
    List<Contract> getCounterContractBySubscriberId(Long subscriberId);

    List<Contract> generateReceipts();

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

    CounterHistory getCounterHistoryByContractId(Long contractId);
    
    boolean login(String user, char[] password);

    void saveOutExpenses(OutExpensesLog outExpensesLog);

    OutExpensesLog getOutExpensesById(Long id);

    void deleteOutExpenses(List<Long> ids);

    void saveAndDeactivateContact(Contract contract, Integer settelementFees, Long oldContractId);

    List<Contract> getActiveContractBySubscriberId(Long subscriberId);
    
}