package com.evote.server.service;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
public class OtpService {

    private final SecureRandom random = new SecureRandom();

    public String generateOtp() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}

