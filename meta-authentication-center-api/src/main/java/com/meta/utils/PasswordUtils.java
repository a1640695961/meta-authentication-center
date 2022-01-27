package com.meta.utils;

import com.meta.commons.lang.RandomUtil;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    // 全局数组
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b",
            "c", "d", "e", "f"};
    private static final String SALT_SEPARTOR = ":";
    private static final String SALT_PASSWORD_TEMPLATE = "[LINA=%s]%s";

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return new String(sBuffer.toString().getBytes(UTF_8), UTF_8);
    }

    private static String encode(String strObj) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            return byteToString(md.digest(strObj.getBytes(UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("Should not be here!");
        }
    }

    public static String MD5(String strObj) {
        return encode(strObj);
    }

    private static String encode(String src, String salt) {
        try {
            src = String.format(SALT_PASSWORD_TEMPLATE, salt, src);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            return byteToString(md.digest(src.getBytes(UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("Should not be here!");
        }
    }

    public static String encryptWithSalt(String password, String salt) {
        String encoded = encode(password, salt);
        return salt + SALT_SEPARTOR + encoded;
    }

    public static String encryptWithRandomSalt(String password) {
        String salt = RandomUtil.getNumbers(8);
        return salt + SALT_SEPARTOR + encode(password, salt);
    }

    static String encrypt(String password) {
        return encode(password);
    }

    public static boolean validate(String password, String encrypted) {
        // 不允许空密码存在。
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(encrypted)) {
            return false;
        }

        // 当密文中包含盐时
        if (encrypted.contains(SALT_SEPARTOR)) {
            // 把盐取出来
            String salt = encrypted.substring(0, encrypted.indexOf(SALT_SEPARTOR));
            // 用盐加密传入的密码
            String compare = encode(password, salt);
            // 把加密摘要取出来
            String encryptedPassword = encrypted.substring(encrypted.indexOf(SALT_SEPARTOR) + 1);
            // 判断摘要是否相等
            boolean result1 = encryptedPassword.equals(compare);
            if (result1) {
                return true;
            }
            //判断md5加密后在加密是否相等（应对前端已经进行md5加密的情况）
            compare = encode(MD5(password), salt);
            boolean result2 = encryptedPassword.equals(compare);
            if (result2 == true) {
                return true;
            }
            return false;
        } else {
            // 当密文中没有盐时，直接退化为用MD5校验。
            return encrypted.equals(encrypt(password));
        }
    }
}