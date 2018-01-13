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

    private StatisticsDTO statistics;

    private int distance;

    private PositionDTO position;


    @Override
    public String toString() {
        return "StationDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", comments=" + comments +
                ", PB95Price=" + PB95Price +
                ", PB98Price=" + PB98Price +
                ", ONPrice=" + ONPrice +
                ", LPGPrice=" + LPGPrice +
                ", city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", street='" + street + '\'' +
                ", homeNumber='" + homeNumber + '\'' +
                ", rating='" + rating + '\'' +
                ", statistics=" + statistics +
                ", distance=" + distance +
                '}';
    }
}
