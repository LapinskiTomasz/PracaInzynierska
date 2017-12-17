package io.cpneo.service.dto;


import lombok.Data;

@Data
public class StationRegisterDTO {

    private String login;

    private String password;

    private String name;

    private String code;

    private String city;

    private String zipcode;

    private String street;

    private String homeNumber;


}
