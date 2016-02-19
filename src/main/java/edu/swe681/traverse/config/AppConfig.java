package edu.swe681.traverse.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("edu.swe681.traverse")
@EnableWebMvc
public class AppConfig {
}
