package io.cpneo.interfaces.dto;

import lombok.Data;

@Data
public class PositionDTO {
    private double latitude;

    private double longitude;

    private String description;
}
