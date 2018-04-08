SELECT 
    transaction_type "Transaction Type", sum(amount) total
FROM
    transaction
WHERE
    insert_date >= "{0}" 
        AND insert_date <= "{1}" 
        group by transaction_type 
        order by total desc;