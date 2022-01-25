package com.meta.oauth2.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * @author Xiong Mao
 * @date 2022/01/19 00:53
 **/
@Configuration
public class TokenStoreConfiguration {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

//    @Autowired
    private DataSource dataSource;

    @Bean
    @ConditionalOnProperty(prefix = "meta.security.oauth2", name = "storeType", havingValue = "redis")
    public TokenStore redisTokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    @ConditionalOnProperty(prefix = "meta.security.oauth2", name = "storeType", havingValue = "jdbc")
    public TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Configuration
    @ConditionalOnProperty(prefix = "meta.security.oauth2", name = "storeType", havingValue = "jwt", matchIfMissing = true)
    public static class JwtTokenConfig{


        public JwtAccessTokenConverter accessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//            converter.setKeyPair(this.keyPair);
            converter.setSigningKey("");

//            DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
//            accessTokenConverter.setUserTokenConverter(new SubjectAttributeUserTokenConverter());
//            converter.setAccessTokenConverter(accessTokenConverter);

            return converter;
        }
    }
}
