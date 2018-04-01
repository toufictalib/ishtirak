package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.aizong.ishtirak.common.misc.utils.BaseEntity;

@Entity
@Table(name = "user")
public class User extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -2682990444979309077L;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "password")
    private String password;

    public User() {
	super();
    }

    public User(Long id) {
	super(id);
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

}
