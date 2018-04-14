package com.aizong.ishtirak.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.aizong.ishtirak.bean.ContractConsumptionBean;
import com.aizong.ishtirak.bean.Enums.SearchCustomerType;
import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.bean.Tuple;
import com.aizong.ishtirak.common.misc.utils.Constant;
import com.aizong.ishtirak.common.misc.utils.SQLUtils;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.CounterHistory;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.Subscriber;
import com.aizong.ishtirak.model.SubscriptionBundle;
import com.aizong.ishtirak.model.User;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class SubscriberDaoImpl extends GenericDaoImpl<Object> implements SubscriberDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

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
    public List<Contract> getCounterContractBySubscriberId(Long subscriberId) {

	String sql = "select * from contract where subscriber_id  =:subscriberId and "
		+ " bundle_id in (select id from bundle where type =:subscriptionBundle) and is_active = 1";
	NativeQuery createSQLQuery = getsession().createSQLQuery(sql);
	createSQLQuery.setParameter("subscriberId", subscriberId).setParameter("subscriptionBundle",
		SubscriptionBundle.class.getSimpleName());
	createSQLQuery.addEntity(Contract.class);
	return createSQLQuery.list();
    }

    @Override
    public List<Contract> getActiveContracts() {
	Criteria criteria = getsession().createCriteria(Contract.class);
	criteria.add(Restrictions.eq("active", true));
	// criteria.add(Restrictions.gt("insert_date", ""));
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
	sqlQuery.addScalar("contractId", StandardBasicTypes.LONG)
		.addScalar("previousCounterValue", StandardBasicTypes.LONG)
		.addScalar("currentCounterValue", StandardBasicTypes.LONG);
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

    @Override
    public List<Long> getCreatedContractsForCurrentMonth(List<Contract> activeContracts, String fromDate,
	    String toDate) {

	if (!activeContracts.isEmpty()) {
	    String sql = "select id_contract from transaction t "
		    + "where insert_date>=:fromDate and insert_date<=toDate "
		    + "and t.id_contract in (:activeContracts) and t.transaction_type <> :transactionType";

	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("activeContracts", activeContracts).setParameter("fromDate", fromDate)
		    .setParameter("transactionType", TransactionType.SETTELMENT_FEES.name())
		    .setParameter("toDate", toDate);
	    sqlQuery.addScalar("id_contract", StandardBasicTypes.LONG);
	    return sqlQuery.list();
	}

	return new ArrayList<Long>();
    }

    @Override
    /**
     * active : null means get all contract per subscriber
     */
    public List<Contract> getContractBySubscriberId(Long subscriberId, Boolean active) {
	Criteria criteria = getsession().createCriteria(Contract.class);
	criteria.add(Restrictions.eq("subscriberId", subscriberId));
	if (active != null) {
	    criteria.add(Restrictions.eq("active", active));
	}
	return criteria.list();
    }

    @Override
    public CounterHistory getCounterHistoryByContractId(String contractUniqueCode, String fromDate, String toDate) {
	String sql = "select * from counter_history " + "where contract_unique_code = :contractId "
		+ "and insert_date >= :fromDate " + "and insert_date <= :toDate";
	NativeQuery<CounterHistory> sqlQuery = getsession().createNativeQuery(sql);
	sqlQuery.setParameter("contractId", contractUniqueCode).setParameter("fromDate", fromDate)
		.setParameter("toDate", toDate).addEntity(CounterHistory.class);
	return sqlQuery.uniqueResult();
    }

    @Override
    public void updateCounterHistory(CounterHistory history) {
	String sql = "update counter_history set consumption = :consumption where id = :id";
	NativeQuery<Void> sqlQuery = getsession().createNativeQuery(sql);
	sqlQuery.setParameter("consumption", history.getConsumption()).setParameter("id", history.getId());
	sqlQuery.executeUpdate();

    }

    @Override
    public User getUserByName(String userName) {
	NativeQuery<User> sqlQuery = getsession().createNativeQuery("select * from user where user_name =:userName");
	sqlQuery.setParameter("userName", userName).addEntity(User.class);
	return sqlQuery.getSingleResult();

    }

    @Override
    public void deleteOutExpenses(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from out_expenses_log where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids", ids);
	    sqlQuery.executeUpdate();
	}

    }

    @Override
    public Map<String, List<Tuple<String, Double>>> getResult(String fromDate, String endDate) {

	String income = SQLUtils.sql("income.sql");
	income = MessageFormat.format(income, fromDate, endDate);

	String expenses = SQLUtils.sql("expenses.sql");
	expenses = MessageFormat.format(expenses, fromDate, endDate);

	List<Tuple<String, Double>> incomeList = jdbcTemplate.query(income,
		new ResultSetExtractor<List<Tuple<String, Double>>>() {

		    @Override
		    public List<Tuple<String, Double>> extractData(ResultSet resultSet)
			    throws SQLException, DataAccessException {
			List<Tuple<String, Double>> list = new ArrayList<>();
			while (resultSet.next()) {
			    list.add(new Tuple<String, Double>(resultSet.getString("Transaction Type"),
				    resultSet.getDouble("total")));
			}
			return list;
		    }
		});

	List<Tuple<String, Double>> expensesList = jdbcTemplate.query(expenses,
		new ResultSetExtractor<List<Tuple<String, Double>>>() {

		    @Override
		    public List<Tuple<String, Double>> extractData(ResultSet resultSet)
			    throws SQLException, DataAccessException {
			List<Tuple<String, Double>> list = new ArrayList<>();
			while (resultSet.next()) {
			    list.add(new Tuple<String, Double>(resultSet.getString("Maintenance Type"),
				    resultSet.getDouble("total")));
			}
			return list;
		    }
		});

	Map<String, List<Tuple<String, Double>>> map = new HashMap<>();
	map.put(Constant.INCOME, incomeList);
	map.put(Constant.EXPENSES, expensesList);

	return map;
    }

    @Override
    public Map<Long, Set<String>> getContractUniqueCodesByEngine() {
	String sql = "select distinct engine_id,contract_unique_code from contract where is_active = 1";

	return jdbcTemplate.query(sql, new ResultSetExtractor<Map<Long, Set<String>>>() {

	    @Override
	    public Map<Long, Set<String>> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<Long, Set<String>> map = new HashMap<>();
		while (rs.next()) {
		    Long key = rs.getLong("engine_id");
		    Set<String> list = map.get(key);
		    if (list == null) {
			list = new HashSet<>();
			map.put(key, list);
		    }
		    list.add(rs.getString("contract_unique_code"));
		}
		return map;
	    }

	});
    }

    @Override
    public void updateCounters(Map<String, Long> e, String startDate, String endDate) {
	deleteCounterHistory(e.keySet(), startDate, endDate);

	List<CounterHistory> counterHistories = new ArrayList<>();
	for (Entry<String, Long> p : e.entrySet()) {
	    CounterHistory counterHistory = new CounterHistory();
	    counterHistory.setContractUniqueCode(p.getKey());
	    counterHistory.setConsumption(p.getValue());

	    counterHistories.add(counterHistory);
	}

	save(new ArrayList<>(counterHistories));
    }
    
    @Override
    public void updatePaid(Map<String, Boolean> updatedTransactionsMap, String startDate, String endDate) {

	String sql = SQLUtils.sql("getNotPaidTransaction.sql", startDate, endDate);

	// map contains as key unique counter id and as value a trasaction id
	Map<String, Long> map = jdbcTemplate.query(sql, new ResultSetExtractor<Map<String, Long>>() {

	    @Override
	    public Map<String, Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, Long> map = new HashMap<>();
		while (rs.next()) {
		    try {
		    map.put(rs.getString(2), rs.getLong(1));
		    }catch (Exception e) {
			System.err.println(e.getMessage());
		    }
		}
		return map;
	    }

	});
	
	if (!map.isEmpty()) {
	    List<Object[]> data = new ArrayList<>();
	    for (Entry<String, Boolean> entry : updatedTransactionsMap.entrySet()) {
		if (entry.getValue() != null && entry.getValue()) {
		    Long id = map.get(entry.getKey());
		    if (id != null) {
			data.add(new Object[] { id, entry.getValue() });
		    }

		}
	    }

	    if (!data.isEmpty()) {
		String batchSql = "update transaction set is_paid = ?,date_paid = now() where id = ?";
		jdbcTemplate.batchUpdate(batchSql, new BatchPreparedStatementSetter() {
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
			ps.setBoolean(1, (Boolean) data.get(i)[1]);
			ps.setLong(2, (Long) data.get(i)[0]);
		    }

		    public int getBatchSize() {
			return data.size();
		    }
		});
	    }
	}

	

    }

    private void deleteCounterHistory(Collection<String> contractUniqueCodes, String startDate, String endDate) {
	
	if(contractUniqueCodes.size()>0) {
	String sql = "delete from counter_history where contract_unique_code in (:ids) and insert_date >= :startDate  and "
		+ "insert_date <=:endDate";
	NativeQuery createSQLQuery = getsession().createSQLQuery(sql);
	createSQLQuery.setParameter("startDate", startDate);
	createSQLQuery.setParameter("endDate", endDate);
	createSQLQuery.setParameterList("ids", contractUniqueCodes);
	createSQLQuery.executeUpdate();


    }
    }
}
