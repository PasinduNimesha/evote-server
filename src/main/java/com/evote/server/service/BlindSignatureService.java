package com.evote.server.service;

import com.evote.server.security.AuthorityKeyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Signature;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlindSignatureService {

    private final AuthorityKeyManager authorityKeyManager;

    public String signBlindedVote(String blindedVote) {
        try {
            // Create SHA-256 hash of the blinded vote
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(Hex.decode(blindedVote));

            // Sign the hash with RSA private key
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(authorityKeyManager.getPrivateKey());
            signature.update(hash);
            byte[] signatureBytes = signature.sign();

            // Convert to hex string
            return Hex.toHexString(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign blinded vote", e);
        }
    }
}
