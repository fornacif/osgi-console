package com.zenika.osgi.console;

import java.util.Collection;
import java.util.Iterator;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.hooks.service.FindHook;

public class ServiceFilter implements FindHook {

	@Override
	public void find(BundleContext bundleContext, String name, String filter, boolean allServices, Collection<ServiceReference<?>> references) {
		if (name != null || filter != null || allServices == true) {
			return;
		}
		
		for (Iterator<ServiceReference<?>> iterator = references.iterator(); iterator.hasNext();) {
			ServiceReference<?> serviceReference = iterator.next();	
			if (bundleContext.getBundle().getSymbolicName().equals("osgi.console") && serviceReference.getUsingBundles() == null) {
				iterator.remove();
			}
		}
	}

}
