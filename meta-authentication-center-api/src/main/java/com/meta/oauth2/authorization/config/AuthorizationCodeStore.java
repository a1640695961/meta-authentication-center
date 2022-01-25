package com.meta.oauth2.authorization.config;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xiong Mao
 * @date 2022/01/19 02:22
 **/
@Repository
public class AuthorizationCodeStore {

    private Map<String, OAuth2Authentication> cache = new ConcurrentHashMap<String, OAuth2Authentication>();

    public void put(String code, OAuth2Authentication oAuth2Authentication) {
        cache.put(code, oAuth2Authentication);
    }

    public OAuth2Authentication remove(String code) {
        OAuth2Authentication oAuth2Authentication = cache.get(code);
        if (cache.remove(code) != null) {
            return oAuth2Authentication;
        }
        return null;
    }
}
