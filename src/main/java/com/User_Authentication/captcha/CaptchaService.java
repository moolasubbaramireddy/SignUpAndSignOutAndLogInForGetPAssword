package com.User_Authentication.captcha;

import com.User_Authentication.entity.User;
import com.User_Authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Configuration
public class CaptchaService {

    @Autowired
    private UserRepository userRepository;

    public String captchaGenerator(String email) throws Exception{
        String s="qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789";
        String captcha="";
        for(int i=0;i<5; i++){
            int index=(int)(Math.random()*62);
            captcha +=s.charAt(index);
        }
        Optional<User> userByEmail= userRepository.findByEmail(email);
        if(!userByEmail.isPresent()){
            throw  new Exception("data is not present foe this Email:"+ userByEmail);
        }

        User user=new User();
        user.setCaptcha(captcha);
        userRepository.save(user);
        return captcha;
    }
}
