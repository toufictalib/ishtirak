package com.aizong.ishtirak.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

public abstract class GenericDaoImpl<T> implements GenericDao<T> {

    @PersistenceContext
    protected EntityManager em;

    private Class<T> type;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public GenericDaoImpl() {
	Type t = getClass().getGenericSuperclass();
	ParameterizedType pt = (ParameterizedType) t;
	type = (Class) pt.getActualTypeArguments()[0];
    }

    public Session getsession() {
	return em.unwrap(Session.class);
    }

    @Override
    public long countAll(final Map<String, Object> params) {

	final StringBuffer queryString = new StringBuffer("SELECT count(o) from ");

	queryString.append(type.getSimpleName()).append(" o ");

	final Query query = this.em.createQuery(queryString.toString());

	return (Long) query.getSingleResult();
    }

    @Override
    public T create(final T t) {
	this.em.persist(t);
	return t;
    }

    @Override
    public void delete(final Object id) {
	this.em.remove(this.em.getReference(type, id));
    }

    @Override
    public T find(final Object id) {
	return find(type, id);
    }

    @Override
    public T update(final T t) {
	return this.em.merge(t);
    }

    @Override
    public void save(Collection<T> list) {
	int i = 0;
	for (T t : list) {
	    Session session = getsession();
	    session.save(t);
	    i++;
	    if (i % 20 == 0) { // 20, same as the JDBC batch size
		// flush a batch of inserts and release memory:
		session.flush();
		session.clear();
	    }
	}
    }

    @Override
    public void massUpdate(Collection<T> list) {
	int i = 0;
	for (T t : list) {
	    Session session = getsession();
	    session.update(t);
	    i++;
	    if (i % 20 == 0) { // 20, same as the JDBC batch size
		// flush a batch of inserts and release memory:
		session.flush();
		session.clear();
	    }
	}
    }

    @Override
    public <V> V find(Class<V> clazz, Object id) {
	return this.em.find(clazz, id);
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    @Override
    public <V> List<V> findAll(Class<V> clazz) {
	return getsession().createCriteria(clazz).list();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void delete(Class<?> clazz, Object id) {
	String hql = "delete from " + clazz.getSimpleName() + " where id= :id";
	@SuppressWarnings("rawtypes")
	org.hibernate.Query query = getsession().createQuery(hql);
	query.setParameter("id", id);
	query.executeUpdate();

    }
}