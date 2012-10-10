package com.zenika.osgi.console.internal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
	private BundleContext bundleContext;
	private ServiceTracker<HttpService, HttpService> serviceTracker;

	public void start(BundleContext bundleContext) throws Exception {	
		this.bundleContext = bundleContext;
		
		serviceTracker = new ServiceTracker<HttpService, HttpService>(bundleContext, HttpService.class, null) {
			@Override
			public HttpService addingService(ServiceReference<HttpService> reference) {
				HttpService httpService = super.addingService(reference);
				try {
					registerResources(httpService);
					//registerConsoleServlet(httpService);
				} catch (ServletException | NamespaceException e) {
					e.printStackTrace();
				}
				return httpService;
			}	
		};
		
		serviceTracker.open();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		serviceTracker.close();
	}
	
	private void registerResources(HttpService httpService) throws ServletException, NamespaceException {
		System.out.println("Registering resources");
		httpService.registerResources("/", "", null);	
	}
	
	@SuppressWarnings("unused")
	private void registerConsoleServlet(HttpService httpService) throws ServletException, NamespaceException {
		ConsoleServlet consoleServlet = new ConsoleServlet();
		Map<String, String> properties = new HashMap<>();
		properties.put("title", "OSGi Console");
		consoleServlet.configure(bundleContext, properties);
		httpService.registerServlet("/console", consoleServlet, null, null);
	}

}
