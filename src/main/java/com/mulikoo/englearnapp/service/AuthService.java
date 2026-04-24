package com.mulikoo.englearnapp.service;

import com.mulikoo.englearnapp.dto.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtEncoder jwtEncoder;

    public AuthResponseDto generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        Instant exp = now.plus(1, ChronoUnit.HOURS);

        String scope = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(exp)
                .subject(userDetails.getUsername())
                .claim("scope", scope)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        AuthResponseDto result = new AuthResponseDto(token, exp.getEpochSecond());
        return result;
    }
}
