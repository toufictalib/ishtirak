SELECT 
    c.id,
    s.name,
    s.last_name,
    v.name 'Village',
    si.main_phone,
    c.counter_id,
    b.name 'Bundle',
    e.name 'Engine'
FROM
    subscriber s,
    village v,
    contract c,
    subscriber_information si,
    bundle b,
    engine e
WHERE
    s.id = c.subscriber_id AND s.id = si.id
        AND si.id_village = v.id
        AND c.bundle_id = b.id 
        AND c.engine_id = e.id
        AND c.is_active
        AND c.id in (:contractIds)
        ;
