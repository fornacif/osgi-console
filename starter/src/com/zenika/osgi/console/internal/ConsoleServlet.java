package com.zenika.osgi.console.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;

@SuppressWarnings("serial")
public class ConsoleServlet extends HttpServlet {
	private String title;
	private BundleContext bundleContext;
	
	public void configure(BundleContext bundleContext, Map<String, ?> properties) {
		this.bundleContext = bundleContext;
		this.title = (String) properties.get("title");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		writer.write(UIHelper.getHeader(title));
		Map<String, String> tabs = new LinkedHashMap<>();
		
		
		
		tabs.put("Properties", UIHelper.getSystemProperties());
		writer.write(UIHelper.getTabs(tabs));
		writer.write(UIHelper.getFooter());
	}

}
