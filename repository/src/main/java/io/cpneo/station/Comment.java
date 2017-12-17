package io.cpneo.station;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name= "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "author")
    private String author;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "station_id", referencedColumnName = "id",nullable = false)
    private Station station;
}
