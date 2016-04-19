package edu.swe681.traverse.application.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
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
			.authorizeRequests()
				.antMatchers("/register.jsp", "/api/register", "/js/**", "/css/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin().and()
			.httpBasic();
	}
}
