/*
 *     © Petshop.ru
 *  All Rights Reserved.
 */
package ru.petshop.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;
import ru.petshop.configuration.properties.C3p0properties;

import javax.annotation.PostConstruct;
import java.beans.PropertyVetoException;

@Configuration
@Log4j2
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@EnableConfigurationProperties({DataSourceProperties.class})
@MapperScan(basePackages = {"ru.petshop.mapper"},
        sqlSessionFactoryRef = "sqlSessionFactory")
public class MyBatisConfiguration {

    private static final String LOG_TAG = "[MYBATIS_CONFIGURATION] ::";
    private static final String DATA_SOURCE_LOG_TAG = "[DATA_SOURCE_CONFIGURATION] ::";

    /**
     * {@link C3p0properties}
     */
    private final C3p0properties dataSourceProperties;

    /**
     * @return {@link ComboPooledDataSource}
     */
    @Bean
    public ComboPooledDataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setMaxPoolSize(dataSourceProperties.getMaxPoolSize());
        comboPooledDataSource.setDriverClass(dataSourceProperties.getDriverClassName());
        comboPooledDataSource.setJdbcUrl(dataSourceProperties.getUrl());
        comboPooledDataSource.setDriverClass(dataSourceProperties.getDriverClassName());
        comboPooledDataSource.setPassword(dataSourceProperties.getPassword());
        comboPooledDataSource.setUser(dataSourceProperties.getUsername());
        comboPooledDataSource.setDataSourceName(dataSourceProperties.getDataSourceName());
        return comboPooledDataSource;
    }

    /**
     * {@link SqlSessionFactoryBean}
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/*.xml"));
        return sessionFactory;
    }

    @PostConstruct
    public void init() {
        validateProperties();
        if (log.isInfoEnabled()) {
            log.info(
                    "{} has been initialized.",
                    LOG_TAG
            );
            log.info(
                    "{} URL - {}, User - {}",
                    DATA_SOURCE_LOG_TAG,
                    dataSourceProperties.getUrl(),
                    dataSourceProperties.getUsername()
            );
        }
    }

    private void validateProperties() {
        // DataSource
        Assert.notNull(dataSourceProperties.getUrl(), "url should be not null");
        Assert.notNull(dataSourceProperties.getDriverClassName(), "driverClassName should be not null");
        Assert.notNull(dataSourceProperties.getUsername(), "username should be not null");
        Assert.notNull(dataSourceProperties.getPassword(), "password should be not null");
    }
}
