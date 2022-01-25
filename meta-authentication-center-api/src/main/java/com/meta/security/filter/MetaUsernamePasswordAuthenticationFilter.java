package com.meta.security.filter;

import com.meta.model.LoginType;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author Xiong Mao
 * @date 2022/01/25 17:18
 **/
public class MetaUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper;




    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        if (!request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE) && !request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            response.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
            throw new AuthenticationServiceException("Content Type not supported: " + request.getContentType());
        }

        try {
            String requestBody = IOUtils.toString(request.getReader());


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String obtainParameter(HttpServletRequest request, String parameter) {
        return request.getParameter(parameter);
    }
}
