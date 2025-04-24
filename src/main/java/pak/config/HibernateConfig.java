package pak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@PropertySource("classpath:configMySql.properties")
@EnableTransactionManagement
@ComponentScan("pak")
public class HibernateConfig {

    private final Environment env;

    public HibernateConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource driverMDS = new DriverManagerDataSource();
        driverMDS.setDriverClassName(Objects.requireNonNull(env.getProperty("db.driver")));
        driverMDS.setUrl(env.getProperty("db.url"));
        driverMDS.setUsername(env.getProperty("db.username"));
        driverMDS.setPassword(env.getProperty("db.password"));
        return driverMDS;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean() {

        LocalContainerEntityManagerFactoryBean entityManager =
                new LocalContainerEntityManagerFactoryBean();

        entityManager.setDataSource(getDataSource());
        entityManager.setPackagesToScan("pak/entity");
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties prop = new Properties();
        prop.put("hibernate.show.sql", env.getProperty("hibernate.show.sql"));
        prop.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        prop.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        prop.put("hibernate.connection.characterEncoding",
                env.getProperty("hibernate.connection.characterEncoding"));
        prop.put("hibernate.connection.CharSet",
                env.getProperty("hibernate.connection.CharSet"));
        prop.put("hibernate.connection.useUnicode",
                env.getProperty("hibernate.connection.useUnicode"));
        entityManager.setJpaProperties(prop);
        return entityManager;
    }

    @Bean
    public PlatformTransactionManager getTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(getEntityManagerFactoryBean().getObject());
        return jpaTransactionManager;
    }
}