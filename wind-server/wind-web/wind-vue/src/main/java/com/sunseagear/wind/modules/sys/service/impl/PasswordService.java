package com.sunseagear.wind.modules.sys.service.impl;

import com.sunseagear.wind.modules.sys.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("passwordService")
public class PasswordService {


    public void encryptPassword(User user) {

        String newPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(newPassword);
    }

    public String getPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
