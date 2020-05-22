package com.mall.discover.service.util;

import com.doubo.common.crypto.PasswordDecrypter;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2018/9/18
 * Description:
 */
public class PasswordDecrypterTest {
    public static void main(String[] s){
        PasswordDecrypter passwordDecrypter = new PasswordDecrypter("$)(#@Si&^%.Bu+=!");
        String data = "root";
        System.out.println(data + "==" + passwordDecrypter.encrypt(data));
        data = "Aa123456";
        System.out.println(data + "==" + passwordDecrypter.encrypt(data));
        data = "123456";
        System.out.println(data + "==" + passwordDecrypter.encrypt(data));
        data = "doubo";
        System.out.println(data + "==" + passwordDecrypter.decrypt("2AQMOFpPta1EmmB3s+/baA=="));
        data = "Aa123456";
        System.out.println(data + "==" + passwordDecrypter.decrypt("emrQgDKiPutfxdVfyeOGbw=="));
    }
}
