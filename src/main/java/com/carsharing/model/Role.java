package com.carsharing.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "role", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Role {

    public Role(String name){
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(name = "name")
    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "role")
    private List<User> users;

}
