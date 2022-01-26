SELECT 
    e.name "Engine", t.transaction_type "Transaction Type", SUM(subscription_fees) total
FROM
    subscription_history sh,
    transaction t,
    contract c,
    engine e
WHERE
    sh.transaction = t.id
        AND t.id_contract = c.id
        AND e.id = c.engine_id
        AND t.insert_date >= "{0}"
        AND t.insert_date <= "{1}"
GROUP BY e.name
