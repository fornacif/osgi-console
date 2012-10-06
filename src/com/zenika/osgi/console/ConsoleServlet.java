package com.zenika.osgi.console;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.jdbc.DataSourceFactory;

@SuppressWarnings("serial")
public class ConsoleServlet extends HttpServlet implements ManagedService {

	private BundleContext bundleContext;
	
	private String title;
	
	private DataSourceFactory dataSourceFactory;
	
	public void activate(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		
//		Dictionary<String, String> properties = new Hashtable<>();
//		properties.put(Constants.SERVICE_PID, "console");
//		bundleContext.registerService(ManagedService.class, this, properties);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.getWriter().print(UIHelper.getHeader(title));
		
		Map<String, String> tabs = new LinkedHashMap<>();
		tabs.put("Bundles", UIHelper.getBundles(bundleContext.getBundles()));
		try {
			tabs.put("All Services", UIHelper.getServices(bundleContext.getAllServiceReferences(null, null)));
			tabs.put("Services In Use", UIHelper.getServices(bundleContext.getServiceReferences((String) null, null)));
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		tabs.put("Framework StartLevel", UIHelper.getFrameworkStartLevel(bundleContext.getBundle(0).adapt(FrameworkStartLevel.class)));	
		tabs.put("H2 settings", UIHelper.getDatabaseSettings(dataSourceFactory));
		tabs.put("Properties", UIHelper.getSystemProperties());
		resp.getWriter().print(UIHelper.getTabs(tabs));
		
		resp.getWriter().print(UIHelper.getFooter());
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void bindDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}
	
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		if (properties != null) {
			title = (String) properties.get("title");
		}
	}

}
