package io.cpneo.station;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name= "station")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "station", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column(name = "PB95Price")
    private double PB95Price;

    @Column(name = "PB98Price")
    private double PB98Price;

    @Column(name = "ONPrice")
    private double ONPrice;

    @Column(name = "LPGPrice")
    private double LPGPrice;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;


}
