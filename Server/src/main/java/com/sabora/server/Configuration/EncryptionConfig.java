package com.sabora.server.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {

    @Value("${ENCRYPTION_SECRET_KEY}")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }
}