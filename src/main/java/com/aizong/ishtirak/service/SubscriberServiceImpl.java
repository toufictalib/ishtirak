package com.aizong.ishtirak.service;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aizong.ishtirak.dao.SubscriberDao;
import com.aizong.ishtirak.subscriber.Subscriber;
import com.aizong.ishtirak.subscriber.Village;

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
	if(subscriber.getId()!=null) {
	    subscriberDao.update(subscriber);
	}else {
	    subscriberDao.save(Arrays.asList(subscriber));
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

}
