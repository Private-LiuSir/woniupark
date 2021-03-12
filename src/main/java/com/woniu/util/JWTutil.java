package com.woniu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTutil {

    //设置过期时间
    private static final long EXIT_TIME=60*30*1000;
    //获取随机盐   每次启动获取一次
    private static final String SIGN="woniupark";


    //获取加密后的JWT字符串
    public static String createJWT(Map<String,String> map){
        JWTCreator.Builder builder = JWT.create();
        //添加数据
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        //设置过期时间
        builder.withExpiresAt(new Date(System.currentTimeMillis()+EXIT_TIME));
        //设置密文并获取token
        String token = builder.sign(Algorithm.HMAC256(SIGN));
        //返回对象
        return token;
    }

    //解密的方法
    public static DecodedJWT vertify(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);

        return verify;
    }

}
