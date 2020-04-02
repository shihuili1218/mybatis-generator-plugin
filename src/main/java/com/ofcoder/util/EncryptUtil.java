package com.ofcoder.util;

import com.ofcoder.exception.EncryptException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author far.liu
 */
public class EncryptUtil {

    private static final String ENCODING_UTF8 = "UTF-8";
    private static final String ENCRYPT_AES = "AES";
    public static final String MD5 = "MD5";

    public static String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decodeBase64(String encodedStr) {
        return Base64.getDecoder().decode(encodedStr);
    }

    public static byte[] aesCBCEncrypt(String src, String sha1key, String sha1e) throws Exception {
        byte[] key = sha1prng(sha1key);
        byte[] e = sha1prng(sha1e);
        return aesCBCEncrypt(src.getBytes(ENCODING_UTF8), key, e);
    }

    public static String aesCBCDecrypt(byte[] src, String sha1key, String sha1e) throws Exception {
        byte[] key = sha1prng(sha1key);
        byte[] e = sha1prng(sha1e);
        return aesCBCDecrypt(src, key, e);
    }

    public static byte[] aesCBCEncrypt(byte[] src, byte[] key, byte[] e) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, ENCRYPT_AES);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(e);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(src);
        return encrypted;
    }

    public static String aesCBCDecrypt(byte[] src, byte[] key, byte[] e) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, ENCRYPT_AES);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(e);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(src);
        return new String(original, ENCODING_UTF8);
    }

    public static byte[] sha1prng(String sha1Key)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator keygen = KeyGenerator.getInstance(ENCRYPT_AES);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(sha1Key.getBytes(ENCODING_UTF8));
        keygen.init(128, secureRandom);
        SecretKey originalKey = keygen.generateKey();
        byte[] raw = originalKey.getEncoded();
        return raw;
    }

    public static String md5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes(ENCODING_UTF8);
            MessageDigest mdInst = MessageDigest.getInstance(MD5);
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            throw new EncryptException(String.format("call EncryptUtil.md5, e.getMessage:[%s]", e.getMessage()), e);
        }
    }
}

