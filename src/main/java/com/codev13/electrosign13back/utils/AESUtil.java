package com.codev13.electrosign13back.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {
    private static final String ALGORITHM = "AES";

    public static String encrypt(String secret, byte[] data) {
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data);
            return Base64.getEncoder().encodeToString(encryptedData);
        }catch (Exception e){
            throw new RuntimeException("Erreur lors de l'encryption", e);
        }
    }

    public static byte[] decrypt(String secret, String encryptedData){
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        }catch (Exception e){
            throw new RuntimeException("Erreur lors de la décryption", e);
        }
    }
}
