package com.eminyidle.tour.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.eminyidle.tour.repository.maria",transactionManagerRef = "mariaTransactionManager")
@EnableTransactionManagement
public class MariaConfig {
    @Bean(name = "maria")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource(){
        return DataSourceBuilder.create().driverClassName("org.mariadb.jdbc.Driver").build();
    }


    @Bean(name = "mariaTransactionManager")
    public JpaTransactionManager mariaTransactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean){
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }

}
