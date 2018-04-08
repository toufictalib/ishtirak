SELECT 
    maintenace_type "Maintenance Type", sum(amount) total
FROM
    expenses_log
WHERE
    insert_date >= "{0}" 
        AND insert_date <= "{1}" 
        group by maintenace_type 
        order by total desc;