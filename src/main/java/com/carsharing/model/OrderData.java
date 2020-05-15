package com.carsharing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_data", schema = "public")
@Getter
@Setter
public class OrderData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "action")
    private int action;

    @Column(name = "timestampstart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Column(name = "timestampend")
    @Temporal(TemporalType.TIMESTAMP)
    private Date end;

    @Column(name = "ended")
    private Boolean ended;

    @Column(name = "price")
    private Float price;

    @ManyToOne
    @JoinColumn(name = "id_order")
    private Order order;

}
