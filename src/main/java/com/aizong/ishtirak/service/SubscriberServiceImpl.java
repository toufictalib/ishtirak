package com.aizong.ishtirak.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aizong.ishtirak.bundle.Bundle;
import com.aizong.ishtirak.bundle.Contract;
import com.aizong.ishtirak.bundle.MonthlyBundle;
import com.aizong.ishtirak.bundle.SubscriptionBundle;
import com.aizong.ishtirak.dao.SubscriberDao;
import com.aizong.ishtirak.engine.Engine;
import com.aizong.ishtirak.subscriber.SearchCustomerCriteria;
import com.aizong.ishtirak.subscriber.model.Subscriber;
import com.aizong.ishtirak.subscriber.model.Village;
import com.aizong.ishtirak.transaction.CounterHistory;
import com.aizong.ishtirak.transaction.SubscriptionHistory;
import com.aizong.ishtirak.transaction.Transaction;
import com.aizong.ishtirak.transaction.TransactionType;

@Service
@Transactional
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    SubscriberDao subscriberDao;

    @Override
    public List<Subscriber> getSubscribers() {
	return subscriberDao.findAll(Subscriber.class);
    }

    @Override
    public List<Village> getVillages() {
	return subscriberDao.findAll(Village.class);
    }

    @Override
    public void saveVillage(Village village) {
	subscriberDao.save(Arrays.asList(village));

    }

    @Override
    public void saveSubscriber(Subscriber subscriber) {
	if (subscriber.getId() != null) {
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
    public List<Contract> getContractBySubscriberId(Long subscriberId) {
	return subscriberDao.getContractBySubscriberId(subscriberId);
    }

    private List<Contract> getActiveContracts() {
	return subscriberDao.getActiveContracts();
    }

    @Override
    public void generateReceipts() {
	List<Contract> contracts = getActiveContracts();

	List<Bundle> allBundles = getAllBundles();
	Map<Long, Bundle> map = allBundles.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
	List<Transaction> transactions = new ArrayList<>();
	List<SubscriptionHistory> subscriptionHistoryList = new ArrayList<>();
	for (Contract contract : contracts) {
	    Bundle bundle = map.get(contract.getBundleId());
	    if (bundle instanceof MonthlyBundle) {
		Transaction transaction = new Transaction();
		transaction.setAmount(((MonthlyBundle) bundle).getFees());
		transaction.setContractId(contract.getId());
		transaction.setTransactionType(TransactionType.MONTHLY_PAYMENT);

		transactions.add(transaction);
	    } else if (bundle instanceof SubscriptionBundle) {
		SubscriptionBundle subscriptionBundle = (SubscriptionBundle) bundle;
		Transaction transaction = new Transaction();
		transaction
			.setAmount(subscriptionBundle.getSubscriptionFees() + subscriptionBundle.getCostPerKb() * 1000);
		transaction.setContractId(contract.getId());
		transaction.setTransactionType(TransactionType.MONTHLY_PAYMENT);
		transactions.add(transaction);

		SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
		subscriptionHistory.setConsumption(1000);
		subscriptionHistory.setCostPerKb(subscriptionBundle.getCostPerKb());
		subscriptionHistory.setSubscriptionFees(subscriptionBundle.getSubscriptionFees());
		subscriptionHistoryList.add(subscriptionHistory);

	    }
	}
	
	subscriberDao.save(new ArrayList<>(transactions));
	subscriberDao.save(new ArrayList<>(subscriptionHistoryList));
    }

}
