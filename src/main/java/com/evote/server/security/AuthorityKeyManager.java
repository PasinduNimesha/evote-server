package com.evote.server.security;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

// Removed javax.annotation.PostConstruct as it is not available in JDK 9+
import java.security.*;
import java.util.Base64;

@Component
public class AuthorityKeyManager {

    private KeyPair keyPair;
    private String publicKeyPem;

    public AuthorityKeyManager() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            keyGen.initialize(2048, new SecureRandom());
            keyPair = keyGen.generateKeyPair();

            // Convert public key to PEM format
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                    Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(publicKeyBytes) +
                    "\n-----END PUBLIC KEY-----";
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize authority keys", e);
        }
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public String getPublicKeyPem() {
        return publicKeyPem;
    }
}
