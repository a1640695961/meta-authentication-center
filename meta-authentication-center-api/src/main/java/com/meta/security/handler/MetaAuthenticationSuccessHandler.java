package com.meta.security.handler;

import cn.hutool.json.JSONUtil;
import com.meta.domain.entity.AccountDetails;
import com.meta.domain.vo.LoginResponseVo;
import com.meta.security.config.CustomHttpSessionRequestCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Xiong Mao
 * @date 2022/01/26 17:14
 **/
@Component
@Slf4j
public class MetaAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private RequestCache requestCache = new CustomHttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("authentication success");

        if (response.isCommitted()) {
            logger.debug(
                    "Response has already been committed. Unable to redirect to ");
            return;
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {

            LoginResponseVo loginResponseVo = new LoginResponseVo();
            loginResponseVo.setSuccess(true);
            loginResponseVo.setMessage("认证登录成功");
//            loginResponseVo.setNeedUserConfirm(true);

            AccountDetails details = (AccountDetails) authentication.getPrincipal();

            loginResponseVo.setRedirectUrl("http://localhost:5555/oauth2/authorization/meta");

            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().println(JSONUtil.toJsonStr(loginResponseVo));
            response.getWriter().flush();

        } else {

            clearAuthenticationAttributes(request);

            //Use the DefaultSavedRequest URL
            String redirectUrl = savedRequest.getRedirectUrl();
            logger.debug("Redirecting to DefaultSavedRequest Url: " + redirectUrl);
            //getRedirectStrategy().sendRedirect(request, response, targetUrl);

            LoginResponseVo loginResponseVo = new LoginResponseVo();
            loginResponseVo.setSuccess(true);
            loginResponseVo.setMessage("授权登录成功");
            loginResponseVo.setRedirectUrl(redirectUrl);
            response.setStatus(HttpStatus.FOUND.value());
            response.setHeader(HttpHeaders.LOCATION, redirectUrl);
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().println(JSONUtil.toJsonStr(loginResponseVo));
            response.getWriter().flush();
            requestCache.removeRequest(request, response);
        }


//        super.onAuthenticationSuccess(request, response, authentication);
    }
}
