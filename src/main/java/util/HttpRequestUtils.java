package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import model.Method;
import model.RequestLine;

public class HttpRequestUtils {
	private static final Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);

	public static String parseQueryByPath(String url) {
		String[] urlArr = url.split("\\?");
		if (urlArr.length > 1) {
			return urlArr[1];
		}
		return null;
	}

	public static String getParameterQuery(RequestLine requestLine, Map<String, String> headers, BufferedReader br)
			throws NumberFormatException, IOException {
		if (requestLine.matchMethod(Method.POST)) {
			return IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
		}
		return HttpRequestUtils.parseQueryByPath(requestLine.getUrl());
	}

	public static String parseUrl(String firstLine) {
		return firstLine.split(" ")[1];
	}

	public static String parseMethod(String firstLine) {
		return firstLine.split(" ")[0].toUpperCase();
	}

	/**
	 * @param queryString은
	 *            URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
	 * @return
	 */
	public static Map<String, String> parseQueryString(String queryString) {
		return parseValues(queryString, "&");
	}

	public static Map<String, String> parseHeaders(BufferedReader br) throws IOException {
		Map<String, String> headers = new HashMap<>();
		String line = br.readLine();
		while (hasNext(line)) {
			log.info(line);
			putHeaders(headers, line);
			line = br.readLine();
		}
		return headers;
	}

	private static void putHeaders(Map<String, String> headers, String line) {
		Pair pair = HttpRequestUtils.parseHeader(line);
		headers.put(pair.getKey(), pair.getValue());
	}

	private static boolean hasNext(String line) {
		return !"".equals(line) && line != null;
	}

	/**
	 * @param 쿠키
	 *            값은 name1=value1; name2=value2 형식임
	 * @return
	 */
	public static Map<String, String> parseCookies(String cookies) {
		return parseValues(cookies, ";");
	}

	private static Map<String, String> parseValues(String values, String separator) {
		if (Strings.isNullOrEmpty(values)) {
			return Maps.newHashMap();
		}

		String[] tokens = values.split(separator);
		return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
				.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
	}

	static Pair getKeyValue(String keyValue, String regex) {
		if (Strings.isNullOrEmpty(keyValue)) {
			return null;
		}

		String[] tokens = keyValue.split(regex);
		if (tokens.length != 2) {
			return null;
		}

		return new Pair(tokens[0], tokens[1]);
	}

	public static Pair parseHeader(String header) {
		return getKeyValue(header, ": ");
	}

	public static class Pair {
		String key;
		String value;

		Pair(String key, String value) {
			this.key = key.trim();
			this.value = value.trim();
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair other = (Pair) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Pair [key=" + key + ", value=" + value + "]";
		}
	}

}
