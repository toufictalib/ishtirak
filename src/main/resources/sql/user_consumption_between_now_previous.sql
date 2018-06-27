SELECT 
    ch.contract_unique_code 'contract_unique_code', ch.consumption 'previous_counter', ch2.consumption 'current_counter'
FROM
    counter_history ch
        LEFT JOIN
    counter_history ch2 ON ch.contract_unique_code = ch2.contract_unique_code
WHERE
    ch.insert_date = (SELECT 
            MAX(insert_date)
        FROM
            counter_history
        WHERE
            contract_unique_code = ch.contract_unique_code
                AND insert_date < "{0}"
        LIMIT 1)
        AND ch2.insert_date >= "{0}"
        AND ch2.insert_date <= "{1}"

     
        