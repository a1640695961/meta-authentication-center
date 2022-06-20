package com.meta.security.filter;

import com.alibaba.fastjson.JSON;
import com.meta.authentication.store.redis.WebLoginStore;
import com.meta.commons.lang.JacksonUtil;
import com.meta.model.LoginAgentConstant;
import com.meta.model.MobileMatchRule;
import com.meta.domain.po.AccountPo;
import com.meta.domain.vo.LoginResponseVo;
import com.meta.model.LoginType;
import com.meta.model.LoginVo;
import com.meta.service.account.AccountLoginRecordService;
import com.meta.service.account.AccountService;
import com.meta.utils.CheckDecodeUtil;
import com.meta.utils.NetWorkUtil;
import eu.bitwalker.useragentutils.BrowserType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


/**
 * @author Xiong Mao
 * @date 2022/01/25 17:18
 **/
@Slf4j
public class MetaUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private AccountService accountService;
    @Autowired
    private WebLoginStore webLoginStore;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountLoginRecordService accountLoginRecordService;

    /**
     * 密码错误超过3次,则需要验证码
     */
    @Value("${meta.authcenter.account.pwderr.cnt_limit_verifycode:3}")
    private int pwdErrCntLimitOfVerifyCode;

    /**
     * 密码错误超过5次,则限制账号5分钟内不登录
     */
    @Value("${meta.authcenter.account.pwderr.cnt_limit_lock:5}")
    private int pwdErrCntLimitOfLock;

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
            String accountName = (loginVo != null && StringUtils.isNotEmpty(loginVo.getAccountName()))
                    ? loginVo.getAccountName() : request.getSession().getId();
            this.fail(response, "Content Type Parse Error. Only Support Json ", accountName, LoginAgentConstant.WEB);
            throw new AuthenticationServiceException("Content Parse Error " + requestBody);
        }

        BrowserType browser = NetWorkUtil.getBrowserType(request);
        String browserType = BrowserType.UNKNOWN.getName();
        if (!Objects.isNull(browser)) {
            browserType = browser.getName();
        }
        String trueIpAddress = NetWorkUtil.getTrueIpAddress(request);
        String osType = NetWorkUtil.getOSType(request);
        String userAgent = NetWorkUtil.getUserAgent(request);

        AccountPo account = null;

        try {
            if (LoginType.VERIFICATION_CODE.equals(loginVo.getLoginType())) {
//            account = this.checkVerificationCodeLogin(loginVo, request);
            } else if (LoginType.PASSWORD.equals(loginVo.getLoginType())) {
                account = this.checkPasswordLogin(loginVo, request);
            }

            //修改账号最后登录日期
            accountService.updateAccountLastLoginTime(account.getId());
            //异步记录登录日志
            accountLoginRecordService.addLoginRecordAsync(account.getId(), loginVo.getAccountName(), account.getUserName(), account.getMobileNo(), trueIpAddress,
                    browserType, osType, userAgent, Boolean.TRUE);
            //清除登录error次数
            webLoginStore.delete(loginVo.getAccountName());
        } catch (AuthenticationServiceException e) {
            if (!Objects.isNull(account)) {
                //异步记录登录日志
                accountLoginRecordService.addLoginRecordAsync(account.getId(), loginVo.getAccountName(), account.getUserName(), account.getMobileNo(), trueIpAddress,
                        browserType, osType, userAgent, Boolean.FALSE);
            }
            String accountName = StringUtils.isNotEmpty(loginVo.getAccountName())
                    ? loginVo.getAccountName() : request.getSession().getId();
            fail(response, e.getMessage(), accountName, LoginAgentConstant.WEB);
            throw e;
        }

        String accountName = loginVo.getAccountName() + ":" + loginVo.getPassword() + ":" + loginVo.getVerifyCode() + ":" + loginVo.getLoginType();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(accountName, loginVo.getPassword());

        // Allow subclasses to set the "details" property
        setDetails(request, token);

        return this.getAuthenticationManager().authenticate(token);


    }

    private AccountPo checkPasswordLogin(LoginVo loginVo, HttpServletRequest request) {
        String accountName = loginVo.getAccountName();
        if (StringUtils.isEmpty(accountName)) {
            throw new AuthenticationServiceException("登录名不能为空");
        }

        String password = loginVo.getPassword();
        if (StringUtils.isEmpty(password)) {
            throw new AuthenticationServiceException("密码不能为空");
        }

        int errorCnt = webLoginStore.getErrorCnt(accountName);
        if (errorCnt >= pwdErrCntLimitOfVerifyCode) {
            String verifyCode = loginVo.getVerifyCode();
            if (StringUtils.isEmpty(verifyCode)) {
                throw new AuthenticationServiceException("验证码不能为空");
            }
//            boolean validVerifyCode = verifyService.validateVerifyCode(VerifyCodeType.LOGIN, verifyCode, request.getSession());
//            if (!validVerifyCode) {
//                throw new AuthenticationServiceException("验证码错误");
//            }
        } else if (errorCnt >= pwdErrCntLimitOfLock) {
            throw new AuthenticationServiceException("你输入错误密码次数过多，请5分钟后再试");
        }

        AccountPo account = accountService.getAccount(accountName);
        if (Objects.isNull(account)) {
            log.error("用户不存在，手机号：{}", accountName);
            throw new AuthenticationServiceException("账号不存在,请注册后再使用");
        }

        if (!passwordEncoder.matches(password, account.getPassword())) {
            log.error("登录密码错误，密码：{}，加密密码：{}", password, account.getPassword());
            throw new AuthenticationServiceException("账号名或密码错误");
        }

        Boolean nonLocked = account.isAccountNonLocked();
        if (Boolean.FALSE.equals(nonLocked)) {
            throw new AuthenticationServiceException("由于用户帐户被锁定，因此无法进行身份验证，请联系管理员解锁");
        }

        return account;
    }


    private void fail(HttpServletResponse response, String message, String accountName, String loginAgent) {

        String accountNameWithAgent = getAccountNameWithAgent(accountName, loginAgent);
        webLoginStore.increaseErrorCnt(accountNameWithAgent);

        LoginResponseVo securityAuthenticationResponseVO = new LoginResponseVo();
        securityAuthenticationResponseVO.setSuccess(false);
        securityAuthenticationResponseVO.setMessage(message);

        int errorCnt = webLoginStore.getErrorCnt(accountName);
        if (errorCnt >= pwdErrCntLimitOfVerifyCode) {
            securityAuthenticationResponseVO.setNeedVerifyCode(true);
        }
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().println(JSON.toJSONString(securityAuthenticationResponseVO));
            response.getWriter().flush();
        } catch (IOException e) {
            log.error(null, e);
        }
    }

    private String getAccountNameWithAgent(String accountName, String loginAgent) {
//        if (LoginAgentConstant.WEB.equals(loginAgent)) {
//            return loginAgent + accountName;
//        }
        return accountName;
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
