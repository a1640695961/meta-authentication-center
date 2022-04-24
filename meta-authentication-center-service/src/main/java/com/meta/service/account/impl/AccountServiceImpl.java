package com.meta.service.account.impl;

import com.meta.authentication.store.mybatis.AccountMapper;
import com.meta.domain.po.AccountPo;
import com.meta.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Xiong Mao
 * @date 2022/03/10 11:27
 **/
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public AccountPo getAccount(String accountName) {
        return null;
    }
}
