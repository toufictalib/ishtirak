package com.aizong.ishtirak.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.misc.BaseEntity;

@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity {

    private static final long serialVersionUID = 57769739643654657L;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "is_paid")
    private boolean paid;

    @Column(name = "date_paid")
    private Date paidDate;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "id_contract")
    private long contractId;

    public Transaction() {
	super();
	// TODO Auto-generated constructor stub
    }

    public Transaction(Long id) {
	super(id);
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public boolean isPaid() {
	return paid;
    }

    public void setPaid(boolean paid) {
	this.paid = paid;
    }

    public Date getPaidDate() {
	return paidDate;
    }

    public void setPaidDate(Date paidDate) {
	this.paidDate = paidDate;
    }

    public TransactionType getTransactionType() {
	return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
	this.transactionType = transactionType;
    }

    public long getContractId() {
	return contractId;
    }

    public void setContractId(long contractId) {
	this.contractId = contractId;
    }

}
