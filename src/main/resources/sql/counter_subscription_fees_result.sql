SELECT 
    sum(subscription_fees)
FROM
    subscription_history
WHERE
    insert_date >= "{0}" 
	AND insert_date <= "{1}" 
	AND YEAR(insert_date) >=2022