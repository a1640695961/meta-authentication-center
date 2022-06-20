package com.meta.security.config;

import com.meta.security.filter.MetaUsernamePasswordAuthenticationFilter;
import com.meta.security.handler.MetaLoginUrlAuthenticationEntryPoint;
import com.meta.security.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Xiong Mao
 * @date 2022/01/18 17:42
 **/
@Configuration
@EnableResourceServer
public class OAuth2ResourceConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Bean
    public MetaLoginUrlAuthenticationEntryPoint metaLoginUrlAuthenticationEntryPoint() {
        return new MetaLoginUrlAuthenticationEntryPoint("/view/login");
    }

    @Bean
    public MetaUsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        MetaUsernamePasswordAuthenticationFilter authenticationFilter = new MetaUsernamePasswordAuthenticationFilter();
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", HttpMethod.POST.name()));
        return authenticationFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers("/view/**").permitAll();

        //自己系统的登陆
        http
                .exceptionHandling()
                .authenticationEntryPoint(new MetaLoginUrlAuthenticationEntryPoint("/login"))
//                .defaultAuthenticationEntryPointFor()
//                .authenticationEntryPoint(metaLoginUrlAuthenticationEntryPoint())
                .and()
                .formLogin()
//                .loginProcessingUrl("https://account.ihr360.com/ac/view/login/#/login")
//                .permitAll()
                .and()
                .headers()
//                .exceptionHandling()
//                .authenticationEntryPoint(metaLoginUrlAuthenticationEntryPoint())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .sessionFixation().none()
                .and()
                .authorizeRequests()
                .antMatchers("/meta/login").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/view/**").permitAll()
                .anyRequest().authenticated() //其余所有请求全部需要鉴权认证
                .and()
                .csrf().disable();

//        http.requestCache().requestCache(new CustomHttpSessionRequestCache());


//        http
//                .formLogin()
//                .and()
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .csrf().disable()
//                .sessionManagement()
//                .sessionCreationPolicy();
    }
}
