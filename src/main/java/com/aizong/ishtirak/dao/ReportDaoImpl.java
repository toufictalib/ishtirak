package com.aizong.ishtirak.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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


    @Override
    public List<Object[]> getTransactions(Date startDate, Date endDate) {
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

}
