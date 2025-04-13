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
// указываем где находится пропертис файл с настройками БД
@PropertySource("classpath:configMySql.properties")
// включаем Spring управление транзакциями
@EnableTransactionManagement
// указываем сканируемую область со всеми bean
@ComponentScan("pak")
public class HibernateConfig {
    // подключаем класс для чтения пропертис файла
    private final Environment env;

    public HibernateConfig(Environment env) {
        this.env = env;
    }
    // bean с настройками подключения БД
    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource driverMDS = new DriverManagerDataSource();
        driverMDS.setDriverClassName(Objects.requireNonNull(env.getProperty("db.driver")));
        driverMDS.setUrl(env.getProperty("db.url"));
        driverMDS.setUsername(env.getProperty("db.username"));
        driverMDS.setPassword(env.getProperty("db.password"));
        return driverMDS;
    }
    // получения bean LocalContainerEntityManagerFactoryBean
    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean() {
        // получаем LocalContainerEntityManagerFactoryBean
        LocalContainerEntityManagerFactoryBean entityManager =
                new LocalContainerEntityManagerFactoryBean();
        // загружаем в entityManager метод getDataSource с DataSource
        // с настройками доступа для БД
        entityManager.setDataSource(getDataSource());
        // указываем в каком месте хранятся модели БД
        entityManager.setPackagesToScan("pak/entity");
        // это установка JPA-провайдера для entityManager в данном случае
        // загружается родной JPA адаптер Hibernate
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Создаем пропертис файл для добавления пропертей
        Properties prop = new Properties();
        // отображение команд SQL в командной строке
        prop.put("hibernate.show.sql", env.getProperty("hibernate.show.sql"));
        // проверка на создание таблиц в БД (авто)
        prop.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        //используется для указания диалекта Hibernate,
        // который должен использоваться при создании SessionFactory
        prop.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        //  устанавливает кодировку символов для соединения с базой данных
        prop.put("hibernate.connection.characterEncoding",
                env.getProperty("hibernate.connection.characterEncoding"));
        // является настройкой для Hibernate, которая устанавливает набор символов
        // (charset) для соединения с базой данных.
        prop.put("hibernate.connection.CharSet",
                env.getProperty("hibernate.connection.CharSet"));
        // использование юникода
        prop.put("hibernate.connection.useUnicode",
                env.getProperty("hibernate.connection.useUnicode"));
        // загрузка пропертей в entityManager
        entityManager.setJpaProperties(prop);
        return entityManager;
    }

    //создает бин PlatformTransactionManager, который управляет транзакциями базы данных
    @Bean
    public PlatformTransactionManager getTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(getEntityManagerFactoryBean().getObject());
        return jpaTransactionManager;
    }
}