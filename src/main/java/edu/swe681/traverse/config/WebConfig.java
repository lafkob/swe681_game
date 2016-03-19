package edu.swe681.traverse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Web-specific configuration, probably some overlap with AppConfig but trying
 * to spread out the configuration in a logical organization.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	/**
	 * Configures the app so / maps to index.html.
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
	}
	
	/**
	 * Handles serving up static resources (non-dynamic pages)
	 * Source:
	 * https://stackoverflow.com/questions/14299149/how-to-use-spring-mvcs-mvcresources-tag-in-a-java-application-context
	 * 
	 * equivalents for <mvc:resources/> tags
	 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/img/**").addResourceLocations("/img/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/*.html*").addResourceLocations("/");
    }

    // TODO: not sure this is needed
	// Source:
	// https://stackoverflow.com/questions/14299149/how-to-use-spring-mvcs-mvcresources-tag-in-a-java-application-context
    // equivalent for <mvc:default-servlet-handler/> tag
//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
}
