package com.carsharing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "client", schema = "public")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq_gen")
    @SequenceGenerator(name = "client_seq_gen", sequenceName = "client_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "secondname")
    private String secondname;

    @Column(name = "middlename")
    private String middlename;

    @Column(name = "mail")
    private String mail;

    @Column(name = "gender")
    private String gender;

    @Column(name = "registration")
    @JsonIgnore
    private Date registrationDate;

    @Column(name = "birthday")
    @JsonIgnore
    private Date birthday;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "activated",columnDefinition = "boolean default false")
    private Boolean activated;

    @Column(name = "enabled",columnDefinition = "boolean default true")
    private Boolean enabled;

    @Column(name = "token")
    private String token;

    @JsonIgnore
    @OneToOne(mappedBy = "client",fetch = FetchType.EAGER)
    private Document document;

    @Override
    public String toString() {
        return secondname + " " + firstname + " " + middlename;
    }
}
