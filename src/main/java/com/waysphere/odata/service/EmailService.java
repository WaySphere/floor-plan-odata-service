package com.waysphere.odata.service;

import com.waysphere.odata.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OrganizationRepository organizationRepository;

    private ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();

    public String sendOtpEmail(String toEmail) {
        String domain = extractDomain(toEmail);

        // ✅ Check if the domain exists in Organization table
        boolean domainExists = organizationRepository.findByDomainName(domain).isPresent();
        if (!domainExists) {
            return "Domain not authorized to receive OTP.";
        }

        String otp = generateOtp();
        otpStore.put(toEmail, otp);

        String subject = "Your OTP Code";
        String body = "Dear User,\n\nYour One-Time Password (OTP) is: " + otp +
                "\n\nPlease do not share this code with anyone.\n\nRegards,\nTeam Waysphere";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);

        return "OTP sent to " + toEmail;
    }

    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStore.remove(email);
            return true;
        }
        return false;
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    private String extractDomain(String email) {
        return email.substring(email.indexOf("@") + 1).toLowerCase();
    }
}

