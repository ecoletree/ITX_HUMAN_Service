/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.common.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import kr.co.ecoletree.common.exception.ETException;


/**
 * @author aileen
 *
 */
public class CryptoUtil {

	/**
	 * 유저 패스워드 암호화
	 * @param pwd
	 * @return
	 * @throws ETException
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static String encodePassword(String pw) throws ETException, NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] b = encryptSHA256(pw);
		Base64 base = new Base64();
		return getHexString(base.encode(b));
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 * @throws ETException
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 */
	private static byte[] encryptSHA256(String data) throws ETException, NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] raw = md.digest(data.getBytes("UTF-8"));
        return raw;
    }
	
	/**
	 * 
	 * @param raw
	 * @return
	 */
	private static String getHexString(byte[] raw) {
		StringBuffer result = new StringBuffer();
        for(int i=0; i<raw.length; i++) {
            result.append(Integer.toHexString(raw[i]));
        }
        return result.toString();
	}
	
	/**
	 * AES256 으로 암호화 한다.
	 * 
	 * @param str
	 *            암호화할 문자열
	 * @return
	 * @throws NoSuchAlgorithmETException
	 * @throws GeneralSecurityETException
	 * @throws UnsupportedEncodingETException
	 */
	public static String encryptAES(String str, byte[] keyBytes) throws NoSuchAlgorithmException,
			GeneralSecurityException, UnsupportedEncodingException {
		
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		Base64 base = new Base64();
		String enStr = new String(base.encode(encrypted));
		return enStr;
	}
		
	/**
	* AES256으로 암호화된 txt 를 복호화한다.
	* 
	* @param str
	*            복호화할 문자열
	* @return
	* @throws NoSuchAlgorithmETException
	* @throws GeneralSecurityETException
	* @throws UnsupportedEncodingETException
	*/
	public static String decryptAES(String str, byte[] keyBytes) throws NoSuchAlgorithmException,
		GeneralSecurityException, UnsupportedEncodingException {
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keySpec);
		Base64 base = new Base64();
		byte[] byteStr = base.decode(str.getBytes());
		
		return new String(c.doFinal(byteStr), "UTF-8");
	}
	
	/**
	 * @param str
	 * @return
	 */
	public static String getBase64String(String str) {
		byte[] byteArr = str.getBytes();
		String newStr = new String(Base64.encodeBase64(byteArr));
		return newStr;
	}
}
