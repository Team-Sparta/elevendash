package com.example.elevendash.global.provider;

import com.example.elevendash.global.constants.AuthConstants;
import com.example.elevendash.global.exception.CustomJwtException;
import com.example.elevendash.global.exception.InvalidParamException;
import com.example.elevendash.global.exception.code.ErrorCode;
import com.example.elevendash.global.principal.UserDetailsServiceImpl;
import com.example.elevendash.global.principal.UserPrincipal;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    public static final Long ACCESS_TOKEN_EXPIRE_TIME = Duration.ofHours(6).toMillis();

    private final SecretKey key;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenProvider(
            @Value("${spring.jwt.secret}") String key,
            UserDetailsServiceImpl userDetailsService
    ) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    public String createAccessToken( Map<String, Object> additionalData) {
        Date now = new Date();
        long accessTokenExpireTime = now.getTime() + ACCESS_TOKEN_EXPIRE_TIME;

        return Jwts.builder()
                .claims(additionalData)
                .issuedAt(now)
                .expiration(new Date(accessTokenExpireTime))
                .signWith(key)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        UserPrincipal userDetails = (UserPrincipal) userDetailsService.loadUserByUsername(getEmailByToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(String header) {
        return Optional.ofNullable(header)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_AUTHENTICATION))
                .replace(AuthConstants.TOKEN_PREFIX, "");
    }

    public boolean isValidateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        return !getExpirationByToken(token).before(new Date());
    }

    public Long getIdFromToken(String token) {
        return parseClaims(token).get("id", Long.class);
    }

    public String getEmailByToken(String token) {
        return parseClaims(token).get("email", String.class);
    }

    public Date getExpirationByToken(String token) {
        return parseClaims(token).getExpiration();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException ex) {
            throw new CustomJwtException(ErrorCode.MALFORMED_JWT_EXCEPTION);
        } catch (ExpiredJwtException ex) {
            throw new CustomJwtException(ErrorCode.EXPIRED_JWT_EXCEPTION);
        } catch (UnsupportedJwtException ex) {
            throw new CustomJwtException(ErrorCode.UNSUPPORTED_JWT_EXCEPTION);
        } catch (IllegalArgumentException ex) {
            throw new CustomJwtException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

}