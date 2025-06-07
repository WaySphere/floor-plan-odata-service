package com.waysphere.odata.controller;


import com.waysphere.odata.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // 1. Send OTP to email
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {

        String domain = emailService.extractDomain(email);

        // ✅ Check if the domain exists in Organization table
        boolean domainExists = emailService.isDomainAuthorized(domain);
        if (!domainExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Domain not authorized to receive OTP.");
        }

        String result = emailService.sendOtpEmail(email);
        return ResponseEntity.ok(result);
    }

    // 2. Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean valid = emailService.verifyOtp(email, otp);
        if (valid) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.status(400).body("Invalid OTP.");
        }
    }
}
