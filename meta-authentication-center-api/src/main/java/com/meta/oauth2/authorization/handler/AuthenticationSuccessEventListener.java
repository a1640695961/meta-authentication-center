package com.meta.oauth2.authorization.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    Logger log = LoggerFactory.getLogger(AuthenticationSuccessEventListener.class.getName());

    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        Authentication authentication = e.getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            log.info("username password authentication token");
        } else {
            log.info(authentication.toString());
        }
    }
}