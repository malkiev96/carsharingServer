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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderData_seq_gen")
    @SequenceGenerator(name = "orderData_seq_gen", sequenceName = "orderData_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "action")
    private int action;

    @Column(name = "timestampstart")
    private Date start;

    @Column(name = "timestampend")
    private Date end;

    @Column(name = "ended")
    private Boolean ended;

    @Column(name = "price")
    private Float price;

    @ManyToOne
    @JoinColumn(name = "id_order")
    private Order order;


}
