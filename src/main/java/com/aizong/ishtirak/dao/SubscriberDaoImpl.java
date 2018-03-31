package com.aizong.ishtirak.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.aizong.ishtirak.bean.ContractConsumptionBean;
import com.aizong.ishtirak.bean.Enums.SearchCustomerType;
import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.Subscriber;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
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

    @Override
    public List<Contract> getContractBySubscriberId(Long subscriberId) {
	Criteria criteria = getsession().createCriteria(Contract.class);
	criteria.add(Restrictions.eq("subscriberId", subscriberId));
	return criteria.list();
    }

    @Override
    public List<Contract> getActiveContracts() {
	Criteria criteria = getsession().createCriteria(Contract.class);
	criteria.add(Restrictions.eq("active", true));
	//criteria.add(Restrictions.gt("insert_date", ""));
	return criteria.list();
    }

    
    @Override
    public List<ContractConsumptionBean> getCounterHistory(int previousMonth, int currentMonth) {

	String sql = "select c1.contract_id as 'contractId',c1.consumption as 'previousCounterValue',c2.consumption as 'currentCounterValue' "
		+ "from counter_history c1,counter_history c2 where "
		+ "c1.contract_id = c2.contract_id and month(c1.insert_date) in (:previousMonth) "
		+ "and month(c2.insert_date) in (:currentMonth)";
	NativeQuery sqlQuery = getsession().createSQLQuery(sql);
	sqlQuery.setParameter("previousMonth", previousMonth).setParameter("currentMonth", currentMonth);
	sqlQuery.addScalar("contractId", StandardBasicTypes.LONG).addScalar("previousCounterValue",
		StandardBasicTypes.LONG).addScalar("currentCounterValue",
			StandardBasicTypes.LONG);
	sqlQuery.setResultTransformer(Transformers.aliasToBean(ContractConsumptionBean.class));
	return sqlQuery.list();
    }

    @Override
    public void deleteVillages(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from village where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids", ids);
	    sqlQuery.executeUpdate();
	}
	
    }

    @Override
    public void deleteEmployeeType(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from employee_type where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids", ids);
	    sqlQuery.executeUpdate();
	}
	
    }

    @Override
    public void deleteEmployees(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from employee where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids", ids);
	    sqlQuery.executeUpdate();
	}
	
    }

    @Override
    public void deleteTransactions(List<Long> contractIds) {
	if (contractIds.size() > 0) {
	    String sql = "delete from transaction where id_contract  in :contractIds";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("contractIds", contractIds);
	    sqlQuery.executeUpdate();
	}
	
    }

    @Override
    public List<Employee> getEmployees(Boolean active) {
	Criteria criteria = getsession().createCriteria(Employee.class);
	criteria.add(Restrictions.eq("active", active));
	return criteria.list();
    }

    @Override
    public void deleteExpenses(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from expenses_log where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids", ids);
	    sqlQuery.executeUpdate();
	}
    }

}
