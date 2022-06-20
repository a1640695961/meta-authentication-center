package com.meta.service.account.impl;

import com.meta.authentication.store.mybatis.AccountLoginRecordMapper;
import com.meta.domain.po.AccountLoginRecordPo;
import com.meta.service.account.AccountLoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Xiong Mao
 * @date 2022/06/10 16:39
 **/
@Service
public class AccountLoginRecordServiceImpl implements AccountLoginRecordService {
    @Autowired
    private AccountLoginRecordMapper accountLoginRecordMapper;

    @Override
    @Async
    public void addLoginRecordAsync(String accountId, String accountName, String userName, String mobileNo, String trueIpAddress, String browserType, String osType, String userAgent, Boolean isLoginSuccess) {
        AccountLoginRecordPo accountLoginRecordPo = new AccountLoginRecordPo();
        accountLoginRecordPo.setAccountId(accountId);
        accountLoginRecordPo.setAccountName(accountName);
        accountLoginRecordPo.setMobileNo(mobileNo);
        accountLoginRecordPo.setLoginIp(trueIpAddress);
        accountLoginRecordPo.setBrowser(browserType);
        accountLoginRecordPo.setOsType(osType);
        accountLoginRecordPo.setUserAgent(userAgent);
        accountLoginRecordPo.setLoginSuccess(isLoginSuccess);
        accountLoginRecordMapper.insert(accountLoginRecordPo);
    }
}
