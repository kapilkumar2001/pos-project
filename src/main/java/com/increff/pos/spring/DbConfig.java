package com.increff.pos.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DbConfig {

	public static final String PACKAGE_POJO = "com.increff.pos.pojo";

	@Autowired
	private ApplicationProperties properties;
	
	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		BasicDataSource bean = new BasicDataSource();
		bean.setDriverClassName(properties.getJdbcDriver());
		bean.setUrl(properties.getJdbcUrl());
		bean.setUsername(properties.getJdbcUsername());
		bean.setPassword(properties.getJdbcPassword());
		bean.setInitialSize(2);
		bean.setDefaultAutoCommit(false);
		bean.setMinIdle(2);
		bean.setValidationQuery("Select 1");
		bean.setTestWhileIdle(true);
		bean.setTimeBetweenEvictionRunsMillis(10 * 60 * 100);
		return bean;
	}

	@Bean(name = "entityManagerFactory")
	@Autowired
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(dataSource);
		bean.setPackagesToScan(PACKAGE_POJO);
		HibernateJpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
		bean.setJpaVendorAdapter(jpaAdapter);
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", properties.getHibernateDialect());
		jpaProperties.put("hibernate.show_sql", properties.getHibernateShowSql());
		jpaProperties.put("hibernate.hbm2ddl.auto", properties.getHibernateHbm2ddl());
		jpaProperties.put("hibernate.hbm2ddl.auto", properties.getHibernateHbm2ddl());
		bean.setJpaProperties(jpaProperties);
		return bean;
	}
	
	@Bean(name = "transactionManager")
	@Autowired
	public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
		JpaTransactionManager bean = new JpaTransactionManager();
		bean.setEntityManagerFactory(emf.getObject());
		return bean;
	}
}
