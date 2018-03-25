package com.aizong.ishtirak.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GenericDao<T> {
    /**
     * Method that returns the number of entries from a table that meet some
     * criteria (where clause params)
     *
     * @param params
     *            sql parameters
     * @return the number of records meeting the criteria
     */
    long countAll(Map<String, Object> params);

    T create(T t);

    void delete(Object id);

    T find(Object id);

    <V> V find(Class<V> clazz, Object id);

    T update(T t);

    void save(Collection<T> collection);

    void massUpdate(Collection<T> collection);

    <V> List<V> findAll(Class<V> clazz);

    void delete(Class<?> clazz, Object id);

}