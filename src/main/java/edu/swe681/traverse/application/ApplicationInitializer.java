package edu.swe681.traverse.application;

import java.util.EnumSet;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import edu.swe681.traverse.application.config.RootConfiguration;
import edu.swe681.traverse.application.config.WebMvcConfiguration;

public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// this will prevent URLs from ever showing the JSESSIONID, they only go in cookies
		servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
		super.onStartup(servletContext);
	}
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { RootConfiguration.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { WebMvcConfiguration.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected Filter[] getServletFilters() {
		return new Filter[] { new HiddenHttpMethodFilter() };
	}
}
