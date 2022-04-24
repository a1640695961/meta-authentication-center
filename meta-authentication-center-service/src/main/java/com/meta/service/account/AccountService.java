package com.meta.service.account;

import com.meta.domain.po.AccountPo;

/**
 * @author Xiong Mao
 * @date 2022/03/10 11:27
 **/
public interface AccountService {
    AccountPo getAccount(String accountName);
}
