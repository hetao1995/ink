package xyz.itao.ink.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author hetao
 * @date 2018-12-01 23:02
 * @description 加密解密相关工具类
 */
public class CryptoUtils {
    /**
     * aes 加密
     *
     * @param data 需要加密的字符串
     * @param key  加密的key
     * @return 加密后的Base64编码字符串
     * @throws Exception
     */
    public static String enAes(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return new BASE64Encoder().encode(encryptedBytes);
    }

    /**
     * aes解密
     *
     * @param data 需要解密的Base64编码字符串
     * @param key  aes密钥
     * @return 解密结果
     * @throws Exception
     */
    public static String deAes(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] cipherTextBytes = new BASE64Decoder().decodeBuffer(data);
        byte[] decValue = cipher.doFinal(cipherTextBytes);
        return new String(decValue);
    }
}
