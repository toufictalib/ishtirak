package com.aizong.ishtirak.subscriber.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.aizong.ishtirak.common.BaseEntity;

@Entity(name = "subscriber")
public class Subscriber extends BaseEntity {
    private static final long serialVersionUID = -4113797215555773450L;

    @Column(name = "name")
    private String name;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "identifier")
    private String identifier;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "subscriber")
    private Information information;

    public Subscriber() {
	super();
    }

    public Subscriber(Long id) {
	super(id);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getFatherName() {
	return fatherName;
    }

    public void setFatherName(String fatherName) {
	this.fatherName = fatherName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getIdentifier() {
	return identifier;
    }

    public void setIdentifier(String identifier) {
	this.identifier = identifier;
    }

    public Information getInformation() {
	return information;
    }

    public void setInformation(Information information) {
	this.information = information;
    }
    
    @Override
    public String toString() {
        return name;
    }

}
