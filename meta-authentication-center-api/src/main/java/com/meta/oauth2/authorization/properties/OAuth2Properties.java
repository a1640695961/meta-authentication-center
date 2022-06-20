package com.meta.oauth2.authorization.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "meta.security.oauth2")
public class OAuth2Properties {
    private String jwtSigningKey = "meta";

    private OAuth2ClientProperties[] clients = {};
}