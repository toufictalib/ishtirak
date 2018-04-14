SELECT 
    c.contract_unique_code, concat(s.name, " ",s.last_name) "Full Name",""
FROM
    contract c, village v, subscriber s
WHERE
	c.id_village = v.id and
    c.subscriber_id = s.id and
    c.is_active = TRUE
        AND c.insert_date < "{0}"
        order by v.order_index;
        ;
;