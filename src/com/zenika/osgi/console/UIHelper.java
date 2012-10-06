package com.zenika.osgi.console;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.service.jdbc.DataSourceFactory;

public final class UIHelper {
	
	public static final String getHeader(String title) {
		String output = "" +
				"<!DOCTYPE html>" +
				"<html lang=\"en\">" +
				"	<head>" +
				"			<title>" + title + "</title>" +
				" 	        <link href=\"bootstrap/css/bootstrap.css\" rel=\"stylesheet\">" +
				" 	        <link href=\"bootstrap/css/bootstrap-responsive.css\" rel=\"stylesheet\">" +
				" 	        <style>" +
				" 	        	body {" +
				" 	        		padding-top: 60px" +
				"				}" +
				" 	        </style> " +
				" 	    </head>" +
				" 	    <body>" +
				"			<div class=\"navbar navbar-inverse navbar-fixed-top\">" + 
				" 	        	<div class=\"navbar-inner\">" + 
				" 	        		<div class=\"container\">" + 
				" 	        			<a class=\"brand\" href=\"#\">" + title + "</a>" + 
				"						<div class=\"nav-collapse collapse\">" +
				"							<p class=\"navbar-text pull-right\"><a href=\"http://www.zenika.com\">Zenika</a></p>" +
				"						</div>" + 
				"					</div>" + 
				"				</div>" +
				"			</div>" +
				"			<div class=\"container\">";
		return output;
	}
	
	public static final String getFooter() {
		String output = "" +
				"			<hr/>" +
				"			<footer>" +
				"				<p>&copy; Zenika 2012</p>" +
				"			</footer>" +
				"		</div>" +
				"		<script src=\"bootstrap/js/jquery.js\"></script>" +
				"		<script src=\"bootstrap/js/bootstrap.js\"></script>" +
				"	</body>" +
				"</html>";
		return output;
	}
	
	public static final String getTabs(Map<String, String> tabs) {
		String output = "" +
				"<div class=\"tabbable\">" +
				"	<ul class=\"nav nav-tabs\">";
		
		String active = "active";
		for (String title : tabs.keySet()) {
			output += "" +
					"	<li class=\"" + active + "\"><a href=\"#" + title.replace(" ", "_") + "\" data-toggle=\"tab\">" + title + "</a></li>";
			if (!active.isEmpty()) {
				active = "";
			}
		}
		
		output += "" +
				"	</ul>" +
				"	<div class=\"tab-content\">";
		
		active = "active";
		for (String title : tabs.keySet()) {
			output += "" +
					"	<div class=\"tab-pane " + active + "\" id=\""+ title.replace(" ", "_") + "\">" + tabs.get(title) + "</div>";
			if (!active.isEmpty()) {
				active = "";
			}
		}
		output += "" +
				"	</div>" +
				"</div>";
		return output;
	}
	
	public static final String getSystemProperties() {
		String output = "" +
				"<table class=\"table table-striped table-hover\">" +
				"	<thead>" +
				"		<tr>" +
				"			<th>PROPERTY</th>" +
				"			<th>VALUE</th>" +
				"		</tr>" +
				"	</thead>" +
				"	<tbody>" +
				"		<tr>" +
				"			<td>OS name</td>" +
				"			<td>" + System.getProperty("os.name") + "</td>" +
				"		</tr>" +
				"		<tr>" +
				"			<td>OS arch</td>" +
				"			<td>" + System.getProperty("os.arch") + "</td>" +
				"		</tr>" +
				"		<tr>" +
				"			<td>OS version</td>" +
				"			<td>" + System.getProperty("os.version") + "</td>" +
				"		</tr>" +
				"		<tr>" +
				"			<td>Java version</td>" +
				"			<td>" + System.getProperty("java.version") + "</td>" +
				"		</tr>" +
				"		<tr>" +
				"			<td>Java vendor</td>" +
				"			<td>" + System.getProperty("java.vendor") + "</td>" +
				"		</tr>" +
				"	</tbody>" +
				"</table>";
		return output;
	}
	
	public static String getFrameworkStartLevel(FrameworkStartLevel frameworkStartLevel) {
		String output = "" +
				"<table class=\"table table-striped table-hover\">" +
				"	<thead>" +
				"		<tr>" +
				"			<th>PROPERTY</th>" +
				"			<th>VALUE</th>" +
				"		</tr>" +
				"	</thead>" +
				"	<tbody>" +
				"		<tr>" +
				"			<td>Initial Bundle StartLevel</td>" +
				"			<td>" + frameworkStartLevel.getInitialBundleStartLevel() + "</td>" +
				"		</tr>" +
				"		<tr>" +
				"			<td>StartLevel</td>" +
				"			<td>" + frameworkStartLevel.getStartLevel() + "</td>" +
				"		</tr>" +
				"	</tbody>" +
				"</table>";
		return output;
	}
	
	public static final String getBundles(Bundle[] bundles) {
		String output = "" +
				"" + bundles.length + " bundles" +
				"<table class=\"table table-striped table-hover\">" +
				"	<thead>" +
				"		<tr>" +
				"			<th>ID</th>" +
				"			<th>STATE</th>" +
				"			<th>SYMBOLIC NAME</th>" +
				"			<th>VERSION</th>" +
				"		</tr>" +
				"	</thead>" +
				"	<tbody>";
		for (Bundle bundle : bundles) {
			output += "" +
				"		<tr>" +
				"			<td>" + bundle.getBundleId() + "</td>" +
				"			<td>" + getState(bundle) + "</td>" +
				"			<td>" + bundle.getSymbolicName() + "</td>" +
				"			<td>" + bundle.getVersion().toString() + "</td>" +
				"		</tr>";
		}
		output += "" +
				"	</tbody>" +
				"</table>";
		return output;
	}
	
	public static final String getServices(ServiceReference<?>[] serviceReferences) {
		String output = "" +
				"" + serviceReferences.length + " services" +
				"<table class=\"table table-striped table-hover\">" +
				"	<thead>" +
				"		<tr>" +
				"			<th>ID</th>" +
				"			<th>OBJECTCLASS</th>" +
				"		</tr>" +
				"	</thead>" +
				"	<tbody>";
		for (ServiceReference<?> serviceReference : serviceReferences) {
			output += "" +
				"		<tr>" +
				"			<td>" + serviceReference.getProperty(Constants.SERVICE_ID) + "</td>" +
				"			<td>" + Arrays.toString((String[])serviceReference.getProperty(Constants.OBJECTCLASS)) + "</td>" +
				"		</tr>";
		}
		output += "" +
				"	</tbody>" +
				"</table>";
		return output;
	}
	
	public static String getDatabaseSettings(DataSourceFactory dataSourceFactory) {		
		Properties properties = new Properties();
		properties.put(DataSourceFactory.JDBC_URL, "jdbc:h2:~/test");
		properties.put(DataSourceFactory.JDBC_USER, "sa");
		properties.put(DataSourceFactory.JDBC_PASSWORD, "");
		
		DataSource dataSource = null;
		try {
			dataSource = dataSourceFactory.createDataSource(properties);
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
			
		try (Connection connection = dataSource.getConnection();
		    Statement statement = connection.createStatement()) {
	    
			ResultSet resultSet = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.SETTINGS");
		    
		    String output = "" +
						"<table class=\"table table-striped table-hover\">" +
						"	<thead>" +
						"		<tr>" +
						"			<th>NAME</th>" +
						"			<th>VALUE</th>" +
						"		</tr>" +
						"	</thead>" +
						"	<tbody>";
		    while (resultSet.next()) {
		    	output += "" +
						"		<tr>" +
						"			<td>" + resultSet.getString(1) + "</td>" +
						"			<td>" + resultSet.getString(2) + "</td>" +
						"		</tr>";
		    }
			output += "" +
						"	</tbody>" +
						"</table>";
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}	
	}
	
	private static final String getState(Bundle bundle) {
		switch (bundle.getState()) {
		case Bundle.ACTIVE:
			return "ACTIVE";
		case Bundle.RESOLVED:
			return "RESOLVED";
		case Bundle.INSTALLED:
			return "INSTALLED";
		default:
			return "OTHER";
		}
	}

	
}
