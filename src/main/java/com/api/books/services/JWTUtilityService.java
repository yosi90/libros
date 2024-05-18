package com.api.books.services;

import com.api.books.persistence.entities.RoleEntity;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.List;

public interface JWTUtilityService {

    String generateJWT(Long userId, List<RoleEntity> roles) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException;

    JWTClaimsSet parseJWT(String jwt) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, ParseException, JOSEException;

    String extendJWTExpiration(String expiredToken) throws ParseException, JOSEException, NoSuchAlgorithmException, InvalidKeySpecException, IOException;
}
