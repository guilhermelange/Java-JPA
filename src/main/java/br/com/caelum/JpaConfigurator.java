package br.com.caelum;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.stat.Statistics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
public class JpaConfigurator {

	@Bean
//	public DataSource getDataSource() {
	public DataSource getDataSource() throws PropertyVetoException {
//	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
		ComboPooledDataSource dataSource = new ComboPooledDataSource();

//	    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//	    dataSource.setUrl("jdbc:mysql://localhost/projeto_jpa?useTimezone=true&serverTimezone=America/Sao_Paulo");
//	    dataSource.setUsername("root");
//	    dataSource.setPassword("root");
	    
	    dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
	    dataSource.setJdbcUrl("jdbc:mysql://localhost/projeto_jpa?useTimezone=true&serverTimezone=America/Sao_Paulo");
	    dataSource.setUser("root");
	    dataSource.setPassword("root");
	    
	    //Padrão é 3
	    dataSource.setMinPoolSize(5);
	    
	    //Padrão é 15
	    dataSource.setMaxPoolSize(15);
	    dataSource.setNumHelperThreads(5);
	    
	    dataSource.setIdleConnectionTestPeriod(1); //a cada um segundo testamos as conexões ociosas

	    return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setPackagesToScan("br.com.caelum");
		entityManagerFactory.setDataSource(dataSource);

		entityManagerFactory
				.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		Properties props = new Properties();

		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		//Habilita a geração de estatísticas
		props.setProperty("hibernate.generate_statistics", "true");
		// Habilita o cache de segundo nível para melhoria de performance.
		props.setProperty("hibernate.cache.use_second_level_cache", "true");
		props.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
		props.setProperty("hibernate.cache.use_query_cache", "true");
       
		entityManagerFactory.setJpaProperties(props);
		return entityManagerFactory;
	}

	@Bean
	public JpaTransactionManager getTransactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}
	
	@Bean
	public Statistics statistics(EntityManagerFactory emf) { 
	    org.hibernate.SessionFactory factory = emf.unwrap(org.hibernate.SessionFactory.class);
	    return factory.getStatistics();
	}

}
