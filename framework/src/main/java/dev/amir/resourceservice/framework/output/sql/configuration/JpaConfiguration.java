package dev.amir.resourceservice.framework.output.sql.configuration;

import jakarta.persistence.SharedCacheMode;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
public class JpaConfiguration {

    @Value("${spring.jpa.packages-to-scan}")
    private String packagesToScan;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        var factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("PersistenceUnit");
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setPackagesToScan(packagesToScan);
        factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factoryBean.setJpaPropertyMap(jpaProperties());
        return factoryBean;
    }

    private Map<String, Object> jpaProperties() {
        var properties = new HashMap<String, Object>();
        properties.put("jakarta.persistence.sharedCache.mode", SharedCacheMode.ENABLE_SELECTIVE);
        return properties;
    }
}
