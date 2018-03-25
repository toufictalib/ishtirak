package com.aizong.ishtirak.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriberDaoImpl extends GenericDaoImpl<Object> implements SubscriberDao {

    @Override
    public void deleteContents(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from subscriber where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids",ids);
	    sqlQuery.executeUpdate();
	}

    }
    
    @Override
    public void deleteEngines(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from engine where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids",ids);
	    sqlQuery.executeUpdate();
	}

    }

}
