package com.meta.security.service;

import com.meta.domain.entity.AccountDetails;
import com.meta.domain.po.AccountPo;
import com.meta.model.LoginType;
import com.meta.security.password.MetaPasswordEncoder;
import com.meta.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xiong Mao
 * @date 2022/01/18 18:01
 **/
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private AccountService accountService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] split = username.split(":");
        String accountName = split[0];
        String password = split[0];
        String verifyCode = split[0];
        String loginType = split[0];

        AccountPo accountPo = accountService.loadUserByUsername(accountName);

        if (LoginType.VERIFICATION_CODE.name().equals(loginType)) {
            //密码设置为短信验证码
            //TODO
            accountPo.setPassword("1");
        }
        AccountDetails accountDetails = new AccountDetails(accountPo.getId(), accountName, accountPo.getPassword(), accountPo.getMobileNo(),
                accountPo.isAccountNonExpired(), accountPo.isAccountNonLocked(), accountPo.isCredentialsNonExpired(), accountPo.isEnabled());
        return accountDetails;
    }


    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", bCryptPasswordEncoder);
        encoders.put("meta", new MetaPasswordEncoder());

        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(
                "bcrypt", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(bCryptPasswordEncoder);


        String encode = passwordEncoder.encode("1");
        System.out.println(encode);
    }
}
