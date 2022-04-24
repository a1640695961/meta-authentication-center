package com.meta.security.config;

import com.meta.security.filter.MetaUsernamePasswordAuthenticationFilter;
import com.meta.security.handler.MetaLoginUrlAuthenticationEntryPoint;
import com.meta.security.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager authenticationManager = super.authenticationManagerBean();
        return authenticationManager;
    }


    @Bean
    public MetaLoginUrlAuthenticationEntryPoint metaLoginUrlAuthenticationEntryPoint() {
        return new MetaLoginUrlAuthenticationEntryPoint("/view/login");
    }

    @Bean
    public MetaUsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        MetaUsernamePasswordAuthenticationFilter authenticationFilter = new MetaUsernamePasswordAuthenticationFilter();
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/meta/login", HttpMethod.POST.name()));
        return authenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //认证中心登陆
        //认证中心退出
        //包含用户信息保存
        //sessionId  cookie

        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/view/**").permitAll();

        //自己系统的登陆
        http
                .exceptionHandling()
//                .authenticationEntryPoint(metaLoginUrlAuthenticationEntryPoint())
                .and()
                .formLogin()
//                .loginProcessingUrl("https://account.ihr360.com/ac/view/login/#/login")
                .permitAll()
                .and()
                .headers()
//                .exceptionHandling()
//                .authenticationEntryPoint(metaLoginUrlAuthenticationEntryPoint())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
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
