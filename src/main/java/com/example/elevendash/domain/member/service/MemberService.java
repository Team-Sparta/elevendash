package com.example.elevendash.domain.member.service;

import com.example.elevendash.domain.member.dto.OAuthLoginInfo;
import com.example.elevendash.domain.member.dto.oauth.OAuthUser;
import com.example.elevendash.domain.member.dto.request.OAuthLoginRequest;
import com.example.elevendash.domain.member.dto.response.OAuthLoginResponse;
import com.example.elevendash.domain.member.dto.response.UpdateProfileResponse;
import com.example.elevendash.domain.member.dto.AuthUserInfo;
import com.example.elevendash.domain.member.dto.response.MemberProfileResponse;
import com.example.elevendash.domain.member.dto.Token;
import com.example.elevendash.domain.member.dto.request.EmailLoginRequest;
import com.example.elevendash.domain.member.dto.request.SignUpRequest;
import com.example.elevendash.domain.member.dto.request.UpdateProfileRequest;
import com.example.elevendash.domain.member.dto.response.EmailLoginResponse;
import com.example.elevendash.domain.member.dto.response.SignUpResponse;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.enums.Provider;
import com.example.elevendash.domain.member.factory.OAuthProviderFactory;
import com.example.elevendash.domain.member.repository.MemberRepository;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.constants.AuthConstants;
import com.example.elevendash.global.exception.AuthenticationException;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.InvalidParamException;
import com.example.elevendash.global.exception.code.ErrorCode;
import com.example.elevendash.global.provider.JwtTokenProvider;
import com.example.elevendash.global.s3.S3Service;
import com.example.elevendash.global.s3.UploadImageInfo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordService passwordService;

    private final OAuthProviderFactory providerFactory;

    private final S3Service s3Service;

    public SignUpResponse signUp(SignUpRequest request) {
        validateEmail(request.email());

        Member member = Member.builder().signUpRequest(request).build();

        member.validateProvider();

        if (request.password() != null) {
            member.setPassword(passwordService.encode(request.password()));
        }

        memberRepository.save(member);

        String token = jwtTokenProvider.createAccessToken(createClaims(member));

        return new SignUpResponse(
                member.getId(),
                new Token(token, jwtTokenProvider.getExpirationByToken(token))
        );
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new InvalidParamException(ErrorCode.EXIST_EMAIL);
        }
    }

    private HashMap<String, Object> createClaims(Member member) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", member.getId());
        hashMap.put("name", member.getName());
        hashMap.put("email", member.getEmail());
        return hashMap;
    }

    public EmailLoginResponse emailLogin(EmailLoginRequest request) {
        Member member = memberRepository.findByEmailAndDeletedAtIsNull(request.email());

        if (!passwordService.matches(request.password(), member.getPassword())) {
            throw new AuthenticationException(ErrorCode.INVALID_AUTHENTICATION);
        }

        memberRepository.save(member);

        String token = jwtTokenProvider.createAccessToken(createClaims(member));

        return new EmailLoginResponse(
                new Token(token, jwtTokenProvider.getExpirationByToken(token)),
                new AuthUserInfo(
                        member.getEmail(),
                        member.getProviderId(),
                        member.getProvider()
                )
        );
    }

    public MemberProfileResponse getMemberProfile(
            Long memberId,
            @LoginMember Member loginMember) {
        return memberRepository.findById(memberId)
                .map(member -> new MemberProfileResponse(
                        member.getId(),
                        member.getName(),
                        member.getProfileImage()))
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public void deleteMember(Member loginMember) {
        loginMember.deleteAccount();
    }

    @Transactional
    public UpdateProfileResponse updateProfile(Member loginMember, UpdateProfileRequest request, MultipartFile image) {

        Member member = memberRepository.findByEmailAndDeletedAtIsNull(loginMember.getEmail());

        String imageUrl = null;

        if (image != null) {
            UploadImageInfo uploadImageInfo = s3Service.uploadMemberProfileImage(image);
            imageUrl = uploadImageInfo.ImageUrl();
        }

        member.updateProfile(request, imageUrl);

        return new UpdateProfileResponse(member.getId());
    }

    public OAuthLoginResponse oAuthLogin(@Valid OAuthLoginRequest request) {
        OAuthLoginInfo authLoginInfo = request.toDto();
        Provider provider = authLoginInfo.provider();

        // 인가 코드를 이용해 토큰 발급 요청
        String accessToken = providerFactory.getAccessToken(
                provider,
                authLoginInfo.propertyMap().get(AuthConstants.REDIRECT_URI),
                authLoginInfo.propertyMap().get(AuthConstants.CODE)
        );

        // 토큰을 이용해 사용자 정보 가져오기
        OAuthUser oAuthUser = providerFactory.getOAuthUser(provider, accessToken);

        // 회원가입 or 로그인
        String email = oAuthUser.email();

        boolean isNewMember = !memberRepository.existsByEmailAndDeletedAtIsNull(email);

        Token token = createTokenForMember(isNewMember, email);

        return new OAuthLoginResponse(
                token,
                isNewMember,
                new AuthUserInfo(email, oAuthUser.id(), provider)
        );
    }

    private Token createTokenForMember(boolean isNewMember, String email) {
        if (isNewMember) {
            return new Token(null, null);
        } else {
            Member member = memberRepository.findByEmailAndDeletedAtIsNull(email);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", member.getId());
            hashMap.put("name", member.getName());
            hashMap.put("email", member.getEmail());
            String generatedToken = jwtTokenProvider.createAccessToken(hashMap);
            return new Token(generatedToken, jwtTokenProvider.getExpirationByToken(generatedToken));
        }
    }
}
