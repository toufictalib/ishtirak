package com.aizong.ishtirak.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.aizong.ishtirak.bundle.Contract;
import com.aizong.ishtirak.subscriber.SearchCustomerCriteria;
import com.aizong.ishtirak.subscriber.form.Enums.SearchCustomerType;
import com.aizong.ishtirak.subscriber.model.Subscriber;

@Repository
public class SubscriberDaoImpl extends GenericDaoImpl<Object> implements SubscriberDao {

    @Override
    public void deleteContents(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from subscriber where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids", ids);
	    sqlQuery.executeUpdate();
	}

    }

    @Override
    public void deleteEngines(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from engine where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids", ids);
	    sqlQuery.executeUpdate();
	}

    }

    @Override
    public void deleteBundles(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from bundle where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids", ids);
	    sqlQuery.executeUpdate();
	}

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Subscriber> searchSubscribers(SearchCustomerCriteria criteria) {
	SearchCustomerType customerType = SearchCustomerType.NAME;
	if (criteria.getCustomerType() != null) {
	    customerType = criteria.getCustomerType();
	}

	String sql = "";
	if (customerType == SearchCustomerType.NAME) {
	    sql = "select * from subscriber where name like ?";
	} else if (customerType == SearchCustomerType.LASTNAME) {
	    sql = "select * from subscriber where last_name like ?";
	} else if (customerType == SearchCustomerType.LAND_LINE) {
	    sql = "select s.* from subscriber s,information i where s.id = i.id and i.land_line like ?";
	} else if (customerType == SearchCustomerType.MAIN_PHONE) {
	    sql = "select s.* from subscriber s,information i where s.id = i.id and i.main_phone like ?";
	}

	NativeQuery createSQLQuery = getsession().createSQLQuery(sql);
	createSQLQuery.setParameter(0, "%" + criteria.getText() + "%");
	createSQLQuery.addEntity(Subscriber.class);
	return createSQLQuery.list();
    }

    @Override
    public void deleteContracts(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from contract where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids", ids);
	    sqlQuery.executeUpdate();
	}

    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    @Override
    public List<Contract> getContractBySubscriberId(Long subscriberId) {
	Criteria criteria = getsession().createCriteria(Contract.class);
	criteria.add(Restrictions.eq("subscriberId", subscriberId));
	return criteria.list();
    }

}
