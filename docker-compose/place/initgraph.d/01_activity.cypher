LOAD CSV WITH HEADERS FROM 'file:///activities.csv' AS row
CREATE (:ACTIVITY {activity: row.activity})