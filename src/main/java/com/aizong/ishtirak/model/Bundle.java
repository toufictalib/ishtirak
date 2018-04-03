package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.aizong.ishtirak.common.misc.utils.BaseEntity;

@Entity
@Table(name = "bundle")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Bundle extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 420270326779212177L;
    @Column(name = "name")
    protected String name;

    @Column(name = "settelment_fees")
    protected Integer settlementFees;

    public Bundle() {
	super();
    }

    public Bundle(Long id) {
	super(id);
    }

    public Bundle(String name) {
	super();
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Integer getSettlementFees() {
	return settlementFees;
    }

    public void setSettlementFees(Integer settlementFees) {
	this.settlementFees = settlementFees;
    }

    @Override
    public String toString() {
	return name;
    }

}
