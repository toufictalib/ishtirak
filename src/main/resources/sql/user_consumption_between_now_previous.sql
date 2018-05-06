SELECT 
    c1.contract_unique_code AS 'contractUniqueCode',
    c1.consumption AS 'previousCounterValue',
    c2.consumption AS 'currentCounterValue'
FROM
    counter_history c1,
    counter_history c2
WHERE
    c1.contract_unique_code = c2.contract_unique_code
        AND (c1.insert_date BETWEEN "{0}" AND "{1}")
        AND (c2.insert_date BETWEEN "{2}" AND "{3}")