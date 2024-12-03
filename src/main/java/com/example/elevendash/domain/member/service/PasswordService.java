package com.example.elevendash.domain.member.service;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PasswordService {

    private final BCrypt.Hasher hasher;
    private final BCrypt.Verifyer verifyer;

    public PasswordService(BCrypt.Hasher hasher, BCrypt.Verifyer verifyer) {
        this.hasher = hasher;
        this.verifyer = verifyer;
    }

    public String encode(String rawPassword) {
        return hasher.hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = verifyer.verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }

}