package edu.swe681.traverse.application.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	DataSource dataSource;

	private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.passwordEncoder(encoder)
			.usersByUsernameQuery("SELECT username,password_hash,enabled FROM users WHERE username=?")
			.authoritiesByUsernameQuery("SELECT username, role FROM user_roles WHERE username=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests() // authorize http requests
				.antMatchers("/register.jsp", "/api/register", "/js/**", "/css/**", "/api/ping").permitAll()
				.anyRequest().authenticated() // anything not matching the above patterns requires authentication
				.and()
			.formLogin().and() // enable form login
			.httpBasic(); // enable basic http login
		
		http
			.sessionManagement()
				.maximumSessions(1).and() // only 1 session per user
				.enableSessionUrlRewriting(false); // do not write sessionids into URLs
	}
}
