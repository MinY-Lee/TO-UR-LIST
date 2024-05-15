LOAD CSV WITH HEADERS FROM 'file:///activities.csv' AS row
CREATE (a:ACTIVITY {activity: row})