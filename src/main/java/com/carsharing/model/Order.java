package com.carsharing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "order", schema = "public")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_gen")
    @SequenceGenerator(name = "order_seq_gen", sequenceName = "order_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "timestampstart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Column(name = "timestampend")
    @Temporal(TemporalType.TIMESTAMP)
    private Date end;

    @Column(name = "blocked")
    private Boolean blocked;

    @Column(name = "ended")
    private Boolean ended;

    @Transient
    private String bookingTime, rentTime, waitingTime;

    @Transient
    private Float price;

    @ManyToOne
    @JoinColumn(name = "id_car", nullable = false)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY)
    private List<OrderData> orderData;

    @OneToOne(mappedBy = "order",fetch = FetchType.EAGER)
    private Payment payment;


}
