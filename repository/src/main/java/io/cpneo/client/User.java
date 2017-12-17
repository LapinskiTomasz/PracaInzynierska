package io.cpneo.client;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name= "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String login;

    @Column(name= "password")
    private String password;

    @Column(name = "token")
    private String token;

    public User() { }

    public User(long id) {
        this.id = id;
    }

    public User(String email, String name, String password) {
        this.email = email;
        this.login = name;
        this.password= password;
    }
}
