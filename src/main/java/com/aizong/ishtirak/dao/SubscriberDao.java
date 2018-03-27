package com.aizong.ishtirak.dao;

import java.util.List;

import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.CounterHistory;
import com.aizong.ishtirak.model.Subscriber;

public interface SubscriberDao extends GenericDao<Object> {

    void deleteContents(List<Long> ids);

    void deleteEngines(List<Long> ids);

    void deleteBundles(List<Long> ids);
    
    List<Subscriber> searchSubscribers(SearchCustomerCriteria criteria);

    void deleteContracts(List<Long> ids);

    List<Contract> getContractBySubscriberId(Long subscriberId);

    List<Contract> getActiveContracts();

    List<CounterHistory> getCounterHistory(int month);
}
