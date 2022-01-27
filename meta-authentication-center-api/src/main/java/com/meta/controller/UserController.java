package com.meta.controller;

import com.meta.domain.entity.AccountDetails;
import com.meta.model.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author Xiong Mao
 * @date 2022/01/27 16:48
 **/
@Controller
@Slf4j
public class UserController {

    @GetMapping("me")
    @ResponseBody
    public UserVo me(OAuth2Authentication oAuth2Authentication, AccountDetails details) {
        if (oAuth2Authentication == null) {
            log.error("Can not get principal info");
            return null;
        }
        log.info(details.toString());
        log.info(oAuth2Authentication.toString());
        Object principal = oAuth2Authentication.getPrincipal();
        String loginAgent = null;
        String accountId = null;
        String target = null;
        if (principal instanceof AccountDetails) {
            AccountDetails accountDetails = (AccountDetails) principal;
            accountId = accountDetails.getAccountId();
        }

        UserVo userVo = new UserVo();
        userVo.setUsername(details.getAccountName());
        userVo.setAuthorities((Set<SimpleGrantedAuthority>) details.getAuthorities());
        return userVo;
    }
}
