package com.dailystudy.swinglab.service.framework.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/** 
 * AES128Util 클래스 
 * AES128 암호화/복호화 (Mysql & MariaDB 암호화 호환 때문에 사용)
 */	
@Slf4j
public class AES128Util {	
	private static final Charset ENCODING_TYPE = StandardCharsets.UTF_8;

    private static final String INSTANCE_TYPE = "AES/ECB/PKCS5Padding";

    private SecretKeySpec secretKeySpec;

    private Cipher cipher;

    private IvParameterSpec ivParameterSpec;

    /** 
	 * AES128Util 생성자 
	 * 
	 * @param key 암호화 key
	 */
    public AES128Util(final String key) {
        try {
            byte[] keyBytes = key.getBytes(ENCODING_TYPE);
            secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            cipher = Cipher.getInstance(INSTANCE_TYPE);
            ivParameterSpec = new IvParameterSpec(keyBytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
    }

    /** 
	 * AES128로 데이터를 암호화한다.
	 * 
	 * @param str 암호화할 데이터 
	 * @return 암호화 된 데이터 
	 */
    public String encrypt(final String str) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cipher.doFinal(str.getBytes(ENCODING_TYPE));
        return new String(Base64.getEncoder().encode(encrypted), ENCODING_TYPE);
    }

    /** 
   	 * AES128로 데이터를 복호화한다.
   	 * 
   	 * @param str 복호화할 데이터 
   	 * @return 복호화 된 데이터 
   	 */
    public String decrypt(final String str) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decoded = Base64.getDecoder().decode(str.getBytes(ENCODING_TYPE));
        return new String(cipher.doFinal(decoded), ENCODING_TYPE);
    }
	
//	public static void main(String[] args) throws Exception  {
//		String key = "passwordpassword";
//		AES128Util aES128Util = new AES128Util(key);
//		System.out.println(aES128Util.encrypt("1234"));
//		System.out.println(aES128Util.decrypt(aES128Util.encrypt("1234")));
//		/* mariadb 에서 암호화 샘플 .
//		 * select AES_DECRYPT(FROM_BASE64('FDC7iwDEdIznW/WRc4SrZw=='), 'passwordpassword');
//
//			select TO_BASE64(AES_ENCRYPT('1234', 'passwordpassword'));
//
//		 * 
//		 * */
//	}
}
