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
public interface BeanParsing {
    
    public void prepare(ModelHolder holder);
    
    public void init();
    
    public List<Column> getColumns();

    public List<List<Object>> getRows();
}
