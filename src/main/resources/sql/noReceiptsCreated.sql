SELECT 
    id
FROM
    contract c
WHERE
    c.is_active = 1
        AND id NOT IN (SELECT 
            id_contract
        FROM
            transaction t
        WHERE
            t.transaction_type <> 'SETTELMENT_FEES');