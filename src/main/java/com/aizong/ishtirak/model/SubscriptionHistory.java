package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.aizong.ishtirak.common.misc.BaseEntity;

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
    
    @OneToOne
    @JoinColumn(name="transaction")
    private Transaction transaction;

    public SubscriptionHistory() {
	super();
    }

    public SubscriptionHistory(Long id) {
	super(id);
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

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    

}
