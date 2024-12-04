package com.example.elevendash.domain.member.service;

import com.example.elevendash.domain.member.dto.response.UpdateProfileResponse;
import com.example.elevendash.domain.member.dto.AuthUserInfo;
import com.example.elevendash.domain.member.dto.MemberProfileResponse;
import com.example.elevendash.domain.member.dto.Token;
import com.example.elevendash.domain.member.dto.request.EmailLoginRequest;
import com.example.elevendash.domain.member.dto.request.SignUpRequest;
import com.example.elevendash.domain.member.dto.request.UpdateProfileRequest;
import com.example.elevendash.domain.member.dto.response.EmailLoginResponse;
import com.example.elevendash.domain.member.dto.response.SignUpResponse;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.repository.MemberRepository;
import com.example.elevendash.global.annotation.LoginMember;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordService passwordService;
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
                        member.getProvider(),
                        member.getProviderId()
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

}
