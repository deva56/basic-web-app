package com.futurefactorytech.reviewer.application_services.services.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public final class CryptoManagerUtil {

    private static final String ENCRYPTION_DECRYPTION_ALG = "AES";
    private static final String CIPHER_TRANSFORMATION_CONSTANT = "AES/ECB/PKCS5Padding";
    private static final String SECRET_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";
    @Value("${application-config.token-encryption-password}")
    private static String TOKEN_ENCRYPTION_PASSWORD;
    @Value("${application-config.token-encryption-salt}")
    private static String TOKEN_ENCRYPTION_SALT;
    private static final Logger logger = LoggerFactory.getLogger(CryptoManagerUtil.class);
    private static SecretKeySpec secretKey;

    static {
        KeySpec keySpec = new PBEKeySpec(TOKEN_ENCRYPTION_PASSWORD.toCharArray(), TOKEN_ENCRYPTION_SALT.getBytes(),
                65536, 256);
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(SECRET_FACTORY_ALGORITHM);
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            secretKey = new SecretKeySpec(key, ENCRYPTION_DECRYPTION_ALG);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Exception caught while initializing CryptoManagerUtil.", e);
        }
    }

    public static String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            logger.error("Encryption text is empty!");
            return null;
        }
        Cipher cipher;
        String encryptedString = null;
        try {
            cipher = Cipher.getInstance(CIPHER_TRANSFORMATION_CONSTANT);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedText = cipher.doFinal(plainText.getBytes());
            encryptedString = Base64.getEncoder().encodeToString(encryptedText);
        } catch (Exception e) {
            logger.error("Exception caught while encrypting token.", e);
        }
        return encryptedString;
    }

    public static String decrypt(String cipherText) {
        if (cipherText == null || cipherText.isEmpty()) {
            logger.error("Decryption text is empty!");
            return null;
        }
        String decryptedString = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_TRANSFORMATION_CONSTANT);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedText = Base64.getDecoder().decode(cipherText.getBytes());
            decryptedString = new String(cipher.doFinal(encryptedText));
        } catch (Exception e) {
            logger.error("Exception caught while decrypting token.", e);
        }
        return decryptedString;
    }
}
