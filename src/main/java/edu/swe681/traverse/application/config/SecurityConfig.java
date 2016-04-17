package edu.swe681.traverse.application.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	DataSource dataSource;
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		
	  auth.jdbcAuthentication()
	  	.dataSource(dataSource)
	  	.passwordEncoder(passwordEncoder())
		.usersByUsernameQuery(
			"SELECT username,password_hash,enabled FROM users WHERE username=?")
		.authoritiesByUsernameQuery(
			"SELECT username, role FROM user_roles WHERE username=?");
	}
	
	//TODO: Could theoretically move this to RootConfiguration if we really want to? I'm still getting
	// the hang of how AutoWired functions, so maybe that wouldn't work..
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		// TODO: Need to pick a strength we think is good. I read somewhere
		// that 12 is the least we'd want to go, though I've seen 10 in a lot of places
		// too...
		PasswordEncoder encoder = new BCryptPasswordEncoder(12);
		return encoder;
	}
}
