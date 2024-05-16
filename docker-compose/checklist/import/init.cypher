LOAD CSV WITH HEADERS FROM 'file:///country.csv' AS row
MERGE (:COUNTRY {countryCode: row.countryCode, countryName: row.countryName});

LOAD CSV WITH HEADERS FROM 'file:///activity.csv' AS row
MERGE (:ACTIVITY {activity: row.activity});

LOAD CSV WITH HEADERS FROM 'file:///activity_item.csv' AS row
MATCH (a:ACTIVITY {activity: row.activity})
MERGE (i:ITEM {item:row.item})
MERGE (a)-[:NEED]->(i);

MERGE (:ACTIVITY {activity: ""});

LOAD CSV WITH HEADERS FROM 'file:///country_item.csv' AS row
MATCH (c:COUNTRY {countryCode: row.countryCode})
MERGE (i:ITEM {item:row.item})
MERGE (c)-[:NEED]->(i);

MERGE (:COMMON);

LOAD CSV WITH HEADERS FROM 'file:///common_item.csv' AS row
MATCH (c:COMMON)
MERGE (i:ITEM {item:row.item})
MERGE (c)-[:NEED]->(i);