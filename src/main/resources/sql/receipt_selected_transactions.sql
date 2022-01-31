SELECT 
	t.id,
    c.contract_unique_code, 
    s.name "Full Name",
    t.amount,
    t.is_paid,
    t.transaction_type,
    t.selected_currency,
    b.name "Bundle",
    e.name "Engine",
    v.name "Village",
    t.insert_date "Transaction Date",
    sh.current_counter,
    sh.previous_counter,
    sh.subscription_fees,
    c.address
    
     
FROM
    contract c,
    subscriber s,
    transaction t left join subscription_history sh on sh.transaction = t.id,
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
    t.insert_date <= "{1}" and
    t.transaction_type in("MONTHLY_PAYMENT","COUNTER_PAYMENT")
    and t.id in ({2})
    and c.is_active = 1
    ORDER by LENGTH(c.contract_unique_code),c.contract_unique_code
    