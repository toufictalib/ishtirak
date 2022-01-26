SELECT 
    e.name, SUM(consumption)
FROM
    subscription_history sh,
    transaction tr,
    contract c,
    engine e
WHERE
    sh.transaction = tr.id
        AND tr.id_contract = c.id
        AND e.id = c.engine_id
        AND tr.insert_date >= "{0}"
        AND tr.insert_date <= "{1}"
GROUP BY e.name