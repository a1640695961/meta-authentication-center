package com.meta.oauth2.authorization.config;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
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

    @CreateCache(name = "authorization:code:cache", expire = 300, cacheType = CacheType.REMOTE)
    private Cache<String, OAuth2Authentication> cache;

    public void put(String code, OAuth2Authentication oAuth2Authentication) {
        cache.put(code, oAuth2Authentication);
    }

    public OAuth2Authentication remove(String code) {
        OAuth2Authentication oAuth2Authentication = cache.get(code);
        if (cache.remove(code)) {
            return oAuth2Authentication;
        }
        return null;
    }
}
