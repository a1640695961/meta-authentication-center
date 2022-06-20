package com.meta.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Xiong Mao
 * @date 2022/06/10 13:56
 **/
public class LoginResponseVo implements Serializable {

    @ApiModelProperty(value = "返回码")
    private int code;

    @ApiModelProperty(value = "是否登录成功")
    private Boolean success;

    @ApiModelProperty(value = "返回消息")
    private String message;

//    @ApiModelProperty(value = "用户角色", notes = "返回用户在当前公司的登录角色")
//    private String userRole;
//
//    @ApiModelProperty(value = "跳转路径", notes = "登录成功后的跳转路径")
//    private String redirectUrl;

    @ApiModelProperty(value = "是否需要图像验证", notes = "错误3次以后需要输入验证码")
    private boolean needVerifyCode;

    @ApiModelProperty(value = "基准路径", notes = "登录成功后请求基准路径")
    private String reqBaseUrl;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isNeedVerifyCode() {
        return needVerifyCode;
    }

    public void setNeedVerifyCode(boolean needVerifyCode) {
        this.needVerifyCode = needVerifyCode;
    }

    public String getReqBaseUrl() {
        return reqBaseUrl;
    }

    public void setReqBaseUrl(String reqBaseUrl) {
        this.reqBaseUrl = reqBaseUrl;
    }
}
