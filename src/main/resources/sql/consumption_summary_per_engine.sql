SELECT 
    e.name "Engine",
    el.maintenace_type,
    SUM(CASE
        WHEN el.maintenace_type = "DIESEL" THEN el.diesel_consumption
        ELSE el.oil_consumption
    END) "subTotal"
FROM
    expenses_log el left join engine e on
    el.engine = e.id
WHERE
    el.insert_date >= "{0}"
        AND el.insert_date <= "{1}"
        AND el.maintenace_type in ("FILTER_OIL_CHANGING","DIESEL")
GROUP BY el.engine , el.maintenace_type
;