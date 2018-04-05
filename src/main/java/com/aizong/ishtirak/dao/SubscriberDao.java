package com.aizong.ishtirak.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aizong.ishtirak.bean.ContractConsumptionBean;
import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.bean.Tuple;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.CounterHistory;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.Subscriber;
import com.aizong.ishtirak.model.User;

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

    List<Long> getCreatedContractsForCurrentMonth(List<Contract> activeContracts, String fromDate, String toDate);

    List<Contract> getContractBySubscriberId(Long subscriberId, Boolean active);

    CounterHistory getCounterHistoryByContractId(Long contractId, String fromDate, String toDate);

    void updateCounterHistory(CounterHistory history);

    User getUserByName(String user);

    void deleteOutExpenses(List<Long> ids);

    Map<String, List<Tuple<String, Double>>> getResult(String fromDate, String endDate);

    Map<Long, Set<String>> getContractUniqueCodesByEngine();
}
