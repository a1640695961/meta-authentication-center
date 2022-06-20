package com.meta.oauth2.authorization.config;

import com.meta.oauth2.authorization.jwt.MetaJwtTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
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

    @Autowired(required = false)
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
    public static class JwtTokenConfig {

        /**
         * 使用jwtTokenStore存储token
         *
         * @return
         */
        @Bean
        public TokenStore jwtTokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        /**
         * 用于生成jwt
         *
         * @return
         */
        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
            accessTokenConverter.setSigningKey("meta");//生成签名的key
            return accessTokenConverter;
        }

        /**
         * 用于扩展JWT
         *
         * @return
         */
        @Bean
        @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
        public TokenEnhancer jwtTokenEnhancer() {
            return new MetaJwtTokenEnhancer();
        }
    }
}
