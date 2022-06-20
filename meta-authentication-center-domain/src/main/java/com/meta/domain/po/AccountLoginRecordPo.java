package com.meta.domain.po;

import com.meta.commons.model.eneity.StringIdWithTime;

import java.util.Date;

/**
 * @author Xiong Mao
 * @date 2022/06/10 16:34
 **/
public class AccountLoginRecordPo extends StringIdWithTime {

    /**
     * 账户id
     */
    private String accountId;
    /**
     * 登录账户号
     */
    private String accountName;
    /**
     * 账户名称
     */
    private String username;
    /**
     * 手机号码
     */
    private String mobileNo;
    /**
     * 登录时间
     */
    private Date loginTime;
    /**
     * 登录ip
     */
    private String loginIp;
    private String userAgent;
    /**
     * 系统类型
     */
    private String osType;
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     *
     */
    private String version;

    private Boolean loginSuccess;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(Boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }
}
