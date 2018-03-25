package com.aizong.ishtirak.dao;

import java.util.List;

import com.aizong.ishtirak.bundle.Contract;
import com.aizong.ishtirak.subscriber.SearchCustomerCriteria;
import com.aizong.ishtirak.subscriber.model.Subscriber;

public interface SubscriberDao extends GenericDao<Object> {

    void deleteContents(List<Long> ids);

    void deleteEngines(List<Long> ids);

    void deleteBundles(List<Long> ids);
    
    List<Subscriber> searchSubscribers(SearchCustomerCriteria criteria);

    void deleteContracts(List<Long> ids);

    List<Contract> getContractBySubscriberId(Long subscriberId);
}
