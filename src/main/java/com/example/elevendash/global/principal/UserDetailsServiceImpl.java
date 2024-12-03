package com.example.elevendash.global.principal;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.repository.MemberRepository;
import com.example.elevendash.global.exception.AuthenticationException;
import com.example.elevendash.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        UserPrincipal userDetails = new UserPrincipal(member);
        validateAuthenticate(userDetails);

        return userDetails;
    }

    private void validateAuthenticate(UserPrincipal member) {
        if (member == null) {
            throw new AuthenticationException(ErrorCode.INTERNAL_AUTHENTICATION_SERVICE);
        }
        validateEnabled(member);
        validateAccountExpired(member);
        validateAccountNonLocked(member);
        validateCredentialNonExpired(member);
    }

    private static void validateEnabled(UserPrincipal user) {
        if (!user.isEnabled()) {
            throw new AuthenticationException(ErrorCode.DISABLE_ACCOUNT);
        }
    }

    private static void validateCredentialNonExpired(UserPrincipal user) {
        if (!user.isCredentialsNonExpired()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

    private static void validateAccountNonLocked(UserPrincipal user) {
        if (!user.isAccountNonLocked()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

    private static void validateAccountExpired(UserPrincipal user) {
        if (!user.isAccountNonExpired()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

}