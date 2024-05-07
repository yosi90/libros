package com.api.books.security;

import com.api.books.services.JWTUtilityService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    JWTUtilityService jwtUtilityService;

    CustomUserDetailsService userDetailsService;

    @Autowired
    public JWTAuthorizationFilter(JWTUtilityService jwtUtilityService, CustomUserDetailsService userDetailsService) {
        this.jwtUtilityService = jwtUtilityService;
        this.userDetailsService = userDetailsService;
    }

    @SuppressWarnings({ "null", "unchecked" })
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);
        try {
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            Long userId = Long.parseLong(claims.getSubject());

            // Obtener roles del token
            List<Map<String, String>> roles = (List<Map<String, String>>) claims.getClaim("roles");

            // Convertir roles en authorities
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Map<String, String> role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.get("name")));
            }

            UserDetails userDetails = userDetailsService.loadUserById(userId);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request, response);
    }
}