package com.aizong.ishtirak.transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.aizong.ishtirak.common.BaseEntity;

@Entity
@Table(name = "subscription_history")
public class SubscriptionHistory extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -5797271317994723725L;

    @Column(name = "cost_per_kb")
    private double costPerKb;

    @Column(name = "subscription_fees")
    private double subscriptionFees;

    @Column(name = "consumption")
    private double consumption;

    public SubscriptionHistory() {
	super();
	// TODO Auto-generated constructor stub
    }

    public SubscriptionHistory(Long id) {
	super(id);
	// TODO Auto-generated constructor stub
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

    public double getConsumption() {
	return consumption;
    }

    public void setConsumption(double consumption) {
	this.consumption = consumption;
    }

}
