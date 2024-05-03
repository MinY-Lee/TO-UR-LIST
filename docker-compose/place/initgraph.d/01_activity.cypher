LOAD CSV WITH HEADERS FROM 'activities.csv' AS row
CREATE (a:ACTIVITY {activity: row})