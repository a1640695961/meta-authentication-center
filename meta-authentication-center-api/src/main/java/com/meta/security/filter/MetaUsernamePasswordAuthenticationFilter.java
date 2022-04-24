package com.meta.security.filter;

import com.meta.commons.lang.JacksonUtil;
import com.meta.constants.MobileMatchRule;
import com.meta.domain.po.AccountPo;
import com.meta.model.LoginType;
import com.meta.model.LoginVo;
import com.meta.service.account.AccountService;
import com.meta.utils.CheckDecodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class MetaUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private AccountService accountService;

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

        LoginVo loginVo = null;
        String requestBody = "";
        try {
            requestBody = IOUtils.toString(request.getReader());
            loginVo = JacksonUtil.String2Obj(requestBody, LoginVo.class);
            loginVo.setAccountName(CheckDecodeUtil.decodeParam(loginVo.getAccountName()));
            loginVo.setPassword(CheckDecodeUtil.decodeParam(loginVo.getPassword()));
        } catch (IOException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new AuthenticationServiceException("Content Parse Error " + requestBody);
        }

        String accountName = "";
        if (LoginType.VERIFICATION_CODE.equals(loginVo.getLoginType())) {
            AccountPo accountPo = this.checkVerificationCodeLogin(loginVo, request);
        } else if (LoginType.PASSWORD.equals(loginVo.getLoginType())) {
//            this.checkPasswordLogin(loginVo, request);
            accountName = loginVo.getAccountName() + ":" + loginVo.getPassword() + ":" + loginVo.getLoginType().name();
        }


        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(accountName, loginVo.getPassword());

        // Allow subclasses to set the "details" property
        setDetails(request, token);

        return this.getAuthenticationManager().authenticate(token);


    }

    private AccountPo checkVerificationCodeLogin(LoginVo loginVo, HttpServletRequest request) {
        String accountName = loginVo.getAccountName();
        if (StringUtils.isEmpty(accountName)) {
            throw new AuthenticationServiceException("手机号不能为空");
        }
        if (!MobileMatchRule.isMobileFormatCorrect(accountName)) {
            throw new AuthenticationServiceException("手机号格式不正确！");
        }
        if (StringUtils.isEmpty(loginVo.getPassword())) {
            throw new AuthenticationServiceException("密码不能为空");
        }
//        if (!loginVerificationCodeService.isVerificationCodeOK(accountName, password)) {
//            String msg = messageSource.getIrsMessage("irs.authcenter.verifycode_is_wrong", "验证码错误");
//            throw new AuthenticationServiceException(msg);
//        }

        AccountPo accountPo = accountService.getAccount(accountName);
        if (accountPo == null) {
            log.info("用户不存在，自动注册账号：{}", accountName);
//            UserRegisterRequest registerRequest = convertLoginReqToRegisterReq(loginRequestParam, request, loginAgent);
//            accountService.autoRegister(registerRequest, LoginAgentConstant.APP.equals(loginAgent));
//            accountPo = accountService.getAccount(accountName);
        }


        return null;
    }

    private String obtainParameter(HttpServletRequest request, String parameter) {
        return request.getParameter(parameter);
    }
}
