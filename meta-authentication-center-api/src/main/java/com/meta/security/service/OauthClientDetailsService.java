package com.meta.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("jdbcClientDetailsService")
    private ClientDetailsService clientDetailsService;

    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return  clientDetailsService.loadClientByClientId(clientId);
    }
}
