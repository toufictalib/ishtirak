package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import com.aizong.ishtirak.common.misc.utils.BaseEntity;

@Entity
public class Engine extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 513072882704990241L;

    @Column(name = "name")
    private String name;

    @Embedded
    private Address address = new Address();

    @Column(name = "kva")
    private Integer kva;
    
    @Column(name = "diesel_consumption")
    //in hour
    private Double dieselConsumption;

    
    public Engine() {
	super();
    }

    public Engine(Long id) {
	super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getKva() {
        return kva;
    }

    public void setKva(Integer kva) {
        this.kva = kva;
    }

    public Double getDieselConsumption() {
        return dieselConsumption;
    }

    public void setDieselConsumption(Double dieselConsumption) {
        this.dieselConsumption = dieselConsumption;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
