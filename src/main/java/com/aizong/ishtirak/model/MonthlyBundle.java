package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MonthlyBundle")
public class MonthlyBundle extends Bundle {

    /**
     * 
     */
    private static final long serialVersionUID = 8130216796555842292L;

    @Column(name = "monthly_fees")
    private double fees;

    public MonthlyBundle() {
	super();
    }

    public MonthlyBundle(Long id) {
	super(id);
    }

    public MonthlyBundle(String name) {
	super(name);
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }
    
    

}
