package com.woniu.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/***
 * @Author Lints
 * @Date 2021/3/2/002 13:14
 * @Description 创建JWT
 * @Since version-1.0
 */
public class JwtUtils {
    // 过期时间 24 小时
    private static final long EXPIRE_TIME = 24*60*60*1000L;
    // 密钥
    private static final String SALT = SaltUtils.getSalt(16);

    /**
     * 生成token
     * @param map  //传入payload
     * @return 返回token
     */
    public static String getCodeToken(HashMap<String, Integer> map){
        JWTCreator.Builder builder = JWT.create();
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        builder.withExpiresAt(new Date(System.currentTimeMillis()+EXPIRE_TIME));
        return builder.sign(Algorithm.HMAC256(SALT)).toString();
    }

    /**
     * @Author Lints
     * @Date 2021/3/2/002 13:28
     * @Description 解析Token
     * @Param [token]
     * @Return com.auth0.jwt.interfaces.DecodedJWT
     * @Since version-1.0
     */

    public static DecodedJWT getDecodeToken(String token){

        return JWT.require(Algorithm.HMAC256(SALT)).build().verify(token);
    }




}
