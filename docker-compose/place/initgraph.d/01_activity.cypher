LOAD CSV WITH HEADERS FROM 'file:///activities.csv' AS row
MERGE (:ACTIVITY {activity: row.activity})