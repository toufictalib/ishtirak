SELECT 
    e.name "Engine",el.maintenace_type,sum(el.amount) "subTotal"
FROM
    expenses_log el left join engine e on
    el.engine = e.id
WHERE
	
    el.insert_date >= "{0}"
        AND el.insert_date <= "{1}"
        
        group by el.engine,el.maintenace_type
;