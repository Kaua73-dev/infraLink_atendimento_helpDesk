package com.infraLink.API.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.infraLink.API.model.entity.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class TokenConfig {

    @Value("{jwt.secret}")
    private String secret;

    public String generateToken(User user){
        Algorithm algorithm = Algorithm.HMAC256(secret);

        try {
            String token = JWT.create()
                    .withIssuer("infraLink_chamado")
                    .withSubject(user.getEmail())
                    .withExpiresAt(generationExpiration())
                    .withIssuedAt(Instant.now())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e){
            throw new RuntimeException("It was not possible to generate the token: " + e);
        }



    }

    private Instant generationExpiration(){
        return LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("-03:00"));
    }


    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e){
            throw new RuntimeException("Error while validate token: " + e);
        }

    }



}
