package com.aizong.ishtirak.dao;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.aizong.ishtirak.bean.TransactionType;
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
    public List<Object[]> getExpenses(Date startDate, Date endDate) {
	String sql = "select m.id, e.name 'engine', m.amount, m.description,m.maintenace_type,m.insert_date "
		+ "from expenses_log m "
		+ "left join engine e on e.id = m.engine "
		+ "where m.insert_date >=:startDate and m.insert_date <=:endDate order by m.insert_date desc";
	NativeQuery<Object[]> createSQLQuery = getsession().createSQLQuery(sql);
	createSQLQuery.setParameter("startDate",startDate).setParameter("endDate", endDate);
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
    public List<Object[]> getSubscriptionsHistory(Long contractId, String fromDate, String endDate, TransactionType transactionType) {
	String sql="SELECT " + 
		"    t.id," + 
		"    t.amount 'total'," + 
		"    t.transaction_type," + 
		"    sh.consumption," + 
		"    sh.cost_per_kb," + 
		"    sh.consumption * sh.cost_per_kb 'subtotal'," + 
		"    sh.subscription_fees," + 
		"     t.insert_date "+ 
		"FROM" + 
		"    transaction t" + 
		"        LEFT JOIN" + 
		"    subscription_history sh ON sh.transaction = t.id " + 
		"WHERE" + 
		"    t.id_contract = {2} " + 
		"        AND t.insert_date >= \"{0}\"" + 
		"        AND t.insert_date <= \"{1}\";";
	
	String sql1="SELECT " + 
		"    t.id," + 
		"    t.amount 'total'," + 
		"    t.transaction_type," + 
		"     t.insert_date " + 
		"FROM" + 
		"    transaction t WHERE" + 
		"    t.id_contract = {2} " + 
		"        AND t.insert_date >= \"{0}\"" + 
		"        AND t.insert_date <= \"{1}\";";
	
	sql = MessageFormat.format(sql, fromDate, endDate, contractId);
	sql1 = MessageFormat.format(sql1, fromDate, endDate, contractId);
	List<Map<String,Object>> rows = jdbcTemplate.queryForList(transactionType==TransactionType.COUNTER_PAYMENT ? sql :sql1);
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

}
