package com.aizong.ishtirak.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aizong.ishtirak.bean.ContractConsumptionBean;
import com.aizong.ishtirak.bean.OrderBean;
import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.bean.Tuple;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.demo.ReceiptBean;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.CounterHistory;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.Subscriber;
import com.aizong.ishtirak.model.User;

public interface SubscriberDao extends GenericDao<Object> {

    void deleteSubscribers(List<Long> ids);

    void deleteEngines(List<Long> ids);

    void deleteBundles(List<Long> ids);
    
    List<Subscriber> searchSubscribers(SearchCustomerCriteria criteria);

    void deleteContracts(List<Long> ids);

    List<Contract> getCounterContractBySubscriberId(Long subscriberId);

    List<Contract> getActiveContracts();

    List<ContractConsumptionBean> getCounterHistory(DateRange currentDateRange);

    void deleteVillages(List<Long> ids);
    
    void deleteEmployeeType(List<Long> ids);
    
    void deleteEmployees(List<Long> ids);

    void deleteTransactions(List<Long> contractIds);

    List<Employee> getEmployees(Boolean active);

    void deleteExpenses(List<Long> ids);

    List<Long> getCreatedContractsForCurrentMonth(List<Long> activeContractIds, String fromDate, String toDate);

    List<Contract> getContractBySubscriberId(Long subscriberId, Boolean active);

    CounterHistory getCounterHistoryByContractId(String contractUniqueCode, String fromDate, String toDate);

    void updateCounterHistory(CounterHistory history);

    User getUserByName(String user);

    void deleteOutExpenses(List<Long> ids);

    Map<String, List<Tuple<String, Double>>> getResult(String fromDate, String endDate);

    Map<Long, Set<String>> getContractUniqueCodesByEngine();

    void updateCounters(Map<String, Long> e,LocalDate now);

    void updatePaid(Map<String, Boolean> e, LocalDate selectedDate);

    void updatePayment(List<Long> transactionIds, boolean paid);

    List<ReceiptBean> getReceipts(List<Long> transactionIds, DateRange dateRange);

    void deleteSubscriptionHistory(List<Long> transactionIds);

    List<Long> getTransactionIdsByContractIds(List<Long> contractIds);

    void deleteCounterHistory(List<Long> contractIds);

    List<Long> getContractIdsBySubscriberId(Long subscriberId);

    List<Long> getTransactionIdsByContractId(Long contractId, String startDate, String endDate);

    void deleteCounterHistoryByIds(List<Long> ids);

    List<OrderBean> getContractsForOrderingPurpose(Long villageId);

    void updateContactOrdering(List<OrderBean> rows);

}
