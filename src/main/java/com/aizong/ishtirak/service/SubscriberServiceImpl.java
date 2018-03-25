package com.aizong.ishtirak.service;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aizong.ishtirak.bundle.Bundle;
import com.aizong.ishtirak.bundle.MonthlyBundle;
import com.aizong.ishtirak.bundle.SubscriptionBundle;
import com.aizong.ishtirak.dao.SubscriberDao;
import com.aizong.ishtirak.engine.Engine;
import com.aizong.ishtirak.subscriber.model.Subscriber;
import com.aizong.ishtirak.subscriber.model.Village;

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
	if(engine.getId()!=null) {
	    subscriberDao.update(engine);

	}else {
	    
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
	if(bundle.getId()!=null) {
	    subscriberDao.update(bundle);
	}else {
	    subscriberDao.save(Arrays.asList(bundle));
	}
	
    }

    @Override
    public Bundle getBundleById(long id) {
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

}
