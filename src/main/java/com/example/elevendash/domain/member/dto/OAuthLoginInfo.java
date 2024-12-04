package com.example.elevendash.domain.member.dto;

import com.example.elevendash.domain.member.enums.Provider;

import java.util.Map;


public record OAuthLoginInfo(
        Provider provider,
        Map<String, String> propertyMap
) {
}