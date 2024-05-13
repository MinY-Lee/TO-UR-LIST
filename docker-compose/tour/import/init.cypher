LOAD CSV WITH HEADERS FROM 'country_info_final_updated.csv' AS row
CREATE (:COUNTRY {countryCode: row.countryCode, countryName: row.countryNameKor})