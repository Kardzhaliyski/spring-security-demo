package com.github.kardzhaliyski.securitydemo.properties;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jwt.security")
@Getter
@Setter
public class JwtProperties {

    private String secretKey;
    private String issuer;
    private Algorithm algorithm;

    public Algorithm getAlgorithm() {
        if(algorithm == null) {
            algorithm = Algorithm.HMAC256(getSecretKey());
        }

        return algorithm;
    }
}
