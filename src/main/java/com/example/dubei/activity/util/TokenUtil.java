package com.example.dubei.activity.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * token util
 * Created by yuyong.zhao on 2017-08-29 11:41.
 */
public class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    public static String generate(String userId) {
        if (userId == null) {
            throw new RuntimeException("userid is null, cant generate token");
        }

        String token = encryptSalt(userId);
        logger.debug(String.format("userid = %s, token = %s", userId, token));
        return token;
    }


    private static String encryptSalt(String info) {
        BASE64Encoder encoder = new BASE64Encoder();
        String token = String.format("%s_%s", encrypt(info), System.currentTimeMillis());

        token = encrypt(token);
        return encoder.encode(token.getBytes());
    }

    private static String encrypt(String info) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(info.getBytes());
    }

    public static String decrypt(String cpherTxt) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return new String(decoder.decodeBuffer(cpherTxt));
        } catch (IOException e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    public static String decryptSalt(String cpherTxt) throws Exception{
        BASE64Decoder decoder = new BASE64Decoder();
        String tmp = null;
        tmp = new String(decoder.decodeBuffer(cpherTxt));
        tmp = new String(decoder.decodeBuffer(tmp));
        String[] tmpArr = tmp.split("_");
        String userId = decrypt(tmpArr[0]);
        return userId + "_" + tmpArr[1];

    }

    public static String getToken(HttpServletRequest request){
        return request.getHeader("token");
    }

    //根据token解析出登陆用户账号
    public static String getAccount(HttpServletRequest request) throws Exception {
        String token = getToken(request);
        return TokenUtil.decryptSalt(token).split("_")[0];
    }

    public static void main(String[] args) throws Exception {
        String userId = "123566";
        String encrpt = encryptSalt(userId);
        String decrpt = decryptSalt(encrpt);
        System.out.printf("userid=%s, encrpt=%s, decrpt=%s\n", userId, encrpt, decrpt);
        System.out.println(generate(userId));
    }
}
