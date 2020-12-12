package com.example.veriparkimkb.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    private String aesKey;
    private String aesIv;
    private byte[] aesKeyBytes;
    private byte[] ivKeyBytes;

    public AESUtil(String aesKey, String aesIv) {
        this.aesKey = aesKey;
        this.aesIv = aesIv;
        aesKeyBytes = Base64.getMimeDecoder().decode(aesKey);
        ivKeyBytes = Base64.getMimeDecoder().decode(aesIv);
    }

    public String aesEncrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        Key secretKeySpec = new SecretKeySpec(aesKeyBytes, "AES");
        AlgorithmParameterSpec algorithmParameterSpec = new IvParameterSpec(ivKeyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, algorithmParameterSpec);
        return Base64.getMimeEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    public String aesDecrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        Key secretKeySpec = new SecretKeySpec(aesKeyBytes, "AES");
        AlgorithmParameterSpec algorithmParameterSpec = new IvParameterSpec(ivKeyBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, algorithmParameterSpec);
        byte[] decodedData = cipher.doFinal(Base64.getMimeDecoder().decode(data));
        return new String(decodedData, StandardCharsets.UTF_8);
    }
}
