package io.cpneo.service.dto;


import lombok.Data;

@Data
public class GenericMessageDTO {

    private String message;

    private Integer code;

    public GenericMessageDTO(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
}
