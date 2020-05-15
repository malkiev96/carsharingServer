package com.carsharing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "document", schema = "public")
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "imagesrc1")
    @NotEmpty
    private String imageSrc1;

    @Column(name = "imagesrc2")
    @NotEmpty
    private String imageSrc2;

    @Column(name = "imagesrc3")
    @NotEmpty
    private String imageSrc3;

    @Column(name = "imagesrc4")
    @NotEmpty
    private String imageSrc4;

    @Column(name = "imagesrc5")
    @NotEmpty
    private String imageSrc5;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;
}
