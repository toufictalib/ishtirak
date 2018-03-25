package com.aizong.ishtirak.engine;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import com.aizong.ishtirak.common.BaseEntity;
import com.aizong.ishtirak.subscriber.model.Address;

@Entity
public class Engine extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 513072882704990241L;

    @Column(name = "name")
    private String name;

    @Embedded
    private Address address;

    @Column(name = "kva")
    private String kva;
    
    @Column(name = "diesel_consumption")
    private double dieselConsumption;

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

    public String getKva() {
        return kva;
    }

    public void setKva(String kva) {
        this.kva = kva;
    }

    public double getDieselConsumption() {
        return dieselConsumption;
    }

    public void setDieselConsumption(double dieselConsumption) {
        this.dieselConsumption = dieselConsumption;
    }
    
    
}
