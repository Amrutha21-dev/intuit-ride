package com.intuit.demo.service;

public interface CryptoService {
    public String encrypt(String message);

    public String decrypt(String encryptedString);

    public String encrypt(String message, String cipherText, String vector);

    public String decrypt(String encryptedValue, String cipherText, String vector);
}
