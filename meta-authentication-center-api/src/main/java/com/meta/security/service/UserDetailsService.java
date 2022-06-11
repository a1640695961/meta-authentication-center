package com.meta.security.service;

import com.meta.domain.entity.AccountDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Xiong Mao
 * @date 2022/01/18 18:01
 **/
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AccountDetails("abc", "admin",
                "{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG"
                , "17521014072", true, true, true, true);
    }
}
