package com.meta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Xiong Mao
 * @date 2022/01/21 16:51
 **/
@Controller
public class IndexController {

    @GetMapping
    @ResponseBody
    public String index(){
        return "index";
    }


    @RequestMapping("/index.html")
    public String root() {
        return "index";
    }
}
