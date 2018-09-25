SELECT 
    c.id,
    c.contract_unique_code,
    s.name,
    s.identifier,
    c.order_index
FROM
    contract c,
    subscriber s
WHERE
    c.subscriber_id = s.id
        AND c.closed_date IS NULL
        AND c.id_village = {0}
        order by c.order_index;