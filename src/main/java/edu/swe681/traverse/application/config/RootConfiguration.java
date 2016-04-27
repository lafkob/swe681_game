package edu.swe681.traverse.application.config;

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan
@EnableScheduling
public class RootConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(RootConfiguration.class);
	
	private final static String JNDI_DATASOURCE = "java:comp/env/jdbc/traverse";

	// This serves to create a datasource bean managed by spring that we can use anywhere
	// default scope is singleton, so there should be only one instance ever
	@Bean
	public DataSource dataSource() throws SQLException {
		DataSource dataSource = null;
        JndiTemplate jndi = new JndiTemplate();
        try {
            dataSource = (DataSource) jndi.lookup(JNDI_DATASOURCE);
            LOG.info("Created DataSource from jndi definition: " + JNDI_DATASOURCE);
        } catch (NamingException e) {
            LOG.error("NamingException for " + JNDI_DATASOURCE, e);
        }
        return dataSource;
	}
}