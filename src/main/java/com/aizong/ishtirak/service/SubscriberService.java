package com.aizong.ishtirak.service;

import java.util.List;

import com.aizong.ishtirak.subscriber.Subscriber;
import com.aizong.ishtirak.subscriber.Village;

public interface SubscriberService {

    List<Subscriber> getSubscribers();

    List<Village> getVillages();
    
    void saveVillage(Village village);
    
    void saveSubscriber(Subscriber subscriber);
    
    Subscriber getSubscriberById(Long id);

    void deleteSubscribers(List<Long> subscriberIds);
    
}
