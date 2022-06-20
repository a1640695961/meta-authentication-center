package com.meta.service.account;

/**
 * @author Xiong Mao
 * @date 2022/06/10 16:39
 **/
public interface AccountLoginRecordService {
    /**
     * 新增登录日志
     *
     * @param accountId      账户Id
     * @param accountName    登录账户名称
     * @param userName       账户名称
     * @param mobileNo       手机号码
     * @param trueIpAddress  登录Ip
     * @param browserType    浏览器类型
     * @param osType         系统类型
     * @param userAgent      user-agent信息
     * @param isLoginSuccess 是否登录成功
     */
    void addLoginRecordAsync(String accountId, String accountName, String userName, String mobileNo, String trueIpAddress,
                             String browserType, String osType, String userAgent, Boolean isLoginSuccess);
}
