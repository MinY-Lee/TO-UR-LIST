create table if not exists country
(
    country_code char(3) not null primary key,
    country_name_eng varchar(50) not null,
    country_name_kor varchar(50) not null,
    climate varchar(150),
    language varchar(20),
    utc double,
    voltage varchar(20),
    plug_type varchar(20)
);

create table if not exists  city
(
    city_id       int  not null auto_increment primary key,
    country_code  char(3)  not null,
    city_name_eng varchar(50) not null,
    city_name_kor varchar(50) not null,
    FOREIGN KEY (country_code) REFERENCES country(country_code)
);

create table if not exists country_currency
(
    country_code  char(3)  not null,
    currency_code varchar(3)  not null,
    currency_sign varchar(5)  not null,
    FOREIGN KEY (country_code) REFERENCES country(country_code)
);


# load data infile '/docker-entrypoint-initdb.d/city_info_final.csv'
#     into table city
#     fields terminated by ','
#     lines terminated by '\n'
#     ignore 1 rows;

# import Local infile
# LOAD DATA LOCAL INFILE 'C:/SSAFY/02_workspace/S10P31A609/docker-compose/local/auth/initdb.d/country_info_final_updated.csv'
#     INTO TABLE country
#     FIELDS
#     TERMINATED BY ','
#     OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"'
#     LINES
#     TERMINATED BY '\n'
#     STARTING BY ''
#     ignore 1 rows;
# LOAD DATA LOCAL INFILE 'C:/SSAFY/02_workspace/S10P31A609/docker-compose/local/auth/initdb.d/city_info_final.csv'
#     INTO TABLE city
#     FIELDS
#     TERMINATED BY ','
#     OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"'
#     LINES
#     TERMINATED BY '\n'
#     STARTING BY ''
#     ignore 1 rows;
# LOAD DATA LOCAL INFILE 'C:/SSAFY/02_workspace/S10P31A609/docker-compose/local/auth/initdb.d/currency_info_final.csv'
#     into table country_currency
#     FIELDS
#     TERMINATED BY ','
#     OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"'
#     LINES
#     TERMINATED BY '\n'
#     STARTING BY ''
#     ignore 1 rows;