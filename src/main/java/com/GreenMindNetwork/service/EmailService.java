package com.GreenMindNetwork.service;

import com.GreenMindNetwork.payloads.EmailResponse;
import jakarta.servlet.http.HttpSession;

public interface EmailService {
    boolean sendEmailOTP(String to,String message);
    String generateOtp();
    Boolean verifyOtp(EmailResponse emailResponse);
     boolean isVerifyOtp = false;
}
