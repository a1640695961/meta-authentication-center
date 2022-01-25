package com.meta.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xiong Mao
 * @date 2022/01/21 16:51
 **/
@RestController
public class ErrorController {

    @GetMapping(value = "/error")
    public String error(){
        return "error";
    }
}
