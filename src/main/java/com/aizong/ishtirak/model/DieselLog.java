package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.aizong.ishtirak.common.misc.BaseEntity;

@Entity
@Table(name = "diesel_log")
public class DieselLog extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -544350477104219047L;

    @Column(name = "description")
    private String description;

    @Column(name = "diesel_amount")
    private double dieselAmount;

    @Column(name = "amount")
    private double amount;

    @Column(name = "maintenace_log_id")
    private Long maintenanceLog;

    public DieselLog() {
	super();
    }

    public DieselLog(Long id) {
	super(id);
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public double getDieselAmount() {
	return dieselAmount;
    }

    public void setDieselAmount(double dieselAmount) {
	this.dieselAmount = dieselAmount;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public Long getMaintenanceLog() {
	return maintenanceLog;
    }

    public void setMaintenanceLog(Long maintenanceLog) {
	this.maintenanceLog = maintenanceLog;
    }

}
