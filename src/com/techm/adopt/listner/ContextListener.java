package com.techm.adopt.listner;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

public class ContextListener implements ServletContextListener {

	/**
	 * Initialize log4j when the application is being started
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		String log4jConfigFile = context
				.getInitParameter("log4j-config-location");
		String fullPath = context.getRealPath("") + File.separator
				+ log4jConfigFile;

		PropertyConfigurator.configure(fullPath);
		System.setProperty("rootPath", context.getRealPath(""));
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}
}