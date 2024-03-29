SELECT 
	t.id,
    c.contract_unique_code, 
    CONCAT(s.name, " ", s.last_name) "Full Name",
    t.amount,
    (CASE
    WHEN YEAR(t.insert_date) >=2022 THEN (select subscription_fees from subscription_history where subscription_history.transaction=t.id)
    ELSE null
	END) "subscription_fees",
    t.is_paid,
    t.transaction_type,
    b.name "Bundle",
    e.name "Engine",
    v.name,
    t.insert_date
    
FROM
    contract c,
    subscriber s,
    transaction t,
    bundle b,
    engine e,
    village v 
WHERE
	c.subscriber_id = s.id and
    t.id_contract = c.id and 
    b.id = c.bundle_id and 
    e.id = c.engine_id and
    v.id = c.id_village and
    t.insert_date >= "{0}" and
    t.insert_date <= "{1}"
    
    ORDER by  LENGTH(c.contract_unique_code),c.contract_unique_code,t.insert_date asc
    
    