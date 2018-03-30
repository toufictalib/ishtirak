package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.aizong.ishtirak.common.misc.utils.BaseEntity;

@MappedSuperclass
public class Person extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -8273677423785836217L;

    @Column(name = "name")
    private String name;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "identifier")
    private String identifier;

    public Person() {
	super();
    }

    public Person(Long id) {
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

    @Override
    public String toString() {
	return name;
    }

}
