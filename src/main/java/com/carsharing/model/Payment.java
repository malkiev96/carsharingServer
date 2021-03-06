package com.carsharing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment", schema = "public")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq_gen")
    @SequenceGenerator(name = "payment_seq_gen", sequenceName = "payment_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "token")
    private String token;

    @Column(name = "price")
    private Float price;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;
}
