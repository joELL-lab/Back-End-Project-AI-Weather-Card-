package com.weatherwise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDTO {
    private Long id;

    @NotBlank(message = "City name is required")
    private String name;

    private String country;

    @NotNull(message = "Latitude is required")
    private Double lat;

    @NotNull(message = "Longitude is required")
    private Double lon;

    private String createdAt;
}
