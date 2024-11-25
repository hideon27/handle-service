package com.example.handle.function;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    /**
     * 生成token  header.payload.singature
     */

    private static final String SING = "jF44FzpQEuaUOMmYXtWhblIm5yTuMOewmUvrok68bkle-jHTXs2Fr3ddKSqxfCENxZYLLTJSmRgPWOlkJSWClg";//固定

    public static String getToken(Map<String, String> map) {

        HashMap<String, Object> head = new HashMap<>();
        head.put("alg","HS256");head.put("typ","JWT");

        Calendar instance = Calendar.getInstance();
        // 1h后令牌token失效
        instance.add(Calendar.HOUR,1);

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();
        // payload
        map.forEach(builder::withClaim);
        return builder.withHeader(head) //header
                .withExpiresAt(instance.getTime())  //指定令牌过期时间
                .sign(Algorithm.HMAC256(SING));
    }

    /**
     * 验证token  合法性
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    /**
     * 获取token信息方法
     */
    /*public static DecodedJWT getTokenInfo(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        return verify;
    }*/
}

