package com.meta.service.validationcode;

/**
 * @author Xiong.Mao
 * @description 手机短信登录验证码服务
 * 手机短信登录时，会对登录验证码进行加密处理
 */
public interface LoginVerificationCodeService {

    /**
     * 通过手机短信登录时，发送手机短信验证码（外部调用）
     *
     * @param ip
     * @param mobileNo
     * @param mobileType
     */
    void sendVerificationCode(String ip, String mobileNo, String mobileType);

    /**
     * 真正执行验证码发送（内部consumer调用）
     *
     * @param ip
     * @param mobileNo
     */
    void sendCode(String ip, String mobileNo);

    /**
     * 校验手机短信登录时，验证码是否正确
     *
     * @param phoneNo
     * @param code
     * @return
     */
    boolean isVerificationCodeOK(String phoneNo, String code);

    /**
     * 获取登录验证码
     *
     * @param phoneNo
     * @return
     */
    String getLoginVerificationCode(String phoneNo);
}
