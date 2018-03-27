package com.aizong.ishtirak.service;

import java.util.List;

import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.model.Bundle;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.CounterHistory;
import com.aizong.ishtirak.model.DieselLog;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.MaintenaceLog;
import com.aizong.ishtirak.model.MonthlyBundle;
import com.aizong.ishtirak.model.Subscriber;
import com.aizong.ishtirak.model.SubscriptionBundle;
import com.aizong.ishtirak.model.Village;

public interface SubscriberService {

    List<Subscriber> getSubscribers();

    List<Village> getVillages();
    
    void saveVillage(Village village);
    
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
    
    void saveContract(Contract contract);
    
    Contract getContractById(Long id);
    
    void deleteContracts(List<Long> ids);

    void saveConsumptionHistory(CounterHistory history);
    
    List<Contract> getContractBySubscriberId(Long subscriberId);

    void generateReceipts();

    void saveMaintenanceLog(MaintenaceLog maintenaceLog);

    void saveDieselLog(MaintenaceLog maintenaceLog, DieselLog dieselLog);
}
