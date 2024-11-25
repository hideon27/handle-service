package com.example.handle.function;
import java.security.SecureRandom;
import java.util.Base64;

public class Signcreate {
    public String GenerateSecretKey(){
        byte[] key = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);

        // 将密钥转换为Base64编码的字符串
        return Base64.getEncoder().encodeToString(key);
    }
}
