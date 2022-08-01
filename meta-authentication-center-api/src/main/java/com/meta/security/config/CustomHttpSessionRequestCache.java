package com.meta.security.config;

import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomHttpSessionRequestCache extends HttpSessionRequestCache {
    static final String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";
    private String sessionAttrName = SAVED_REQUEST;
    private PortResolver portResolver = new PortResolverImpl();

    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        DefaultSavedRequest savedRequest = new DefaultSavedRequest(request,
                portResolver);
        if (request.getRequestURI().equals("/ac/oauth/authorize")){
            request.getSession().setAttribute(this.sessionAttrName, savedRequest);
        }
    }
}