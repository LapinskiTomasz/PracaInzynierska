package io.cpneo.interfaces.dto;

import lombok.Data;

import java.util.List;

@Data
public class StationDTO {

    private long id;

    private String name;

    private List<CommentDTO> comments;

    private double PB95Price;

    private double PB98Price;

    private double ONPrice;

    private double LPGPrice;

    private String city;

    private String zipcode;

    private String street;

    private String homeNumber;

    private String rating;


}
