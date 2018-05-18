
SELECT 
	ch.id,
    c.contract_unique_code,
    e.name "engine",
    ch.consumption,
    ch.insert_date
FROM
    contract c,
    counter_history ch,
    engine e
WHERE
    c.contract_unique_code = ch.contract_unique_code
        AND e.id = c.engine_id
        AND c.subscriber_id = {0}
         AND ch.insert_date >= "{1}"
        AND ch.insert_date <= "{2}"
        order by ch.insert_date desc;
