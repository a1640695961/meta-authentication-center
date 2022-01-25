package com.meta.oauth2.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

/**
 * @author Xiong Mao
 * @date 2022/01/19 02:19
 **/
public class CustomAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {
    @Autowired
    private AuthorizationCodeStore authorizationCodeStore;

    protected void store(String code, OAuth2Authentication authentication) {
        authorizationCodeStore.put(code, authentication);
    }

    protected OAuth2Authentication remove(String code) {
        return authorizationCodeStore.remove(code);
    }
}
