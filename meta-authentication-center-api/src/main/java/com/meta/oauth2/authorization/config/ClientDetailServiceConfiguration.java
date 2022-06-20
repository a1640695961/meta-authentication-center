package com.meta.oauth2.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * @author Xiong Mao
 * @date 2022/06/09 22:31
 **/
@Configuration
public class ClientDetailServiceConfiguration {
    @Autowired(required = false)
    private DataSource dataSource;

    @Bean
    @ConditionalOnProperty(prefix = "meta.security.oauth2", name = "client", havingValue = "jdbc")
    public ClientDetailsService jdbcClientDetailsService(){
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        return clientDetailsService;
    }

    @Bean
    @ConditionalOnProperty(prefix = "meta.security.oauth2", name = "client", havingValue = "memory")
    public ClientDetailsService InMemoryClientDetailsService() throws Exception {
        InMemoryClientDetailsServiceBuilder inMemoryClientDetailsServiceBuilder = new InMemoryClientDetailsServiceBuilder();
//        inMemoryClientDetailsServiceBuilder
//                .withClient("meta")
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
//                .authorities("ROLE_ADMIN", "ROLE_USER")
//                .secret("{bcrypt}$2a$10$tTR.qnOGeD4JS7or8IixVepdp9cOwzh4vPREwqjVCxDlPsHuhK2DS")
//                .scopes("client")
//                .redirectUris("http://localhost:8080/login/oauth2/code/meta")
//                .autoApprove(true)
//                .and()
//                .withClient("meta_gateway")
//                .authorizedGrantTypes("authorization_code","refresh_token")
//                .authorities("ROLE_USER","ROLE_ADMIN")
//                .secret("{bcrypt}$2a$10$tTR.qnOGeD4JS7or8IixVepdp9cOwzh4vPREwqjVCxDlPsHuhK2DS")
//                .scopes("client")
//                .redirectUris("http://localhost:5555/login/oauth2/code/");
        return inMemoryClientDetailsServiceBuilder.build();
    }

}
