SELECT 
    c.contract_unique_code, CONCAT(s.name, "  ", s.last_name) "Full Name",
    t.amount,
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
    c.contract_unique_code = "{0}" and
    t.insert_date >= "{1}" and
    t.insert_date <= "{2}"
    
    
    