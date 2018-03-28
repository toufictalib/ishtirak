package com.aizong.ishtirak.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "subscriber_information")
public class SubscriberInformation extends Information {

    /**
     * 
     */
    private static final long serialVersionUID = 2127247177174729404L;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Subscriber subscriber;

    public Subscriber getSubscriber() {
	return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
	this.subscriber = subscriber;
    }
}
