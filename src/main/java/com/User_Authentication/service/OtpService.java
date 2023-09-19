package com.User_Authentication.service;



import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Value("${twilio.account.sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;
    // Store OTPs temporarily in memory (You should use a database or a cache in a production environment)
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();


    public void generateAndSendOtp(String mobileNumber) {
        String otp = generateOtp();
        // Send OTP via Twilio
        Twilio.init(twilioAccountSid, twilioAuthToken);
        Message.creator(
                new PhoneNumber(mobileNumber),
                new PhoneNumber(twilioPhoneNumber),
                "Your OTP is: " + otp
        ).create();

        // Store OTP in memory
        otpStorage.put(mobileNumber, otp);
    }

    public boolean verifyOtp(String mobileNumber, String otp) {
        // Get the stored OTP for the mobile number
        String storedOtp = otpStorage.get(mobileNumber);
        if (storedOtp != null && storedOtp.equals(otp)) {
            // OTP is valid, remove it from storage (single use)
            otpStorage.remove(mobileNumber);
            return true;
        }
        return false;
    }

    private String generateOtp() {
        // Generate a 6-digit numeric OTP
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }
}
