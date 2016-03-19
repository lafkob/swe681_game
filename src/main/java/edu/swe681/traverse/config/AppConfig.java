package edu.swe681.traverse.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Application Configuration, handles some stuff via spring annotation configuration.
 * Mostly covers the component scanning.
 */
@Configuration
@ComponentScan("edu.swe681.traverse")
@EnableWebMvc
public class AppConfig {
}
