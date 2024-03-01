package com.dailystudy.swinglab.service.framework.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtil 클래스 
 * 문자열에서 사용하는 Util 기능을 제공한다.
 */
public class StringUtil {

	public static final Pattern SPACE_PATTERN = Pattern.compile("(\\s)+");
	public static final Pattern FIRST_SPACE_PATTERN = Pattern.compile("^(\\s)+");
	public static final Pattern LAST_SPACE_PATTERN = Pattern.compile("(\\s)+$");
	public static final Pattern MORE_SPACE_PATTERN = Pattern.compile("(\\s){2,}");
	private static final Pattern PATTERN_ZIP = Pattern.compile("^(\\d\\d\\d)?([-])?(\\d\\d\\d)?$");
	private static final Pattern PATTERN_PHONE_NO = Pattern.compile("^(\\+)?(\\d)+(([\\s\\-])*(\\d)+)+$");
	private static final Pattern PATTERN_PHONE = Pattern.compile("^(\\d\\d(\\d)?(\\d)?)?([-])?(\\d\\d\\d(\\d)?)+([-])?(\\d\\d\\d\\d)+$");
	private static final Pattern PATTERN_KOREAN_PHONE = Pattern.compile("^(02|031|062|053|042|051|032|052|033|055|054|061|063|064|041|043|070|0303|010|011|016|017|018|019)?([-])?(\\d\\d\\d(\\d)?)+([-])?(\\d\\d\\d\\d)+$");
	private static final Pattern PATTERN_KOREAN_MOBILE = Pattern.compile("^(010|011|016|017|018|019)?([-])?(\\d\\d\\d(\\d)?)+([-])?(\\d\\d\\d\\d)+$");
	private static final Pattern PATTERN_EMAIL = Pattern.compile("^(.+)?@(.+\\.[a-z]+)?$");
	private static final Pattern PATTERN_ALPHABET = Pattern.compile("^[a-zA-Z]+$");
	private static final Pattern PATTERN_LOWER_ALPHABET = Pattern.compile("^[a-z]+$");
	private static final Pattern PATTERN_UPPER_ALPHABET = Pattern.compile("^[A-Z]+$");
	private static final Pattern PATTERN_ALPHABET_DIGIT = Pattern.compile("^[a-zA-Z0-9]+$");
	private static final Pattern PATTERN_DIGIT = Pattern.compile("^[0-9]+$");
	private static final Pattern PATTERN_LOWER_ALPHABET_DIGIT = Pattern.compile("^[a-z0-9]+$");
	private static final Pattern PATTERN_UPPER_ALPHABET_DIGIT = Pattern.compile("^[A-Z0-9]+$");
	private static final Pattern PATTERN_ALPHABET_DIGIT_WITH_WHITESPACE = Pattern.compile("^[a-zA-Z0-9\\s]+$");
	private static final Pattern PATTERN_TRIM_FIRST_DIGITS = Pattern.compile("^([0-9])+");
	private static final Pattern PATTERN_MACID = Pattern.compile("^([0-9a-fA-F]{0,2})?(:)?([0-9a-fA-F]{0,2})?(:)?([0-9a-fA-F]{0,2})?(:)?([0-9a-fA-F]{0,2})?(:)?([0-9a-fA-F]{0,2})?(:)?([0-9a-fA-F]{0,2})?$");
	private static final Pattern PATTERN_MACID_EXACT = Pattern.compile("^(([a-fA-F0-9]{2}(:)){5,6}([a-fA-F0-9]{2}))?$");
	private static final String PATTERN_DECIMAL = "^-?\\d{0,%s}(\\.\\d{0,%s})?$";
	private static final Pattern PATTERN_MACID_IP = Pattern.compile("((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])([.](?!$)|$)){4}");
	
	private StringUtil() {
	}

	/**
	 * 반각문자로 변경한다
	 * @param src 변경할 값
	 * @return String 변경된 값
	 */
	public static String halfChar(String src) {
		if (src == null) {
			return "";
		}
		StringBuilder strBuf = new StringBuilder();
		char c = 0;
		int nSrcLength = src.length();
		for (int i = 0; i < nSrcLength; i++) {
			c = src.charAt(i);
			if (c >= '！' && c <= '～') {
				c -= 0xfee0;
			} else if (c == '　') {
				c = 0x20;
			}

			strBuf.append(c);
		}
		return strBuf.toString();
	}

	/**
	 * 전각문자로 변경한다.
	 * @param src 변경할 값
	 * @return String 변경된 값
	 */
	public static String fullChar(String src) {
		if (src == null) {
			return "";
		}

		StringBuilder strBuf = new StringBuilder();
		char c = 0;
		int nSrcLength = src.length();
		for (int i = 0; i < nSrcLength; i++) {
			c = src.charAt(i);
			if (c >= 0x21 && c <= 0x7e) {
				c += 0xfee0;
			} else if (c == 0x20) {
				c = 0x3000;
			}
			strBuf.append(c);
		}
		return strBuf.toString();
	}

	/**
	 * 빈문자열인지 확인한다.
	 * @param value 확인할 값
	 * @return boolean 빈문자열 여부 
	 */
	public static boolean isEmptyString(String value) {
		if (value == null) {
			return true;
		}
		if (value.trim().isEmpty()) {
			return true;
		}

		return false;
	}
	
	/**
	 * 문자열에 데이터가 있는 지 확인한다.
	 * @param value 확인할 값
	 * @return boolean 문자열 데이터 존재 여부 
	 */
	public static boolean isNotEmptyString(String value) {
		if (value == null) {
			return false;
		}
		if (value.trim().isEmpty()) {
			return false;
		}

		return true;
	}

	/**
	 * 문자열이 NULL 인지 확인한다.
	 * @param value 확인할 값
	 * @return boolean 문자열 NULL 여부 
	 */
	public static boolean isNullString(String value) {
		if (value == null) {
			return true;
		}

		return false;
	}
	
	/**
	 * 우편번호 패턴의 첫번째를 리턴한다.
	 * @param value 우편번호 문자열 
	 * @return String 우편번호 첫번째 패턴 값 
	 */
	public static String zip1(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		Matcher matcher = PATTERN_ZIP.matcher(value);
		if (matcher.find()) {
			String retValue = matcher.group(1);
			if (retValue == null) {
				return "";
			}
			return retValue;
		}
		return "";
	}

	/**
	 * 우편번호 패턴의 두번째를 리턴한다.
	 * @param value 우편번호 문자열 
	 * @return String 우편번호 두번째 패턴 값 
	 */
	public static String zip2(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		Matcher matcher = PATTERN_ZIP.matcher(value);
		if (matcher.find()) {
			String retValue = matcher.group(3);
			if (retValue == null) {
				return "";
			}
			return retValue;
		}
		return "";
	}

	/**
	 * 우편번호 패턴 값을 찾아 리턴한다.
	 * @param value 우편번호 문자열 
	 * @return String 우편번호 매핑 되는 패턴 값 
	 */
	public static String zip(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		String retValue = "";
		value = value.trim();
		Matcher matcher = PATTERN_ZIP.matcher(value);
		if (matcher.find()) {
			String zip1 = matcher.group(1);
			String zip2 = matcher.group(3);
			if (zip1 != null && !"".equals(zip1.trim())) {
				retValue = zip1 + "-";
			}
			if (zip2 != null && !"".equals(zip2.trim())) {
				retValue = retValue + zip2;
			}
		}
		return retValue;
	}

	/**
	 * 전화번호 패턴의 첫번째를 리턴한다.
	 * @param value 전화번호 문자열 
	 * @return String 전화번호 첫번째 패턴 값 
	 */
	public static String telNo1(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		Matcher matcher = PATTERN_PHONE.matcher(value);
		if (matcher.find()) {
			String retValue = matcher.group(1);
			if (retValue == null) {
				return "";
			}
			return retValue;
		}
		return "";
	}

	/**
	 * 전화번호 패턴의 두번째를 리턴한다.
	 * @param value 전화번호 문자열 
	 * @return String 전화번호 두번째 패턴 값 
	 */
	public static String telNo2(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		Matcher matcher = PATTERN_PHONE.matcher(value);
		if (matcher.find()) {
			String retValue = matcher.group(5);
			if (retValue == null) {
				return "";
			}
			return retValue;
		}
		return "";
	}

	/**
	 * 전화번호 패턴의 세번째를 리턴한다.
	 * @param value 전화번호 문자열 
	 * @return String 전화번호 세번째 패턴 값 
	 */
	public static String telNo3(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		Matcher matcher = PATTERN_PHONE.matcher(value);
		if (matcher.find()) {
			String retValue = matcher.group(8);
			if (retValue == null) {
				return "";
			}
			return retValue;
		}
		return "";
	}

	/**
	 * 전화번호 패턴 값을 리턴한다.
	 * @param value 전화번호 문자열 
	 * @return String 전화번호 패턴 값에 매핑되는 데이터 
	 */
	public static String telNo(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		StringBuilder sb = new StringBuilder();
		Matcher matcher = PATTERN_PHONE.matcher(value);
		if (matcher.find()) {
			String telNo1 = matcher.group(1);
			String telNo2 = matcher.group(5);
			String telNo3 = matcher.group(8);
			if (telNo1 != null && !"".equals(telNo1.trim())) {
				sb.append(telNo1).append("-");
			}
			if (telNo2 != null && !"".equals(telNo2.trim())) {
				sb.append(telNo2).append("-");
			}
			if (telNo3 != null && !"".equals(telNo3.trim())) {
				sb.append(telNo3);
			}
		}
		return sb.toString();
	}

	/**
	 * 전화번호(한국) 패턴 값을 리턴한다.
	 * @param value 전화번호 문자열 
	 * @return String 전화번호 패턴 값에 매핑되는 데이터 
	 */
	public static String koreanTelNo(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		StringBuilder sb = new StringBuilder();
		Matcher matcher = PATTERN_KOREAN_PHONE.matcher(value);
		if (matcher.find()) {
			String telNo1 = matcher.group(1);
			String telNo2 = matcher.group(3);
			String telNo3 = matcher.group(6);
			if (telNo1 != null && !"".equals(telNo1.trim())) {
				sb.append(telNo1).append("-");
			}
			if (telNo2 != null && !"".equals(telNo2.trim())) {
				sb.append(telNo2).append("-");
			}
			if (telNo3 != null && !"".equals(telNo3.trim())) {
				sb.append(telNo3);
			}
		}
		return sb.toString();
	}

	/**
	 * 전화번호(한국) 패턴의 첫번째를 리턴한다.
	 * @param value 전화번호 문자열 
	 * @return String 전화번호 첫번째 패턴 값 
	 */
	public static String koreanTelNo1(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		Matcher matcher = PATTERN_KOREAN_PHONE.matcher(value);
		if (matcher.find()) {
			String retValue = matcher.group(1);
			if (retValue == null) {
				return "";
			}
			return retValue;
		}
		return "";
	}

	/**
	 * 전화번호(한국) 패턴의 두번째를 리턴한다.
	 * @param value 전화번호 문자열 
	 * @return String 전화번호 두번째 패턴 값 
	 */
	public static String koreanTelNo2(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		Matcher matcher = PATTERN_KOREAN_PHONE.matcher(value);
		if (matcher.find()) {
			String retValue = matcher.group(3);
			if (retValue == null) {
				return "";
			}
			return retValue;
		}
		return "";
	}

	/**
	 * 전화번호(한국) 패턴의 세번째를 리턴한다.
	 * @param value 전화번호 문자열 
	 * @return String 전화번호 세번째 패턴 값 
	 */
	public static String koreanTelNo3(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		Matcher matcher = PATTERN_KOREAN_PHONE.matcher(value);
		if (matcher.find()) {
			String retValue = matcher.group(6);
			if (retValue == null) {
				return "";
			}
			return retValue;
		}
		return "";
	}

	/**
	 * 이메일 패턴의 데이터를 리턴한다.
	 * @param value 이메일 패턴 문자열 
	 * @return String 이메일 패턴에 매핑되는 값 
	 */
	public static String email(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		Matcher matcher = PATTERN_EMAIL.matcher(value);
		if (matcher.find()) {
			String emailId = matcher.group(1);
			if (emailId == null) {
				emailId = "";
			}
			String emailDomain = matcher.group(2);
			if (emailDomain == null) {
				emailDomain = "";
			}
			if (!"".equals(emailId) && !"".equals(emailDomain)) {
				return value;
			}
		}
		return "";
	}

	/**
	 * 이메일 패턴의 아이디를 리턴한다.
	 * @param value 이메일 패턴 문자열 
	 * @return String 이메일 패턴에 매핑되는 아이디 값 
	 */
	public static String emailId(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		Matcher matcher = PATTERN_EMAIL.matcher(value);
		if (matcher.find()) {
			String retValue = matcher.group(1);
			if (retValue == null) {
				return "";
			}
			return retValue;
		}
		return "";
	}

	/**
	 * 이메일 패턴의 도메인 값을 리턴한다.
	 * @param value 이메일 패턴 문자열 
	 * @return String 이메일 패턴에 매핑되는 도메인 값 
	 */
	public static String emailDomain(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		value = value.trim();
		Matcher matcher = PATTERN_EMAIL.matcher(value);
		if (matcher.find()) {
			String retValue = matcher.group(2);
			if (retValue == null) {
				return "";
			}
			return retValue;
		}
		return "";
	}

	/**
	 * 맥아이디 패턴의 데이터를 리턴한다.
	 * @param value 맥 패턴 문자열 (XX:XX:XX:XX:XX:XX)
	 * @param index 맥 패턴에서 추출할 데이터 인덱스 
	 * @return String 맥 패턴 인덱스에 매핑되는 값 
	 */
	public static String macId(String value, int index) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		int idx = 0;
		switch (index) {
			case 1:
				idx = 1;
				break;
			case 2:
				idx = 3;
				break;
			case 3:
				idx = 5;
				break;
			case 4:
				idx = 7;
				break;
			case 5:
				idx = 9;
				break;
			case 6:
				idx = 11;
				break;
			default:
				idx = 0;
		}
		value = value.trim();
		Matcher matcher = PATTERN_MACID.matcher(value);
		if (matcher.find()) {
			String retValue = matcher.group(idx);
			if (retValue == null) {
				return "";
			}
			return retValue;
		}
		return "";
	}

	/**
	 * URL Encoding 데이터를 Decode 한다. 
	 *
	 * @param s The URI encoded String 
	 * @param charset
	 * @return the decoded String
	 */
	public static String decodeURIComponent(String s, String charset) {
		if (s == null) {
			return null;
		}

		String result = null;

		try {
			result = URLDecoder.decode(s, charset);
		} // This exception should never occur.
		catch (UnsupportedEncodingException e) {
			result = s;
		}

		return result;
	}

	/**
	 * URL Encoding 데이터를 Decode 한다. 
	 *
	 * @param s The UTF-8 encoded String to be decoded
	 * @return the decoded String
	 */
	public static String decodeURIComponent(String s) {
		return decodeURIComponent(s, "UTF-8");
	}

	/**
	 * 데이터를 URI Encode 한다. 
	 *
	 * @param s The String to be encoded
	 * @param charset 
	 * @return the encoded String
	 */
	public static String encodeURIComponent(String s, String charset) {
		String result = null;

		try {
			result = URLEncoder.encode(s, charset).replaceAll("\\+", "%20").replaceAll("\\%21", "!").replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")").replaceAll("\\%7E", "~");
		} // This exception should never occur.
		catch (UnsupportedEncodingException e) {
			result = s;
		}

		return result;
	}

	/**
	 * 데이터를 UTF-8 URI Encode 한다. 
	 *
	 * @param s The String to be encoded
	 * @return the encoded String
	 */
	public static String encodeURIComponent(String s) {
		return encodeURIComponent(s, "UTF-8");
	}

	/**
	 * Tag 형식의 String을 replace 한다. 
	 *
	 * @param source replace할 데이터 
	 * @return Replace Tag String
	 */
	public static String trimTag(String source) {
		return (source == null ? source : source.replaceAll("\\<.*?\\>", ""));
	}

	/**
	 * LTrim 을 한다.
	 *
	 * @param source ltrim 할 데이터 
	 * @return Ltrim 된 데이터.
	 */
	public static String ltrim(String source) {
		try {
			StringBuffer output = new StringBuffer();
			Matcher matcher = FIRST_SPACE_PATTERN.matcher(source);
			while (matcher.find()) {
				matcher.appendReplacement(output, "");
			}
			matcher.appendTail(output);
			return output.toString();
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			return source;
		}
	}

	/**
	 * RTrim 을 한다.
	 *
	 * @param source rtrim 할 데이터 
	 * @return Rtrim 된 데이터.
	 */
	public static String rtrim(String source) {
		try {
			StringBuffer output = new StringBuffer();
			Matcher matcher = LAST_SPACE_PATTERN.matcher(source);
			while (matcher.find()) {
				matcher.appendReplacement(output, "");
			}
			matcher.appendTail(output);
			return output.toString();
		} catch (Exception ex) {
			return source;
		}
	}

	/**
	 * Trim 을 한다.
	 *
	 * @param source trim 할 데이터 
	 * @return trim 된 데이터.
	 */
	public static String trimAll(String source) {
		try {
			StringBuffer output = new StringBuffer();
			Matcher matcher = SPACE_PATTERN.matcher(source);
			while (matcher.find()) {
				matcher.appendReplacement(output, "");
			}
			matcher.appendTail(output);
			return output.toString();
		} catch (Exception ex) {
			return source;
		}
	}

	/**
	 * 길이만큼 데이터가 되지 않을 시 왼쪽부터 padding 데이터를 채운다.
	 *
	 * @param value Padding 할 데이터 
	 * @param padding LPadding으로 채울 문자   
	 * @param length 길이 
	 * @param maxlength 최대 길이  
	 * @return Lpadding 된 데이터 
	 */
	public static String paddingLeft(String value, String padding, int length, int maxlength) {
		if (value != null && value.length() > maxlength) {
			value = value.substring(0, maxlength);
		}
		return paddingLeft(value, padding, length);
	}

	/**
	 * 길이만큼 데이터가 되지 않을 시 왼쪽부터 padding 데이터를 채운다.
	 *
	 * @param value Padding 할 데이터 
	 * @param padding LPadding으로 채울 문자   
	 * @param length 길이  
	 * @return Lpadding 된 데이터 
	 */
	public static String paddingLeft(String value, String padding, int length) {
		if (value == null || "".equals(value.trim())) {
			return repeat(padding, length);
		}
		if (value.length() > length) {
			return value;
		}
		return repeat(padding, length - value.length()) + value;
	}

	/**
	 * 길이만큼 데이터가 되지 않을 시 오른쪽부터 padding 데이터를 채운다.
	 *
	 * @param value Padding 할 데이터 
	 * @param padding RPadding으로 채울 문자   
	 * @param length 길이  
	 * @param maxlength 최대 길이  
	 * @return Rpadding 된 데이터 
	 */
	public static String paddingRight(String value, String padding, int length, int maxlength) {
		if (value != null && value.length() > maxlength) {
			value = value.substring(0, maxlength);
		}
		return paddingRight(value, padding, length);
	}

	/**
	 * 길이만큼 데이터가 되지 않을 시 오른쪽부터 padding 데이터를 채운다.
	 *
	 * @param value Padding 할 데이터 
	 * @param padding RPadding으로 채울 문자   
	 * @param length 길이  
	 * @return Rpadding 된 데이터 
	 */
	public static String paddingRight(String value, String padding, int length) {
		if (value == null || "".equals(value.trim())) {
			return repeat(padding, length);
		}
		if (value.length() > length) {
			return value;
		}
		return value + repeat(padding, length - value.length());
	}

	/**
	 * value 값을 length 만큼 반복해서 append한다.
	 *
	 * @param value 반복 할 데이터 
	 * @param length 반복 횟수 
	 * @return String append 된 데이터  
	 */
	public static String repeat(String value, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(value);
		}
		return sb.toString();
	}

	/**
	 * String 데이터가 URL 데이터인지 확인한다.
	 *
	 * @param url 확인 할 데이터
	 * @return boolean URL 데이터 여부 
	 */
	public static boolean isValidURL(String url) {
		return URLValidator.isValidUrl(url);
	}

	/**
	 * 특수문자를 html 문자로 치환한다.
	 *
	 * @param source 변경 할 데이터
	 * @return String 치환 된 데이터  
	 */
	public static String html2special(String source) {
		source = (source == null ? "" : source.trim());
		source = special2html(source);
		source = source.replaceAll("&", "&amp;");
		source = source.replaceAll("\"", "&quot;");
		source = source.replaceAll("<", "&lt;");
		source = source.replaceAll(">", "&gt;");
		return source;
	}

	/**
	 * html 문자를 특수문자로 치환한다.
	 *
	 * @param source 변경 할 데이터
	 * @return String 치환 된 데이터  
	 */
	public static String special2html(String source) {
		source = (source == null ? "" : source.trim());
		source = source.replaceAll("&gt;", ">");
		source = source.replaceAll("&lt;", "<");
		source = source.replaceAll("&quot;", "\"");
		source = source.replaceAll("&amp;", "&");
		return source;
	}

	/**
	 * 개행문자(\n)를 html <br>로 치환한다.
	 *
	 * @param source 변경 할 데이터
	 * @return String 치환 된 데이터  
	 */
	public static String newline2br(String source) {
		source = (source == null ? "" : source.trim());
		source = source.replaceAll("\n", "<br/>\n");
		return source;
	}

	/**
	 * html <br>을 개행문자(\n)로 치환한다.
	 *
	 * @param source 변경 할 데이터
	 * @return String 치환 된 데이터  
	 */
	public static String newline2string(String source, String target) {
		source = (source == null ? "" : source.trim());
		source = source.replaceAll("\n", "\n" + target);
		return source;
	}

	/**
	 * 특수문자를 html 문자로 치환한다.
	 *
	 * @param source 변경 할 데이터
	 * @return String 치환 된 데이터  
	 */
	public static String plain2html(String source) {
		source = (source == null ? "" : source.trim());
		source = html2special(source);
		source = newline2br(source);
		return source;
	}

	/**
	 * 문자열을 치환한다.
	 *
	 * @param source 원문 데이터
	 * @param src 치환 대상 데이터
	 * @param dest 치환 데이터
	 * @return String 치환 된 데이터  
	 */
	public static String replace(String source, String src, String dest) {
		if (source == null || src == null || dest == null) {
			return source;
		}
		int fromIndex = 0;
		while (source.indexOf(src, fromIndex) != -1) {
			String prefixstr = source.substring(0, source.indexOf(src, fromIndex));
			String surfixstr = source.substring(source.indexOf(src, fromIndex) + src.length(), source.length());
			source = prefixstr + dest + surfixstr;
			fromIndex = prefixstr.length() + dest.length();
		}
		return source;
	}


	/**
	 * 태그 문자열을 삭제한다.
	 *
	 * @param source 원문 데이터
	 * @return String 태그가 삭제 된 데이터  
	 */
	public static String removeTag(String source) {
		if (source == null) {
			return source;
		}
		return source.replaceAll("\\<.*?\\>", "");
	}
	
	/**
	 * Decimal 패턴 데이터인지 확인한다.
	 *
	 * @param value 원문 데이터
	 * @param upPattern decimal의 십진수 자리수 
	 * @param downPattern decimal의 소수점 자리수 
	 * @return boolean Decimal 패턴 체크 여부 
	 */
	public static boolean isDecimal(String value, Integer upPattern, Integer downPattern) {
		if (value == null) {
			return false;
		}
		
		return Pattern.compile(String.format(PATTERN_DECIMAL, upPattern, downPattern)).matcher(value).matches();
	}
	
	/**
	 * Numeric 패턴 데이터인지 확인한다.
	 *
	 * @param value 원문 데이터
	 * @return boolean Numeric 패턴 체크 여부 
	 */
	public static boolean isNumeric(String value) {
		if (value == null) {
			return false;
		}
		return PATTERN_DIGIT.matcher(value).matches();
	}
	
	/**
	 * MacId 패턴 데이터인지 확인한다.
	 *
	 * @param macId 원문 데이터
	 * @return boolean macId 패턴 체크 여부 
	 */
	public static boolean isMacId(String macId) {
		return PATTERN_MACID_EXACT.matcher(macId).matches();
	}

	/**
	 * email 패턴 데이터인지 확인한다.
	 *
	 * @param email 원문 데이터
	 * @return boolean email 패턴 체크 여부 
	 */
	public static boolean isValidEmail(String email) {
		return PATTERN_EMAIL.matcher(email).matches();
	}

	/**
	 * 우편번호 패턴 데이터인지 확인한다.
	 *
	 * @param zip 원문 데이터
	 * @return boolean 우편번호 패턴 체크 여부 
	 */
	public static boolean isValidZip(String zip) {
		return PATTERN_ZIP.matcher(zip).matches();
	}

	/**
	 * phone 패턴 데이터인지 확인한다.
	 *
	 * @param phone 원문 데이터
	 * @return boolean phone 패턴 체크 여부 
	 */
	public static boolean isValidPhone(String phone) {
		return PATTERN_PHONE.matcher(phone).matches();
	}
	
	/**
	 * phone no 패턴 데이터인지 확인한다.
	 *
	 * @param phone 원문 데이터
	 * @return boolean phone no 패턴 체크 여부 
	 */
	public static boolean isValidPhoneNo(String phone) {
		return PATTERN_PHONE_NO.matcher(phone).matches();
	}

	/**
	 * mobile no 패턴 데이터인지 확인한다.
	 *
	 * @param mobile 원문 데이터
	 * @return boolean mobile no 패턴 체크 여부 
	 */
	public static boolean isValidMobile(String mobile) {
		return PATTERN_KOREAN_MOBILE.matcher(mobile).matches();
	}

	/**
	 * 알파벳 패턴 데이터인지 확인한다.
	 *
	 * @param value 원문 데이터
	 * @return boolean 알파벳 패턴 체크 여부 
	 */
	public static boolean isValidAlphabet(String value) {
		return PATTERN_ALPHABET.matcher(value).matches();
	}

	/**
	 * 소문자 알파벳 패턴 데이터인지 확인한다.
	 *
	 * @param value 원문 데이터
	 * @return boolean 소문자 알파벳 패턴 체크 여부 
	 */
	public static boolean isValidLowerAlphabet(String value) {
		return PATTERN_LOWER_ALPHABET.matcher(value).matches();
	}

	/**
	 * 대문자 알파벳 패턴 데이터인지 확인한다.
	 *
	 * @param value 원문 데이터
	 * @return boolean 대문자 알파벳 패턴 체크 여부 
	 */
	public static boolean isValidUpperAlphabet(String value) {
		return PATTERN_UPPER_ALPHABET.matcher(value).matches();
	}

	/**
	 * 알파벳 및 숫자 패턴 데이터인지 확인한다.
	 *
	 * @param value 원문 데이터
	 * @return boolean 알파벳 및 숫자 패턴 체크 여부 
	 */
	public static boolean isValidAlphaDigit(String value) {
		return PATTERN_ALPHABET_DIGIT.matcher(value).matches();
	}

	/**
	 * 소문자 알파벳 및 숫자 패턴 데이터인지 확인한다.
	 *
	 * @param value 원문 데이터
	 * @return boolean 소문자 알파벳 및 숫자 패턴 체크 여부 
	 */
	public static boolean isValidLowerAlphaDigit(String value) {
		return PATTERN_LOWER_ALPHABET_DIGIT.matcher(value).matches();
	}

	/**
	 * 대문자 알파벳 및 숫자 패턴 데이터인지 확인한다.
	 *
	 * @param value 원문 데이터
	 * @return boolean 대문자 알파벳 및 숫자 패턴 체크 여부 
	 */
	public static boolean isValidUpperAlphaDigit(String value) {
		return PATTERN_UPPER_ALPHABET_DIGIT.matcher(value).matches();
	}

	/**
	 * 알파벳 및 숫자 및 스페이스 패턴 데이터인지 확인한다.
	 *
	 * @param value 원문 데이터
	 * @return boolean 알파벳 및 숫자 및 스페이스 패턴 체크 여부 
	 */
	public static boolean isValidAlphaDigitWithWhiteSpace(String value) {
		return PATTERN_ALPHABET_DIGIT_WITH_WHITESPACE.matcher(value).matches();
	}

	/**
	 * 숫자로 시작되는 문자열을 공백문자열로 replace (trim) 한다.
	 *
	 * @param source 원문 데이터
	 * @return String 숫자로 시작되는 문자열이 제거 된 문자열 
	 */
	public static String ltrimDigits(String source) {
		try {
			StringBuffer output = new StringBuffer();
			Matcher matcher = PATTERN_TRIM_FIRST_DIGITS.matcher(source);
			while (matcher.find()) {
				matcher.appendReplacement(output, "");
			}
			matcher.appendTail(output);
			return output.toString();
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			return source;
		}
	}
	
	/**
	 * 문자열 배열에 value값을 append 시킨다. 
	 *
	 * @param source 원문 문자열 배열 데이터
	 * @param value 추가 할 데이터
	 * @return String append 된 문자열 
	 */
	public static String join(String[] sources, String value) {
		if (sources == null || sources.length == 0) {
			return "";
		}
		
		if (sources.length == 0) {
			return sources[0];
		}
		
		StringBuilder sb = new StringBuilder();
		
		if (value == null) {
			value = "";
		}
		
		int index = 0;
		for (String source : sources) {
			sb.append(source == null ? "" : source);
			index++;
			if (index < sources.length) {
				sb.append(value);
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * value 값에 '0' 문자열을 길이만큼 채운다.
	 *
	 * @param value 값 
	 * @param zeroCount 채울 0의 길이 
	 * @return String 0 이 append 된 문자열 
	 */
	public static String zeroPadding(int value, int zeroCount) {
		return String.format("%0"+zeroCount+"d", value);
	}
	
    /**
     * IP 패턴이 맞는지 체크한다. 
     */
    public static Boolean checkIpPattern(String str)
    {
        if(StringUtils.isEmpty(str))
        {
            return null;
        }
        
        return PATTERN_MACID_IP.matcher(str).matches();
    }

    public static String makeString(String... params)
    {
        StringBuffer sb = new StringBuffer();
        for (String param : params)
        {
            sb.append(param);
        }
        return sb.toString();
    }

	public static int countChar(String str, char ch)
	{
		if(StringUtils.isEmpty(str))
		{
			return 0;
		}

		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ch) {
				count++;
			}
		}
		return count;
	}
	public static String getPhoneNo(String str) {
	    String phoneNo = null;
	    if(str.length() == 10) {
	        phoneNo = StringUtils.joinWith("-", StringUtils.substring(str, 0, 3),
                StringUtils.substring(str, 3, 6),
                StringUtils.substring(str, 7, 10));
        }
        else if(str.length() == 11) {
            phoneNo = StringUtils.joinWith("-", StringUtils.substring(str, 0, 3),
                StringUtils.substring(str, 3, 7),
                StringUtils.substring(str, 7, 11));
        }
        else {
            phoneNo = str;
        }
	    
	    return phoneNo;
	}
}
