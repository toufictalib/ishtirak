SELECT 
    t.id, c.contract_unique_code
FROM
    transaction t,
    contract c
WHERE
    t.id_contract = c.id
        AND t.insert_date >= "{0}"
        AND t.insert_date <= "{1}"
        AND c.is_active = 1
        AND t.is_paid != 1
        AND transaction_type <> "SETTELMENT_FEES"
