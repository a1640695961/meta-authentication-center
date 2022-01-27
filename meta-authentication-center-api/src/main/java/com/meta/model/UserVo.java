package com.meta.model;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

/**
 * @author Xiong Mao
 * @date 2022/01/27 16:52
 **/
@Data
public class UserVo {

    private String username;

    private Set<SimpleGrantedAuthority> authorities;
}
