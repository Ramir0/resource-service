package dev.amir.resourceservice.framework.output.sql.configuration;

import dev.amir.resourceservice.domain.profile.Profiles;
import jakarta.persistence.SharedCacheMode;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Profile("!" + Profiles.TEST)
@Configuration
public class JpaConfiguration {

    @Value("${spring.jpa.packages-to-scan}")
    private String packagesToScan;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        var entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPersistenceUnitName("PersistenceUnit");
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setPackagesToScan(packagesToScan);
        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactory.setJpaPropertyMap(jpaProperties());
        return entityManagerFactory;
    }

    private Map<String, Object> jpaProperties() {
        var properties = new HashMap<String, Object>();
        properties.put("jakarta.persistence.sharedCache.mode", SharedCacheMode.ENABLE_SELECTIVE);
        return properties;
    }
}
