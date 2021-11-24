package com.university.medvladbe.service;


import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.university.medvladbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;

@Service
public class OtcService {
    @Autowired
    private EmailService emailService;
    @Autowired
    private OtcService otcService;
    @Autowired
    private UserRepository userRepository;

    private static final Integer EXPIRE_MINS = 5;

    private LoadingCache<String, Integer> otpCache;

    public OtcService(){
        super();
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void forgotPassword(String email) throws UsernameNotFoundException,MessagingException{
        if (userRepository.findByEmail(email)==null){
            throw new UsernameNotFoundException("");
        }
        int otc = otcService.generateOTP(email);
        String link = "http://localhost:3000/resetPassword/" + otc +"/" + email ;
        String emailText = "Access <a href=\"" + link + "\"" + ">" + link + "</a> to reset you password. Available 5 minutes.";
        emailService.sendHtmlEmail(email, "Password Reset", emailText);
    }

    //This method is used to push the opt number against Key. Rewrite the OTP if it exists
    //Using user id  as key
    public int generateOTP(String key){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    //This method is used to return the OPT number against Key->Key values is username
    public int getOtp(String key){
        try{
            return otpCache.get(key);
        }catch (Exception e){
            return 0;
        }
    }

    //This method is used to clear the OTP catched already
    public void clearOTP(String key){
        otpCache.invalidate(key);
    }
}
