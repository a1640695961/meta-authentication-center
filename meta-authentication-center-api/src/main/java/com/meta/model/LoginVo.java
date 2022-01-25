package com.meta.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiong Mao
 * @date 2022/01/25 17:37
 **/
@ApiModel(value = "登录请求",description = "登录请求实体")
@Data
public class LoginVo {

    @ApiModelProperty(value = "账户名",notes = "手机号或邮箱")
    private String accountName;

    @ApiModelProperty(value = "登录密码", notes = "web的登录需要对密码做前端MD5加密后进行登录；根据登录方式不同，值可以为账号密码或短信验证码")
    private String password;

    @ApiModelProperty(value = "图像验证码",notes = "图像验证码只能使用一次，如果输入错误，需要重新获取验证码")
    private String verifyCode;

    @ApiModelProperty(value = "登录方式", notes = "loginType取值可为PASSWORD,VERIFICATION_CODE,分别表示账号密码登录/手机验证码登录")
    private LoginType loginType;
}
