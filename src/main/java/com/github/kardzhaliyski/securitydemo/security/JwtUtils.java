package com.github.kardzhaliyski.securitydemo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.kardzhaliyski.securitydemo.properties.JwtProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class JwtUtils {

    public static final String ISSUER = "kardzhaliyski.github.com";

   private final JwtProperties jwtProperties;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String issue(UserPrincipal principal) {
        return JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .withSubject(principal.getId().toString())
                .withClaim("e", principal.getEmail())
                .withClaim("n", principal.getName())
                .withClaim("a", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withIssuer(ISSUER)
                .sign(jwtProperties.getAlgorithm());
    }

    public DecodedJWT decode(String token) throws TokenExpiredException {
        return JWT.require(jwtProperties.getAlgorithm())
                .withIssuer(ISSUER)
                .build()
                .verify(token);
    }

    public UserPrincipal convert(DecodedJWT token) {
        return UserPrincipal.builder()
                .id(Long.parseLong(token.getSubject()))
                .email(token.getClaim("e").asString())
                .name(token.getClaim("n").toString())
                .authorities(extractAuthoritiesFromClaim(token))
                .build();
    }

    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim(DecodedJWT token) {
        Claim claim = token.getClaim("a");
        if(claim.isNull() || claim.isMissing()) {
            return List.of();
        }

        return claim.asList(SimpleGrantedAuthority.class);
    }
}
