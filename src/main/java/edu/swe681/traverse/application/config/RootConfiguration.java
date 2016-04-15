package edu.swe681.traverse.application.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;

@Configuration
@ComponentScan
public class RootConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(RootConfiguration.class);
	
	private final static String JNDI_DATASOURCE = "java:comp/env/jdbc/traverse";

	@Bean
	public DataSource datasource() throws SQLException {
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