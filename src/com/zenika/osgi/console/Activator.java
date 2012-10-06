package com.zenika.osgi.console;

import javax.servlet.ServletException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
	private ServiceTracker<HttpService, HttpService> serviceTracker;
//	private BundleContext bundleContext;

	public void start(BundleContext bundleContext) throws Exception {	
//		this.bundleContext = bundleContext;

		serviceTracker = new ServiceTracker<HttpService, HttpService>(bundleContext, HttpService.class, null) {
			@Override
			public HttpService addingService(ServiceReference<HttpService> reference) {
				HttpService httpService = super.addingService(reference);
				try {
					registerResources(httpService);
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
		//httpService.registerServlet("/console", new ConsoleServlet(bundleContext), null, null);
		httpService.registerResources("/", "", null);
	}

}
