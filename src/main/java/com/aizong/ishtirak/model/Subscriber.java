package com.aizong.ishtirak.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity(name = "subscriber")
public class Subscriber extends Person {
    private static final long serialVersionUID = -4113797215555773450L;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "subscriber")
    private SubscriberInformation information;

    public SubscriberInformation getInformation() {
	return information;
    }

    public void setInformation(SubscriberInformation information) {
	this.information = information;
    }

}
