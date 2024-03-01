package com.dailystudy.swinglab.service.framework.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA256 클래스 
 * SHA256 암호화 기능을 제공한다.
 */
public class SHA256 {
	/**
	 * SHA256 으로 암호화하여 데이터를 리턴한다.
	 *
	 * @param value 암호화할 String Data
	 * @return String SHA256으로 암호화 된 데이터
	 */
	public static String encryptSHA256(String value) throws NoSuchAlgorithmException{
	     StringBuffer encryptData = new StringBuffer();
	     MessageDigest sha = MessageDigest.getInstance("SHA-256");
	     
	     sha.update(value.getBytes());
	     
	     byte[] digest = sha.digest();
	     
	     for (int i=0; i<digest.length; i++) {
	    	 encryptData.append(String.format("%02x", digest[i] & 0xFF));
	     }
	     
	     return encryptData.toString();
	 }
}
