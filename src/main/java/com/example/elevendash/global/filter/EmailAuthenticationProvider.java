package com.example.elevendash.global.filter;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.repository.MemberRepository;
import com.example.elevendash.domain.member.service.PasswordService;
import com.example.elevendash.global.exception.AuthenticationException;
import com.example.elevendash.global.exception.code.ErrorCode;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class EmailAuthenticationProvider implements AuthenticationProvider {

    final MemberRepository memberRepository;
    final PasswordService passwordService;

    public EmailAuthenticationProvider(MemberRepository memberRepository, PasswordService passwordService) {
        this.memberRepository = memberRepository;
        this.passwordService = passwordService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();

        // Custom authentication logic (e.g., check the database for the user)
        Member member = memberRepository.findByEmailAndDeletedAtIsNull(email);

        // Here you would validate the password (e.g., using BCryptPasswordEncoder)

        if (!passwordService.matches(password, member.getPassword())) {
            throw new AuthenticationException(ErrorCode.INVALID_AUTHENTICATION);
        }

        // Return an authenticated token with the user details
        return new UsernamePasswordAuthenticationToken(member, null, Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
