package com.meta.service.validationcode.impl;

import com.meta.authentication.store.redis.LoginVerificationCodeStore;
import com.meta.domain.entity.ValidationCode;
import com.meta.service.validationcode.LoginVerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Xiong Mao
 * @date 2022/03/10 17:03
 **/
@Service
@Slf4j
public class LoginVerificationCodeServiceImpl implements LoginVerificationCodeService {

    @Autowired
    private LoginVerificationCodeStore loginVerificationCodeStore;

    public void sendVerificationCode(String ip, String mobileNo, String mobileType) {

    }

    public void sendCode(String ip, String mobileNo) {

    }

    public boolean isVerificationCodeOK(String phoneNo, String code) {
        if (StringUtils.isBlank(phoneNo)) {
            log.warn("校验短信验证码的手机号为空");
            return false;
        }

        String key = getKey(phoneNo);
        log.info("校验手机验证码:" + key);
        log.info("传入验证码:" + code);
        ValidationCode codeVo = loginVerificationCodeStore.get(key);
        if (codeVo == null) {
            log.warn("未找到验证码数据，校验短信验证码的手机号不正确或验证码超时失效");
            return false;
        }
        log.info("获取验证码:" + codeVo.getCode());
        if (codeVo.getValidateCount() > 10) {
            loginVerificationCodeStore.delete(key);
            log.warn("验证码使用超过10次");
            return false;
        }
        codeVo.increaseValidateCount();
        loginVerificationCodeStore.put(codeVo);

        if (!passwordEncoder.matches(code, codeVo.getCode())) {
            log.error("手机短信登录验证码错误，验证码：{}，加密验证码：{}", code, codeVo.getCode());
            return false;
        }
        if (System.currentTimeMillis() > codeVo.getExpireTime()) {
            loginVerificationCodeStore.delete(key);
            log.warn("验证码超时失效");
            return false;
        }
        return true;
    }

    public String getLoginVerificationCode(String phoneNo) {
        return null;
    }
}
