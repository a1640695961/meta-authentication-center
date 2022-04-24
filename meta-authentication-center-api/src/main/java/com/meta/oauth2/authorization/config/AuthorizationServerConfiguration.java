package com.meta.oauth2.authorization.config;

import com.meta.security.service.OauthClientDetailsService;
import com.meta.security.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/chaosbreakers/metagraph-auth/blob/master/src/main/java/io/metagraph/auth/configurer/AuthorizationServerConfigurer.java
 * <p/>
 * https://projects.spring.io/spring-security-oauth/docs/oauth2.html
 *
 * @author Xiong Mao
 * @date 2022/01/18 17:32
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private OauthClientDetailsService oauthClientDetailsService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 开启/oauth/token_key验证端口无权限访问
                .tokenKeyAccess("isAuthenticated()")
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("permitAll()");

        //oauth/authorize
        //oauth/token
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients
//                .inMemory()
//                .withClient("meta")
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
//                .authorities("ROLE_ADMIN","ROLE_USER")
//                .secret("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
//                .scopes("client")
//                .redirectUris("http://localhost:8080/login/oauth2/code/meta");
        clients.withClientDetails(oauthClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //支持password模式
                .authenticationManager(authenticationManager)
                //password验证
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore)
                .tokenServices(tokenServices())
                .tokenGranter(tokenGranter())
                .authorizationCodeServices(authorizationCodeServices());
    }

    @Bean
    @Primary
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore);
        //refresh token 不重新使用
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setClientDetailsService(oauthClientDetailsService);
        return tokenServices;
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new CustomAuthorizationCodeServices();
    }

    @Bean
    public TokenGranter tokenGranter() {
        return new CompositeTokenGranter(getCustomizedTokenGranters());
    }

    private List<TokenGranter> getCustomizedTokenGranters() {
        AuthorizationServerTokenServices tokenServices = tokenServices();
        ClientDetailsService clientDetails = oauthClientDetailsService;
        AuthorizationCodeServices authorizationCodeServices = authorizationCodeServices();
        OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetails);

        AuthorizationCodeTokenGranter authorizationCodeTokenGranter = new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails, requestFactory);
        RefreshTokenGranter refreshTokenGranter = new RefreshTokenGranter(tokenServices, clientDetails, requestFactory);
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory);
        ClientCredentialsTokenGranter clientCredentialsTokenGranter = new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory);
        clientCredentialsTokenGranter.setAllowRefresh(true);//custom config, see AuthorizationServerEndpointsConfigurer.getDefaultTokenGranters

        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
        tokenGranters.add(authorizationCodeTokenGranter);
        tokenGranters.add(refreshTokenGranter);
        tokenGranters.add(implicit);
        tokenGranters.add(clientCredentialsTokenGranter);
        if (authenticationManager != null) {
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetails, requestFactory));
        }

        return tokenGranters;
    }

}
