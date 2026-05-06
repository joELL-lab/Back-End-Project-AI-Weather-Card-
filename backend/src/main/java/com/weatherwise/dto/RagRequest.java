package com.weatherwise.dto;

import lombok.Data;

@Data
public class RagRequest {
    private String question;
    private String city;
    private String weatherCondition;
}
