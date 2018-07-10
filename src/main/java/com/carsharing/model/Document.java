package com.carsharing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "document", schema = "public")
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_seq_gen")
    @SequenceGenerator(name = "document_seq_gen", sequenceName = "document_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "imagesrc1")
    private String imageSrc1;

    @Column(name = "imagesrc2")
    private String imageSrc2;

    @Column(name = "imagesrc3")
    private String imageSrc3;

    @Column(name = "imagesrc4")
    private String imageSrc4;

    @Column(name = "imagesrc5")
    private String imageSrc5;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;
}
