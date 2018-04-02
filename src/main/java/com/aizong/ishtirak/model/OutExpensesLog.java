package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.aizong.ishtirak.common.misc.utils.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "out_expenses_log")
public class OutExpensesLog extends BaseEntity {

    @Column(name = "description")
    private String desc;

    @Column(name = "amount")
    private double amount;

    @Column(name = "employee")
    private Long employeeId;

    @Lob
    @Column(name = "note")
    private String note;

    public String getDesc() {
	return desc;
    }

    public void setDesc(String desc) {
	this.desc = desc;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public Long getEmployeeId() {
	return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
	this.employeeId = employeeId;
    }

    public String getNote() {
	return note;
    }

    public void setNote(String note) {
	this.note = note;
    }

    
}
