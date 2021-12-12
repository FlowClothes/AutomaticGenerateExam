package com.example.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {

    private static final String SING = "!Q@W#E$R";

    public static String getToken(Map<String,String> map){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,7); //7天后过期

        JWTCreator.Builder builder = JWT.create();

        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });

        String token = builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SING));
        return token;
    }

    public static DecodedJWT verify(String token){
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        return decodedJWT;
    }

}
