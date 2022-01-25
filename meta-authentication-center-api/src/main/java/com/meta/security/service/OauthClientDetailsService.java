package com.meta.security.service;

import com.meta.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

/**
 * @author Xiong Mao
 * @date 2022/01/19 01:15
 **/
@Component("oauthClientDetailsService")
public class OauthClientDetailsService implements ClientDetailsService {
    @Autowired
    private ClientService clientService;

    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        InMemoryClientDetailsServiceBuilder inMemoryClientDetailsServiceBuilder = new InMemoryClientDetailsServiceBuilder();
        inMemoryClientDetailsServiceBuilder
                .withClient("meta")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorities("ROLE_ADMIN", "ROLE_USER")
                .secret("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
                .scopes("client")
                .redirectUris("http://localhost:8080/login/oauth2/code/meta")
                .autoApprove(true);
        try {
            ClientDetailsService clientDetailsService = inMemoryClientDetailsServiceBuilder.build();
            return clientDetailsService.loadClientByClientId(clientId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
