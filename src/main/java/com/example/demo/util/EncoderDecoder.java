package com.example.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.logging.Logger;

@Component
public class EncoderDecoder {

    private static final Logger logger = Logger.getLogger(EncoderDecoder.class.getName());

    @Value("${hrs.token.secretKey}")
    private String tokenSecret;

    private SecretKey secretKey;

    // Method to generate the secret key from the token secret
    private synchronized void generateSecretKey() {
        if (secretKey == null) {
            byte[] decodedKey = Base64.getDecoder().decode(tokenSecret);
            this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            logger.info("Secret key generated.");
        }
    }

    // Encrypt the plaintext using AES encryption
    public String encryptUsingSecretKey(String plainText) {
        if (secretKey == null) {
            generateSecretKey(); // Generate secret key if not initialized
        }
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes); // Return encrypted text as Base64 encoded string
        } catch (GeneralSecurityException e) {
            logger.severe("Encryption error: " + e.getMessage());
            throw new RuntimeException("Encryption error: " + e.getMessage(), e);
        }
    }

    // Decrypt the encrypted text using AES decryption
    public String decryptUsingSecretKey(String encryptedText) {
        if (secretKey == null) {
            generateSecretKey(); // Generate secret key if not initialized
        }
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText); // Decode Base64 encrypted string
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes); // Decrypt the bytes
            return new String(decryptedBytes, StandardCharsets.UTF_8); // Return decrypted text as UTF-8 string
        } catch (GeneralSecurityException e) {
            logger.severe("Decryption error: " + e.getMessage());
            throw new RuntimeException("Decryption error: " + e.getMessage(), e);
        }
    }
}
