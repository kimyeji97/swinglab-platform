package com.dailystudy.swinglab.service.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * URLValidator 클래스 
 * URL 정보의 Util 기능을 제공한다.
 */
public class URLValidator {

	private static final Pattern PATTERN_URL = Pattern.compile("^((http(s)?|ftp):\\/\\/)?+((([^\\/:]\\S+\\.)?([[^\\/.:]&&\\S]+)?+)(:(\\p{Digit}+))?+)?+(\\/([\\S&&[^\\?#]])*)?(\\??(&?[\\S&&[^&=#]]+=?[\\S&&[^&=#]]+)*)?(#.*)?$");
	private boolean isValid;
	private String scheme;
	private String domain;
	private int port;
	private String path;
	private String queryString;
	private String fragmentId;
	private String url;

	public URLValidator(String url) {
		this.url = url;
		Matcher matcher = PATTERN_URL.matcher(url);
		isValid = matcher.matches();
		if (isValid) {
			scheme = matcher.group(2);
			if (matcher.group(3) != null) {
				scheme = scheme + matcher.group(3);
			}
			domain = matcher.group(5);
			if (matcher.group(9) != null) {
				try {
					port = Integer.parseInt(matcher.group(9));
				} catch (NumberFormatException ex) {
				}
			}
			if (port == 0) {
				if ("http".equalsIgnoreCase(scheme)) {
					port = 80;
				} else if ("https".equalsIgnoreCase(scheme)) {
					port = 443;
				} else if ("ftp".equalsIgnoreCase(scheme)) {
					port = 21;
				}
			}
			if (matcher.group(10) != null) {
				path = matcher.group(10);
			}
			if (matcher.group(12) != null) {
				queryString = matcher.group(12);
				if (queryString.startsWith("?")) {
					queryString = queryString.substring(1);
				}
			}
			if (matcher.group(14) != null) {
				fragmentId = matcher.group(14);
				if (fragmentId.startsWith("#")) {
					fragmentId = fragmentId.substring(1);
				}
			}
		}
	}

	/**
	 * URL 패턴값이 맞는 지 확인한다.
	 * @param url 확인 할 URL 값
	 * @return boolean URL 패턴 여부 
	 */
	public static boolean isValidUrl(String url) {
		return PATTERN_URL.matcher(url).matches();
	}

	/**
	 * URL 데이터를 리턴한다.
	 * @return String URL 데이터
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Domain 데이터를 리턴한다.
	 * @return String Domain 데이터
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * FragmentId를 리턴한다.
	 * @return String FragmentId 데이터
	 */
	public String getFragmentId() {
		return fragmentId;
	}

	/**
	 * URL 패턴값이 맞는 지 확인한다.
	 * @return boolean URL 패턴 여부 
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * URL Path 값을 리턴한다.
	 * @return String URL Path 데이터
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Port 리턴한다.
	 * @return Integer Port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * QueryString 리턴한다.
	 * @return String QueryString 데이터
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * Scheme 리턴한다.
	 * @return String Scheme 데이터
	 */
	public String getScheme() {
		return scheme;
	}
}
