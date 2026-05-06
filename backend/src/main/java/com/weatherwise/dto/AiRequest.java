package com.weatherwise.dto;

import lombok.Data;

@Data
public class AiRequest {
    private String systemMessage;
    private String userMessage;
}
