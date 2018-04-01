package com.aizong.ishtirak.dao;

import java.util.List;

import com.aizong.ishtirak.bean.ContractConsumptionBean;
import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.CounterHistory;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.Subscriber;

public interface SubscriberDao extends GenericDao<Object> {

    void deleteContents(List<Long> ids);

    void deleteEngines(List<Long> ids);

    void deleteBundles(List<Long> ids);
    
    List<Subscriber> searchSubscribers(SearchCustomerCriteria criteria);

    void deleteContracts(List<Long> ids);

    List<Contract> getCounterContractBySubscriberId(Long subscriberId);

    List<Contract> getActiveContracts();

    List<ContractConsumptionBean> getCounterHistory(int previousMonth, int currentMonth);

    void deleteVillages(List<Long> ids);
    
    void deleteEmployeeType(List<Long> ids);
    
    void deleteEmployees(List<Long> ids);

    void deleteTransactions(List<Long> contractIds);

    List<Employee> getEmployees(Boolean active);

    void deleteExpenses(List<Long> ids);

    List<Long> getCreatedContractsForCurrentMonth(List<Contract> activeContracts, int currentMonth);

    List<Contract> getContractBySubscriberId(Long subscriberId);

    CounterHistory getCounterHistoryByContractId(Long contractId, int month);

    void updateCounterHistory(CounterHistory history);
}
