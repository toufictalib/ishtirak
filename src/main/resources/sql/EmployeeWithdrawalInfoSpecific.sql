SELECT 
    o.id,
    et.name 'job',
    e.name,
    e.last_name,
    o.amount,
    o.insert_date
FROM
    employee e,
    employee_type et,
    out_expenses_log o
WHERE
    et.id = e.employee_type_id
        AND e.id = o.employee 
        AND o.insert_date >= {1}
        AND o.insert_date <= {2}
        AND e.id = {0}