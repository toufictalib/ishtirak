SELECT 
    t.id,
    t.amount total,
    b.name 'bundle',
    t.transaction_type,
    t.insert_date
FROM
    transaction t,
     contract c, bundle b 
WHERE
    c.contract_unique_code = "{0}" 
    AND c.bundle_id = b.id
    AND t.id_contract = c.id
        AND t.insert_date >= "{1}"
        AND t.insert_date <= "{2}"