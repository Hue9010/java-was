package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;

public class RequestLine {
	private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
	private Method method;
	private String url;
	
	public RequestLine(String line){
		log.info(line);
		method = Method.valueOf(HttpRequestUtils.parseMethod(line));
		url = HttpRequestUtils.parseUrl(line);
	}

	public Method getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public boolean matchMethod(Method method) {
		return this.method == method;
	}
}