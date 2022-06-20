package com.meta.service.account.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.meta.authentication.store.mybatis.AccountMapper;
import com.meta.domain.po.AccountPo;
import com.meta.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
        LambdaQueryWrapper<AccountPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountPo::getMobileNo, accountName);
        AccountPo accountPo = accountMapper.selectOne(wrapper);
        return accountPo;
    }

    @Override
    public void accountLock(String accountName) {
        AccountPo account = this.getAccount(accountName);

        account.setAccountNonLocked(true);

        accountMapper.updateById(account);
    }

    @Override
    public void updateAccountLastLoginTime(String id) {
        AccountPo accountPo = accountMapper.selectById(id);

        accountPo.setLastLoginTime(new Date());

        accountMapper.updateById(accountPo);
    }

    @Override
    public AccountPo loadUserByUsername(String accountName) {
        return this.getAccount(accountName);
    }
}
