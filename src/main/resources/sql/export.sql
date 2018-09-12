SELECT 
    c.contract_unique_code, s.name, ""
FROM
    contract c,
    village v,
    subscriber s
WHERE
    c.id_village = v.id
        AND c.subscriber_id = s.id
        AND c.is_active = TRUE
        AND c.insert_date < "{0}"
ORDER BY v.order_index ,c.order_index, LENGTH(contract_unique_code),c.contract_unique_code ;
;