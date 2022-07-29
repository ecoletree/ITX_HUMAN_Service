package kr.co.ecoletree.common.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import net.sf.json.JSONObject;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class AesUtil {
    private final int keySize;
    private final int iterationCount;
    private final Cipher cipher;
    
    public AesUtil(int keySize, int iterationCount) {
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw fail(e);
        }
    }
    
    private String getSecretKey(int param) {
    	String text1="";
    	String alphabet = "abcdefghijklmnopqrstuvwxyz";
    	String num = "0123456789";
	    for (int i=0; i < (param/2) ; i++) {
	    	text1 += alphabet.charAt((int)Math.floor(Math.random() * alphabet.length()));
	    	text1 += num.charAt((int)Math.floor(Math.random() * num.length()));
	    }
	    return text1;
    }
    
    /**
     * 암호화 할 데이터를 암호화 한다
     * @param ciphertext
     * @return
     */
    public String encrypt(String ciphertext) {
    	String passphrase = getSecretKey(16);
    	String iv = Hex.encodeHexString(getSecretKey(16).getBytes());
    	String salt = Hex.encodeHexString(getSecretKey(16).getBytes());
    	String aesParamData = "";
    	try {
            SecretKey key = generateKey(salt, passphrase);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(hex(iv)));
            byte[] encrypted = Base64.encodeBase64(cipher.doFinal(ciphertext.getBytes()));
            ciphertext = new String(encrypted, "UTF-8");
            aesParamData = passphrase+"§"+iv + "§" + salt + "§" + ciphertext; 
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }catch (Exception e){
            return null;
        }
    	
    	return  aesParamData;
    }
    
    public static void main(String[] args) {
    	try {
    		AesUtil aesUtil = new AesUtil(256, 1000);
    		
    		JSONObject json = new JSONObject();
    		json.accumulate("data", "tewat");
    		
    		String decryptedPassword = aesUtil.encrypt(json.toString());
    		System.out.println(decryptedPassword);
    		
    		String plaintext = "";
            if (decryptedPassword != null && decryptedPassword.split("§").length == 4) {
            	try {
            		String secret = decryptedPassword.split("§")[0];
            		String iv = decryptedPassword.split("§")[1];
            		String salt = decryptedPassword.split("§")[2];
            		String data = decryptedPassword.split("§")[3];
    	        	plaintext = aesUtil.decrypt(salt, iv, secret, data);
    	        	
    	        	System.out.println(plaintext);
            	} catch(Exception e) {
            		e.printStackTrace();
            	}
            }
    		
    	} catch(Exception e) {
    		
    	}
    	
    }
    
    /**
     * 데이터를 복호화
     * @param salt
     * @param iv
     * @param passphrase
     * @param ciphertext
     * @return
     */
    public String decrypt(String salt, String iv, String passphrase, String ciphertext) {
        try {
            SecretKey key = generateKey(salt, passphrase);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
            return new String(decrypted, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }catch (Exception e){
            return null;
        }
    }
    
    
    
    
    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
        try {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
        }
        catch (InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException e) {
        	e.printStackTrace();
            return null;
        }
    }
    
    private SecretKey generateKey(String salt, String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }

    public static byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }
    
    public static byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        }
        catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private IllegalStateException fail(Exception e) {
        return null;
    }

}