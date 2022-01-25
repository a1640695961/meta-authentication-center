package com.meta.oauth2.authorization.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    Logger logger = LoggerFactory.getLogger(AuthenticationFailureListener.class.getName());


    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        Authentication authentication = e.getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            logger.error("username password authentication token failed:{}", e);
        } else {
            logger.error(authentication.toString());
        }
    }
}