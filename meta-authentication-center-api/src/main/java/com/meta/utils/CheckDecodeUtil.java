package com.meta.utils;

import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

/**
 * @author eden
 * @date 19-5-16
 */
public class CheckDecodeUtil {

    /**
     * base64加密前缀
     */
    public final static String BASE64_ENCODE_PREFIX = "Meta:";

    /**
     * 分隔符
     */
    public final static String BASE64_ENCODE_SEPARATOR = ":";


    /**
     * 需要解密则返回解密后的字符串
     * 无需解密则直接返回原字符串
     *
     * @param param
     * @return
     */
    public static String decodeParam(String param) {
        if (StringUtils.startsWithIgnoreCase(param, BASE64_ENCODE_PREFIX)) {
            //encode str
            String encodeStr = param.substring(param.indexOf(BASE64_ENCODE_SEPARATOR) + 1);
            String decodeStr = new String(Base64Utils.decodeFromString(encodeStr));
            return decodeStr;
        }
        return param;
    }

}
