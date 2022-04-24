package com.meta.authentication.store.redis;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.meta.domain.entity.ValidationCode;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LoginVerificationCodeStore {

    @CreateCache(name = "LoginVerificationCode:Cache", expire = 5, timeUnit = TimeUnit.MINUTES, cacheType = CacheType.REMOTE)
    private Cache<String, ValidationCode> verifyKeyVoCache;

    public void put(ValidationCode vo) {
        verifyKeyVoCache.put(vo.getValidationKey(), vo);
    }

    public ValidationCode get(String id) {
        return verifyKeyVoCache.get(id);
    }

    public void delete(String id) {
        verifyKeyVoCache.remove(id);
    }
}