package com.meta.authentication.store.redis;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import org.springframework.stereotype.Component;

/**
 * 主要验证5分钟内可用的验证逻辑
 */
@Component
public class WebLoginStore {
    @CreateCache(name = "WebLogin:Cache", expire = 300, cacheType = CacheType.REMOTE)
    private Cache<String, Integer> loginRecordCache;

    public void delete(String accountName) {
        loginRecordCache.remove(accountName);
    }

    public int getErrorCnt(String account) {
        Integer errCounts = loginRecordCache.get(account);
        if (errCounts == null) {
            return 0;
        }
        return errCounts;
    }

    public void increaseErrorCnt(String account) {
        Integer errCounts = loginRecordCache.get(account);
        if (errCounts == null) {
            errCounts = 1;
        }else{
            errCounts = errCounts + 1;
        }
        loginRecordCache.put(account, errCounts);
    }

}