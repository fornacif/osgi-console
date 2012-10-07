package com.zenika.osgi.console;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.service.jdbc.DataSourceFactory;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@SuppressWarnings("serial")
@Component(name="console", provide = {Servlet.class}, designate=ConsoleServletConfig.class)
public class ConsoleServlet extends HttpServlet {
	private BundleContext bundleContext;
	
	private String title;
	
	private DataSourceFactory dataSourceFactory;
	
	@Activate
	public void configure(BundleContext bundleContext, Map<String, ?> properties) {
		this.bundleContext = bundleContext;
		title = (String) properties.get("title");
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
		if (dataSourceFactory != null) {
			tabs.put("H2 settings", UIHelper.getDatabaseSettings(dataSourceFactory));
		}
		tabs.put("Properties", UIHelper.getSystemProperties());
		resp.getWriter().print(UIHelper.getTabs(tabs));
		
		resp.getWriter().print(UIHelper.getFooter());
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Reference(optional=true, dynamic=true)
	public void bindDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}
	
	public void unbindDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = null;
	}

}
