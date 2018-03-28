package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.aizong.ishtirak.common.misc.BaseEntity;

@Entity
@Table(name = "employee_type")
public class EmployeeType extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 7650307440047929314L;

    @Column(name = "name")
    private String name;

    public EmployeeType() {
	super();
    }

    public EmployeeType(Long id) {
	super(id);
    }

    public EmployeeType(String label) {
	super();
	this.name = label;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}
