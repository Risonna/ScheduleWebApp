package com.risonna.schedulewebapp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.UUID;

public class JWTClass {
    Algorithm algorithm = Algorithm.HMAC256("abobus");




    public String generateToken(String userName, String role){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 3600000L); // Token expires in 1 hour


        String jwtToken = JWT.create()
                .withIssuer("WebSchedule Server")
                .withSubject(userName)
                .withClaim("role", role)
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .withJWTId(UUID.randomUUID()
                        .toString())
                .withNotBefore(now)
                .sign(algorithm);

        return jwtToken;

    }

    public boolean verifyToken(String jwtToken, String systemRole){
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("WebSchedule Server")
                .build();


        try {
            DecodedJWT decodedJWT = verifier.verify(jwtToken);

            Claim claim = decodedJWT.getClaim("role");
            String role = claim.asString();

            String subject = decodedJWT.getSubject();
            System.out.println(subject);
            System.out.println(role);
            if(role.equals(systemRole)){
                return true;
            }
            return false;

        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }








}

