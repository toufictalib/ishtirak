/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc;

import java.util.List;

/**
 *
 * @author User
 */
public class ModelHolder {

    private List data;
    private Class clazz;

    public ModelHolder(List data, Class clazz) {
        this.data = data;
        this.clazz = clazz;
    }

    public ModelHolder(List data) {
        this.data = data;
        if (data.isEmpty()) {
            throw new IllegalArgumentException("You should pass class of current crud panel");
        }

        this.clazz = data.get(0).getClass();
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

}
