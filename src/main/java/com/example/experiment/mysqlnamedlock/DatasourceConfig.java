package com.example.experiment.mysqlnamedlock;

import com.example.experiment.mysqlnamedlock.repository.LockRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {

    @Primary
    @ConfigurationProperties("spring.datasource.main")
    @Bean
    public DataSource mainDatasource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @ConfigurationProperties("spring.datasource.lock")
    @Bean
    public DataSource lockDatasource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @ConfigurationProperties("spring.datasource.lock-with-aop")
    @Bean
    public DataSource lockWithAopDatasource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public LockRepository lockRepository() {
        return new LockRepository(lockDatasource());
    }

    @Bean
    public NamedLockAop namedLockAop() {
        return new NamedLockAop(lockWithAopDatasource());
    }
}
