SELECT 
    t.id,
    t.amount total,
    b.name 'bundle',
    t.transaction_type,
    sh.previous_counter,
    sh.current_counter,
    sh.consumption,
    sh.cost_per_kb,
    sh.consumption * sh.cost_per_kb subtotal,
    sh.subscription_fees,
    t.insert_date
FROM
    transaction t 
        LEFT JOIN
    subscription_history sh ON sh.transaction = t.id,
     contract c, bundle b 
WHERE
    c.contract_unique_code = "{0}" 
    AND c.bundle_id = b.id
    AND t.id_contract = c.id
        AND t.insert_date >= "{1}"
        AND t.insert_date <= "{2}"