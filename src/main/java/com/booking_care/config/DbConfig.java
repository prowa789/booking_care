package com.booking_care.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DbConfig {
    @Bean({"dsConfig"})
    @ConditionalOnProperty(prefix = "spring", name = {"datasource.jdbc-url"})
    @ConfigurationProperties("spring.datasource")
    public HikariConfig primaryConfig() {
        return new HikariConfig();
    }

    @Bean
    @Primary
    public HikariDataSource primaryDataSource(@Qualifier("dsConfig") HikariConfig dsConfig, StringEncryptor encryptor) throws InterruptedException {
        System.out.println(dsConfig.getPassword());
        String decryptPassword = encryptor.decrypt(dsConfig.getPassword());
        dsConfig.setPassword(decryptPassword);
        System.out.println(decryptPassword);
        HikariDataSource ds = new HikariDataSource(dsConfig);
        return ds;
    }
}
