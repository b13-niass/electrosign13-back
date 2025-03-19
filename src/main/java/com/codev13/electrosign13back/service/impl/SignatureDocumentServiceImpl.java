package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.service.SignatureDocumentService;
import com.codev13.electrosign13back.utils.AESUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Service
public class SignatureDocumentServiceImpl implements SignatureDocumentService {
    @Value("${keySecret}")
    private String SECRET;
    public String signDocument(InputStream fileStream, String encryptedPrivateKey) {
        try {
            // 1. Déchiffrer la clé privée
            byte[] decryptedPrivateKeyBytes = AESUtil.decrypt(SECRET, encryptedPrivateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decryptedPrivateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // 2. Lire le fichier et créer un hash SHA-256
            byte[] fileHash = generateFileHash(fileStream);

            // 3. Signer le hash avec la clé privée
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(fileHash);
            byte[] digitalSignature = signature.sign();

            // 4. Retourner la signature en Base64
            return Base64.getEncoder().encodeToString(digitalSignature);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la signature du fichier", e);
        }
    }

//    PrivateKey privateKey = keyPair.getPrivate();
//    String signature = signatureService.signFile(file.getInputStream(), privateKey);

//    PublicKey publicKey = keyPair.getPublic();
//    boolean isValid = verificationService.verifySignature(file.getInputStream(), signature, publicKey);

    public boolean verifySignature(InputStream fileStream, String signature, PublicKey publicKey) {
        try {
            byte[] fileHash = generateFileHash(fileStream);

            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(fileHash);
            byte[] decodedSignature = Base64.getDecoder().decode(signature);

            return sig.verify(decodedSignature);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la vérification de la signature", e);
        }
    }
    private byte[] generateFileHash(InputStream fileStream) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = fileStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }
        return digest.digest();
    }
}
