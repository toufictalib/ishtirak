package com.aizong.ishtirak.dao;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.misc.utils.SQLUtils;
import com.ss.rlib.concurrent.atomic.AtomicInteger;

@Repository
@Transactional
@SuppressWarnings("deprecation")
public class ReportDaoImpl extends GenericDaoImpl<Object> implements ReportDao {

    @Autowired
    JdbcTemplate jdbcTemplate ;
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getSubscriptionsIncomeReport(Date startDate, Date endDate) {
	NativeQuery<Object[]> createSQLQuery = getsession().createNativeQuery("SELECT \n" + 
		"    c.id as 'contract id', s.name as 'Subsciber Id',s.last_name as 'Last Name',concat(c.region,':',address) as address,\n" + 
		"    b.name as 'Bundle Name' ,t.amount,t.is_paid, t.transaction_type " + 
		"FROM\n" + 
		"    contract c,\n" + 
		"    subscriber s,\n" + 
		"    bundle b,\n" + 
		"    transaction t\n" + 
		"    \n" + 
		"WHERE\n" + 
		"    c.subscriber_id = s.id\n" + 
		"    and c.bundle_id = b.id\n" + 
		"    and t.id_contract = c.id\n" + 
		"    and t.insert_date >=:startDate and t.insert_date <=:endDate order by t.insert_date desc;");
	
	createSQLQuery.setParameter("startDate",startDate).setParameter("endDate", endDate);
	return createSQLQuery.list();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getExpenses(ExpensesType expensesType, String startDate, String endDate) {
	String sql = "select m.id, e.name 'engine', m.amount, m.description,m.maintenace_type,m.insert_date "
		+ "from expenses_log m "
		+ "left join engine e on e.id = m.engine "
		+ "where m.insert_date >=:startDate and m.insert_date <=:endDate ";
	
	if(expensesType!=null) {
	    sql+=" and m.maintenace_type = :expensesType ";
	}
	
	sql+=" order by m.insert_date desc";
	NativeQuery<Object[]> createSQLQuery = getsession().createSQLQuery(sql);
	createSQLQuery.setParameter("startDate",startDate).setParameter("endDate", endDate);
	if (expensesType != null) {
	    createSQLQuery.setParameter("expensesType", expensesType.name());
	}

	createSQLQuery.addScalar("id",StandardBasicTypes.LONG).
	addScalar("engine",StandardBasicTypes.STRING).
	addScalar("amount",StandardBasicTypes.DOUBLE).
	addScalar("description",StandardBasicTypes.STRING).
	addScalar("maintenace_type",StandardBasicTypes.STRING).
	addScalar("insert_date",StandardBasicTypes.DATE);
	
	
	return createSQLQuery.list();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getOutExpenses(Date startDate, Date endDate) {
	String sql = "select m.id, e.name 'Employee Name',e.last_name 'Employee Last Name', m.amount, m.description,m.maintenace_type,m.insert_date "
		+ "from out_expenses_log m "
		+ "left join employee e on e.id = m.employee "
		+ "where m.insert_date >=:startDate and m.insert_date <=:endDate order by m.insert_date desc";
	NativeQuery<Object[]> createSQLQuery = getsession().createNativeQuery(sql);
	createSQLQuery.setParameter("startDate",startDate).setParameter("endDate", endDate);
	createSQLQuery.addScalar("id",StandardBasicTypes.LONG).
	addScalar("Employee Name",StandardBasicTypes.STRING).
	addScalar("Employee Last Name",StandardBasicTypes.STRING).
	addScalar("amount",StandardBasicTypes.DOUBLE).
	addScalar("description",StandardBasicTypes.STRING).
	addScalar("maintenace_type",StandardBasicTypes.STRING).
	addScalar("insert_date",StandardBasicTypes.DATE);
	return createSQLQuery.list();
    }


    @Override
    public List<Object[]> getSubscribers() {
	String sql = "select s.id,s.name,s.father_name,s.last_name, s.identifier, si.main_phone,v.name 'village',si.address "
		+ "from subscriber s,subscriber_information si,village v where s.id = si.id and si.id_village = v.id;";
	@SuppressWarnings("unchecked")
	NativeQuery<Object[]> createSQLQuery = getsession().createNativeQuery(sql);
	createSQLQuery.addScalar("id",StandardBasicTypes.LONG).
	addScalar("name",StandardBasicTypes.STRING).
	addScalar("father_name",StandardBasicTypes.STRING).
	addScalar("last_name",StandardBasicTypes.STRING).
	addScalar("identifier",StandardBasicTypes.STRING).
	addScalar("main_phone",StandardBasicTypes.STRING).
	addScalar("village",StandardBasicTypes.STRING).
	addScalar("address",StandardBasicTypes.STRING);
	return createSQLQuery.list();
    }


    @Override
    public List<Object[]> getSubscriptionsHistory(String  contractUniqueCode, String fromDate, String endDate, TransactionType transactionType) {
	
	String sql = SQLUtils.sql(transactionType==TransactionType.COUNTER_PAYMENT ? "contractCounterHistory.sql" :"contractHistory.sql");
	
	sql = MessageFormat.format(sql, contractUniqueCode, fromDate, endDate);
	return toList(sql);
    }
    
    @Override
    public List<Object[]> getActiveIshtirakInfo(List<Long> contractIds){
	String sql = "SELECT " + 
		"    c.id,\n" + 
		"    s.name,\n" + 
		"    s.last_name,\n" + 
		"    v.name 'Village',\n" + 
		"    si.main_phone,\n" + 
		"    c.contract_unique_code,\n" + 
		"    b.name 'Bundle',\n" + 
		"    e.name 'Engine'\n" + 
		"FROM\n" + 
		"    subscriber s,\n" + 
		"    village v,\n" + 
		"    contract c,\n" + 
		"    subscriber_information si,\n" + 
		"    bundle b,\n" + 
		"    engine e\n" + 
		"WHERE\n" + 
		"    s.id = c.subscriber_id AND s.id = si.id\n" + 
		"        AND si.id_village = v.id\n" + 
		"        AND c.bundle_id = b.id\n" + 
		"        AND c.engine_id = e.id\n" + 
		"        AND c.is_active\n" + 
		"";
	
	if(!contractIds.isEmpty()) {
	    sql+= " AND c.id in ({0})";
	    sql = MessageFormat.format(sql, contractIds.stream().map(e->String.valueOf(e)).collect(Collectors.joining(",")));
	}
	
	return toList(sql);
	
    }


    private List<Object[]> toList(String sql) {
	List<Map<String,Object>> rows = jdbcTemplate.queryForList(sql);
	return rows.stream().map(e->{
	    Object[] row = new Object[e.size()];
	    try(AtomicInteger counter = new AtomicInteger()){
	    e.entrySet().forEach(v->{
		row[counter.getAndIncrement()] = v.getValue();
	    });
	    }
	    return row;
	}).collect(Collectors.toList());
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getActiveContractWithoutReceipts() {
	String sql = "SELECT \n" + 
		"    id\n" + 
		"FROM\n" + 
		"    contract c\n" + 
		"WHERE\n" + 
		"    c.is_active = 1\n" + 
		"        AND id NOT IN (SELECT \n" + 
		"            id_contract\n" + 
		"        FROM\n" + 
		"            transaction t\n" + 
		"        WHERE\n" + 
		"            t.transaction_type <> 'SETTELMENT_FEES');";
	NativeQuery<Long> createSQLQuery = getsession().createNativeQuery(sql);
	createSQLQuery.addScalar("id",StandardBasicTypes.LONG);
	return createSQLQuery.list();
    }


    @Override
    public List<Object[]> getEmployeesPayments(Long employeeId, String fromDate, String endDate) {
	
	String file = employeeId!=null ? "EmployeeWithdrawalInfoSpecific.sql" : "EmployeeWithdrawalInfo.sql";
	String sql = SQLUtils.sql(file);
	
	if(employeeId!=null) {
	    sql = MessageFormat.format(sql, employeeId,"\""+fromDate+"\"", "\""+endDate+"\"");
	}else {
	    sql = MessageFormat.format(sql, "\""+fromDate+"\"", "\""+endDate+"\"");
	}
	return toList(sql);
    }
    
    @Override
    public List<Object[]> getCounterHistory(Long subscriberId, String fromDate, String endDate) {

	String sql = SQLUtils.sql("counterHistoryReport.sql");
	sql = MessageFormat.format(sql, subscriberId, "\"" + fromDate + "\"", "\"" + endDate + "\"");
	return toList(sql);
    }
    
    @Override
    public boolean hasCounterBundle(String contractUniqueCode) {
	String sql ="select count(bundle_id) from contract "
		+ "where contract_unique_code = ?  and bundle_id in \n" + 
		"(select id from bundle where type=\"SubscriptionBundle\");";
	
	int count = jdbcTemplate.queryForObject(sql, new Object[] { contractUniqueCode }, Integer.class);
	return count > 0;
    }
    
    @Override
    public List<Object[]> getContractHistoryPerContractOrALl(String uniqueContractId, String fromDate, String toDate, Boolean paid) {
	
	boolean isAllContract = StringUtils.isBlank(uniqueContractId);
	
	String sql = SQLUtils.sql(isAllContract ? "all_contract_history.sql" : "contract_history.sql");
	
	if(isAllContract) {
	    sql = MessageFormat.format(sql, fromDate,toDate);
	}else {
	    sql = MessageFormat.format(sql, uniqueContractId, fromDate, toDate);
	}
	
	if(paid!=null) {
	    sql+=" and is_paid = "+paid;
	}
	return toList(sql);
    }
    
    @Override
    public List<Object[]> getExpensesPerEngine(String engine, String fromDate, String toDate) {
	String sql = SQLUtils.sql("expenses_summary_per_engine.sql", fromDate, toDate);
	return toList(sql);
    }

    @Override
    public List<Object[]> getConsumptionPerEngine(String engine, String fromDate, String toDate) {
	String sql = SQLUtils.sql("consumption_summary_per_engine.sql", fromDate, toDate);
	return toList(sql);
    }

    @Override
    public List<Object[]> getIncomePerEngine(String engine, String fromDate, String toDate) {
	String sql = SQLUtils.sql("income_per_engine.sql", fromDate, toDate);
	return toList(sql);
    }

    @Override
    public List<Object[]> getExportedFiles(String date) {
	String sql = SQLUtils.sql("export.sql", date);
	return toList(sql);
    }
    
}
