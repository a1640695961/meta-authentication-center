package com.meta.security.config;

import com.meta.security.handler.MetaLoginUrlAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author Xiong Mao
 * @date 2022/06/17 17:25
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager authenticationManager = super.authenticationManagerBean();
        return authenticationManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .exceptionHandling()
                .authenticationEntryPoint(new MetaLoginUrlAuthenticationEntryPoint("/view/login"))
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .requestCache();


//        http
//                .exceptionHandling()
//                .and()
//                .formLogin()
////                .permitAll()
//                .and()
//                .logout().logoutSuccessHandler(logoutSuccessHandler);
    }
}
