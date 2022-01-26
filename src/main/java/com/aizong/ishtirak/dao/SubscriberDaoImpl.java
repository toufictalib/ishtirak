package com.aizong.ishtirak.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import org.hibernate.criterion.Projections;
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
import com.aizong.ishtirak.bean.CurrencyManager;
import com.aizong.ishtirak.bean.Enums.SearchCustomerType;
import com.aizong.ishtirak.bean.OrderBean;
import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.bean.Tuple;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.Constant;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.SQLUtils;
import com.aizong.ishtirak.demo.ReceiptBean;
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
    
    @Autowired
    CurrencyManager currencyManager;

    @Override
    public void deleteSubscribers(List<Long> ids) {
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
    public List<ContractConsumptionBean> getCounterHistory(DateRange currentDateRange) {
	String sql = SQLUtils.sql("user_consumption_between_now_previous.sql", currentDateRange.getStartDateAsString(), currentDateRange.getEndDateAsString());

	return jdbcTemplate.query(sql,
		new ResultSetExtractor<List<ContractConsumptionBean>>() {

		    @Override
		    public List<ContractConsumptionBean> extractData(ResultSet rs)
			    throws SQLException, DataAccessException {
			List<ContractConsumptionBean> list = new ArrayList<>();
			while (rs.next()) {
			    
			    list.add(new ContractConsumptionBean(rs.getString("contract_unique_code"),
				    rs.getLong("previous_counter"), rs.getLong("current_counter")));
			}
			return list;
		    }
		});

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
    public void deleteTransactions(List<Long> ids) {
	if (ids.size() > 0) {
	    String sql = "delete from transaction where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids", ids);
	    sqlQuery.executeUpdate();
	}

    }
    
    @Override
    public void deleteSubscriptionHistory(List<Long> transactionIds) {
	if (transactionIds.size() > 0) {
	    String sql = "delete from subscription_history where transaction  in :transactionIds";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("transactionIds", transactionIds);
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
    public List<Long> getCreatedContractsForCurrentMonth(List<Long> activeContractIds, String fromDate,
	    String toDate) {

	if (!activeContractIds.isEmpty()) {
	    String sql = "select id_contract from transaction t "
		    + "where insert_date>=:fromDate and insert_date<= :toDate "
		    + "and t.id_contract in (:activeContracts) and t.transaction_type <> :transactionType";

	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("activeContracts", activeContractIds).setParameter("fromDate", fromDate)
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
	    criteria.add(Restrictions.isNull("closeDate"));
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
    public Map<String, List<Tuple<String, Double>>> getResult(String fromDate,  String endDate) {

	String income = SQLUtils.sql("income.sql", fromDate, endDate);

	String expenses = SQLUtils.sql("expenses.sql",fromDate, endDate);

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
    public void updateCounters(Map<String, Long> e, LocalDate selectedDate) {
	
	DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth(selectedDate);
	deleteCounterHistory(e.keySet(), dateRange.getStartDateAsString(), dateRange.getEndDateAsString());

	List<CounterHistory> counterHistories = new ArrayList<>();
	for (Entry<String, Long> p : e.entrySet()) {
	    CounterHistory counterHistory = new CounterHistory();
	    counterHistory.setContractUniqueCode(p.getKey());
	    counterHistory.setConsumption(p.getValue());
	    counterHistory.setInsertDate(DateUtil.fromLocalDate(selectedDate));
	    counterHistories.add(counterHistory);
	}

	save(new ArrayList<>(counterHistories));
    }
    
    @Override
    public void updatePaid(Map<String, Boolean> updatedTransactionsMap, LocalDate selectedDate) {

	//***** map transaction to contract unique code per each contract
	DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth(selectedDate);
	String sql = SQLUtils.sql("getNotPaidTransaction.sql", dateRange.getStartDateAsString(), dateRange.getEndDateAsString());

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
		    @Override
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
			ps.setBoolean(1, (Boolean) data.get(i)[1]);
			ps.setLong(2, (Long) data.get(i)[0]);
		    }

		    @Override
		    public int getBatchSize() {
			return data.size();
		    }
		});
	    }
	}

	

    }

    private void deleteCounterHistory(Collection<String> contractUniqueCodes, String startDate, String endDate) {

	if (contractUniqueCodes.size() > 0) {
	    String sql = "delete from counter_history where contract_unique_code in (:ids) and insert_date >= :startDate  and "
		    + "insert_date <=:endDate";
	    NativeQuery createSQLQuery = getsession().createSQLQuery(sql);
	    createSQLQuery.setParameter("startDate", startDate);
	    createSQLQuery.setParameter("endDate", endDate);
	    createSQLQuery.setParameterList("ids", contractUniqueCodes);
	    createSQLQuery.executeUpdate();

	}
    }

    @Override
    public void updatePayment(List<Long> transactionIds, boolean paid) {
	if(!transactionIds.isEmpty()) {
	    String sql = "UPDATE transaction t SET t.is_paid = 1,date_paid = now() WHERE id IN (:transactionIds)";
		if(!paid) {
		    sql = "UPDATE transaction t SET t.is_paid = 0,date_paid = null WHERE id IN (:transactionIds)";
		}
		
		NativeQuery createNativeQuery = getsession().createNativeQuery(sql);
		createNativeQuery.setParameterList("transactionIds", transactionIds);
		createNativeQuery.executeUpdate();
	}
    }
  
    @Override
    public List<ReceiptBean> getReceipts(List<Long> transactionIds, DateRange dateRange) {
	String sql = SQLUtils.sql("receipt.sql", dateRange.getStartDateAsString(), dateRange.getEndDateAsString());
	
	if(transactionIds!=null && !transactionIds.isEmpty()) {
	    String parameterList = SQLUtils.toParameterList(transactionIds);
	    sql = SQLUtils.sql("receipt_selected_transactions.sql", dateRange.getStartDateAsString(), dateRange.getEndDateAsString(), parameterList);
	}
	return jdbcTemplate.query(sql, new ResultSetExtractor<List<ReceiptBean>>() {

	    @Override
	    public List<ReceiptBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<ReceiptBean> receiptBeans = new ArrayList<>();
		while (rs.next()) {
		    
		    
		    ReceiptBean bean = new ReceiptBean(rs.getString("Full Name"), rs.getString("Village"),
			    rs.getString("address"), DateUtil.getCurrentMonthLabel(DateUtil.localDate(dateRange.getStartDate())), rs.getLong("previous_counter"),
			    rs.getLong("current_counter"), rs.getString("Bundle"),
			    !TransactionType.COUNTER_PAYMENT.name().equals(rs.getString("transaction_type")),
			    rs.getString("contract_unique_code"), rs.getDouble("amount"),rs.getDouble("subscription_fees"), currencyManager.getselectCurrency());
		    receiptBeans.add(bean);
		}
		return receiptBeans;
	    }
	});
    }

    @Override
    public List<Long> getTransactionIdsByContractIds(List<Long> contractIds) {
	if (contractIds != null && !contractIds.isEmpty()) {
	    String sql = "select id from transaction where id_contract in (:contractIds)";
	    NativeQuery createNativeQuery = getsession().createNativeQuery(sql);
	    createNativeQuery.setParameterList("contractIds", contractIds);
	    createNativeQuery.addScalar("id", StandardBasicTypes.LONG);
	    return createNativeQuery.list();
	}
	return new ArrayList<>();

    }
    
    @Override
    public List<Long> getTransactionIdsByContractId(Long contractId, String startDate, String endDate) {
	String sql = "select id from transaction where id_contract = :contractId and insert_date >= :startDate "
		+ "and insert_date <= :endDate";
	NativeQuery createNativeQuery = getsession().createNativeQuery(sql);
	createNativeQuery.setParameter("contractId", contractId).setParameter("startDate", startDate)
		.setParameter("endDate", endDate);
	createNativeQuery.addScalar("id", StandardBasicTypes.LONG);
	return createNativeQuery.list();

    }

    @Override
    public void deleteCounterHistory(List<Long> contractIds) {

	if (contractIds != null && !contractIds.isEmpty()) {
	    String sql = "delete from counter_history where  contract_unique_code in ("
		    + "select contract_unique_code from contract where id in (:contractIds)" + ")";
	    NativeQuery createNativeQuery = getsession().createNativeQuery(sql);
	    createNativeQuery.setParameterList("contractIds", contractIds);
	    createNativeQuery.executeUpdate();
	}

    }
    
    @Override
    public List<Long> getContractIdsBySubscriberId(Long subscriberId) {
  	Criteria criteria = getsession().createCriteria(Contract.class);
  	criteria.setProjection(Projections.projectionList()
  		      .add(Projections.property("id"), "id"))
  		    .setResultTransformer(Transformers.aliasToBean(Contract.class));
  	 
  	criteria.add(Restrictions.eq("subscriberId", subscriberId));
  	return criteria.list();
      }

    @Override
    public void deleteCounterHistoryByIds(List<Long> ids) {
	if(!ids.isEmpty()) {
	    String sql = "delete from counter_history where id  in :ids";
	    SQLQuery sqlQuery = getsession().createSQLQuery(sql);
	    sqlQuery.setParameterList("ids", ids);
	    sqlQuery.executeUpdate();
	}
	
    }
    
    @Override
    public List<OrderBean> getContractsForOrderingPurpose(Long villageId){
	String sql = SQLUtils.sql("sorting_subscribers.sql", villageId);

	return jdbcTemplate.query(sql,
		new ResultSetExtractor<List<OrderBean>>() {

		    @Override
		    public List<OrderBean> extractData(ResultSet rs)
			    throws SQLException, DataAccessException {
			List<OrderBean> list = new ArrayList<>();
			while (rs.next()) {
			    
			    list.add(new OrderBean(rs.getLong(1), rs.getString(2), rs.getString(3), 
				    rs.getString(4), rs.getInt(5)));
			}
			return list;
		    }
		});
    }

    @Override
    public void updateContactOrdering(List<OrderBean> rows) {
	String sql = "update contract set order_index = ? where id = ?";
	
	List<Object[]> batch = new ArrayList<Object[]>();
	for(OrderBean orderBean : rows) {
	    Object[] row = new Object[] {orderBean.getOrderIndex(), orderBean.getContractId()};
	    batch.add(row);
	}
	jdbcTemplate.batchUpdate(sql, batch);
	
    }
}
