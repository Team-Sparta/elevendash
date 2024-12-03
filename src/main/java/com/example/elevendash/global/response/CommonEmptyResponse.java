package com.example.elevendash.global.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties // Ensures serialization for an empty object
public record CommonEmptyResponse() {
}