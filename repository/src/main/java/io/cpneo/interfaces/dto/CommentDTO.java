package io.cpneo.interfaces.dto;

import lombok.Data;

@Data
public class CommentDTO {

    private long stationID;

    private String comment;

    private int rating;

    private String author;
}
