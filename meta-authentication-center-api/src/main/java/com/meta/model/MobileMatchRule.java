package com.meta.model;

import java.util.regex.Pattern;

/**
 * @author Xiong Mao
 * @date 2022/03/10 15:17
 **/
public class MobileMatchRule {

    public static final String MATCH_RULE = "^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$";


    public static boolean isMobileFormatCorrect(String mobileNo) {
        return Pattern.compile(MATCH_RULE).matcher(mobileNo).matches();
    }


    public static void main(String[] args) {
        boolean matches = Pattern.compile(MATCH_RULE).matcher("17521014072").matches();

        System.out.println(matches);
    }
}
