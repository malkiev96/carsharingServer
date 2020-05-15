package com.carsharing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "client", schema = "public")
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "firstname")
    @NotEmpty
    private String firstName;

    @Column(name = "secondname")
    @NotEmpty
    private String secondName;

    @Column(name = "middlename")
    @NotEmpty
    private String middleName;

    @Column(name = "mail")
    @Email
    private String email;

    @Column(name = "gender")
    @NotEmpty
    private String gender;

    @Column(name = "registration")
    @JsonIgnore
    private Date registrationDate;

    @Column(name = "birthday")
    @JsonIgnore
    private Date birthday;

    @Column(name = "telephone")
    @NotEmpty
    private String telephone;

    @Column(name = "password")
    @JsonIgnore
    @NotEmpty
    private String password;

    @Column(name = "activated", columnDefinition = "boolean default false")
    private Boolean activated;

    @Column(name = "enabled", columnDefinition = "boolean default true")
    private Boolean enabled;

    @Column(name = "token")
    private String token;

    @JsonIgnore
    @OneToOne(mappedBy = "client", fetch = FetchType.EAGER)
    private Document document;

    @Override
    public String toString() {
        return secondName + " " + firstName + " " + middleName;
    }
}
