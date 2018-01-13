package io.cpneo.station;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name= "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "city")
    private String city;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "street")
    private String street;

    @Column(name = "home_number")
    private String homeNumber;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

    @OneToOne
    @JoinColumn(name = "station_id", referencedColumnName = "id")
    private Station station;
}
