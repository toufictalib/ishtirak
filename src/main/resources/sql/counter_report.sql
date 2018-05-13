SELECT 
    c.contract_unique_code, s.name, ch.consumption
FROM
    contract c
        LEFT JOIN
    counter_history ch ON c.contract_unique_code = ch.contract_unique_code,
    village v,
    subscriber s
WHERE
    c.id_village = v.id
        AND c.subscriber_id = s.id
        AND c.is_active = TRUE
        AND ch.insert_date >= "{0}"
        AND ch.insert_date <= "{1}"
ORDER BY v.order_index , LENGTH(c.contract_unique_code) , c.contract_unique_code;
;