package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SubscriptionBundle")
public class SubscriptionBundle extends Bundle {

    /**
     * 
     */
    private static final long serialVersionUID = -7952151033833356266L;

    @Column(name = "cost_per_kb")
    private double costPerKb;

    @Column(name = "subscription_fees")
    private double subscriptionFees;

    public SubscriptionBundle() {
	super();
    }

    public SubscriptionBundle(Long id) {
	super(id);
    }

    public SubscriptionBundle(String name) {
	super(name);
    }

    public double getCostPerKb() {
	return costPerKb;
    }

    public void setCostPerKb(double costPerKb) {
	this.costPerKb = costPerKb;
    }

    public double getSubscriptionFees() {
	return subscriptionFees;
    }

    public void setSubscriptionFees(double subscriptionFees) {
	this.subscriptionFees = subscriptionFees;
    }

}
