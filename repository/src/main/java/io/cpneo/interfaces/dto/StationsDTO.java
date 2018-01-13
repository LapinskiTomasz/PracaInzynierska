package io.cpneo.interfaces.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class StationsDTO {

    ArrayList<StationDTO> stationsDTO;

    PositionDTO userPosition;
}
