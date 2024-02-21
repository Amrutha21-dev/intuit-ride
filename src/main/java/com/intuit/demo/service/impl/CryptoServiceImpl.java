package com.intuit.demo.service.impl;

import com.intuit.demo.service.CryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
@Slf4j
public final class CryptoServiceImpl implements CryptoService {

    private final String AES_ALGORITHM = "AES";

    @Value("${crypto.cipher-text}")
    private String defaultCipherText;

    @Value("${crypto.vector}")
    private String defaultVector;

    public String encrypt(String message){
        return encrypt(message, defaultCipherText, defaultVector);
    }

    public String decrypt(String encryptedString){
        return decrypt(encryptedString, defaultCipherText, defaultVector);
    }

    public String encrypt(String message, String cipherText, String vector){
        try{
            String algoPKCSPadding = "AES/CBC/PKCS5Padding";
            Cipher cipher = Cipher.getInstance(algoPKCSPadding);
            SecretKeySpec keyspec = new SecretKeySpec(cipherText.getBytes(), AES_ALGORITHM);
            IvParameterSpec ivspec = new IvParameterSpec(vector.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encryptedText = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedText);
        } catch (Exception e){
            log.error("Error when encrypting", e);
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String encryptedValue, String cipherText, String vector){
        try{
            String algoNoPadding = "AES/CBC/NoPadding";
            Cipher cipher = Cipher.getInstance(algoNoPadding);
            SecretKeySpec keyspec = new SecretKeySpec(cipherText.getBytes(), AES_ALGORITHM);
            IvParameterSpec ivspec = new IvParameterSpec(vector.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] base64 = Base64.getDecoder().decode(encryptedValue);
            byte[] original  = cipher.doFinal(base64);
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e){
            log.error("Error when decrypting", e);
            throw new RuntimeException(e);
        }
    }
}
