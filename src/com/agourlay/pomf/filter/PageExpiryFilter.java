package com.agourlay.pomf.filter;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Set the "expires" header on pages which are configured in expires.properties
 * 
 * 1) Create a file "expires.properties" in your src/main/resources. It should
 * be accessible via the classpath '/expires.properties'.
 * 
 * 2) Each line in the properties file is a resource path regex mapped to the
 * age of the page. Examples:
 * 
 * ^/images/.*$ = 2M # 2 month expiry for all resources in images directory
 * ^.*\\.css$ = 1M # 1 month expiry for all pages which have .css extension ^/$
 * = 1h # 1 hr expiry for home page .* = 1m # 1 min expiry for all other pages
 * 
 * Left hand side is full regex. Right hand side is the age. The supported units
 * are:
 * 
 * m - minutes h - hours d - days w - weeks M - months
 * 
 * 3) Order is important. The first matching regular expression will be used.
 * 
 * @author Vineet Manohar http://www.vineetmanohar.com
 * 
 *         Etag: W/"14961-1287895605000"
 * 
 *         Expires: Wed, 24 Nov 2010 08:48:33 EST
 */
public class PageExpiryFilter implements javax.servlet.Filter {
	private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(PageExpiryFilter.class);

	private Map<String, Integer> pathRegex;

	public void destroy() {
		// no resources to be released
	}

	/**
	 * The doFilter method of the Filter is called by the container each time a
	 * request/response pair is passed through the chain due to a client request
	 * for a resource at the end of the chain.
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (response instanceof HttpServletResponse) {
			if (request instanceof HttpServletRequest) {
				HttpServletRequest httpServletRequest = (HttpServletRequest) request;
				String path = httpServletRequest.getServletPath();
				HttpServletResponse httpServletResponse = (HttpServletResponse) response;

				String expiryDate = null;
				String lastModified = null;

				for (String regex : pathRegex.keySet()) {
					if (path.matches(regex)) {
						Integer age = pathRegex.get(regex);
						// expiry date
						{
							Calendar calendar = Calendar.getInstance();
							calendar.add(Calendar.MINUTE, age);

							expiryDate = new SimpleDateFormat(
									"EEE, dd MMM yyyy HH:mm:ss z")
									.format(calendar.getTime());
							httpServletResponse
									.setHeader("Expires", expiryDate);
							log.debug("expiration date set for path: " + path
									+ " = " + expiryDate);
						}
						// last modified
						/*
						 * { Calendar calendar = Calendar.getInstance();
						 * calendar.add(Calendar.MINUTE, -age);
						 * 
						 * lastModified = new SimpleDateFormat(
						 * "EEE, dd MMM yyyy HH:mm:ss z")
						 * .format(calendar.getTime());
						 * 
						 * httpServletResponse.setHeader("Last-Modified",
						 * lastModified);
						 * log.debug("last modified date set for path: " + path
						 * + " = " + lastModified); }
						 */

						break;
					}
				}

				if (expiryDate == null) {
					log.warn("No expiration date set for path: " + path);
				}
			}
		}

		// chain.doFilter() should be called after writing the header. If it is
		// called _before_ writing the header
		chain.doFilter(request, response);
	}

	/**
	 * Get the expiry header to set for the given path
	 * 
	 * @param path
	 *            the relative path of the resource within the application. e.g.
	 *            /images/logo.png
	 * 
	 * @return the expiry header formatted as "EEE, dd MMM yyyy HH:mm:ss z", or
	 *         null if no expiry date has been specified
	 */
	private String getExpiryHeaderFor(String path) {
		for (String regex : pathRegex.keySet()) {
			if (path.matches(regex)) {
				Integer age = pathRegex.get(regex);
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MINUTE, age);

				return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z")
						.format(calendar.getTime());
			}
		}

		return null;
	}

	/**
	 * Called by the web container to indicate to a filter that it is being
	 * placed into service.
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		pathRegex = new LinkedHashMap<String, Integer>();

		Properties properties = new Properties();

		String resourceName = "/expires.properties";

		InputStream inputStream = getClass().getResourceAsStream(resourceName);

		if (inputStream != null) {
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				throw new ServletException(
						"Error while opening classpath resource '"
								+ resourceName
								+ "'. Make sure that it is a valid properties",
						e);
			}
		}

		for (Object key : properties.keySet()) {
			String regex = (String) key;
			String value = properties.getProperty(regex);

			if (value != null && value.length() == 0) {
				value = null;
			}

			Integer expInMinutes = getExpirationInMinutes(value);

			pathRegex.put(String.valueOf(key), expInMinutes);
		}
	}

	static Integer getExpirationInMinutes(String value) {
		if (value == null) {
			return null;
		}
		value = value.trim();
		if (value.length() == 0) {
			return null;
		}
		Pattern pattern = Pattern.compile("^(\\d+)(m|M|h|d|w)$");
		Matcher m = pattern.matcher(value);
		if (m.matches()) {
			int val = Integer.parseInt(m.group(1));
			String denomination = m.group(2);
			int factor;
			if (denomination.equals("m")) {
				factor = 1;
			} else if (denomination.equals("h")) {
				factor = 60;
			} else if (denomination.equals("d")) {
				factor = 60 * 24;
			} else if (denomination.equals("M")) {
				factor = 60 * 24 * 30;
			} else if (denomination.equals("w")) {
				factor = 60 * 24 * 7;
			} else {
				throw new RuntimeException(
						"Developer error. Unhandled pattern '" + value + "'"
								+ " for '" + value
								+ "'. Valid patterns are 2m, 10h, 1d, 1w, 1M");
			}

			return val * factor;
		} else {
			throw new RuntimeException(
					"Invalid pattern '"
							+ value
							+ "'"
							+ " for '"
							+ value
							+ "'. Valid patterns are '2m' for 2 minutes, '10h' for 10 hours, '1d' for 1 day, '1w' for 1 week, '1M' for 1 month");
		}
	}
}