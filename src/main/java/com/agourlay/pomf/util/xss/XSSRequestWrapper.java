package com.agourlay.pomf.util.xss;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
 
public class XSSRequestWrapper extends HttpServletRequestWrapper {
 
	private final String body;  
	
    private static Pattern[] patterns = new Pattern[]{
        // Script fragments
        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
        // src='...'
        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // lonely script tags
        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // eval(...)
        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // expression(...)
        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // javascript:...
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        // vbscript:...
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        // onmouseover(...)=...
        Pattern.compile("onmouseover(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // onclick(...)=...
        Pattern.compile("onclick(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // onload(...)=...
        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };
 
    public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
        // read the original payload into the xmlPayload variable
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            // read the payload into the StringBuilder
            InputStream inputStream = servletRequest.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                // make an empty string since there is no payload
                stringBuilder.append("");
            }
        } catch (IOException ex) {
        	// ignore
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException iox) {
                    // ignore
                }
            }
        }
        body = stripXSS(stringBuilder.toString());
    }
 
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
 
        if (values == null) {
            return null;
        }
 
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }
 
        return encodedValues;
    }
 
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return stripXSS(value);
    }
 
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSS(value);
    }
 
    private String stripXSS(String value) {
        if (value != null) { 
            // Avoid null characters
            value = value.replaceAll("\0", "");
 
            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream inputStream = new ServletInputStream() {
            public int read () 
                throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return inputStream;
	}
}