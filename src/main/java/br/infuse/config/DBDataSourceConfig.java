package br.infuse.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "lyceumEntityManagerFactory",
        transactionManagerRef = "lyceumTransactionManager",
        basePackages = {"br.infuse.core.repository.lyceum"})
public class DBDataSourceConfig {
    @Bean(name = "lyceumDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource-lyceum")
    public DataSourceProperties lyceumDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "lyceumDataSource")
    @ConfigurationProperties(prefix = "spring.datasource-lyceum.configuration")
    public DataSource lyceumDataSource(@Qualifier("lyceumDataSourceProperties") DataSourceProperties lyceumDataSourceProperties) {
        return lyceumDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "lyceumEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean lyceumEntityManagerFactory(
            EntityManagerFactoryBuilder lyceumEntityManagerFactoryBuilder, @Qualifier("lyceumDataSource") DataSource lyceumDataSource) {

        Map<String, String> lyceumJpaProperties = new HashMap<>();
        lyceumJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");

        return lyceumEntityManagerFactoryBuilder
                .dataSource(lyceumDataSource)
                .packages("br.infuse.core.model.lyceum")
                .persistenceUnit("lyceumDataSource")
                .properties(lyceumJpaProperties)
                .build();
    }

    @Bean(name = "lyceumTransactionManager")
    public PlatformTransactionManager lyceumTransactionManager(
            @Qualifier("lyceumEntityManagerFactory") EntityManagerFactory lyceumEntityManagerFactory) {

        return new JpaTransactionManager(lyceumEntityManagerFactory);
    }
}