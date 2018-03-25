package com.aizong.ishtirak.subscriber.model;

import javax.persistence.Entity;

import com.aizong.ishtirak.common.BaseEntity;

@Entity(name = "village")
public class Village extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 7885874793830935824L;
    private String name;

    public Village() {
	super();
    }

    public Village(Long id) {
	this(id, null);
    }

    public Village(Long id, String name) {
	super(id);
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return name;
    }

}
