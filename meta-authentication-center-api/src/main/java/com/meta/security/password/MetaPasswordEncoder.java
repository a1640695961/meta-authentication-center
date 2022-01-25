package com.meta.security.password;

import com.meta.utils.PasswordUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * meta 密码编码器
 */
public class MetaPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return PasswordUtils.MD5(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return PasswordUtils.validate(rawPassword.toString(), encodedPassword);
    }
}