package com.codev13.electrosign13back.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class KeyGeneratorUtil {


    public static KeyPair getKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            return kpg.genKeyPair();
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors de la génération de clés RSA", e);
        }
    }

    public static String encodeKey(byte[] key) {
        return Base64.getEncoder().encodeToString(key);
    }
}
