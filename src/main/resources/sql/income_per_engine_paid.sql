SELECT 
    e.name "Engine", sum(amount) total
FROM
    transaction t,
    contract c,
    engine e
WHERE
	t.id_contract = c.id AND
    c.engine_id = e.id and 
    t.insert_date >= "{0}" 
        AND t.insert_date <= "{1}"
        AND t.is_paid = 1
        group by e.id 
        order by total desc;