package com.meta.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @author Xiong Mao
 * @date 2022/03/10 10:58
 **/
@TableName("ac_account")
public class AccountPo {

    private String id;

    private String userName;

    private String mobileNo;

    private String email;

    private String password;

    private String accountType;

    private Date lastLoginTime;

    private Date registerTime;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

}
