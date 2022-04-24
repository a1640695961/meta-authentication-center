package com.meta.domain.entity;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.io.Serializable;

public class ValidationCode implements Serializable {

    private static final long serialVersionUID = 8544226759007695512L;
    private String mobileNo;
    private Long validTimeLength;
    private ValidationCodeType type;
    private SendValidationCodeType sendValidationCodeType;
    private String ip;
    private Integer codeLength;//验证码长度

    private String validationKey;

    public ValidationCode(){}

    public ValidationCode(String mobileNo, String ip) {
        this.mobileNo = mobileNo;
        this.ip = ip;
    }

    public ValidationCode(String mobileNo, ValidationCodeType type,
                          Long validTimeLength, SendValidationCodeType sendValidationCodeType,
                          String ip, Integer codeLength) {
        this.mobileNo = mobileNo;
        this.type = type;
        this.validTimeLength = validTimeLength;
        this.sendValidationCodeType = sendValidationCodeType;
        this.ip = ip;
        this.codeLength = codeLength;

        this.validationKey = genKey(mobileNo, sessionId, type);
    }

    public static String genKey(String mobileNo, String sessionId, ValidationCodeType type) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotEmpty(mobileNo)) {
            builder.append(mobileNo);
        }
        if (StringUtils.isNotEmpty(sessionId)) {
            builder.append(sessionId);
        }
        if (type != null) {
            builder.append(type.name());
        }
        return builder.toString();
    }

    public SendValidationCodeType getSendValidationCodeType() {
        return sendValidationCodeType;
    }

    public void setSendValidationCodeType(SendValidationCodeType sendValidationCodeType) {
        this.sendValidationCodeType = sendValidationCodeType;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Long getValidTimeLength() {
        return validTimeLength;
    }

    public void setValidTimeLength(Long validTimeLength) {
        this.validTimeLength = validTimeLength;
    }

    public ValidationCodeType getType() {
        return type;
    }

    public void setType(ValidationCodeType type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(Integer codeLength) {
        this.codeLength = codeLength;
    }

    public String getValidationKey() {
        return validationKey;
    }

    public void setValidationKey(String validationKey) {
        this.validationKey = validationKey;
    }
}
