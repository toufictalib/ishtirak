SELECT 
    c.contract_unique_code, s.name,(SELECT 
    consumption
FROM
    counter_history
WHERE
    contract_unique_code = c.contract_unique_code
        AND insert_date < "{0}"
ORDER BY insert_date DESC
LIMIT 1) as 'Old Counter', (SELECT 
    consumption
FROM
    counter_history
WHERE
    contract_unique_code = c.contract_unique_code
        AND insert_date >= "{0}"
        AND insert_date <= "{1}"
ORDER BY insert_date DESC
LIMIT 1) as 'Current Counter'
FROM
    contract c,
    village v,
    subscriber s
WHERE
    c.id_village = v.id
        AND c.subscriber_id = s.id
        AND c.is_active = TRUE
        AND c.insert_date <= "{1}"
ORDER BY v.order_index , LENGTH(contract_unique_code),c.contract_unique_code ;
;